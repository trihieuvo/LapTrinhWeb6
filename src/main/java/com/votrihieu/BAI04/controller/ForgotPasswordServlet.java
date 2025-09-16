package com.votrihieu.BAI04.controller;

import java.io.IOException;
import java.util.Date;

import com.votrihieu.BAI04.dao.UserDAO;
import com.votrihieu.BAI04.model.User;
import com.votrihieu.BAI04.utils.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAO();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/user/forgot-password.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action.equals("send-otp")) {
			sendOtp(request, response);
		} else if (action.equals("verify-otp")) {
			verifyOtpAndResetPassword(request, response);
		}
	}

	private void sendOtp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		User user = userDAO.findByEmail(email);

		if (user == null) {
			request.setAttribute("error", "Email không tồn tại trong hệ thống!");
			doGet(request, response);
			return;
		}

		String otp = EmailUtil.generateOtp();
		EmailUtil.sendOtpEmail(email, otp);

		// Lưu OTP và thời gian hết hạn vào DB
		user.setResetToken(otp);
		user.setTokenExpiryDate(new Date(System.currentTimeMillis() + 10 * 60 * 1000)); //  10 phút
		userDAO.update(user);

		// Lưu email vào session để bước sau xác thực
		HttpSession session = request.getSession();
		session.setAttribute("resetEmail", email);

		request.setAttribute("stage", "verify");
		request.setAttribute("message", "Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra.");
		doGet(request, response);
	}

	private void verifyOtpAndResetPassword(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String email = (String) session.getAttribute("resetEmail");
		String otp = request.getParameter("otp");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");

		User user = userDAO.findByEmail(email);

		// --- VALIDATION ---
		if (!newPassword.equals(confirmPassword)) {
			request.setAttribute("error", "Mật khẩu mới không khớp!");
		} else if (user == null || !otp.equals(user.getResetToken())) {
			request.setAttribute("error", "Mã OTP không chính xác!");
		} else if (user.getTokenExpiryDate().before(new Date())) {
			request.setAttribute("error", "Mã OTP đã hết hạn!");
		} else {
			// --- PROCESS ---
			user.setPassword(newPassword);
			user.setResetToken(null); 
			user.setTokenExpiryDate(null);
			userDAO.update(user);

			session.removeAttribute("resetEmail");
			session.setAttribute("successMessage", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại.");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// Nếu có lỗi, quay lại trang xác thực
		request.setAttribute("stage", "verify");
		doGet(request, response);
	}
}