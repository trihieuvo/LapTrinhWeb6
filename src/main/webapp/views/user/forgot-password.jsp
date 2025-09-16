<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>Quên mật khẩu</title>

<div class="login-container">

	<c:if test="${empty stage or stage == 'send'}">
		<%-- Giai đoạn 1: Nhập Email --%>
		<h2 class="text-center mb-4">Quên Mật Khẩu</h2>
		<p class="text-muted text-center">Vui lòng nhập email của bạn để
			nhận mã OTP.</p>

		<c:if test="${not empty error}">
			<div class="alert alert-danger">${error}</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/forgot-password"
			method="post">
			<input type="hidden" name="action" value="send-otp">
			<div class="mb-3">
				<label for="email" class="form-label">Email:</label> <input
					type="email" class="form-control" id="email" name="email" required>
			</div>
			<button type="submit" class="btn btn-primary w-100">Gửi mã
				OTP</button>
		</form>
	</c:if>

	<c:if test="${stage == 'verify'}">
		<%-- Giai đoạn 2: Nhập OTP và Mật khẩu mới --%>
		<h2 class="text-center mb-4">Xác thực OTP</h2>
		<p class="text-muted text-center">${message}</p>

		<c:if test="${not empty error}">
			<div class="alert alert-danger">${error}</div>
		</c:if>

		<form action="${pageContext.request.contextPath}/forgot-password"
			method="post">
			<input type="hidden" name="action" value="verify-otp">
			<div class="mb-3">
				<label for="otp" class="form-label">Mã OTP:</label> <input
					type="text" class="form-control" id="otp" name="otp" required>
			</div>
			<div class="mb-3">
				<label for="newPassword" class="form-label">Mật khẩu mới:</label> <input
					type="password" class="form-control" id="newPassword"
					name="newPassword" required>
			</div>
			<div class="mb-3">
				<label for="confirmPassword" class="form-label">Xác nhận mật
					khẩu mới:</label> <input type="password" class="form-control"
					id="confirmPassword" name="confirmPassword" required>
			</div>
			<button type="submit" class="btn btn-primary w-100">Đặt lại
				mật khẩu</button>
		</form>
	</c:if>

	<div class="text-center mt-3">
		<a href="${pageContext.request.contextPath}/login">Quay lại trang
			đăng nhập</a>
	</div>
</div>