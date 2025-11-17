package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.KhachHangDAO;
import iuh.fit.se.nhom10.model.KhachHang;

import java.sql.SQLException;
import java.util.List;

/**
 * Service cho khách hàng - chứa business logic
 */
public class KhachHangService {
    private KhachHangDAO dao;

    public KhachHangService() throws SQLException {
        this.dao = new KhachHangDAO();
    }

    /**
     * Thêm khách hàng mới
     * Return custom error messages
     */
    public String addKhachHang(KhachHang kh) {
        // Kiểm tra mã khách hàng
        if (kh.getMaKH() == null || kh.getMaKH().trim().isEmpty()) {
            return "Mã khách hàng không được để trống!";
        }
        
        if (dao.isKhachHangExists(kh.getMaKH())) {
            return "Mã khách hàng đã tồn tại!";
        }
        
        // Kiểm tra tên khách hàng
        if (kh.getTenKH() == null || kh.getTenKH().trim().isEmpty()) {
            return "Tên khách hàng không được để trống!";
        }
        
        if (!KhachHangDAO.isPhoneValid(kh.getSoDienThoai())) {
            return "Số điện thoại không hợp lệ (phải là 10 chữ số)!";
        }
        
        if (dao.createKhachHang(kh)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi thêm khách hàng!";
        }
    }

    /**
     * Sửa khách hàng
     * Return custom error messages
     */
    public String updateKhachHang(KhachHang kh) {
        // Kiểm tra tên khách hàng
        if (kh.getTenKH() == null || kh.getTenKH().trim().isEmpty()) {
            return "Tên khách hàng không được để trống!";
        }
        
        if (!KhachHangDAO.isPhoneValid(kh.getSoDienThoai())) {
            return "Số điện thoại không hợp lệ (phải là 10 chữ số)!";
        }
        
        if (dao.updateKhachHang(kh)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi cập nhật khách hàng!";
        }
    }

    /**
     * Xóa khách hàng
     */
    public String deleteKhachHang(String maKH) {
        if (dao.deleteKhachHang(maKH)) {
            return "SUCCESS";
        } else {
            return "Lỗi khi xóa khách hàng!";
        }
    }

    /**
     * Lấy tất cả khách hàng
     */
    public List<KhachHang> getAllKhachHang() {
        return dao.getAllKhachHang();
    }

    /**
     * Lấy khách hàng theo mã
     */
    public KhachHang getKhachHangByMa(String maKH) {
        return dao.getKhachHangByMa(maKH);
    }

    /**
     * Tìm kiếm khách hàng
     */
    public List<KhachHang> searchKhachHang(String keyword) {
        return dao.searchKhachHang(keyword);
    }

    /**
     * Lấy mã khách hàng tiếp theo
     */
    public String getNextKhachHangId() {
        return dao.getNextKhachHangId();
    }
}
