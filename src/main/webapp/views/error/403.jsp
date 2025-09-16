<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<title>Lỗi 403 - Truy cập bị từ chối</title>

<div class="container error-container">
    <h1>Lỗi 403 - Truy Cập Bị Từ Chối</h1>
    <p class="lead">${not empty message ? message : 'Bạn không có quyền truy cập vào tài nguyên này.'}</p>
    <div class="mt-4">
        <button onclick="history.back()" class="btn btn-secondary">Quay lại trang trước</button>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Về trang đăng nhập</a>
    </div>
</div>