<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="bg-dark border-right p-3" id="sidebar-wrapper">
	<h4 class="text-light">Admin Panel</h4>
	<div class="list-group list-group-flush">
		<a href="#"
			class="list-group-item list-group-item-action bg-dark text-light">Dashboard</a>
		<a href="${pageContext.request.contextPath}/admin/users"
			class="list-group-item list-group-item-action bg-dark text-light">Quản
			lý User</a> <a href="${pageContext.request.contextPath}/admin/categories"
			class="list-group-item list-group-item-action bg-dark text-light">Quản
			lý Category</a>
		<%-- SỬA DÒNG NÀY --%>
		<a href="${pageContext.request.contextPath}/admin/videos"
			class="list-group-item list-group-item-action bg-dark text-light">Quản
			lý Video</a>
	</div>
</div>