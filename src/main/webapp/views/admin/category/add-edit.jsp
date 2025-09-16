<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>${formAction == 'create' ? 'Thêm' : 'Cập nhật'}Danh mục</title>

<div class="container mt-4">
	<div class="row justify-content-center">
		<div class="col-md-8">
			<div class="card">
				<div class="card-header">
					<h3>${formAction == 'create' ? 'Tạo danh mục mới' : 'Cập nhật danh mục'}</h3>
				</div>
				<div class="card-body">
					<form
						action="${pageContext.request.contextPath}/admin/categories/${formAction}"
						method="post" enctype="multipart/form-data">
						<c:if test="${formAction == 'update'}">
							<input type="hidden" name="cateId" value="${category.cateId}">
							<input type="hidden" name="oldIcon" value="${category.icons}">
						</c:if>

						<div class="mb-3">
							<label for="cateName" class="form-label">Tên Danh mục</label> <input
								type="text" class="form-control" id="cateName" name="cateName"
								value="<c:out value='${category.cateName}' />" required>
						</div>

						<div class="mb-3">
							<label for="iconFile" class="form-label">Icon</label> <input
								class="form-control" type="file" id="iconFile" name="iconFile"
								accept="image/*">
							<c:if
								test="${formAction == 'update' && not empty category.icons}">
								<p class="mt-2">Ảnh hiện tại:</p>
								<img
									src="${pageContext.request.contextPath}/images/${category.icons}"
									alt="Icon" class="current-icon">
							</c:if>
						</div>

						<button type="submit" class="btn btn-primary">Lưu lại</button>
						<a href="${pageContext.request.contextPath}/admin/categories"
							class="btn btn-secondary">Hủy bỏ</a>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>