package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.LoaiPhong;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho loại phòng chiếu
 */
public class LoaiPhongDAO {
    private Connection connection;

    public LoaiPhongDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm loại phòng mới
     */
    public boolean createLoaiPhong(LoaiPhong loaiPhong) {
        String sql = "INSERT INTO LoaiPhong (tenLoaiPhong) VALUES (?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, loaiPhong.getTenLoaiPhong());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm loại phòng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy loại phòng theo mã
     */
    public LoaiPhong getLoaiPhongByMa(int maLoaiPhong) {
        String sql = "SELECT * FROM LoaiPhong WHERE maLoaiPhong = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maLoaiPhong);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LoaiPhong loaiPhong = new LoaiPhong();
                loaiPhong.setMaLoaiPhong(rs.getInt("maLoaiPhong"));
                loaiPhong.setTenLoaiPhong(rs.getString("tenLoaiPhong"));
                
                rs.close();
                ps.close();
                return loaiPhong;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy loại phòng: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật loại phòng
     */
    public boolean updateLoaiPhong(LoaiPhong loaiPhong) {
        String sql = "UPDATE LoaiPhong SET tenLoaiPhong = ? WHERE maLoaiPhong = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, loaiPhong.getTenLoaiPhong());
            ps.setInt(2, loaiPhong.getMaLoaiPhong());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật loại phòng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa loại phòng (xóa phòng trước, rồi xóa loại phòng)
     */
    public boolean deleteLoaiPhong(int maLoaiPhong) {
        try {
            // Lấy tất cả phòng của loại này
            String sqlGetPhong = "SELECT maPhong FROM PhongChieu WHERE maLoaiPhong = ?";
            PreparedStatement psGetPhong = connection.prepareStatement(sqlGetPhong);
            psGetPhong.setInt(1, maLoaiPhong);
            ResultSet rs = psGetPhong.executeQuery();
            
            // Xóa từng phòng
            PhongChieuDAO phongDAO = new PhongChieuDAO();
            while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                phongDAO.deletePhongChieu(maPhong);
            }
            rs.close();
            psGetPhong.close();
            
            // Xóa loại phòng
            String sql = "DELETE FROM LoaiPhong WHERE maLoaiPhong = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maLoaiPhong);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa loại phòng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả loại phòng
     */
    public List<LoaiPhong> getAllLoaiPhong() {
        List<LoaiPhong> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiPhong";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                LoaiPhong loaiPhong = new LoaiPhong();
                loaiPhong.setMaLoaiPhong(rs.getInt("maLoaiPhong"));
                loaiPhong.setTenLoaiPhong(rs.getString("tenLoaiPhong"));
                list.add(loaiPhong);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách loại phòng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Kiểm tra xem loại phòng có tồn tại không (by tên)
     */
    public boolean isLoaiPhongExistsByName(String tenLoaiPhong) {
        String sql = "SELECT COUNT(*) as count FROM LoaiPhong WHERE tenLoaiPhong = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, tenLoaiPhong);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                rs.close();
                ps.close();
                return count > 0;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra loại phòng: " + e.getMessage());
        }
        return false;
    }
}
