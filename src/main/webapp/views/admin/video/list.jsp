<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<title>Quản lý Video</title>

<div class="container mt-4">
    <div class="card">
        <div class="card-header">
            <div class="d-flex justify-content-between align-items-center">
                <h3>Danh sách Video</h3>
                <a href="${pageContext.request.contextPath}/admin/videos/add" class="btn btn-success">Thêm mới</a>
            </div>
            <hr>
            <form action="${pageContext.request.contextPath}/admin/videos/list" method="get" class="row g-3">
                <div class="col-md-10"><input type="text" class="form-control" name="keyword" placeholder="Nhập tiêu đề video để tìm kiếm..." value="${keyword}"></div>
                <div class="col-md-2"><button type="submit" class="btn btn-primary w-100">Tìm kiếm</button></div>
            </form>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Poster</th>
                            <th>Tiêu đề</th>
                            <th>YouTube ID</th>
                            <th>Người tạo</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${videoList}">
                            <tr>
                                <td>${v.id}</td>
                                <td><img src="${pageContext.request.contextPath}/images/posters/${v.poster}" class="icon-image"></td>
                                <td><c:out value="${v.title}" /></td>
                                <td>${v.youtubeId}</td>
                                <td>${v.user.username}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/videos/edit?id=${v.id}" class="btn btn-sm btn-warning">Sửa</a>
                                    <a href="${pageContext.request.contextPath}/admin/videos/delete?id=${v.id}" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa video này không?')">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>