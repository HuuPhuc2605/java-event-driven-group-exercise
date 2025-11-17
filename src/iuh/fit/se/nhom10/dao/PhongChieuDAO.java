package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.model.PhongChieu;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho phòng chiếu
 */
public class PhongChieuDAO {
    private Connection connection;

    public PhongChieuDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm phòng chiếu mới
     */
    public boolean createPhongChieu(PhongChieu phong) {
        String sql = "INSERT INTO PhongChieu (maPhong, tenPhong, maLoaiPhong) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phong.getMaPhong());
            ps.setString(2, phong.getTenPhong());
            ps.setInt(3, phong.getMaLoaiPhong());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm phòng chiếu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy phòng chiếu theo mã
     */
    public PhongChieu getPhongByMa(String maPhong) {
        String sql = "SELECT * FROM PhongChieu WHERE maPhong = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhong);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PhongChieu phong = new PhongChieu();
                phong.setMaPhong(rs.getString("maPhong"));
                phong.setTenPhong(rs.getString("tenPhong"));
                phong.setMaLoaiPhong(rs.getInt("maLoaiPhong"));
                
                rs.close();
                ps.close();
                return phong;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy phòng chiếu: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật phòng chiếu
     */
    public boolean updatePhongChieu(PhongChieu phong) {
        String sql = "UPDATE PhongChieu SET tenPhong = ?, maLoaiPhong = ? WHERE maPhong = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, phong.getTenPhong());
            ps.setInt(2, phong.getMaLoaiPhong());
            ps.setString(3, phong.getMaPhong());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật phòng chiếu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa phòng chiếu (xóa ghế trước, rồi xóa lịch chiếu, cuối cùng xóa phòng)
     */
    public boolean deletePhongChieu(String maPhong) {
        try {
            // Xóa tất cả ghế của phòng này
            GheNgoiDAO gheDAO = new GheNgoiDAO();
            List<GheNgoi> seats = gheDAO.getGheByPhong(maPhong);
            for (GheNgoi ghe : seats) {
                gheDAO.deleteGheWithReferences(ghe.getMaGhe());
            }
            
            // Xóa tất cả lịch chiếu của phòng này
            LichChieuDAO lichDAO = new LichChieuDAO();
            lichDAO.deleteByPhong(maPhong);
            
            // Xóa phòng
            String sql = "DELETE FROM PhongChieu WHERE maPhong = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhong);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa phòng chiếu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả phòng chiếu
     */
    public List<PhongChieu> getAllPhongChieu() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                PhongChieu phong = new PhongChieu();
                phong.setMaPhong(rs.getString("maPhong"));
                phong.setTenPhong(rs.getString("tenPhong"));
                phong.setMaLoaiPhong(rs.getInt("maLoaiPhong"));
                list.add(phong);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách phòng chiếu: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy số ghế trong phòng (count từ GheNgoi table)
     */
    public int getSoGheByPhong(String maPhong) {
        String sql = "SELECT COUNT(*) as soGhe FROM GheNgoi WHERE maPhong = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhong);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("soGhe");
                rs.close();
                ps.close();
                return count;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy số ghế: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Kiểm tra xem mã phòng chiếu có trùng không
     */
    public boolean isPhongChieuExists(String maPhong) {
        return getPhongByMa(maPhong) != null;
    }
}
