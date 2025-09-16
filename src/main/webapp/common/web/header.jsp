<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<header>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="${pageContext.request.contextPath}/">Project
				BAI06</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav ms-auto">
					<c:if test="${not empty sessionScope.user}">
						<!-- Dropdown Menu cho User -->
						<li class="nav-item dropdown"><a
							class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
							role="button" data-bs-toggle="dropdown" aria-expanded="false">
								<c:if test="${not empty sessionScope.user.avatar}">
									<img
										src="${pageContext.request.contextPath}/images/avatars/${sessionScope.user.avatar}"
										alt="Avatar"
										style="width: 30px; height: 30px; border-radius: 50%; object-fit: cover; margin-right: 5px;">
								</c:if> <strong>${sessionScope.user.fullname}</strong>
						</a>
							<ul class="dropdown-menu dropdown-menu-end"
								aria-labelledby="navbarDropdown">
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/profile">Hồ sơ cá
										nhân</a></li>
								<li><hr class="dropdown-divider"></li>
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/logout"
									onclick="return confirm('Bạn có chắc chắn muốn đăng xuất không?')">Đăng
										xuất</a></li>
							</ul></li>
					</c:if>
					<c:if test="${empty sessionScope.user}">
						<li class="nav-item"><a class="nav-link"
							href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
					</c:if>
				</ul>
			</div>
		</div>
	</nav>
</header>