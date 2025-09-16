package com.votrihieu.BAI04.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.votrihieu.BAI04.dao.VideoDAO;
import com.votrihieu.BAI04.model.User;
import com.votrihieu.BAI04.model.Video;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/admin/videos/*")
@MultipartConfig
public class AdminVideoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VideoDAO videoDAO;
	private static final String UPLOAD_DIRECTORY = "D:/WEB/image/posters";

	@Override
	public void init() throws ServletException {
		videoDAO = new VideoDAO();
		File uploadDir = new File(UPLOAD_DIRECTORY);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
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
			deleteVideo(request, response);
			break;
		default:
			listVideos(request, response);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		switch (pathInfo) {
		case "/create":
			insertVideo(request, response);
			break;
		case "/update":
			updateVideo(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	private void listVideos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyword = request.getParameter("keyword");
		List<Video> videoList = (keyword != null && !keyword.trim().isEmpty()) ? videoDAO.searchByTitle(keyword)
				: videoDAO.findAll();
		request.setAttribute("videoList", videoList);
		request.setAttribute("keyword", keyword);
		request.getRequestDispatcher("/views/admin/video/list.jsp").forward(request, response);
	}

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("video", new Video());
		request.setAttribute("formAction", "create");
		request.getRequestDispatcher("/views/admin/video/add-edit.jsp").forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("video", videoDAO.findById(id));
		request.setAttribute("formAction", "update");
		request.getRequestDispatcher("/views/admin/video/add-edit.jsp").forward(request, response);
	}

	private void insertVideo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Video video = new Video();
		video.setTitle(request.getParameter("title"));
		video.setYoutubeId(request.getParameter("youtubeId"));
		video.setDescription(request.getParameter("description"));
		video.setUser((User) request.getSession().getAttribute("user"));

		Part filePart = request.getPart("posterFile");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		if (fileName != null && !fileName.isEmpty()) {
			File file = new File(UPLOAD_DIRECTORY, fileName);
			Files.copy(filePart.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			video.setPoster(fileName);
		}

		videoDAO.insert(video);
		response.sendRedirect(request.getContextPath() + "/admin/videos/list");
	}

	private void updateVideo(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		Video video = videoDAO.findById(id);
		video.setTitle(request.getParameter("title"));
		video.setYoutubeId(request.getParameter("youtubeId"));
		video.setDescription(request.getParameter("description"));

		Part filePart = request.getPart("posterFile");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		if (fileName != null && !fileName.isEmpty()) {
			// Xóa poster cũ
			String oldPoster = video.getPoster();
			if (oldPoster != null && !oldPoster.isEmpty()) {
				new File(UPLOAD_DIRECTORY, oldPoster).delete();
			}
			// Lưu poster mới
			File file = new File(UPLOAD_DIRECTORY, fileName);
			Files.copy(filePart.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			video.setPoster(fileName);
		}

		videoDAO.update(video);
		response.sendRedirect(request.getContextPath() + "/admin/videos/list");
	}

	private void deleteVideo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Video video = videoDAO.findById(id);
		if (video != null) {
			String poster = video.getPoster();
			videoDAO.delete(id);
			if (poster != null && !poster.isEmpty()) {
				new File(UPLOAD_DIRECTORY, poster).delete();
			}
		}
		response.sendRedirect(request.getContextPath() + "/admin/videos/list");
	}
}