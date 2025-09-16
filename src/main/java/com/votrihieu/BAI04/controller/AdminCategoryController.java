package com.votrihieu.BAI04.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.votrihieu.BAI04.dao.CategoryDAO;
import com.votrihieu.BAI04.model.Category;
import com.votrihieu.BAI04.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/admin/categories/*")
@MultipartConfig
public class AdminCategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO categoryDAO;
    private static final String UPLOAD_DIRECTORY = "D:/WEB/image";

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo() == null ? "/list" : request.getPathInfo();

        switch (pathInfo) {
            case "/add":
                showAddForm(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/delete":
                deleteCategory(request, response);
                break;
            default:
                listCategories(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        switch (pathInfo) {
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

    private void listCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Category> categoryList;
        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryList = categoryDAO.searchByName(keyword);
        } else {
            categoryList = categoryDAO.findAll();
        }
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/admin/category/list.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("category", new Category());
        request.setAttribute("formAction", "create");
        request.getRequestDispatcher("/views/admin/category/add-edit.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Category existingCategory = categoryDAO.findById(id);
        request.setAttribute("category", existingCategory);
        request.setAttribute("formAction", "update");
        request.getRequestDispatcher("/views/admin/category/add-edit.jsp").forward(request, response);
    }

    private void insertCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String cateName = request.getParameter("cateName");
        Part filePart = request.getPart("iconFile");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(UPLOAD_DIRECTORY, fileName);
            try (var input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            fileName = null;
        }
        
        Category newCategory = new Category();
        newCategory.setCateName(cateName);
        newCategory.setIcons(fileName);
        
        User currentUser = (User) request.getSession().getAttribute("user");
        newCategory.setUser(currentUser); // Gán người tạo là Admin đang đăng nhập
        
        categoryDAO.insert(newCategory);
        response.sendRedirect(request.getContextPath() + "/admin/categories/list");
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int cateId = Integer.parseInt(request.getParameter("cateId"));
        String cateName = request.getParameter("cateName");
        String oldIcon = request.getParameter("oldIcon");
        
        Category categoryToUpdate = categoryDAO.findById(cateId);
        categoryToUpdate.setCateName(cateName);

        Part filePart = request.getPart("iconFile");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        if (fileName != null && !fileName.isEmpty()) {
            File file = new File(UPLOAD_DIRECTORY, fileName);
            try (var input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            categoryToUpdate.setIcons(fileName);

            if (oldIcon != null && !oldIcon.isEmpty()) {
                File oldFile = new File(UPLOAD_DIRECTORY, oldIcon);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
        }
        
        categoryDAO.update(categoryToUpdate);
        response.sendRedirect(request.getContextPath() + "/admin/categories/list");
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Category categoryToDelete = categoryDAO.findById(id);

        if (categoryToDelete != null) {
            String iconFile = categoryToDelete.getIcons();
            categoryDAO.delete(id); // Xóa trong DB trước
            
            // Sau đó xóa file ảnh trên đĩa
            if (iconFile != null && !iconFile.isEmpty()) {
                File file = new File(UPLOAD_DIRECTORY, iconFile);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/categories/list");
    }
}