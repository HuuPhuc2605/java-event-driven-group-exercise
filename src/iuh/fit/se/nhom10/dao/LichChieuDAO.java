package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho lịch chiếu
 */
public class LichChieuDAO {
    private Connection connection;

    public LichChieuDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Xóa tất cả lịch chiếu của một phim
     * Added cascading delete for VeXemPhim before deleting LichChieu
     */
    public boolean deleteByPhim(String maPhim) {
        try {
            // Trước tiên xóa tất cả vé liên quan đến các lịch chiếu
            String sqlDeleteVe = "DELETE FROM VeXemPhim WHERE maLich IN (SELECT maLich FROM LichChieu WHERE maPhim = ?)";
            PreparedStatement psVe = connection.prepareStatement(sqlDeleteVe);
            psVe.setString(1, maPhim);
            psVe.executeUpdate();
            psVe.close();
            
            // Sau đó xóa lịch chiếu
            String sql = "DELETE FROM LichChieu WHERE maPhim = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhim);
            int result = ps.executeUpdate();
            ps.close();
            return result >= 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa lịch chiếu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Added new method to delete schedules by room (for cascading delete)
     */
    public boolean deleteByPhong(String maPhong) {
        try {
            // Xóa tất cả vé liên quan đến các lịch chiếu của phòng này
            String sqlDeleteVe = "DELETE FROM VeXemPhim WHERE maLich IN (SELECT maLich FROM LichChieu WHERE maPhong = ?)";
            PreparedStatement psVe = connection.prepareStatement(sqlDeleteVe);
            psVe.setString(1, maPhong);
            psVe.executeUpdate();
            psVe.close();
            
            // Sau đó xóa lịch chiếu
            String sql = "DELETE FROM LichChieu WHERE maPhong = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhong);
            int result = ps.executeUpdate();
            ps.close();
            return result >= 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa lịch chiếu theo phòng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả lịch chiếu
     */
    public List<LichChieu> getAllLichChieu() {
        List<LichChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM LichChieu";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LichChieu lc = new LichChieu();
                lc.setMaLich(rs.getString("maLich"));
                lc.setNgayChieu(rs.getDate("ngayChieu"));
                lc.setMaPhim(rs.getString("maPhim"));
                list.add(lc);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch chiếu: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy lịch chiếu theo ngày
     */
    public List<LichChieu> getLichChieuByNgay(Date ngay) {
        List<LichChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM LichChieu WHERE ngayChieu = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDate(1, ngay);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LichChieu lc = new LichChieu();
                lc.setMaLich(rs.getString("maLich"));
                lc.setNgayChieu(rs.getDate("ngayChieu"));
                lc.setMaPhim(rs.getString("maPhim"));
                list.add(lc);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch chiếu theo ngày: " + e.getMessage());
        }
        return list;
    }
}
