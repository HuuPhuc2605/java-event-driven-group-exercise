package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.TaiKhoanNhanVienDAO;
import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;

import java.sql.SQLException;
import java.util.List;

/**
 * Service cho tài khoản nhân viên - chứa business logic
 */
public class TaiKhoanNhanVienService {
    private TaiKhoanNhanVienDAO dao;

    public TaiKhoanNhanVienService() throws SQLException {
        this.dao = new TaiKhoanNhanVienDAO();
    }

    /**
     * Kiểm tra đăng nhập với validation
     */
    public TaiKhoanNhanVien authenticateAdmin(String tenDangNhap, String matKhau) {
        // Kiểm tra input
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            System.out.println("Tên đăng nhập không được để trống!");
            return null;
        }
        
        if (matKhau == null || matKhau.trim().isEmpty()) {
            System.out.println("Mật khẩu không được để trống!");
            return null;
        }
        
        // Gọi DAO để kiểm tra
        TaiKhoanNhanVien taiKhoan = dao.login(tenDangNhap, matKhau);
        
        if (taiKhoan != null) {
            System.out.println("Đăng nhập thành công! Chào " + taiKhoan.getNhanVien().getTenNV());
        } else {
            System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
        }
        
        return taiKhoan;
    }

    /**
     * Kiểm tra xem tài khoản đã tồn tại chưa
     */
    public boolean isUsernameExists(String tenDangNhap) {
        return dao.getTaiKhoanByUsername(tenDangNhap) != null;
    }

    /**
     * Tạo tài khoản admin mới với validation
     */
    public boolean registerAdmin(String tenDangNhap, String matKhau, String vaiTro, String maNV) {
        // Kiểm tra validation
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            System.out.println("Tên đăng nhập không được để trống!");
            return false;
        }
        
        if (matKhau == null || matKhau.trim().isEmpty()) {
            System.out.println("Mật khẩu không được để trống!");
            return false;
        }
        
        if (matKhau.length() < 6) {
            System.out.println("Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }
        
        if (isUsernameExists(tenDangNhap)) {
            System.out.println("Tên đăng nhập đã tồn tại!");
            return false;
        }
        
        TaiKhoanNhanVien taiKhoan = new TaiKhoanNhanVien(tenDangNhap, matKhau, vaiTro, maNV);
        return dao.createTaiKhoan(taiKhoan);
    }

    /**
     * Đổi mật khẩu
     */
    public boolean changePassword(String tenDangNhap, String matKhauCu, String matKhauMoi) {
        TaiKhoanNhanVien taiKhoan = dao.login(tenDangNhap, matKhauCu);
        
        if (taiKhoan == null) {
            System.out.println("Mật khẩu cũ không đúng!");
            return false;
        }
        
        if (matKhauMoi == null || matKhauMoi.trim().isEmpty()) {
            System.out.println("Mật khẩu mới không được để trống!");
            return false;
        }
        
        if (matKhauMoi.length() < 6) {
            System.out.println("Mật khẩu mới phải có ít nhất 6 ký tự!");
            return false;
        }
        
        return dao.updatePassword(tenDangNhap, matKhauMoi);
    }

    /**
     * Lấy danh sách tất cả admin
     */
    public List<TaiKhoanNhanVien> getAllAdmins() {
        return dao.getAllTaiKhoan();
    }

    /**
     * Lấy tài khoản theo username để gợi ý mật khẩu
     * added method to retrieve account by username for password autocomplete
     */
    public TaiKhoanNhanVien getTaiKhoanByUsername(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            return null;
        }
        return dao.getTaiKhoanByUsername(tenDangNhap);
    }
}
