-- ========================
-- 1️⃣ BẢNG CHỨC VỤ
-- ========================
CREATE TABLE ChucVu (
    maChucVu INT IDENTITY(1,1) PRIMARY KEY,
    tenChucVu NVARCHAR(50) NOT NULL
);

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

-- ========================
-- 4️⃣ KHÁCH HÀNG
-- ========================
CREATE TABLE KhachHang (
    maKH CHAR(10) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(15)
);

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

-- ========================
-- 6️⃣ HÓA ĐƠN
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

-- ========================
-- 7️⃣ THANH TOÁN
-- ========================
CREATE TABLE ThanhToan (
    maThanhToan CHAR(10) PRIMARY KEY,
    maHD CHAR(10) NOT NULL UNIQUE,
    phuongThuc NVARCHAR(50),
    ngayThanhToan DATETIME DEFAULT GETDATE(),
    trangThai NVARCHAR(50),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD)
);

-- ========================
-- 8️⃣ ĐẠO DIỄN
-- ========================
CREATE TABLE DaoDien (
    maDD CHAR(10) PRIMARY KEY,
    tenDD NVARCHAR(100),
    quocTich NVARCHAR(50)
);

-- ========================
-- 9️⃣ THỂ LOẠI
-- ========================
CREATE TABLE TheLoai (
    maTheLoai INT IDENTITY(1,1) PRIMARY KEY,
    tenTheLoai NVARCHAR(100) NOT NULL
);

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

-- ========================
-- 1️⃣1️⃣ LOẠI PHÒNG
-- ========================
CREATE TABLE LoaiPhong (
    maLoaiPhong INT IDENTITY(1,1) PRIMARY KEY,
    tenLoaiPhong NVARCHAR(100) NOT NULL
);

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

-- ========================
-- 1️⃣4️⃣ GHẾ NGỒI
-- ========================
CREATE TABLE GheNgoi (
    maGhe CHAR(10) PRIMARY KEY,
    hang NVARCHAR(10),
    cot INT,
    trangThai NVARCHAR(20)
);

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

-- ========================
-- 1️⃣6️⃣ CHI TIẾT HÓA ĐƠN
-- ========================
CREATE TABLE ChiTietHoaDon (
    maCTHD INT IDENTITY(1,1) PRIMARY KEY,
    maHD CHAR(10) NOT NULL,
    maVe CHAR(10) NOT NULL,
    soLuong INT DEFAULT 1,
    donGia DECIMAL(10,2),
    thanhTien AS (soLuong * donGia) PERSISTED,
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maVe) REFERENCES VeXemPhim(maVe)
);
