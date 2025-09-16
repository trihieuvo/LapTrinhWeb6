<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<title>${formAction == 'create' ? 'Thêm' : 'Cập nhật'}Video</title>

<div class="container mt-4">
	<div class="row justify-content-center">
		<div class="col-md-8">
			<div class="card">
				<div class="card-header">
					<h3>${formAction == 'create' ? 'Tạo video mới' : 'Cập nhật video'}</h3>
				</div>
				<div class="card-body">
					<form
						action="${pageContext.request.contextPath}/admin/videos/${formAction}"
						method="post" enctype="multipart/form-data">
						<c:if test="${formAction == 'update'}">
							<input type="hidden" name="id" value="${video.id}">
						</c:if>
						<div class="mb-3">
							<label for="title" class="form-label">Tiêu đề</label> <input
								type="text" class="form-control" id="title" name="title"
								value="<c:out value='${video.title}' />" required>
						</div>
						<div class="mb-3">
							<label for="youtubeId" class="form-label">YouTube ID</label> <input
								type="text" class="form-control" id="youtubeId" name="youtubeId"
								value="${video.youtubeId}" required>
						</div>
						<div class="mb-3">
							<label for="description" class="form-label">Mô tả</label>
							<textarea class="form-control" id="description"
								name="description" rows="3"><c:out
									value='${video.description}' /></textarea>
						</div>
						<div class="mb-3">
							<label for="posterFile" class="form-label">Poster</label> <input
								class="form-control" type="file" id="posterFile"
								name="posterFile" accept="image/*">
							<c:if test="${formAction == 'update' && not empty video.poster}">
								<p class="mt-2">Ảnh hiện tại:</p>
								<img
									src="${pageContext.request.contextPath}/images/posters/${video.poster}"
									class="current-icon">
							</c:if>
						</div>
						<button type="submit" class="btn btn-primary">Lưu lại</button>
						<a href="${pageContext.request.contextPath}/admin/videos"
							class="btn btn-secondary">Hủy bỏ</a>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>