<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>Đăng nhập</title>

<div class="login-container">
	<h2 class="text-center mb-4">Đăng Nhập</h2>

	<c:if test="${not empty error}">
		<div class="alert alert-danger" role="alert">${error}</div>
	</c:if>

	<form action="${pageContext.request.contextPath}/login" method="post">
		<div class="mb-3">
			<label for="username" class="form-label">Tên đăng nhập:</label> <input
				type="text" class="form-control" id="username" name="username"
				required>
		</div>
		<div class="mb-3">
			<label for="password" class="form-label">Mật khẩu:</label> <input
				type="password" class="form-control" id="password" name="password"
				required>
		</div>
		<button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
	</form>
	<div class="d-flex justify-content-between mt-3">
		<a href="${pageContext.request.contextPath}/forgot-password">Quên
			mật khẩu?</a> <a href="${pageContext.request.contextPath}/register">Đăng
			ký tài khoản</a>
	</div>
</div>