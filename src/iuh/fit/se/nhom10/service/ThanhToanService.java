package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.ThanhToanDAO;
import iuh.fit.se.nhom10.model.ThanhToan;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service xử lý thanh toán - Business logic
 */
public class ThanhToanService {
    private ThanhToanDAO dao;

    public ThanhToanService() throws SQLException {
        this.dao = new ThanhToanDAO();
    }

    /**
     * Tạo thanh toán mới
     */
    public ThanhToan createThanhToan(String maHD, String phuongThuc, String trangThai) throws SQLException {
        // Validate input
        if (maHD == null || maHD.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống");
        }
        if (phuongThuc == null || phuongThuc.trim().isEmpty()) {
            throw new IllegalArgumentException("Phương thức thanh toán không được để trống");
        }

        String maThanhToan = generateMaThanhToan();
        ThanhToan thanhToan = new ThanhToan(maThanhToan, maHD, phuongThuc, trangThai);
        thanhToan.setNgayThanhToan(LocalDateTime.now());

        if (dao.createThanhToan(thanhToan)) {
            return thanhToan;
        }
        throw new SQLException("Không thể tạo thanh toán");
    }

    /**
     * Lấy thanh toán theo mã
     */
    public ThanhToan getThanhToanByMa(String maThanhToan) throws SQLException {
        if (maThanhToan == null || maThanhToan.trim().isEmpty()) {
            return null;
        }
        return dao.getThanhToanByMa(maThanhToan);
    }

    /**
     * Lấy thanh toán theo mã hóa đơn
     */
    public ThanhToan getThanhToanByMaHoaDon(String maHD) throws SQLException {
        if (maHD == null || maHD.trim().isEmpty()) {
            return null;
        }
        return dao.getThanhToanByMaHoaDon(maHD);
    }

    /**
     * Lấy tất cả thanh toán
     */
    public List<ThanhToan> getAllThanhToan() throws SQLException {
        return dao.getAllThanhToan();
    }

    /**
     * Cập nhật trạng thái thanh toán
     */
    public boolean updateTrangThai(String maThanhToan, String trangThai) throws SQLException {
        if (maThanhToan == null || maThanhToan.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thanh toán không được để trống");
        }
        if (trangThai == null || trangThai.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }

        ThanhToan thanhToan = dao.getThanhToanByMa(maThanhToan);
        if (thanhToan == null) {
            throw new SQLException("Thanh toán không tồn tại");
        }

        thanhToan.setTrangThai(trangThai);
        return dao.updateThanhToan(thanhToan);
    }

    /**
     * Xóa thanh toán
     */
    public boolean deleteThanhToan(String maThanhToan) throws SQLException {
        if (maThanhToan == null || maThanhToan.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thanh toán không được để trống");
        }
        return dao.deleteThanhToan(maThanhToan);
    }

    /**
     * Tìm kiếm thanh toán theo phương thức
     */
    public List<ThanhToan> getThanhToanByPhuongThuc(String phuongThuc) throws SQLException {
        List<ThanhToan> allThanhToan = dao.getAllThanhToan();
        return allThanhToan.stream()
                .filter(tt -> tt.getPhuongThuc().equalsIgnoreCase(phuongThuc))
                .toList();
    }

    /**
     * Tìm kiếm thanh toán theo trạng thái
     */
    public List<ThanhToan> getThanhToanByTrangThai(String trangThai) throws SQLException {
        List<ThanhToan> allThanhToan = dao.getAllThanhToan();
        return allThanhToan.stream()
                .filter(tt -> tt.getTrangThai().equalsIgnoreCase(trangThai))
                .toList();
    }

    /**
     * Sinh mã thanh toán - FIXED: Mã giới hạn trong CHAR(10)
     */
    private String generateMaThanhToan() {
        // Format: TT + 8 digits from timestamp
        long timestamp = System.currentTimeMillis() % 100000000L;
        return String.format("TT%08d", timestamp);
    }
}
