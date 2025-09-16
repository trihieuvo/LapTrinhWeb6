package com.votrihieu.BAI04.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.votrihieu.BAI04.dao.UserDAO;
import com.votrihieu.BAI04.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/profile/*")
@MultipartConfig // Bắt buộc để xử lý upload file
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String UPLOAD_DIRECTORY = "D:/WEB/image/avatars";
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action != null && action.equals("/update")) {
            updateProfile(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void updateProfile(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Lấy thông tin từ form
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        
        // Xử lý upload file
        Part filePart = request.getPart("avatarFile");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // Nếu người dùng có upload file mới
        if (fileName != null && !fileName.isEmpty()) {
            if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
                File oldFile = new File(UPLOAD_DIRECTORY, currentUser.getAvatar());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
            
            File file = new File(UPLOAD_DIRECTORY, fileName);
            try (var input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
           
            currentUser.setAvatar(fileName);
        }
        
        currentUser.setFullname(fullname);
        currentUser.setPhone(phone);
        
        userDAO.update(currentUser);
        
        session.setAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}