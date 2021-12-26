create database dbQLSV

use dbQLSV

create table Lop
(
IDLop int primary key,
TenLop nvarchar(50)
)
create table SinhVien
(
IDSinhVien int primary key,
TenSinhVien nvarchar(50),
NgaySinh date,
DiaChi nvarchar(50),
QueQuan nvarchar(50),
SDT nvarchar(50),
GioiTinh nvarchar(50),
IDLop int foreign key references Lop(IDLop)
)

insert into Lop values
(1,N'Công nghệ thông tin'),
(2,N'Công nghệ thực phẩm'),
(3,N'Cơ khí'),
(4,N'Hoá'),
(5,N'Quản lý dự án');

insert into SinhVien values
(1,N'Nguyễn A','20000101',N'1 Lê Duẩn',N'Đà Nẵng','0900000001',N'Nam',1),
(2,N'Nguyễn B','20000102',N'2 Lê Duẩn',N'Huế','0900000002',N'Nữ',1),
(3,N'Nguyễn C','20000103',N'3 Lê Duẩn',N'Quảng Trị','0900000003',N'Nam',2),
(4,N'Nguyễn D','20000104',N'4 Lê Duẩn',N'Quảng Bình','0900000004',N'Nam',2),
(5,N'Nguyễn E','20000105',N'5 Lê Duẩn',N'Quảng Nam','0900000005',N'Nữ',3),
(6,N'Nguyễn F','20000106',N'6 Lê Duẩn',N'Hà Tĩnh','0900000006',N'Nữ',3),
(7,N'Nguyễn G','20000107',N'7 Lê Duẩn',N'Hà Nội','0900000007',N'Nam',4),
(8,N'Nguyễn H','20000108',N'8 Lê Duẩn',N'Hồ Chí Minh','0900000008',N'Nữ',4),
(9,N'Nguyễn I','20000109',N'9 Lê Duẩn',N'Đà Nẵng','0900000009',N'Nữ',5),
(10,N'Nguyễn J','20000110',N'10 Lê Duẩn',N'Đà Nẵng','0900000010',N'Nam',5);

select * from Lop
select * from SinhVien
