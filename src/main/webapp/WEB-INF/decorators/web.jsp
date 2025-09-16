<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title><sitemesh:write property='title' /> | Web App</title>

<!-- Bootstrap 5 CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- CSS tùy chỉnh của chúng ta -->
<link href="${pageContext.request.contextPath}/templates/css/style.css"
	rel="stylesheet">
</head>
<body>
	<%@ include file="/common/web/header.jsp"%>

	<div class="d-flex" id="wrapper">
		<c:if test="${sessionScope.user.roleid == 3}">
			<%@ include file="/common/admin/sidebar.jsp"%>
		</c:if>

		<div id="page-content-wrapper" class="w-100">
			<main class="main-content container-fluid">
				<sitemesh:write property='body' />
			</main>
		</div>
	</div>

	<%@ include file="/common/web/footer.jsp"%>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>