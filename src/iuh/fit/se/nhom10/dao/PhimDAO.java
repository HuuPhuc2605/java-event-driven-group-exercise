package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho phim
 */
public class PhimDAO {
    private Connection connection;

    public PhimDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm phim mới
     */
    public boolean createPhim(Phim phim) {
        String sql = "INSERT INTO Phim (maPhim, tenPhim, maTheLoai, thoiLuong, maDD) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phim.getMaPhim());
            ps.setString(2, phim.getTenPhim());
            ps.setInt(3, phim.getMaTheLoai());
            ps.setInt(4, phim.getThoiLuong());
            ps.setString(5, phim.getMaDD());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm phim: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy phim theo mã
     */
    public Phim getPhimByMa(String maPhim) {
        String sql = "SELECT * FROM Phim WHERE maPhim = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhim);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Phim phim = new Phim();
                phim.setMaPhim(rs.getString("maPhim"));
                phim.setTenPhim(rs.getString("tenPhim"));
                phim.setMaTheLoai(rs.getInt("maTheLoai"));
                phim.setThoiLuong(rs.getInt("thoiLuong"));
                phim.setMaDD(rs.getString("maDD"));
                
                rs.close();
                ps.close();
                return phim;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy phim: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật thông tin phim
     */
    public boolean updatePhim(Phim phim) {
        String sql = "UPDATE Phim SET tenPhim = ?, maTheLoai = ?, thoiLuong = ?, maDD = ? WHERE maPhim = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phim.getTenPhim());
            ps.setInt(2, phim.getMaTheLoai());
            ps.setInt(3, phim.getThoiLuong());
            ps.setString(4, phim.getMaDD());
            ps.setString(5, phim.getMaPhim());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật phim: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa phim
     */
    public boolean deletePhim(String maPhim) {
        String sql = "DELETE FROM Phim WHERE maPhim = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhim);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa phim: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả phim
     */
    public List<Phim> getAllPhim() {
        List<Phim> list = new ArrayList<>();
        String sql = "SELECT * FROM Phim";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Phim phim = new Phim();
                phim.setMaPhim(rs.getString("maPhim"));
                phim.setTenPhim(rs.getString("tenPhim"));
                phim.setMaTheLoai(rs.getInt("maTheLoai"));
                phim.setThoiLuong(rs.getInt("thoiLuong"));
                phim.setMaDD(rs.getString("maDD"));
                list.add(phim);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách phim: " + e.getMessage());
        }
        return list;
    }
}
