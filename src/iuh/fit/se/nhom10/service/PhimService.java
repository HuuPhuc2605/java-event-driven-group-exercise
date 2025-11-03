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
     */
    public boolean addPhim(Phim phim, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            System.out.println("Chỉ admin mới có quyền thêm phim!");
            return false;
        }
        
        // Kiểm tra validation
        if (phim.getMaPhim() == null || phim.getMaPhim().trim().isEmpty()) {
            System.out.println("Mã phim không được để trống!");
            return false;
        }
        
        if (phim.getTenPhim() == null || phim.getTenPhim().trim().isEmpty()) {
            System.out.println("Tên phim không được để trống!");
            return false;
        }
        
        if (phim.getThoiLuong() <= 0) {
            System.out.println("Thời lượng phim phải > 0!");
            return false;
        }
        
        if (dao.getPhimByMa(phim.getMaPhim()) != null) {
            System.out.println("Mã phim đã tồn tại!");
            return false;
        }
        
        return dao.createPhim(phim);
    }

    /**
     * Sửa phim - chỉ admin
     */
    public boolean updatePhim(Phim phim, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            System.out.println("Chỉ admin mới có quyền sửa phim!");
            return false;
        }
        
        // Kiểm tra validation
        if (phim.getTenPhim() == null || phim.getTenPhim().trim().isEmpty()) {
            System.out.println("Tên phim không được để trống!");
            return false;
        }
        
        if (phim.getThoiLuong() <= 0) {
            System.out.println("Thời lượng phim phải > 0!");
            return false;
        }
        
        return dao.updatePhim(phim);
    }

    /**
     * Xóa phim - chỉ admin
     */
    public boolean deletePhim(String maPhim, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            System.out.println("Chỉ admin mới có quyền xóa phim!");
            return false;
        }
        
        return dao.deletePhim(maPhim);
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
