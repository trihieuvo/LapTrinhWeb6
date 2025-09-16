package com.votrihieu.BAI04.controller;

import java.io.IOException;
import com.votrihieu.BAI04.dao.UserDAO;
import com.votrihieu.BAI04.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // --- VALIDATION ---
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            doGet(request, response);
            return;
        }
        if (userDAO.findByUsername(username) != null) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại!");
            doGet(request, response);
            return;
        }
        if (userDAO.findByEmail(email) != null) {
            request.setAttribute("error", "Email đã được sử dụng!");
            doGet(request, response);
            return;
        }
        
        // --- PROCESS ---
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); 
        newUser.setRoleid(1); 
        newUser.setFullname(username);
        userDAO.insert(newUser);

        
        request.getSession().setAttribute("successMessage", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}