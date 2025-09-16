<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>Trang chủ</title>

<div class="container">
	<div class="d-flex justify-content-between align-items-center mb-4">
		<h1>${viewName}</h1>
	</div>

	<a href="${pageContext.request.contextPath}/category/add"
		class="btn btn-success mb-3">Thêm Category mới</a>

	<div class="table-responsive">
		<table class="table table-bordered table-striped table-hover">
			<thead class="table-dark">
				<tr>
					<th>ID</th>
					<th>Tên Category</th>
					<th>Icon</th>
					<c:if test="${sessionScope.user.roleid != 2}">
						<th>Người tạo</th>
					</c:if>
					<th>Hành động</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="cat" items="${categories}">
					<tr>
						<td>${cat.cateId}</td>
						<td><c:out value="${cat.cateName}" /></td>
						<td><c:if test="${not empty cat.icons}">
								<img
									src="${pageContext.request.contextPath}/images/${cat.icons}"
									alt="Icon" class="icon-image">
							</c:if> <c:if test="${empty cat.icons}">
                                (Không có ảnh)
                            </c:if></td>
						<c:if test="${sessionScope.user.roleid != 2}">
							<td><c:out value="${cat.user.username}" /></td>
						</c:if>
						<td class="actions"><a
							href="${pageContext.request.contextPath}/category/edit?id=${cat.cateId}"
							class="btn btn-sm btn-warning">Sửa</a> <a
							href="${pageContext.request.contextPath}/category/delete?id=${cat.cateId}"
							class="btn btn-sm btn-danger delete"
							onclick="return confirm('Bạn có chắc chắn muốn xóa category \'${cat.cateName}\' không?')">Xóa</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>