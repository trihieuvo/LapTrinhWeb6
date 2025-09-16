package com.votrihieu.BAI04.controller;

import java.io.IOException;
import java.util.List;
import com.votrihieu.BAI04.dao.UserDAO;
import com.votrihieu.BAI04.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/users/*")
public class AdminUserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo() == null ? "/list" : request.getPathInfo();

		switch (pathInfo) {
		case "/add":
			showAddForm(request, response);
			break;
		case "/edit":
			showEditForm(request, response);
			break;
		case "/delete":
			deleteUser(request, response);
			break;
		default: // "/list" hoặc "/"
			listUsers(request, response);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		switch (pathInfo) {
		case "/create":
			insertUser(request, response);
			break;
		case "/update":
			updateUser(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	private void listUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		List<User> userList;
		if (keyword != null && !keyword.trim().isEmpty()) {
			userList = userDAO.searchByUsernameOrFullname(keyword);
		} else {
			userList = userDAO.findAll();
		}
		request.setAttribute("userList", userList);
		request.setAttribute("keyword", keyword);
		request.getRequestDispatcher("/views/admin/user/list.jsp").forward(request, response);
	}

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("user", new User());
		request.setAttribute("formAction", "create");
		request.getRequestDispatcher("/views/admin/user/add-edit.jsp").forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDAO.findById(id);
		request.setAttribute("user", existingUser);
		request.setAttribute("formAction", "update");
		request.getRequestDispatcher("/views/admin/user/add-edit.jsp").forward(request, response);
	}

	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User newUser = new User();
		newUser.setUsername(request.getParameter("username"));
		newUser.setPassword(request.getParameter("password")); // Cần mã hóa trong thực tế
		newUser.setEmail(request.getParameter("email"));
		newUser.setFullname(request.getParameter("fullname"));
		newUser.setPhone(request.getParameter("phone"));
		newUser.setRoleid(Integer.parseInt(request.getParameter("roleid")));

		userDAO.insert(newUser);
		response.sendRedirect(request.getContextPath() + "/admin/users/list");
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User user = userDAO.findById(id);

		user.setEmail(request.getParameter("email"));
		user.setFullname(request.getParameter("fullname"));
		user.setPhone(request.getParameter("phone"));
		user.setRoleid(Integer.parseInt(request.getParameter("roleid")));

		userDAO.update(user);
		response.sendRedirect(request.getContextPath() + "/admin/users/list");
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User currentUser = (User) request.getSession().getAttribute("user");

		// Ngăn Admin tự xóa chính mình
		if (currentUser.getId() != id) {
			userDAO.delete(id);
		}

		response.sendRedirect(request.getContextPath() + "/admin/users/list");
	}
}