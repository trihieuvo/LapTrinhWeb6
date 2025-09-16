<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>Quản lý Danh mục</title>

<div class="container mt-4">
	<div class="card">
		<div class="card-header">
			<div class="d-flex justify-content-between align-items-center">
				<h3>Danh sách Danh mục</h3>
				<a href="${pageContext.request.contextPath}/admin/categories/add"
					class="btn btn-success">Thêm mới</a>
			</div>
			<hr>
			<form
				action="${pageContext.request.contextPath}/admin/categories/list"
				method="get" class="row g-3">
				<div class="col-md-10">
					<input type="text" class="form-control" name="keyword"
						placeholder="Nhập tên danh mục để tìm kiếm..." value="${keyword}">
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
							<th>Icon</th>
							<th>Tên Danh mục</th>
							<th>Người tạo</th>
							<th>Hành động</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="cat" items="${categoryList}">
							<tr>
								<td>${cat.cateId}</td>
								<td><c:if test="${not empty cat.icons}">
										<img
											src="${pageContext.request.contextPath}/images/${cat.icons}"
											alt="Icon" class="icon-image">
									</c:if> <c:if test="${empty cat.icons}">(Không có ảnh)</c:if></td>
								<td><c:out value="${cat.cateName}" /></td>
								<td>${cat.user.username}</td>
								<td><a
									href="${pageContext.request.contextPath}/admin/categories/edit?id=${cat.cateId}"
									class="btn btn-sm btn-warning">Sửa</a> <a
									href="${pageContext.request.contextPath}/admin/categories/delete?id=${cat.cateId}"
									class="btn btn-sm btn-danger"
									onclick="return confirm('Bạn có chắc muốn xóa danh mục \'${cat.cateName}\' không?')">Xóa</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>