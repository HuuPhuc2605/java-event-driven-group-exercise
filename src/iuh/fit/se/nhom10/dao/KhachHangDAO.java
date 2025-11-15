package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.KhachHang;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho khách hàng
 */
public class KhachHangDAO {
    private Connection connection;

    public KhachHangDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Lấy khách hàng theo mã
     */
    public KhachHang getKhachHangByMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maKH);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                
                rs.close();
                ps.close();
                return kh;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy khách hàng: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả khách hàng
     */
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                list.add(kh);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách khách hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Thêm khách hàng mới
     */
    public boolean createKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, soDienThoai) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSoDienThoai());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm khách hàng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin khách hàng
     */
    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH = ?, soDienThoai = ? WHERE maKH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getMaKH());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật khách hàng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa khách hàng
     */
    public boolean deleteKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maKH);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa khách hàng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Tìm kiếm khách hàng theo tên
     */
    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE tenKH LIKE ? OR maKH LIKE ? OR soDienThoai LIKE ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getString("maKH"));
                kh.setTenKH(rs.getString("tenKH"));
                kh.setSoDienThoai(rs.getString("soDienThoai"));
                list.add(kh);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm khách hàng: " + e.getMessage());
        }
        return list;
    }
}
