package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.LichChieu;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
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
     * Thêm lịch chiếu mới (CREATE)
     */
    public boolean createLichChieu(LichChieu lichChieu) {
        String sql = "INSERT INTO LichChieu (maLich, ngayChieu, gioBatDau, gioKetThuc, maPhim, maPhong) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, lichChieu.getMaLich());
            ps.setDate(2, lichChieu.getNgayChieu());
            ps.setTime(3, lichChieu.getGioBatDau());
            ps.setTime(4, lichChieu.getGioKetThuc());
            ps.setString(5, lichChieu.getMaPhim());
            ps.setString(6, lichChieu.getMaPhong());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm lịch chiếu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy lịch chiếu theo mã (READ)
     */
    public LichChieu getLichChieuByMa(String maLich) {
        String sql = "SELECT * FROM LichChieu WHERE maLich = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maLich);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LichChieu lc = new LichChieu();
                lc.setMaLich(rs.getString("maLich"));
                lc.setNgayChieu(rs.getDate("ngayChieu"));
                lc.setGioBatDau(rs.getTime("gioBatDau"));
                lc.setGioKetThuc(rs.getTime("gioKetThuc"));
                lc.setMaPhim(rs.getString("maPhim"));
                lc.setMaPhong(rs.getString("maPhong"));
                
                rs.close();
                ps.close();
                return lc;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch chiếu: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật lịch chiếu (UPDATE)
     */
    public boolean updateLichChieu(LichChieu lichChieu) {
        String sql = "UPDATE LichChieu SET ngayChieu = ?, gioBatDau = ?, gioKetThuc = ?, maPhim = ?, maPhong = ? WHERE maLich = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDate(1, lichChieu.getNgayChieu());
            ps.setTime(2, lichChieu.getGioBatDau());
            ps.setTime(3, lichChieu.getGioKetThuc());
            ps.setString(4, lichChieu.getMaPhim());
            ps.setString(5, lichChieu.getMaPhong());
            ps.setString(6, lichChieu.getMaLich());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật lịch chiếu: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa lịch chiếu (DELETE)
     */
    public boolean deleteLichChieu(String maLich) {
        try {
            // Xóa tất cả vé liên quan
            String sqlDeleteVe = "DELETE FROM VeXemPhim WHERE maLich = ?";
            PreparedStatement psVe = connection.prepareStatement(sqlDeleteVe);
            psVe.setString(1, maLich);
            psVe.executeUpdate();
            psVe.close();
            
            // Xóa lịch chiếu
            String sql = "DELETE FROM LichChieu WHERE maLich = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maLich);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa lịch chiếu: " + e.getMessage());
        }
        return false;
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
                lc.setGioBatDau(rs.getTime("gioBatDau"));
                lc.setGioKetThuc(rs.getTime("gioKetThuc"));
                lc.setMaPhim(rs.getString("maPhim"));
                lc.setMaPhong(rs.getString("maPhong"));
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
                lc.setGioBatDau(rs.getTime("gioBatDau"));
                lc.setGioKetThuc(rs.getTime("gioKetThuc"));
                lc.setMaPhim(rs.getString("maPhim"));
                lc.setMaPhong(rs.getString("maPhong"));
                list.add(lc);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch chiếu theo ngày: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy lịch chiếu theo phòng
     */
    public List<LichChieu> getLichChieuByPhong(String maPhong) {
        List<LichChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM LichChieu WHERE maPhong = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LichChieu lc = new LichChieu();
                lc.setMaLich(rs.getString("maLich"));
                lc.setNgayChieu(rs.getDate("ngayChieu"));
                lc.setGioBatDau(rs.getTime("gioBatDau"));
                lc.setGioKetThuc(rs.getTime("gioKetThuc"));
                lc.setMaPhim(rs.getString("maPhim"));
                lc.setMaPhong(rs.getString("maPhong"));
                list.add(lc);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch chiếu theo phòng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy lịch chiếu theo phim
     */
    public List<LichChieu> getLichChieuByPhim(String maPhim) {
        List<LichChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM LichChieu WHERE maPhim = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maPhim);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LichChieu lc = new LichChieu();
                lc.setMaLich(rs.getString("maLich"));
                lc.setNgayChieu(rs.getDate("ngayChieu"));
                lc.setGioBatDau(rs.getTime("gioBatDau"));
                lc.setGioKetThuc(rs.getTime("gioKetThuc"));
                lc.setMaPhim(rs.getString("maPhim"));
                lc.setMaPhong(rs.getString("maPhong"));
                list.add(lc);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch chiếu theo phim: " + e.getMessage());
        }
        return list;
    }

    /**
     * Kiểm tra xem mã lịch chiếu có trùng không
     */
    public boolean isLichChieuExists(String maLich) {
        return getLichChieuByMa(maLich) != null;
    }
}
