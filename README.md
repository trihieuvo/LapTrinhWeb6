# Project Lập Trình Web 

Đây là dự án môn Lập trình Web, xây dựng một ứng dụng web hoàn chỉnh sử dụng kiến trúc MVC với Servlet, JSP và JPA/Hibernate. Ứng dụng cho phép quản lý người dùng, danh mục và video với hệ thống phân quyền.

## Các tính năng chính

- **Xác thực & Phân quyền:**
  - Đăng nhập, Đăng xuất, Đăng ký tài khoản.
  - Quên mật khẩu qua email (sử dụng SMTP và mã OTP).
  - Phân quyền theo vai trò (User, Manager, Admin) sử dụng Filter.

- **Người dùng: User/ Manager**
  - Cập nhật thông tin cá nhân (Họ tên, số điện thoại).
  - Thay đổi ảnh đại diện (Avatar) với chức năng upload file.

- **Trang Admin:**
  - Giao diện quản trị riêng với menu điều hướng.
  - **Quản lý Người dùng:** CRUD (Thêm, Xem, Sửa, Xóa) và tìm kiếm người dùng.
  - **Quản lý Danh mục:** CRUD và tìm kiếm danh mục, tích hợp upload ảnh icon.
  - **Quản lý Video:** CRUD và tìm kiếm video, tích hợp upload ảnh poster.

## Công nghệ sử dụng

- **Backend:**
  - **Ngôn ngữ:** Java 24
  - **Web Server:** Apache Tomcat 10.1
  - **Framework/API:** Jakarta Servlet 6.0, Jakarta Server Pages (JSP) 3.1
  - **ORM:** Jakarta Persistence (JPA) 3.0 với Hibernate 6.4 làm provider.
  - **Database:** Microsoft SQL Server
  - **Build Tool:** Apache Maven 3.9

- **Frontend:**
  - HTML5, CSS3
  - Bootstrap 5
  - Jakarta Standard Tag Library (JSTL)

## Hướng dẫn Cài đặt và Chạy dự án

#### 1. Cài đặt môi trường:
- JDK 24 hoặc cao hơn.
- Apache Maven 3.9 hoặc cao hơn.
- Apache Tomcat 10.1 hoặc cao hơn.
- Microsoft SQL Server.

#### 2. Cấu hình Cơ sở dữ liệu:
- Mở SQL Server Management Studio (SSMS).
- Thực thi file SQL `database_setup.sql` (cung cấp bên dưới) để tạo database `BAI04_DB` và các bảng cần thiết.
- **Lưu ý:** Script được thiết lập để sử dụng tài khoản `sa` với mật khẩu `123`.

#### 3. Cấu hình Thư mục lưu trữ file:
Ứng dụng yêu cầu một cấu trúc thư mục cụ thể trên ổ đĩa `D:` 
- D:\WEB\image


#### 4. Cấu hình Project:
- **Kết nối Database:** Mở file `src/main/resources/META-INF/persistence.xml` và đảm bảo các thông tin `jakarta.persistence.jdbc.url`, `user`, `password` khớp với cấu hình SQL Server của bạn.
- **Cấu hình Email (cho chức năng Quên mật khẩu):** Mở file `src/main/java/com/votrihieu/BAI04/utils/EmailUtil.java` và thay đổi các hằng số `FROM_EMAIL` và `APP_PASSWORD` bằng thông tin tài khoản email của bạn. (Lưu ý: Nếu dùng Gmail, bạn cần tạo "Mật khẩu ứng dụng").


---

Phần 2: Script SQL tạo bảng
Đây là script SQL bạn có thể đặt trong một file tên là database_setup.sql và đưa lên GitHub cùng với README. Script này sẽ xóa database cũ (nếu có) và tạo lại toàn bộ cấu trúc bảng.
SQL

-- SCRIPT KHỞI TẠO CƠ SỞ DỮ LIỆU VÀ CÁC BẢNG CHO PROJECT 
-- =================================================================

-- Sử dụng master để có thể thao tác với các database
USE master;
GO

-- Xóa database nếu đã tồn tại để đảm bảo môi trường sạch
IF DB_ID('BAI04_DB') IS NOT NULL
BEGIN
    ALTER DATABASE BAI04_DB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE BAI04_DB;
    PRINT 'Database BAI04_DB da duoc xoa.';
END
GO

-- Tạo database mới
CREATE DATABASE BAI04_DB;
GO
PRINT 'Database BAI04_DB da duoc tao thanh cong.';

-- Chuyển sang sử dụng database vừa tạo
USE BAI04_DB;
GO

-- 1. TẠO BẢNG USERS
-- Bảng này lưu thông tin tài khoản, vai trò và các token liên quan.
PRINT 'Dang tao bang Users...';
CREATE TABLE Users (
    id INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(50) NOT NULL UNIQUE,
    password NVARCHAR(50) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    fullname NVARCHAR(100) NULL,
    avatar NVARCHAR(255) NULL,
    roleid INT NOT NULL,
    phone NVARCHAR(20) NULL,
    createdDate DATE DEFAULT GETDATE(),
    reset_token NVARCHAR(255) NULL,
    token_expiry_date DATETIME NULL
);
GO

-- 2. TẠO BẢNG CATEGORY
-- Bảng này lưu các danh mục, mỗi danh mục thuộc về một người dùng.
PRINT 'Dang tao bang Category...';
CREATE TABLE Category (
    cate_id INT PRIMARY KEY IDENTITY(1,1),
    cate_name NVARCHAR(255) NOT NULL,
    icons NVARCHAR(255) NULL,
    userid INT NULL,
    
   
);
GO

-- 3. TẠO BẢNG VIDEOS
-- Bảng này lưu thông tin các video.
PRINT 'Dang tao bang Videos...';
CREATE TABLE Videos (
    id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL,
    youtubeId VARCHAR(50) NOT NULL,
    description NVARCHAR(MAX) NULL,
    poster NVARCHAR(255) NULL,
    userid INT NULL,


);
GO

PRINT 'Hoan tat viec tao cau truc co so du lieu!';
