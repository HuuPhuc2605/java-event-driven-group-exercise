package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.PhimDAO;
import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;

import java.sql.SQLException;
import java.util.List;

/**
 * Service cho phim - chứa business logic
 * Chỉ admin mới có thể thêm, sửa, xóa phim
 */
public class PhimService {
    private PhimDAO dao;

    public PhimService() throws SQLException {
        this.dao = new PhimDAO();
    }

    /**
     * Thêm phim mới - chỉ admin
     * Return custom error messages instead of just false
     */
    public String addPhim(Phim phim, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            return "Chỉ admin mới có quyền thêm phim!";
        }
        
        // Kiểm tra validation
        if (phim.getMaPhim() == null || phim.getMaPhim().trim().isEmpty()) {
            return "Mã phim không được để trống!";
        }
        
        if (dao.isPhimExists(phim.getMaPhim())) {
            return "Mã phim đã tồn tại!";
        }
        
        if (phim.getTenPhim() == null || phim.getTenPhim().trim().isEmpty()) {
            return "Tên phim không được để trống!";
        }
        
        if (phim.getThoiLuong() <= 0) {
            return "Thời lượng phim phải lớn hơn 0!";
        }
        
        if (phim.getMaTheLoai() <= 0) {
            return "Thể loại không hợp lệ!";
        }
        
        if (phim.getMaDD() == null || phim.getMaDD().trim().isEmpty()) {
            return "Đạo diễn không hợp lệ!";
        }
        
        if (dao.createPhim(phim)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi thêm phim vào cơ sở dữ liệu!";
        }
    }

    /**
     * Sửa phim - chỉ admin
     * Return custom error messages instead of just false
     */
    public String updatePhim(Phim phim, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            return "Chỉ admin mới có quyền sửa phim!";
        }
        
        // Kiểm tra validation
        if (phim.getTenPhim() == null || phim.getTenPhim().trim().isEmpty()) {
            return "Tên phim không được để trống!";
        }
        
        if (phim.getThoiLuong() <= 0) {
            return "Thời lượng phim phải lớn hơn 0!";
        }
        
        if (phim.getMaTheLoai() <= 0) {
            return "Thể loại không hợp lệ!";
        }
        
        if (phim.getMaDD() == null || phim.getMaDD().trim().isEmpty()) {
            return "Đạo diễn không hợp lệ!";
        }
        
        if (dao.updatePhim(phim)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi cập nhật phim!";
        }
    }

    /**
     * Xóa phim - chỉ admin
     * Return custom error messages
     */
    public String deletePhim(String maPhim, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            return "Chỉ admin mới có quyền xóa phim!";
        }
        
        if (dao.deletePhim(maPhim)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi xóa phim!";
        }
    }

    /**
     * Lấy tất cả phim
     */
    public List<Phim> getAllPhim() {
        return dao.getAllPhim();
    }

    /**
     * Lấy phim theo mã
     */
    public Phim getPhimByMa(String maPhim) {
        return dao.getPhimByMa(maPhim);
    }
}
