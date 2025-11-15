package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.dao.LichChieuDAO;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Service cho quản lý lịch chiếu
 */
public class LichChieuService {
    private LichChieuDAO lichChieuDAO;

    public LichChieuService() {
        this.lichChieuDAO = new LichChieuDAO();
    }

    /**
     * Thêm lịch chiếu mới
     */
    public boolean addLichChieu(String maLich, Date ngayChieu, Time gioBatDau, Time gioKetThuc, String maPhim, String maPhong) {
        // Validate input
        if (maLich == null || maLich.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch chiếu không được trống");
        }
        if (ngayChieu == null) {
            throw new IllegalArgumentException("Ngày chiếu không được trống");
        }
        if (gioBatDau == null) {
            throw new IllegalArgumentException("Giờ bắt đầu không được trống");
        }
        if (gioKetThuc == null) {
            throw new IllegalArgumentException("Giờ kết thúc không được trống");
        }
        if (gioBatDau.getTime() >= gioKetThuc.getTime()) {
            throw new IllegalArgumentException("Giờ bắt đầu phải nhỏ hơn giờ kết thúc");
        }
        if (maPhim == null || maPhim.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phim không được trống");
        }
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được trống");
        }

        LichChieu lichChieu = new LichChieu(maLich, ngayChieu, gioBatDau, gioKetThuc, maPhim, maPhong);
        return lichChieuDAO.createLichChieu(lichChieu);
    }

    /**
     * Cập nhật lịch chiếu
     */
    public boolean updateLichChieu(String maLich, Date ngayChieu, Time gioBatDau, Time gioKetThuc, String maPhim, String maPhong) {
        // Validate input
        if (maLich == null || maLich.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch chiếu không được trống");
        }
        if (ngayChieu == null) {
            throw new IllegalArgumentException("Ngày chiếu không được trống");
        }
        if (gioBatDau == null) {
            throw new IllegalArgumentException("Giờ bắt đầu không được trống");
        }
        if (gioKetThuc == null) {
            throw new IllegalArgumentException("Giờ kết thúc không được trống");
        }
        if (gioBatDau.getTime() >= gioKetThuc.getTime()) {
            throw new IllegalArgumentException("Giờ bắt đầu phải nhỏ hơn giờ kết thúc");
        }
        if (maPhim == null || maPhim.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phim không được trống");
        }
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được trống");
        }

        LichChieu lichChieu = new LichChieu(maLich, ngayChieu, gioBatDau, gioKetThuc, maPhim, maPhong);
        return lichChieuDAO.updateLichChieu(lichChieu);
    }

    /**
     * Xóa lịch chiếu
     */
    public boolean deleteLichChieu(String maLich) {
        if (maLich == null || maLich.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch chiếu không được trống");
        }
        return lichChieuDAO.deleteLichChieu(maLich);
    }

    /**
     * Lấy lịch chiếu theo mã
     */
    public LichChieu getLichChieuByMa(String maLich) {
        if (maLich == null || maLich.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch chiếu không được trống");
        }
        return lichChieuDAO.getLichChieuByMa(maLich);
    }

    /**
     * Lấy tất cả lịch chiếu
     */
    public List<LichChieu> getAllLichChieu() {
        return lichChieuDAO.getAllLichChieu();
    }

    /**
     * Lấy lịch chiếu theo ngày
     */
    public List<LichChieu> getLichChieuByNgay(Date ngay) {
        if (ngay == null) {
            throw new IllegalArgumentException("Ngày không được trống");
        }
        return lichChieuDAO.getLichChieuByNgay(ngay);
    }

    /**
     * Lấy lịch chiếu theo phòng
     */
    public List<LichChieu> getLichChieuByPhong(String maPhong) {
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được trống");
        }
        return lichChieuDAO.getLichChieuByPhong(maPhong);
    }

    /**
     * Lấy lịch chiếu theo phim
     */
    public List<LichChieu> getLichChieuByPhim(String maPhim) {
        if (maPhim == null || maPhim.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phim không được trống");
        }
        return lichChieuDAO.getLichChieuByPhim(maPhim);
    }
}
