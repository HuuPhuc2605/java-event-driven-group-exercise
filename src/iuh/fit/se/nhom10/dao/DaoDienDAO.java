package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.DaoDien;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho đạo diễn
 */
public class DaoDienDAO {
    private Connection connection;

    public DaoDienDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm đạo diễn mới
     */
    public boolean createDaoDien(DaoDien daoDien) {
        String sql = "INSERT INTO DaoDien (maDD, tenDD, quocTich) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, daoDien.getMaDD());
            ps.setString(2, daoDien.getTenDD());
            ps.setString(3, daoDien.getQuocTich());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm đạo diễn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy đạo diễn theo mã
     */
    public DaoDien getDaoDienByMa(String maDD) {
        String sql = "SELECT * FROM DaoDien WHERE maDD = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maDD);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DaoDien daoDien = new DaoDien();
                daoDien.setMaDD(rs.getString("maDD"));
                daoDien.setTenDD(rs.getString("tenDD"));
                daoDien.setQuocTich(rs.getString("quocTich"));
                
                rs.close();
                ps.close();
                return daoDien;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy đạo diễn: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật thông tin đạo diễn
     */
    public boolean updateDaoDien(DaoDien daoDien) {
        String sql = "UPDATE DaoDien SET tenDD = ?, quocTich = ? WHERE maDD = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, daoDien.getTenDD());
            ps.setString(2, daoDien.getQuocTich());
            ps.setString(3, daoDien.getMaDD());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật đạo diễn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa đạo diễn (set NULL các phim liên quan trước)
     */
    public boolean deleteDaoDien(String maDD) {
        try {
            // Trước tiên set NULL maDD trong bảng Phim
            String sqlUpdatePhim = "UPDATE Phim SET maDD = NULL WHERE maDD = ?";
            PreparedStatement psUpdate = connection.prepareStatement(sqlUpdatePhim);
            psUpdate.setString(1, maDD);
            psUpdate.executeUpdate();
            psUpdate.close();
            
            // Sau đó xóa đạo diễn
            String sql = "DELETE FROM DaoDien WHERE maDD = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maDD);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa đạo diễn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả đạo diễn
     */
    public List<DaoDien> getAllDaoDien() {
        List<DaoDien> list = new ArrayList<>();
        String sql = "SELECT * FROM DaoDien";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                DaoDien daoDien = new DaoDien();
                daoDien.setMaDD(rs.getString("maDD"));
                daoDien.setTenDD(rs.getString("tenDD"));
                daoDien.setQuocTich(rs.getString("quocTich"));
                list.add(daoDien);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách đạo diễn: " + e.getMessage());
        }
        return list;
    }
}
