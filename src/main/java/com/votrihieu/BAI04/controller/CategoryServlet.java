package com.votrihieu.BAI04.controller;

import java.io.IOException;

import com.votrihieu.BAI04.dao.CategoryDAO;
import com.votrihieu.BAI04.model.Category;
import com.votrihieu.BAI04.model.User;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/category/*")
@MultipartConfig
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoryDAO categoryDAO;

	private static final String UPLOAD_DIRECTORY = "D:/WEB/image";

	@Override
	public void init() throws ServletException {
		categoryDAO = new CategoryDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Lấy action từ URL (phần sau /category/)
		String action = request.getPathInfo();

		switch (action) {
		case "/add":
			showAddForm(request, response);
			break;
		case "/delete":
			deleteCategory(request, response);
			break;
		case "/edit":
			showEditForm(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();

		switch (action) {
		case "/create":
			insertCategory(request, response);
			break;
		case "/update":
			updateCategory(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	/**
	 * Hiển thị trang JSP để thêm category mới.
	 */
	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Đặt một đối tượng Category trống vào request để form có thể dùng chung cho cả
		// add và edit
		request.setAttribute("category", new Category());
		request.setAttribute("formAction", "create");
		request.getRequestDispatcher("/views/category/add-edit.jsp").forward(request, response);
	}

	/**
	 * Xử lý việc chèn category mới vào database.
	 */
	private void insertCategory(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String cateName = request.getParameter("cateName");
		Part filePart = request.getPart("iconFile"); // Lấy file từ form
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Lấy tên file

		// Lưu file vào thư mục UPLOAD_DIRECTORY
		if (fileName != null && !fileName.isEmpty()) {
			File file = new File(UPLOAD_DIRECTORY, fileName);
			try (var input = filePart.getInputStream()) {
				Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} else {
			fileName = null; // Không có file nào được upload
		}

		User currentUser = (User) request.getSession().getAttribute("user");

		Category newCategory = new Category();
		newCategory.setCateName(cateName);
		newCategory.setIcons(fileName); // Lưu TÊN FILE vào database
		newCategory.setUser(currentUser);

		categoryDAO.insert(newCategory);

		redirectToHome(request, response, currentUser);
	}

	private void updateCategory(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int cateId = Integer.parseInt(request.getParameter("cateId"));
		String cateName = request.getParameter("cateName");
		String oldIcon = request.getParameter("oldIcon"); // Lấy tên file icon cũ

		Part filePart = request.getPart("iconFile");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

		Category categoryToUpdate = categoryDAO.findById(cateId);

		// KIỂM TRA QUYỀN (giữ nguyên)
		User currentUser = (User) request.getSession().getAttribute("user");
		if (categoryToUpdate == null
				|| !(currentUser.getRoleid() == 3 || categoryToUpdate.getUser().getId() == currentUser.getId())) {
			request.setAttribute("message", "Bạn không có quyền thực hiện hành động này.");
			request.getRequestDispatcher("/views/error/403.jsp").forward(request, response);
			return;
		}

		// Nếu có file mới được upload
		if (fileName != null && !fileName.isEmpty()) {
			// Lưu file mới
			File file = new File(UPLOAD_DIRECTORY, fileName);
			try (var input = filePart.getInputStream()) {
				Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			categoryToUpdate.setIcons(fileName); // Cập nhật tên file mới

			// (Tùy chọn) Xóa file ảnh cũ nếu tồn tại
			if (oldIcon != null && !oldIcon.isEmpty()) {
				File oldFile = new File(UPLOAD_DIRECTORY, oldIcon);
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}
		} else {
			// Nếu không có file mới, giữ lại file cũ
			categoryToUpdate.setIcons(oldIcon);
		}

		categoryToUpdate.setCateName(cateName);
		categoryDAO.update(categoryToUpdate);

		redirectToHome(request, response, currentUser);
	}

	private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));

			User currentUser = (User) request.getSession().getAttribute("user");
			Category categoryToDelete = categoryDAO.findById(id);

			// KIỂM TRA QUYỀN: Admin (roleid=3) hoặc chủ sở hữu mới được xóa
			if (categoryToDelete != null
					&& (currentUser.getRoleid() == 3 || categoryToDelete.getUser().getId() == currentUser.getId())) {
				categoryDAO.delete(id);
				redirectToHome(request, response, currentUser);
			} else {
				// Không có quyền, chuyển đến trang lỗi
				request.setAttribute("message", "Bạn không có quyền thực hiện hành động này.");
				request.getRequestDispatcher("/views/error/403.jsp").forward(request, response);
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Category không hợp lệ.");
		}
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Category existingCategory = categoryDAO.findById(id);
			User currentUser = (User) request.getSession().getAttribute("user");

			// KIỂM TRA QUYỀN: Admin hoặc chủ sở hữu mới được sửa
			if (existingCategory != null
					&& (currentUser.getRoleid() == 3 || existingCategory.getUser().getId() == currentUser.getId())) {
				request.setAttribute("category", existingCategory);
				request.setAttribute("formAction", "update");
				request.getRequestDispatcher("/views/category/add-edit.jsp").forward(request, response);
			} else {
				request.setAttribute("message", "Bạn không có quyền thực hiện hành động này.");
				request.getRequestDispatcher("/views/error/403.jsp").forward(request, response);
			}
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID Category không hợp lệ.");
		}
	}

	/**
	 * Phương thức tiện ích để chuyển hướng về trang home dựa trên role.
	 */
	private void redirectToHome(HttpServletRequest request, HttpServletResponse response, User user)
			throws IOException {
		String homeUrl = "/login"; // Mặc định nếu role không xác định
		switch (user.getRoleid()) {
		case 1:
			homeUrl = "/user/home";
			break;
		case 2:
			homeUrl = "/manager/home";
			break;
		case 3:
			homeUrl = "/admin/home";
			break;
		}
		response.sendRedirect(request.getContextPath() + homeUrl);
	}
}