package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.NhanVienDAO;
import iuh.fit.se.nhom10.model.NhanVien;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;

import java.sql.SQLException;
import java.util.List;

/**
 * Service cho nhân viên - chứa business logic
 * Chỉ admin mới có thể thêm, sửa, xóa nhân viên
 */
public class NhanVienService {
    private NhanVienDAO dao;

    public NhanVienService() throws SQLException {
        this.dao = new NhanVienDAO();
    }

    /**
     * Thêm nhân viên mới - chỉ admin
     * Return custom error messages
     */
    public String addNhanVien(NhanVien nv, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            return "Chỉ admin mới có quyền thêm nhân viên!";
        }
        
        // Kiểm tra mã nhân viên
        if (nv.getMaNV() == null || nv.getMaNV().trim().isEmpty()) {
            return "Mã nhân viên không được để trống!";
        }
        
        if (dao.isNhanVienExists(nv.getMaNV())) {
            return "Mã nhân viên đã tồn tại!";
        }
        
        // Kiểm tra tên nhân viên
        if (nv.getTenNV() == null || nv.getTenNV().trim().isEmpty()) {
            return "Tên nhân viên không được để trống!";
        }
        
        // Kiểm tra số điện thoại
        if (!NhanVienDAO.isPhoneValid(nv.getSoDienThoai())) {
            return "Số điện thoại không hợp lệ (phải là 10 chữ số)!";
        }
        
        if (nv.getLuong() == null || nv.getLuong().doubleValue() <= 0) {
            return "Lương phải lớn hơn 0!";
        }
        
        if (dao.createNhanVien(nv)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi thêm nhân viên!";
        }
    }

    /**
     * Sửa nhân viên - chỉ admin
     * Return custom error messages
     */
    public String updateNhanVien(NhanVien nv, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            return "Chỉ admin mới có quyền sửa nhân viên!";
        }
        
        // Kiểm tra tên nhân viên
        if (nv.getTenNV() == null || nv.getTenNV().trim().isEmpty()) {
            return "Tên nhân viên không được để trống!";
        }
        
        // Kiểm tra số điện thoại
        if (!NhanVienDAO.isPhoneValid(nv.getSoDienThoai())) {
            return "Số điện thoại không hợp lệ (phải là 10 chữ số)!";
        }
        
        // Kiểm tra lương
        if (nv.getLuong() == null || nv.getLuong().doubleValue() <= 0) {
            return "Lương phải lớn hơn 0!";
        }
        
        if (dao.updateNhanVien(nv)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi cập nhật nhân viên!";
        }
    }

    /**
     * Xóa nhân viên - chỉ admin
     */
    public String deleteNhanVien(String maNV, TaiKhoanNhanVien taiKhoan) {
        // Kiểm tra quyền admin
        if (taiKhoan == null || !"Admin".equalsIgnoreCase(taiKhoan.getVaiTro())) {
            return "Chỉ admin mới có quyền xóa nhân viên!";
        }
        
        if (dao.deleteNhanVien(maNV)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi xóa nhân viên!";
        }
    }

    /**
     * Lấy tất cả nhân viên
     */
    public List<NhanVien> getAllNhanVien() {
        return dao.getAllNhanVien();
    }

    /**
     * Lấy nhân viên theo mã
     */
    public NhanVien getNhanVienByMa(String maNV) {
        return dao.getNhanVienByMa(maNV);
    }

    /**
     * Tìm kiếm nhân viên
     */
    public List<NhanVien> searchNhanVien(String keyword) {
        return dao.searchNhanVien(keyword);
    }
}
