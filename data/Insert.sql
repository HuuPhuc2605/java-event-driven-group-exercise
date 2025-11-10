

-- ========================
-- 1️⃣ BẢNG CHỨC VỤ
-- ========================
CREATE TABLE ChucVu (
    maChucVu INT IDENTITY(1,1) PRIMARY KEY,
    tenChucVu NVARCHAR(50) NOT NULL
);

INSERT INTO ChucVu (tenChucVu) VALUES 
('Nhân viên bán vé'),
('Kế toán'),
('Quản lý rạp');

-- ========================
-- 2️⃣ NHÂN VIÊN
-- ========================
CREATE TABLE NhanVien (
    maNV CHAR(10) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    maChucVu INT NOT NULL,
    luong DECIMAL(12,2) NOT NULL,
    soDienThoai VARCHAR(15),
    FOREIGN KEY (maChucVu) REFERENCES ChucVu(maChucVu)
);

INSERT INTO NhanVien (maNV, tenNV, maChucVu, luong, soDienThoai) VALUES
('NV001', 'Nguyễn Văn A', 1, 7000000, '0901234567'),
('NV002', 'Trần Thị B', 2, 9000000, '0912345678'),
('NV003', 'Lê Văn C', 3, 12000000, '0923456789');

-- ========================
-- 3️⃣ TÀI KHOẢN NHÂN VIÊN
-- ========================
CREATE TABLE TaiKhoanNhanVien (
    tenDangNhap VARCHAR(50) PRIMARY KEY,
    matKhau VARCHAR(100) NOT NULL,
    vaiTro NVARCHAR(50),
    maNV CHAR(10) NOT NULL UNIQUE,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

INSERT INTO TaiKhoanNhanVien (tenDangNhap, matKhau, vaiTro, maNV) VALUES
('nva', '123456', 'NhanVien', 'NV001'),
('ttb', '123456', 'KeToan', 'NV002'),
('lvc', '123456', 'Admin', 'NV003');

-- ========================
-- 4️⃣ KHÁCH HÀNG
-- ========================
CREATE TABLE KhachHang (
    maKH CHAR(10) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15)
);

INSERT INTO KhachHang (maKH, tenKH, soDienThoai) VALUES
('KH001', 'Phạm Văn D', '0987654321'),
('KH002', 'Nguyễn Thị E', '0976543210');

-- ========================
-- 5️⃣ KHUYẾN MÃI
-- ========================
CREATE TABLE KhuyenMai (
    maKM CHAR(10) PRIMARY KEY,
    tenKM NVARCHAR(100),
    moTa NVARCHAR(255),
    tiLeGiam DECIMAL(5,2),
    ngayBatDau DATE,
    ngayKetThuc DATE
);

INSERT INTO KhuyenMai (maKM, tenKM, moTa, tiLeGiam, ngayBatDau, ngayKetThuc) VALUES
('KM001', 'Giảm 10%', 'Giảm giá 10% cho tất cả khách hàng', 10, '2025-11-01', '2025-11-30'),
('KM002', 'Mua 1 tặng 1', 'Khuyến mãi mua 1 vé tặng 1 vé', 50, '2025-11-05', '2025-11-15');

-- ========================
-- 6️⃣ ĐẠO DIỄN
-- ========================
CREATE TABLE DaoDien (
    maDD CHAR(10) PRIMARY KEY,
    tenDD NVARCHAR(100),
    quocTich NVARCHAR(50)
);

INSERT INTO DaoDien (maDD, tenDD, quocTich) VALUES
('DD001', 'Christopher Nolan', 'Mỹ'),
('DD002', 'Hayao Miyazaki', 'Nhật Bản');

-- ========================
-- 7️⃣ THỂ LOẠI
-- ========================
CREATE TABLE TheLoai (
    maTheLoai INT IDENTITY(1,1) PRIMARY KEY,
    tenTheLoai NVARCHAR(100) NOT NULL
);

INSERT INTO TheLoai (tenTheLoai) VALUES
('Hành động'),
('Hoạt hình'),
('Kinh dị');

-- ========================
-- 🔟 PHIM
-- ========================
CREATE TABLE Phim (
    maPhim CHAR(10) PRIMARY KEY,
    tenPhim NVARCHAR(200) NOT NULL,
    maTheLoai INT NOT NULL,
    thoiLuong INT,
    maDD CHAR(10) NOT NULL,
    FOREIGN KEY (maTheLoai) REFERENCES TheLoai(maTheLoai),
    FOREIGN KEY (maDD) REFERENCES DaoDien(maDD)
);

INSERT INTO Phim (maPhim, tenPhim, maTheLoai, thoiLuong, maDD) VALUES
('P001', 'Inception', 1, 148, 'DD001'),
('P002', 'Spirited Away', 2, 125, 'DD002');

-- ========================
-- 1️⃣1️⃣ LOẠI PHÒNG
-- ========================
CREATE TABLE LoaiPhong (
    maLoaiPhong INT IDENTITY(1,1) PRIMARY KEY,
    tenLoaiPhong NVARCHAR(100) NOT NULL
);

INSERT INTO LoaiPhong (tenLoaiPhong) VALUES
('Phòng thường'),
('Phòng VIP');

-- ========================
-- 1️⃣2️⃣ PHÒNG CHIẾU
-- ========================
CREATE TABLE PhongChieu (
    maPhong CHAR(10) PRIMARY KEY,
    tenPhong NVARCHAR(100),
    soGhe INT,
    maLoaiPhong INT NOT NULL,
    FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong(maLoaiPhong)
);

INSERT INTO PhongChieu (maPhong, tenPhong, soGhe, maLoaiPhong) VALUES
('PC001', 'Phòng 1', 100, 1),
('PC002', 'Phòng 2', 50, 2);

-- ========================
-- 1️⃣3️⃣ LỊCH CHIẾU
-- ========================
CREATE TABLE LichChieu (
    maLich CHAR(10) PRIMARY KEY,
    ngayChieu DATE,
    gioBatDau TIME,
    gioKetThuc TIME,
    maPhim CHAR(10) NOT NULL,
    maPhong CHAR(10) NOT NULL,
    FOREIGN KEY (maPhim) REFERENCES Phim(maPhim),
    FOREIGN KEY (maPhong) REFERENCES PhongChieu(maPhong)
);

INSERT INTO LichChieu (maLich, ngayChieu, gioBatDau, gioKetThuc, maPhim, maPhong) VALUES
('LC001', '2025-11-10', '18:00', '20:30', 'P001', 'PC001'),
('LC002', '2025-11-10', '20:30', '22:35', 'P002', 'PC002');

-- ========================
-- 1️⃣4️⃣ GHẾ NGỒI
-- ========================
CREATE TABLE GheNgoi (
    maGhe CHAR(10) PRIMARY KEY,
    hang NVARCHAR(10),
    cot INT,
    trangThai NVARCHAR(20)
);

INSERT INTO GheNgoi (maGhe, hang, cot, trangThai) VALUES
('G001', 'A', 1, 'Trống'),
('G002', 'A', 2, 'Trống'),
('G003', 'B', 1, 'Trống');

-- ========================
-- 1️⃣5️⃣ VÉ XEM PHIM
-- ========================
CREATE TABLE VeXemPhim (
    maVe CHAR(10) PRIMARY KEY,
    ngayBan DATE DEFAULT GETDATE(),
    maLich CHAR(10) NOT NULL,
    maGhe CHAR(10) NOT NULL,
    donGia DECIMAL(10,2),
    FOREIGN KEY (maLich) REFERENCES LichChieu(maLich),
    FOREIGN KEY (maGhe) REFERENCES GheNgoi(maGhe)
);

INSERT INTO VeXemPhim (maVe, maLich, maGhe, donGia) VALUES
('V001', 'LC001', 'G001', 50000),
('V002', 'LC001', 'G002', 50000),
('V003', 'LC002', 'G003', 70000);

-- ========================
-- 1️⃣6️⃣ HÓA ĐƠN
-- ========================
CREATE TABLE HoaDon (
    maHD CHAR(10) PRIMARY KEY,
    ngayLap DATETIME NOT NULL DEFAULT GETDATE(),
    tongTien DECIMAL(12,2),
    maNV CHAR(10) NOT NULL,
    maKH CHAR(10) NULL,
    maKM CHAR(10) NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);

INSERT INTO HoaDon (maHD, maNV, maKH, maKM, tongTien) VALUES
('HD001', 'NV001', 'KH001', 'KM001', 90000),
('HD002', 'NV001', 'KH002', NULL, 70000);

-- ========================
-- 1️⃣7️⃣ CHI TIẾT HÓA ĐƠN
-- ========================
CREATE TABLE ChiTietHoaDon (
    maHD CHAR(10) NOT NULL,
    maVe CHAR(10) NOT NULL,
    soLuong INT DEFAULT 1,
    donGia DECIMAL(10,2),
    thanhTien AS (soLuong * donGia) PERSISTED,
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maVe) REFERENCES VeXemPhim(maVe)
);

INSERT INTO ChiTietHoaDon (maHD, maVe, soLuong, donGia) VALUES
('HD001', 'V001', 1, 50000),
('HD001', 'V002', 1, 50000),
('HD002', 'V003', 1, 70000);

-- ========================
-- 1️⃣8️⃣ THANH TOÁN
-- ========================
CREATE TABLE ThanhToan (
    maThanhToan CHAR(10) PRIMARY KEY,
    maHD CHAR(10) NOT NULL UNIQUE,
    phuongThuc NVARCHAR(50),
    ngayThanhToan DATETIME DEFAULT GETDATE(),
    trangThai NVARCHAR(50),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD)
);

INSERT INTO ThanhToan (maThanhToan, maHD, phuongThuc, trangThai) VALUES
('TT001', 'HD001', 'Tiền mặt', 'Đã thanh toán'),
('TT002', 'HD002', 'Thẻ', 'Chưa thanh toán');
