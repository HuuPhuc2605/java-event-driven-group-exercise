package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho ghế ngồi
 */
public class GheNgoiDAO {
    private Connection connection;

    public GheNgoiDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm ghế mới
     */
    public boolean createGheNgoi(GheNgoi ghe) {
        String sql = "INSERT INTO GheNgoi (maGhe, hang, cot, trangThai) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, ghe.getMaGhe());
            ps.setString(2, ghe.getHang());
            ps.setInt(3, ghe.getCot());
            ps.setString(4, ghe.getTrangThai());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm ghế: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy ghế theo mã
     */
    public GheNgoi getGheByMa(String maGhe) {
        String sql = "SELECT * FROM GheNgoi WHERE maGhe = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maGhe);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GheNgoi ghe = new GheNgoi();
                ghe.setMaGhe(rs.getString("maGhe"));
                ghe.setHang(rs.getString("hang"));
                ghe.setCot(rs.getInt("cot"));
                ghe.setTrangThai(rs.getString("trangThai"));
                
                rs.close();
                ps.close();
                return ghe;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy ghế: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật trạng thái ghế
     */
    public boolean updateGheNgoi(GheNgoi ghe) {
        String sql = "UPDATE GheNgoi SET hang = ?, cot = ?, trangThai = ? WHERE maGhe = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, ghe.getHang());
            ps.setInt(2, ghe.getCot());
            ps.setString(3, ghe.getTrangThai());
            ps.setString(4, ghe.getMaGhe());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật ghế: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa ghế
     */
    public boolean deleteGheNgoi(String maGhe) {
        String sql = "DELETE FROM GheNgoi WHERE maGhe = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maGhe);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa ghế: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả ghế
     */
    public List<GheNgoi> getAllGheNgoi() {
        List<GheNgoi> list = new ArrayList<>();
        String sql = "SELECT * FROM GheNgoi";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                GheNgoi ghe = new GheNgoi();
                ghe.setMaGhe(rs.getString("maGhe"));
                ghe.setHang(rs.getString("hang"));
                ghe.setCot(rs.getInt("cot"));
                ghe.setTrangThai(rs.getString("trangThai"));
                list.add(ghe);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách ghế: " + e.getMessage());
        }
        return list;
    }

    /**
     * Xóa ghế theo tên ghế (xóa VeXemPhim trước)
     */
    public boolean deleteGheWithReferences(String maGhe) {
        try {
            // Xóa VeXemPhim liên quan
            String sqlDeleteVe = "DELETE FROM VeXemPhim WHERE maGhe = ?";
            PreparedStatement psDeleteVe = connection.prepareStatement(sqlDeleteVe);
            psDeleteVe.setString(1, maGhe);
            psDeleteVe.executeUpdate();
            psDeleteVe.close();
            
            // Xóa ghế
            return deleteGheNgoi(maGhe);
        } catch (SQLException e) {
            System.out.println("Lỗi xóa ghế với references: " + e.getMessage());
        }
        return false;
    }
}
