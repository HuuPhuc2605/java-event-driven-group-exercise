package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.Phim;
import iuh.fit.se.nhom10.model.TheLoai;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho thể loại phim
 */
public class TheLoaiDAO {
    private Connection connection;

    public TheLoaiDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm thể loại mới
     */
    public boolean createTheLoai(TheLoai theLoai) {
        String sql = "INSERT INTO TheLoai (tenTheLoai) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, theLoai.getTenTheLoai());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm thể loại: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy thể loại theo mã
     */
    public TheLoai getTheLoaiByMa(int maTheLoai) {
        String sql = "SELECT * FROM TheLoai WHERE maTheLoai = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maTheLoai);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TheLoai theLoai = new TheLoai();
                theLoai.setMaTheLoai(rs.getInt("maTheLoai"));
                theLoai.setTenTheLoai(rs.getString("tenTheLoai"));
                
                rs.close();
                ps.close();
                return theLoai;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy thể loại: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật thông tin thể loại
     */
    public boolean updateTheLoai(TheLoai theLoai) {
        String sql = "UPDATE TheLoai SET tenTheLoai = ? WHERE maTheLoai = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, theLoai.getTenTheLoai());
            ps.setInt(2, theLoai.getMaTheLoai());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật thể loại: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa thể loại
     * Added cascading delete - delete Phim first, then delete TheLoai
     */
    public boolean deleteTheLoai(int maTheLoai) {
        try {
            // Trước tiên xóa tất cả phim của thể loại này
            PhimDAO phimDAO = new PhimDAO();
            List<Phim> phims = getAllPhimByTheLoai(maTheLoai);
            
            for (Phim phim : phims) {
                phimDAO.deletePhim(phim.getMaPhim());
            }
            
            // Sau đó xóa thể loại
            String sql = "DELETE FROM TheLoai WHERE maTheLoai = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maTheLoai);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa thể loại: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Lấy tất cả phim của một thể loại
     */
    private List<Phim> getAllPhimByTheLoai(int maTheLoai) {
        List<Phim> list = new ArrayList<>();
        String sql = "SELECT * FROM Phim WHERE maTheLoai = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maTheLoai);
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
            System.out.println("Lỗi lấy phim theo thể loại: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy tất cả thể loại
     */
    public List<TheLoai> getAllTheLoai() {
        List<TheLoai> list = new ArrayList<>();
        String sql = "SELECT * FROM TheLoai";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                TheLoai theLoai = new TheLoai();
                theLoai.setMaTheLoai(rs.getInt("maTheLoai"));
                theLoai.setTenTheLoai(rs.getString("tenTheLoai"));
                list.add(theLoai);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách thể loại: " + e.getMessage());
        }
        return list;
    }
}
