package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.TaiKhoanNhanVien;
import iuh.fit.se.nhom10.model.NhanVien;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho tài khoản nhân viên (Admin)
 */
public class TaiKhoanNhanVienDAO {
    private Connection connection;

    public TaiKhoanNhanVienDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Đăng nhập - kiểm tra tên đăng nhập và mật khẩu
     */
    public TaiKhoanNhanVien login(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM TaiKhoanNhanVien WHERE tenDangNhap = ? AND matKhau = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaiKhoanNhanVien taiKhoan = new TaiKhoanNhanVien();
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                taiKhoan.setMaNV(rs.getString("maNV"));
                
                // Lấy thông tin nhân viên
                NhanVien nhanVien = getNhanVienByMa(rs.getString("maNV"));
                taiKhoan.setNhanVien(nhanVien);
                
                rs.close();
                ps.close();
                return taiKhoan;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi đăng nhập: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tài khoản theo tên đăng nhập
     */
    public TaiKhoanNhanVien getTaiKhoanByUsername(String tenDangNhap) {
        String sql = "SELECT * FROM TaiKhoanNhanVien WHERE tenDangNhap = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaiKhoanNhanVien taiKhoan = new TaiKhoanNhanVien();
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                taiKhoan.setMaNV(rs.getString("maNV"));
                taiKhoan.setNhanVien(getNhanVienByMa(rs.getString("maNV")));
                
                rs.close();
                ps.close();
                return taiKhoan;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy tài khoản: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tạo tài khoản admin mới
     */
    public boolean createTaiKhoan(TaiKhoanNhanVien taiKhoan) {
        String sql = "INSERT INTO TaiKhoanNhanVien (tenDangNhap, matKhau, vaiTro, maNV) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, taiKhoan.getTenDangNhap());
            ps.setString(2, taiKhoan.getMatKhau());
            ps.setString(3, taiKhoan.getVaiTro());
            ps.setString(4, taiKhoan.getMaNV());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi tạo tài khoản: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật mật khẩu
     */
    public boolean updatePassword(String tenDangNhap, String matKhauMoi) {
        String sql = "UPDATE TaiKhoanNhanVien SET matKhau = ? WHERE tenDangNhap = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, matKhauMoi);
            ps.setString(2, tenDangNhap);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật mật khẩu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa tài khoản
     */
    public boolean deleteTaiKhoan(String tenDangNhap) {
        String sql = "DELETE FROM TaiKhoanNhanVien WHERE tenDangNhap = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa tài khoản: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả tài khoản
     */
    public List<TaiKhoanNhanVien> getAllTaiKhoan() {
        List<TaiKhoanNhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoanNhanVien";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                TaiKhoanNhanVien taiKhoan = new TaiKhoanNhanVien();
                taiKhoan.setTenDangNhap(rs.getString("tenDangNhap"));
                taiKhoan.setMatKhau(rs.getString("matKhau"));
                taiKhoan.setVaiTro(rs.getString("vaiTro"));
                taiKhoan.setMaNV(rs.getString("maNV"));
                taiKhoan.setNhanVien(getNhanVienByMa(rs.getString("maNV")));
                list.add(taiKhoan);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách tài khoản: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy nhân viên theo mã
     */
    private NhanVien getNhanVienByMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maNV);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setMaChucVu(rs.getInt("maChucVu"));
                nv.setLuong(rs.getBigDecimal("luong"));
                nv.setSoDienThoai(rs.getString("soDienThoai"));
                
                rs.close();
                ps.close();
                return nv;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy nhân viên: " + e.getMessage());
        }
        return null;
    }
}
