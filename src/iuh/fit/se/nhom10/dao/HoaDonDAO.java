package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho hóa đơn
 */
public class HoaDonDAO {
    private Connection connection;

    public HoaDonDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm hóa đơn mới
     */
    public boolean createHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon (maHD, ngayLap, tongTien, maNV, maKH, maKM) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, hoaDon.getMaHD());
            ps.setTimestamp(2, Timestamp.valueOf(hoaDon.getNgayLap()));
            ps.setBigDecimal(3, hoaDon.getTongTien());
            ps.setString(4, hoaDon.getMaNV());
            ps.setString(5, hoaDon.getMaKH());
            ps.setString(6, hoaDon.getMaKM());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm hóa đơn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy hóa đơn theo mã
     */
    public HoaDon getHoaDonByMa(String maHD) {
        String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maHD);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));
                hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                hd.setTongTien(rs.getBigDecimal("tongTien"));
                hd.setMaNV(rs.getString("maNV"));
                hd.setMaKH(rs.getString("maKH"));
                hd.setMaKM(rs.getString("maKM"));
                
                rs.close();
                ps.close();
                return hd;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy hóa đơn: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật hóa đơn
     */
    public boolean updateHoaDon(HoaDon hoaDon) {
        String sql = "UPDATE HoaDon SET ngayLap = ?, tongTien = ?, maNV = ?, maKH = ?, maKM = ? WHERE maHD = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(hoaDon.getNgayLap()));
            ps.setBigDecimal(2, hoaDon.getTongTien());
            ps.setString(3, hoaDon.getMaNV());
            ps.setString(4, hoaDon.getMaKH());
            ps.setString(5, hoaDon.getMaKM());
            ps.setString(6, hoaDon.getMaHD());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật hóa đơn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa hóa đơn (chỉ khi chưa thanh toán)
     */
    public boolean deleteHoaDon(String maHD) {
        try {
            // Kiểm tra xem có thanh toán hay vé chưa
            String sqlCheckThanhToan = "SELECT COUNT(*) FROM ThanhToan WHERE maHD = ? AND trangThai = 'Đã thanh toán'";
            PreparedStatement psCheckThanhToan = connection.prepareStatement(sqlCheckThanhToan);
            psCheckThanhToan.setString(1, maHD);
            ResultSet rsThanhToan = psCheckThanhToan.executeQuery();
            rsThanhToan.next();
            int countThanhToan = rsThanhToan.getInt(1);
            rsThanhToan.close();
            psCheckThanhToan.close();
            
            String sqlCheckVe = "SELECT COUNT(*) FROM VeXemPhim WHERE maHD = ?";
            PreparedStatement psCheckVe = connection.prepareStatement(sqlCheckVe);
            psCheckVe.setString(1, maHD);
            ResultSet rsVe = psCheckVe.executeQuery();
            rsVe.next();
            int countVe = rsVe.getInt(1);
            rsVe.close();
            psCheckVe.close();
            
            if (countThanhToan > 0 || countVe > 0) {
                System.out.println("Không thể xóa hóa đơn vì đã được thanh toán hoặc có vé!");
                return false;
            }
            
            // Xóa bản ghi thanh toán liên quan (nếu có)
            String sqlDeleteThanhToan = "DELETE FROM ThanhToan WHERE maHD = ?";
            PreparedStatement psDeleteThanhToan = connection.prepareStatement(sqlDeleteThanhToan);
            psDeleteThanhToan.setString(1, maHD);
            psDeleteThanhToan.executeUpdate();
            psDeleteThanhToan.close();
            
            // Xóa hóa đơn
            String sql = "DELETE FROM HoaDon WHERE maHD = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maHD);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa hóa đơn: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả hóa đơn
     */
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY ngayLap DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));
                hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                hd.setTongTien(rs.getBigDecimal("tongTien"));
                hd.setMaNV(rs.getString("maNV"));
                hd.setMaKH(rs.getString("maKH"));
                hd.setMaKM(rs.getString("maKM"));
                list.add(hd);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách hóa đơn: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tìm hóa đơn theo mã hoặc khách hàng
     */
    public List<HoaDon> searchHoaDon(String keyword) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE maHD LIKE ? OR maKH LIKE ? ORDER BY ngayLap DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));
                hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                hd.setTongTien(rs.getBigDecimal("tongTien"));
                hd.setMaNV(rs.getString("maNV"));
                hd.setMaKH(rs.getString("maKH"));
                hd.setMaKM(rs.getString("maKM"));
                list.add(hd);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi tìm hóa đơn: " + e.getMessage());
        }
        return list;
    }
}
