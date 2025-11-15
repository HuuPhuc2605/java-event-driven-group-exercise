INSERT INTO ChucVu (tenChucVu) VALUES
(N'Quản lý'), (N'Nhân viên bán vé'), (N'Kế toán');
GO

-- =========================
-- 2. NhanVien (tham chiếu ChucVu)
-- =========================
INSERT INTO NhanVien (maNV, tenNV, maChucVu, luong, soDienThoai) VALUES
('NV001', N'Nguyễn Văn A', 1, 15000000, '0901111111'),
('NV002', N'Trần Thị B', 2,  9000000,  '0902222222'),
('NV003', N'Lê Văn C',   2,  9000000,  '0903333333');
GO

-- =========================
-- 3. TaiKhoanNhanVien (tham chiếu NhanVien)
-- =========================
INSERT INTO TaiKhoanNhanVien (tenDangNhap, matKhau, vaiTro, maNV) VALUES
('admin','admin123',N'Quản lý','NV001'),
('nv_bv1','123456',N'Nhân viên','NV002'),
('nv_bv2','123456',N'Nhân viên','NV003');
GO

-- =========================
-- 4. KhachHang
-- =========================
INSERT INTO KhachHang (maKH, tenKH, soDienThoai) VALUES
('KH001', N'Phạm Minh', '0911111001'),
('KH002', N'Hồ Lan',    '0911111002'),
('KH003', N'Võ Hưng',   '0911111003'),
('KH004', N'Đặng Tuyết','0911111004'),
('KH005', N'Nguyễn Ly', '0911111005');
GO

-- =========================
-- 5. KhuyenMai
-- =========================
INSERT INTO KhuyenMai (maKM, tenKM, moTa, tiLeGiam, ngayBatDau, ngayKetThuc) VALUES
('KM01', N'Giảm 10%', N'Ưu đãi Tết/đợt', 10.00, '2025-01-01', '2025-12-31'),
('KM02', N'Giảm 5%',  N'Ưu đãi cuối tuần', 5.00,  '2025-01-01', '2025-12-31'),
('KM03', N'Giảm 15%', N'Ưu đãi HSSV',     15.00, '2025-01-01', '2025-12-31');
GO

-- =========================
-- 6. TheLoai
-- =========================
INSERT INTO TheLoai (tenTheLoai) VALUES
(N'Hành động'), (N'Kinh dị'), (N'Tình cảm'), (N'Hoạt hình');
GO

-- =========================
-- 7. DaoDien
-- =========================
INSERT INTO DaoDien (maDD, tenDD, quocTich) VALUES
('DD01', N'Christopher Nolan', N'Mỹ'),
('DD02', N'James Wan',         N'Malaysia'),
('DD03', N'Phan Gia Nhật Linh',N'Việt Nam');
GO

-- =========================
-- 8. Phim (tham chiếu TheLoai, DaoDien)
-- =========================
INSERT INTO Phim (maPhim, tenPhim, maTheLoai, thoiLuong, maDD) VALUES
('P001', N'Avengers: Endgame', 1, 180, 'DD01'),
('P002', N'The Nun',           2, 100, 'DD02'),
('P003', N'Em và Trịnh',      3, 120, 'DD03');
GO

-- =========================
-- 9. LoaiPhong
-- =========================
INSERT INTO LoaiPhong (tenLoaiPhong) VALUES
(N'2D'), (N'3D'), (N'IMAX');
GO

-- =========================
-- 10. PhongChieu (tham chiếu LoaiPhong)
-- =========================
INSERT INTO PhongChieu (maPhong, tenPhong, maLoaiPhong) VALUES
('PC01', N'Phòng 01', 1),
('PC02', N'Phòng 02', 2);
GO

-- =========================
-- 11. GheNgoi
-- PC01: A..E (5 hàng) × 1..10 (10 cột) = 50 ghế
-- PC02: A..D (4 hàng) × 1..8 (8 cột) = 32 ghế
-- maGhe format: PC01_A1, PC02_D8, ...
-- =========================
-- PC01 (50 ghế)
INSERT INTO GheNgoi (maGhe, hang, cot, maPhong) VALUES
('PC01_A1','A',1,'PC01'), ('PC01_A2','A',2,'PC01'), ('PC01_A3','A',3,'PC01'), ('PC01_A4','A',4,'PC01'), ('PC01_A5','A',5,'PC01'),
('PC01_A6','A',6,'PC01'), ('PC01_A7','A',7,'PC01'), ('PC01_A8','A',8,'PC01'), ('PC01_A9','A',9,'PC01'), ('PC01_A10','A',10,'PC01'),
('PC01_B1','B',1,'PC01'), ('PC01_B2','B',2,'PC01'), ('PC01_B3','B',3,'PC01'), ('PC01_B4','B',4,'PC01'), ('PC01_B5','B',5,'PC01'),
('PC01_B6','B',6,'PC01'), ('PC01_B7','B',7,'PC01'), ('PC01_B8','B',8,'PC01'), ('PC01_B9','B',9,'PC01'), ('PC01_B10','B',10,'PC01'),
('PC01_C1','C',1,'PC01'), ('PC01_C2','C',2,'PC01'), ('PC01_C3','C',3,'PC01'), ('PC01_C4','C',4,'PC01'), ('PC01_C5','C',5,'PC01'),
('PC01_C6','C',6,'PC01'), ('PC01_C7','C',7,'PC01'), ('PC01_C8','C',8,'PC01'), ('PC01_C9','C',9,'PC01'), ('PC01_C10','C',10,'PC01'),
('PC01_D1','D',1,'PC01'), ('PC01_D2','D',2,'PC01'), ('PC01_D3','D',3,'PC01'), ('PC01_D4','D',4,'PC01'), ('PC01_D5','D',5,'PC01'),
('PC01_D6','D',6,'PC01'), ('PC01_D7','D',7,'PC01'), ('PC01_D8','D',8,'PC01'), ('PC01_D9','D',9,'PC01'), ('PC01_D10','D',10,'PC01'),
('PC01_E1','E',1,'PC01'), ('PC01_E2','E',2,'PC01'), ('PC01_E3','E',3,'PC01'), ('PC01_E4','E',4,'PC01'), ('PC01_E5','E',5,'PC01'),
('PC01_E6','E',6,'PC01'), ('PC01_E7','E',7,'PC01'), ('PC01_E8','E',8,'PC01'), ('PC01_E9','E',9,'PC01'), ('PC01_E10','E',10,'PC01');
GO

-- PC02 (32 ghế)
INSERT INTO GheNgoi (maGhe, hang, cot, maPhong) VALUES
('PC02_A1','A',1,'PC02'), ('PC02_A2','A',2,'PC02'), ('PC02_A3','A',3,'PC02'), ('PC02_A4','A',4,'PC02'),
('PC02_A5','A',5,'PC02'), ('PC02_A6','A',6,'PC02'), ('PC02_A7','A',7,'PC02'), ('PC02_A8','A',8,'PC02'),
('PC02_B1','B',1,'PC02'), ('PC02_B2','B',2,'PC02'), ('PC02_B3','B',3,'PC02'), ('PC02_B4','B',4,'PC02'),
('PC02_B5','B',5,'PC02'), ('PC02_B6','B',6,'PC02'), ('PC02_B7','B',7,'PC02'), ('PC02_B8','B',8,'PC02'),
('PC02_C1','C',1,'PC02'), ('PC02_C2','C',2,'PC02'), ('PC02_C3','C',3,'PC02'), ('PC02_C4','C',4,'PC02'),
('PC02_C5','C',5,'PC02'), ('PC02_C6','C',6,'PC02'), ('PC02_C7','C',7,'PC02'), ('PC02_C8','C',8,'PC02'),
('PC02_D1','D',1,'PC02'), ('PC02_D2','D',2,'PC02'), ('PC02_D3','D',3,'PC02'), ('PC02_D4','D',4,'PC02'),
('PC02_D5','D',5,'PC02'), ('PC02_D6','D',6,'PC02'), ('PC02_D7','D',7,'PC02'), ('PC02_D8','D',8,'PC02');
GO

-- =========================
-- 12. LichChieu
-- 5 lịch (phân bố 2025-01-10 .. 2025-01-13)
-- =========================
INSERT INTO LichChieu (maLich, ngayChieu, gioBatDau, gioKetThuc, maPhim, maPhong) VALUES
('LC01','2025-01-10','10:00','12:30','P001','PC01'),
('LC02','2025-01-10','14:00','16:30','P001','PC01'),
('LC03','2025-01-11','09:30','11:00','P002','PC02'),
('LC04','2025-01-12','16:00','18:00','P003','PC02'),
('LC05','2025-01-13','19:00','21:30','P001','PC01');
GO

-- =========================
-- 13. VeXemPhim (maVe = 'V_' + maGhe)
-- tạo đủ vé sẽ dùng trong 20 hóa đơn (mỗi tuyến 1..3 vé)
-- =========================
-- LC01 (PC01) giá 90000
INSERT INTO VeXemPhim (maVe, ngayBan, maLich, maGhe, donGia) VALUES
('V_PC01_A1','2025-01-10','LC01','PC01_A1',90000),
('V_PC01_A2','2025-01-10','LC01','PC01_A2',90000),
('V_PC01_A3','2025-01-10','LC01','PC01_A3',90000),
('V_PC01_A4','2025-01-10','LC01','PC01_A4',90000),
('V_PC01_A5','2025-01-10','LC01','PC01_A5',90000),
('V_PC01_B1','2025-01-10','LC01','PC01_B1',90000),
('V_PC01_B2','2025-01-10','LC01','PC01_B2',90000),
('V_PC01_B3','2025-01-10','LC01','PC01_B3',90000),
('V_PC01_B4','2025-01-10','LC01','PC01_B4',90000),
('V_PC01_C1','2025-01-10','LC01','PC01_C1',90000),
('V_PC01_C2','2025-01-10','LC01','PC01_C2',90000),
('V_PC01_C3','2025-01-10','LC01','PC01_C3',90000);

-- LC02 (PC01) giá 90000
INSERT INTO VeXemPhim (maVe, ngayBan, maLich, maGhe, donGia) VALUES
('V_PC01_A6','2025-01-10','LC02','PC01_A6',90000),
('V_PC01_A7','2025-01-10','LC02','PC01_A7',90000),
('V_PC01_A8','2025-01-10','LC02','PC01_A8',90000),
('V_PC01_A9','2025-01-10','LC02','PC01_A9',90000),
('V_PC01_A10','2025-01-10','LC02','PC01_A10',90000);

-- LC03 (PC02) giá 80000
INSERT INTO VeXemPhim (maVe, ngayBan, maLich, maGhe, donGia) VALUES
('V_PC02_A1','2025-01-11','LC03','PC02_A1',80000),
('V_PC02_A2','2025-01-11','LC03','PC02_A2',80000),
('V_PC02_A3','2025-01-11','LC03','PC02_A3',80000),
('V_PC02_A4','2025-01-11','LC03','PC02_A4',80000),
('V_PC02_A5','2025-01-11','LC03','PC02_A5',80000);

-- LC04 (PC02) giá 70000
INSERT INTO VeXemPhim (maVe, ngayBan, maLich, maGhe, donGia) VALUES
('V_PC02_B1','2025-01-12','LC04','PC02_B1',70000),
('V_PC02_B2','2025-01-12','LC04','PC02_B2',70000),
('V_PC02_B3','2025-01-12','LC04','PC02_B3',70000),
('V_PC02_C1','2025-01-12','LC04','PC02_C1',70000),
('V_PC02_D1','2025-01-12','LC04','PC02_D1',70000);

-- LC05 (PC01) giá 90000
INSERT INTO VeXemPhim (maVe, ngayBan, maLich, maGhe, donGia) VALUES
('V_PC01_B5','2025-01-13','LC05','PC01_B5',90000),
('V_PC01_C4','2025-01-13','LC05','PC01_C4',90000),
('V_PC01_D4','2025-01-13','LC05','PC01_D4',90000);
GO

-- =========================
-- 14. HoaDon (20 hóa đơn) - đã tính tổng/tỉ lệ giảm/thanhToan
-- ngày từ 2025-01-10 -> 2025-01-16
-- =========================
INSERT INTO HoaDon (maHD, ngayLap, tongTien, giamGia, thanhToan, maNV, maKH, maKM) VALUES
-- 2025-01-10
('HD001','2025-01-10',90000, 9000, 81000, 'NV002','KH001','KM01'),  -- 1 vé V_PC01_A1 (90000) -10%
('HD002','2025-01-10',90000, 4500, 85500, 'NV002','KH002','KM02'),  -- 1 vé V_PC01_A2 (90000) -5%
('HD003','2025-01-10',180000,   0,180000, 'NV003','KH003', NULL),   -- 2 vé V_PC01_A3+V_PC01_A4 (2x90000) no KM
-- 2025-01-11
('HD004','2025-01-11',80000, 4000, 76000, 'NV001','KH001','KM02'),  -- 1 vé V_PC02_A1 (80000) -5%
('HD005','2025-01-11',80000,    0, 80000, 'NV001','KH002', NULL),   -- 1 vé V_PC02_A2
('HD006','2025-01-11',160000,16000,144000, 'NV002','KH003','KM01'), -- 2 vé V_PC02_A3+V_PC02_A4 (2x80000) -10%
-- 2025-01-12
('HD007','2025-01-12',90000,13500,76500, 'NV002','KH001','KM03'),  -- 1 vé V_PC01_B1 (90000) -15%
('HD008','2025-01-12',270000,13500,256500,'NV002','KH004','KM02'), -- 3 vé V_PC01_B2+B3+B4 (3x90000) -5%
('HD009','2025-01-12',70000,    0, 70000, 'NV003','KH002', NULL),  -- 1 vé V_PC02_B1
-- 2025-01-13
('HD010','2025-01-13',90000, 9000, 81000, 'NV003','KH001','KM01'),  -- 1 vé V_PC01_C1 -10%
('HD011','2025-01-13',180000, 9000,171000, 'NV002','KH003','KM02'), -- 2 vé V_PC01_C2+C3 -5%
('HD012','2025-01-13',70000,    0, 70000, 'NV001','KH004', NULL),  -- 1 vé V_PC02_C1
-- 2025-01-14
('HD013','2025-01-14',270000,27000,243000, 'NV001','KH002','KM01'), -- 3 vé V_PC01_D1+D2+D3 -10%
('HD014','2025-01-14',70000,    0, 70000, 'NV002','KH003', NULL),  -- 1 vé V_PC02_D1
('HD015','2025-01-14',70000, 7000, 63000, 'NV003','KH004','KM01'),  -- 1 vé V_PC02_D2 -10%
-- 2025-01-15
('HD016','2025-01-15',90000, 4500, 85500, 'NV002','KH001','KM02'),  -- 1 vé V_PC01_E1 -5%
('HD017','2025-01-15',180000,   0,180000, 'NV003','KH002', NULL),   -- 2 vé V_PC01_E2+E3 no KM
('HD018','2025-01-15',80000,12000,68000, 'NV001','KH003','KM03'),  -- 1 vé V_PC02_A5 -15%
-- 2025-01-16
('HD019','2025-01-16',90000,    0,90000, 'NV001','KH004', NULL),   -- 1 vé V_PC01_B5
('HD020','2025-01-16',140000, 7000,133000, 'NV002','KH001','KM02'); -- 2 vé V_PC02_A6+A7 (2x70000) -5%
GO

-- =========================
-- 15. ChiTietHoaDon (maHD -> maVe, donGia)
-- =========================

-- INSERT CHI TIET HOA DON (đã khớp 100% với VeXemPhim & HoaDon bạn cung cấp)
INSERT INTO ChiTietHoaDon (maHD, maVe, donGia) VALUES
-- 2025-01-10
('HD001','V_PC01_A1',90000),
('HD002','V_PC01_A2',90000),
('HD003','V_PC01_A3',90000),
('HD003','V_PC01_A4',90000),

-- 2025-01-11
('HD004','V_PC02_A1',80000),
('HD005','V_PC02_A2',80000),
('HD006','V_PC02_A3',80000),
('HD006','V_PC02_A4',80000),

-- 2025-01-12
('HD007','V_PC01_B1',90000),
('HD008','V_PC01_B2',90000),
('HD008','V_PC01_B3',90000),
('HD008','V_PC01_B4',90000),
('HD009','V_PC02_B1',70000),

-- 2025-01-13
('HD010','V_PC01_C1',90000),
('HD011','V_PC01_C2',90000),
('HD011','V_PC01_C3',90000),
('HD012','V_PC02_C1',70000),

-- 2025-01-14
-- (HD013 cần 3 vé, dùng các vé 90000 có sẵn)
('HD013','V_PC01_A5',90000),
('HD013','V_PC01_A6',90000),
('HD013','V_PC01_A7',90000),
('HD014','V_PC02_D1',70000),
('HD015','V_PC02_B2',70000),

-- 2025-01-15
('HD016','V_PC01_A8',90000),
('HD017','V_PC01_A9',90000),
('HD017','V_PC01_A10',90000),
('HD018','V_PC02_A5',80000),

-- 2025-01-16
('HD019','V_PC01_B5',90000),
-- HD020: dùng V_PC02_B3 (70000) + V_PC01_C4 (90000) — cả 2 đều tồn tại
('HD020','V_PC02_B3',70000),
('HD020','V_PC01_C4',90000);
GO

-- =========================
-- 16. ThanhToan
-- =========================
INSERT INTO ThanhToan (maThanhToan, maHD, phuongThuc, ngayThanhToan, trangThai) VALUES
('TT001','HD001',N'Tiền mặt','2025-01-10',N'Hoàn tất'),
('TT002','HD002',N'VNPay','2025-01-10',N'Hoàn tất'),
('TT003','HD003',N'Tiền mặt','2025-01-10',N'Hoàn tất'),
('TT004','HD004',N'VNPay','2025-01-11',N'Hoàn tất'),
('TT005','HD005',N'Tiền mặt','2025-01-11',N'Hoàn tất'),
('TT006','HD006',N'Thẻ tín dụng','2025-01-11',N'Hoàn tất'),
('TT007','HD007',N'Tiền mặt','2025-01-12',N'Hoàn tất'),
('TT008','HD008',N'VNPay','2025-01-12',N'Hoàn tất'),
('TT009','HD009',N'Tiền mặt','2025-01-12',N'Hoàn tất'),
('TT010','HD010',N'Thẻ tín dụng','2025-01-13',N'Hoàn tất'),
('TT011','HD011',N'VNPay','2025-01-13',N'Hoàn tất'),
('TT012','HD012',N'Tiền mặt','2025-01-13',N'Hoàn tất'),
('TT013','HD013',N'VNPay','2025-01-14',N'Hoàn tất'),
('TT014','HD014',N'Tiền mặt','2025-01-14',N'Hoàn tất'),
('TT015','HD015',N'Thẻ tín dụng','2025-01-14',N'Hoàn tất'),
('TT016','HD016',N'VNPay','2025-01-15',N'Hoàn tất'),
('TT017','HD017',N'Tiền mặt','2025-01-15',N'Hoàn tất'),
('TT018','HD018',N'VNPay','2025-01-15',N'Hoàn tất'),
('TT019','HD019',N'Tiền mặt','2025-01-16',N'Hoàn tất'),
('TT020','HD020',N'Thẻ tín dụng','2025-01-16',N'Hoàn tất');
GO