<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>Đăng ký tài khoản</title>

<div class="login-container">
	<h2 class="text-center mb-4">Tạo tài khoản mới</h2>

	<c:if test="${not empty error}">
		<div class="alert alert-danger" role="alert">${error}</div>
	</c:if>

	<form action="${pageContext.request.contextPath}/register"
		method="post">
		<div class="mb-3">
			<label for="username" class="form-label">Tên đăng nhập:</label> <input
				type="text" class="form-control" id="username" name="username"
				required>
		</div>
		<div class="mb-3">
			<label for="email" class="form-label">Email:</label> <input
				type="email" class="form-control" id="email" name="email" required>
		</div>
		<div class="mb-3">
			<label for="password" class="form-label">Mật khẩu:</label> <input
				type="password" class="form-control" id="password" name="password"
				required>
		</div>
		<div class="mb-3">
			<label for="confirmPassword" class="form-label">Xác nhận mật
				khẩu:</label> <input type="password" class="form-control"
				id="confirmPassword" name="confirmPassword" required>
		</div>
		<button type="submit" class="btn btn-primary w-100">Đăng ký</button>
	</form>
	<div class="text-center mt-3">
		<p>
			Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng
				nhập ngay</a>
		</p>
	</div>
</div>