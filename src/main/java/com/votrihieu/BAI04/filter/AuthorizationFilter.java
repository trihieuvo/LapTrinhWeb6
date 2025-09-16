package com.votrihieu.BAI04.filter;

import java.io.IOException;

import com.votrihieu.BAI04.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/user/*", "/manager/*", "/admin/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Lấy session hiện tại, không tạo mới nếu chưa có
        HttpSession session = httpRequest.getSession(false);
        
        // KIỂM TRA 1: Người dùng đã đăng nhập chưa?
        if (session == null || session.getAttribute("user") == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        // Lấy thông tin user từ session
        User user = (User) session.getAttribute("user");
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // KIỂM TRA 2: Người dùng có đúng vai trò để truy cập vào URL không?
        boolean allowed = false;
        if (path.startsWith("/admin/") && user.getRoleid() == 3) {
            allowed = true;
        } else if (path.startsWith("/manager/") && user.getRoleid() == 2) {
            allowed = true;
        } else if (path.startsWith("/user/") && user.getRoleid() == 1) {
            allowed = true;
        }

        if (allowed) {
           
            chain.doFilter(request, response);
        } else {
            // Nếu không có quyền, chuyển hướng đến trang báo lỗi
            request.setAttribute("message", "Bạn không có quyền truy cập vào trang này.");
            request.getRequestDispatcher("/views/error/403.jsp").forward(request, response);
        }
    }
}