package com.votrihieu.BAI04.controller;

import java.io.IOException;
import java.util.List;

import com.votrihieu.BAI04.dao.CategoryDAO;
import com.votrihieu.BAI04.model.Category;
import com.votrihieu.BAI04.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/user/home", "/manager/home", "/admin/home" })
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoryDAO categoryDAO;

	@Override
	public void init() throws ServletException {
		categoryDAO = new CategoryDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Filter đã đảm bảo user đã đăng nhập, nên ta có thể lấy thông tin an toàn
		User currentUser = (User) request.getSession().getAttribute("user");

		// Lấy đường dẫn URI để xác định vai trò
		String path = request.getRequestURI();

		List<Category> categoryList;
		String viewName = ""; // Tên trang JSP để forward đến

		if (path.contains("/admin/home") || path.contains("/user/home")) {
			// Admin và User xem tất cả category
			categoryList = categoryDAO.findAll();
			viewName = "Danh sách tất cả Category";
		} else if (path.contains("/manager/home")) {
			// Manager chỉ xem category của chính mình
			categoryList = categoryDAO.findByUserId(currentUser.getId());
			viewName = "Danh sách Category của bạn";
		} else {
			// Trường hợp không mong muốn
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// Đặt danh sách category và tiêu đề vào request để JSP có thể sử dụng
		request.setAttribute("categories", categoryList);
		request.setAttribute("viewName", viewName);

		// Chuyển hướng đến trang JSP để hiển thị
		request.getRequestDispatcher("/views/home/home.jsp").forward(request, response);
	}
}