<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<title>${formAction == 'create' ? 'Thêm' : 'Cập nhật'} Category</title>

<div class="form-container">
    <h2 class="text-center mb-4">${formAction == 'create' ? 'Thêm Category Mới' : 'Cập Nhật Category'}</h2>
    
    <form action="${pageContext.request.contextPath}/category/${formAction}" method="post" enctype="multipart/form-data">
        
        <c:if test="${formAction == 'update'}">
            <input type="hidden" name="cateId" value="${category.cateId}" />
            <input type="hidden" name="oldIcon" value="${category.icons}" />
        </c:if>
        
        <div class="mb-3">
            <label for="cateName" class="form-label">Tên Category:</label>
            <input type="text" class="form-control" id="cateName" name="cateName" value="<c:out value='${category.cateName}' />" required>
        </div>
        
        <div class="mb-3">
            <label for="iconFile" class="form-label">Icon (Ảnh):</label>
            <input type="file" class="form-control" id="iconFile" name="iconFile" accept="image/*">
            
            <c:if test="${formAction == 'update' && not empty category.icons}">
                <p class="mt-2">Ảnh hiện tại:</p>
                <img src="${pageContext.request.contextPath}/images/${category.icons}" alt="Icon" class="current-icon">
            </c:if>
        </div>
        
        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-primary">Lưu lại</button>
            <a href="${pageContext.request.contextPath}/${sessionScope.user.roleid == 1 ? 'user' : (sessionScope.user.roleid == 2 ? 'manager' : 'admin')}/home" class="btn btn-secondary">Hủy bỏ</a>
        </div>
    </form>
</div>