package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.KhuyenMai;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho chương trình khuyến mãi
 */
public class KhuyenMaiDAO {
    private Connection connection;

    public KhuyenMaiDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Thêm khuyến mãi mới
     */
    public boolean createKhuyenMai(KhuyenMai khuyenMai) {
        if (isKhuyenMaiExists(khuyenMai.getMaKM())) {
            System.out.println("Mã khuyến mãi đã tồn tại!");
            return false;
        }
        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, moTa, tiLeGiam, ngayBatDau, ngayKetThuc) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, khuyenMai.getMaKM());
            ps.setString(2, khuyenMai.getTenKM());
            ps.setString(3, khuyenMai.getMoTa());
            ps.setDouble(4, khuyenMai.getTiLeGiam());
            ps.setDate(5, Date.valueOf(khuyenMai.getNgayBatDau()));
            ps.setDate(6, Date.valueOf(khuyenMai.getNgayKetThuc()));
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm khuyến mãi: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy khuyến mãi theo mã
     */
    public KhuyenMai getKhuyenMaiByMa(String maKM) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maKM);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("maKM"));
                km.setTenKM(rs.getString("tenKM"));
                km.setMoTa(rs.getString("moTa"));
                km.setTiLeGiam(rs.getDouble("tiLeGiam"));
                km.setNgayBatDau(rs.getDate("ngayBatDau").toLocalDate());
                km.setNgayKetThuc(rs.getDate("ngayKetThuc").toLocalDate());
                
                rs.close();
                ps.close();
                return km;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy khuyến mãi: " + e.getMessage());
        }
        return null;
    }

    /**
     * Cập nhật khuyến mãi
     */
    public boolean updateKhuyenMai(KhuyenMai khuyenMai) {
        String sql = "UPDATE KhuyenMai SET tenKM = ?, moTa = ?, tiLeGiam = ?, ngayBatDau = ?, ngayKetThuc = ? WHERE maKM = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, khuyenMai.getTenKM());
            ps.setString(2, khuyenMai.getMoTa());
            ps.setDouble(3, khuyenMai.getTiLeGiam());
            ps.setDate(4, Date.valueOf(khuyenMai.getNgayBatDau()));
            ps.setDate(5, Date.valueOf(khuyenMai.getNgayKetThuc()));
            ps.setString(6, khuyenMai.getMaKM());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật khuyến mãi: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa khuyến mãi (nếu không được sử dụng trong hóa đơn)
     */
    public boolean deleteKhuyenMai(String maKM) {
        try {
            // Kiểm tra xem khuyến mãi có được sử dụng không
            String sqlCheck = "SELECT COUNT(*) FROM HoaDon WHERE maKM = ?";
            PreparedStatement psCheck = connection.prepareStatement(sqlCheck);
            psCheck.setString(1, maKM);
            ResultSet rs = psCheck.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            psCheck.close();
            
            if (count > 0) {
                System.out.println("Không thể xóa khuyến mãi vì đã được sử dụng!");
                return false;
            }
            
            String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maKM);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa khuyến mãi: " + e.getMessage());
        }
        return false;
    }

    /**
     * Lấy tất cả khuyến mãi
     */
    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai ORDER BY ngayBatDau DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("maKM"));
                km.setTenKM(rs.getString("tenKM"));
                km.setMoTa(rs.getString("moTa"));
                km.setTiLeGiam(rs.getDouble("tiLeGiam"));
                km.setNgayBatDau(rs.getDate("ngayBatDau").toLocalDate());
                km.setNgayKetThuc(rs.getDate("ngayKetThuc").toLocalDate());
                list.add(km);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách khuyến mãi: " + e.getMessage());
        }
        return list;
    }

    /**
     * Tìm khuyến mãi theo tên
     */
    public List<KhuyenMai> searchKhuyenMai(String tenKM) {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE tenKM LIKE ? ORDER BY ngayBatDau DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + tenKM + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai();
                km.setMaKM(rs.getString("maKM"));
                km.setTenKM(rs.getString("tenKM"));
                km.setMoTa(rs.getString("moTa"));
                km.setTiLeGiam(rs.getDouble("tiLeGiam"));
                km.setNgayBatDau(rs.getDate("ngayBatDau").toLocalDate());
                km.setNgayKetThuc(rs.getDate("ngayKetThuc").toLocalDate());
                list.add(km);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi tìm khuyến mãi: " + e.getMessage());
        }
        return list;
    }

    /**
     * Kiểm tra xem mã khuyến mãi có trùng không
     */
    public boolean isKhuyenMaiExists(String maKM) {
        return getKhuyenMaiByMa(maKM) != null;
    }
}
