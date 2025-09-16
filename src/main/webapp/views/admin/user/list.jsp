<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>Quản lý Người dùng</title>

<div class="container mt-4">
	<div class="card">
		<div class="card-header">
			<div class="d-flex justify-content-between align-items-center">
				<h3>Danh sách Người dùng</h3>
				<a href="${pageContext.request.contextPath}/admin/users/add"
					class="btn btn-success">Thêm mới</a>
			</div>
			<hr>
			<form action="${pageContext.request.contextPath}/admin/users/list"
				method="get" class="row g-3">
				<div class="col-md-10">
					<input type="text" class="form-control" name="keyword"
						placeholder="Nhập username hoặc fullname để tìm kiếm..."
						value="${keyword}">
				</div>
				<div class="col-md-2">
					<button type="submit" class="btn btn-primary w-100">Tìm
						kiếm</button>
				</div>
			</form>
		</div>
		<div class="card-body">
			<div class="table-responsive">
				<table class="table table-bordered table-hover">
					<thead class="table-dark">
						<tr>
							<th>ID</th>
							<th>Username</th>
							<th>Họ và Tên</th>
							<th>Email</th>
							<th>Vai trò</th>
							<th>Hành động</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="u" items="${userList}">
							<tr>
								<td>${u.id}</td>
								<td>${u.username}</td>
								<td><c:out value="${u.fullname}" /></td>
								<td>${u.email}</td>
								<td><c:if test="${u.roleid == 1}">
										<span class="badge bg-secondary">User</span>
									</c:if> <c:if test="${u.roleid == 2}">
										<span class="badge bg-info">Manager</span>
									</c:if> <c:if test="${u.roleid == 3}">
										<span class="badge bg-primary">Admin</span>
									</c:if></td>
								<td><a
									href="${pageContext.request.contextPath}/admin/users/edit?id=${u.id}"
									class="btn btn-sm btn-warning">Sửa</a> <c:if
										test="${sessionScope.user.id != u.id}">
										<%-- Ngăn admin tự xóa mình --%>
										<a
											href="${pageContext.request.contextPath}/admin/users/delete?id=${u.id}"
											class="btn btn-sm btn-danger"
											onclick="return confirm('Bạn có chắc muốn xóa người dùng \'${u.username}\' không?')">Xóa</a>
									</c:if></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>