<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<title>Hồ sơ cá nhân</title>

<div class="container mt-5">
	<div class="row">
		<!-- Cột Avatar -->
		<div class="col-md-4 text-center">
			<c:if test="${not empty sessionScope.user.avatar}">
				<img
					src="${pageContext.request.contextPath}/images/avatars/${sessionScope.user.avatar}"
					class="img-fluid rounded-circle" alt="Avatar"
					style="width: 200px; height: 200px; object-fit: cover; border: 4px solid #fff; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);">
			</c:if>
			<c:if test="${empty sessionScope.user.avatar}">
				<img src="https://via.placeholder.com/200"
					class="img-fluid rounded-circle" alt="Avatar mặc định">
			</c:if>
			<h3 class="mt-3">${sessionScope.user.fullname}</h3>
			<p class="text-muted">@${sessionScope.user.username}</p>
		</div>

		<!-- Cột Form thông tin -->
		<div class="col-md-8">
			<div class="card">
				<div class="card-header">
					<h4>Chỉnh sửa thông tin cá nhân</h4>
				</div>
				<div class="card-body">
					<!-- Hiển thị thông báo thành công (flash message) -->
					<c:if test="${not empty sessionScope.successMessage}">
						<div class="alert alert-success" role="alert">
							${sessionScope.successMessage}</div>
						<%-- Xóa message sau khi hiển thị để không hiện lại --%>
						<c:remove var="successMessage" scope="session" />
					</c:if>

					<form action="${pageContext.request.contextPath}/profile/update"
						method="post" enctype="multipart/form-data">
						<div class="mb-3">
							<label class="form-label">Email (Không thể thay đổi)</label> <input
								type="email" class="form-control"
								value="${sessionScope.user.email}" readonly>
						</div>

						<div class="mb-3">
							<label for="fullname" class="form-label">Họ và Tên</label> <input
								type="text" class="form-control" id="fullname" name="fullname"
								value="${sessionScope.user.fullname}" required>
						</div>

						<div class="mb-3">
							<label for="phone" class="form-label">Số điện thoại</label> <input
								type="text" class="form-control" id="phone" name="phone"
								value="${sessionScope.user.phone}">
						</div>

						<div class="mb-3">
							<label for="avatarFile" class="form-label">Thay đổi ảnh
								đại diện</label> <input class="form-control" type="file" id="avatarFile"
								name="avatarFile" accept="image/*">
						</div>

						<button type="submit" class="btn btn-primary">Lưu thay
							đổi</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>