<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<title>${formAction == 'create' ? 'Thêm' : 'Cập nhật'} Người dùng</title>

<div class="container mt-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <h3>${formAction == 'create' ? 'Tạo tài khoản mới' : 'Cập nhật thông tin'}</h3>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/users/${formAction}" method="post">
                        <c:if test="${formAction == 'update'}">
                            <input type="hidden" name="id" value="${user.id}">
                        </c:if>

                        <div class="mb-3">
                            <label for="username" class="form-label">Tên đăng nhập</label>
                            <input type="text" class="form-control" id="username" name="username" value="${user.username}" ${formAction == 'update' ? 'readonly' : 'required'}>
                        </div>
                        
                        <c:if test="${formAction == 'create'}">
                             <div class="mb-3">
                                <label for="password" class="form-label">Mật khẩu</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                        </c:if>
                        
                        <div class="mb-3">
                            <label for="fullname" class="form-label">Họ và Tên</label>
                            <input type="text" class="form-control" id="fullname" name="fullname" value="${user.fullname}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" value="${user.email}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="phone" class="form-label">Số điện thoại</label>
                            <input type="text" class="form-control" id="phone" name="phone" value="${user.phone}">
                        </div>

                        <div class="mb-3">
                            <label for="roleid" class="form-label">Vai trò</label>
                            <select class="form-select" id="roleid" name="roleid">
                                <option value="1" ${user.roleid == 1 ? 'selected' : ''}>User</option>
                                <option value="2" ${user.roleid == 2 ? 'selected' : ''}>Manager</option>
                                <option value="3" ${user.roleid == 3 ? 'selected' : ''}>Admin</option>
                            </select>
                        </div>
                        
                        <button type="submit" class="btn btn-primary">Lưu lại</button>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Hủy bỏ</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>