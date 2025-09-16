package com.votrihieu.BAI04.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String IMAGE_DIRECTORY = "D:/WEB/image";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Lấy tên file từ URL (ví dụ: /images/my-icon.png -> my-icon.png)
		String filename = request.getPathInfo().substring(1);
		File file = new File(IMAGE_DIRECTORY, filename);

		if (!file.exists() || file.isDirectory()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // Trả về lỗi 404 nếu không tìm thấy file
			return;
		}

		// Xác định loại content (MIME type) của file ảnh
		String mimeType = getServletContext().getMimeType(file.getAbsolutePath());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);

		// Set header để trình duyệt biết kích thước file
		response.setContentLength((int) file.length());

		// Đọc file và ghi vào response output stream
		Files.copy(file.toPath(), response.getOutputStream());
	}
}