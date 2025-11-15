package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.ThanhToan;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho thanh toán hóa đơn
 */
public class ThanhToanDAO {
    private Connection connection;

    public ThanhToanDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm bản ghi thanh toán
     */
    public boolean createThanhToan(ThanhToan thanhToan) {
        String sql = "INSERT INTO ThanhToan (maThanhToan, maHD, phuongThuc, ngayThanhToan, trangThai) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, thanhToan.getMaThanhToan());
            ps.setString(2, thanhToan.getMaHD());
            ps.setString(3, thanhToan.getPhuongThuc());
            ps.setTimestamp(4, Timestamp.valueOf(thanhToan.getNgayThanhToan()));
            ps.setString(5, thanhToan.getTrangThai());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm thanh toán: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy thanh toán theo mã
     */
    public ThanhToan getThanhToanByMa(String maThanhToan) {
        String sql = "SELECT * FROM ThanhToan WHERE maThanhToan = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maThanhToan);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ThanhToan tt = new ThanhToan();
                tt.setMaThanhToan(rs.getString("maThanhToan"));
                tt.setMaHD(rs.getString("maHD"));
                tt.setPhuongThuc(rs.getString("phuongThuc"));
                tt.setNgayThanhToan(rs.getTimestamp("ngayThanhToan").toLocalDateTime());
                tt.setTrangThai(rs.getString("trangThai"));
                
                rs.close();
                ps.close();
                return tt;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy thanh toán: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy thanh toán theo mã hóa đơn
     */
    public ThanhToan getThanhToanByMaHD(String maHD) {
        String sql = "SELECT * FROM ThanhToan WHERE maHD = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maHD);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ThanhToan tt = new ThanhToan();
                tt.setMaThanhToan(rs.getString("maThanhToan"));
                tt.setMaHD(rs.getString("maHD"));
                tt.setPhuongThuc(rs.getString("phuongThuc"));
                tt.setNgayThanhToan(rs.getTimestamp("ngayThanhToan").toLocalDateTime());
                tt.setTrangThai(rs.getString("trangThai"));
                
                rs.close();
                ps.close();
                return tt;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy thanh toán: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật thanh toán
     */
    public boolean updateThanhToan(ThanhToan thanhToan) {
        String sql = "UPDATE ThanhToan SET phuongThuc = ?, ngayThanhToan = ?, trangThai = ? WHERE maThanhToan = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, thanhToan.getPhuongThuc());
            ps.setTimestamp(2, Timestamp.valueOf(thanhToan.getNgayThanhToan()));
            ps.setString(3, thanhToan.getTrangThai());
            ps.setString(4, thanhToan.getMaThanhToan());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật thanh toán: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa thanh toán
     */
    public boolean deleteThanhToan(String maThanhToan) {
        String sql = "DELETE FROM ThanhToan WHERE maThanhToan = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maThanhToan);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa thanh toán: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả thanh toán
     */
    public List<ThanhToan> getAllThanhToan() {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM ThanhToan ORDER BY ngayThanhToan DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThanhToan tt = new ThanhToan();
                tt.setMaThanhToan(rs.getString("maThanhToan"));
                tt.setMaHD(rs.getString("maHD"));
                tt.setPhuongThuc(rs.getString("phuongThuc"));
                tt.setNgayThanhToan(rs.getTimestamp("ngayThanhToan").toLocalDateTime());
                tt.setTrangThai(rs.getString("trangThai"));
                list.add(tt);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách thanh toán: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tìm thanh toán theo trạng thái
     */
    public List<ThanhToan> searchByTrangThai(String trangThai) {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM ThanhToan WHERE trangThai = ? ORDER BY ngayThanhToan DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, trangThai);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ThanhToan tt = new ThanhToan();
                tt.setMaThanhToan(rs.getString("maThanhToan"));
                tt.setMaHD(rs.getString("maHD"));
                tt.setPhuongThuc(rs.getString("phuongThuc"));
                tt.setNgayThanhToan(rs.getTimestamp("ngayThanhToan").toLocalDateTime());
                tt.setTrangThai(rs.getString("trangThai"));
                list.add(tt);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi tìm thanh toán: " + e.getMessage());
        }
        return list;
    }
}
