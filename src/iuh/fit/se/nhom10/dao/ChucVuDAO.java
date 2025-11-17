package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.ChucVu;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho chức vụ
 */
public class ChucVuDAO {
    private Connection connection;

    public ChucVuDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Lấy chức vụ theo mã
     */
    public ChucVu getChucVuByMa(int maChucVu) {
        String sql = "SELECT * FROM ChucVu WHERE maChucVu = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, maChucVu);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaChucVu(rs.getInt("maChucVu"));
                cv.setTenChucVu(rs.getString("tenChucVu"));
                
                rs.close();
                ps.close();
                return cv;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy chức vụ: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả chức vụ
     */
    public List<ChucVu> getAllChucVu() {
        List<ChucVu> list = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaChucVu(rs.getInt("maChucVu"));
                cv.setTenChucVu(rs.getString("tenChucVu"));
                list.add(cv);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách chức vụ: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy chức vụ "Nhân Viên"
     */
    public ChucVu getChucVuNhanVien() {
        String sql = "SELECT * FROM ChucVu WHERE tenChucVu = 'Nhân Viên'";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaChucVu(rs.getInt("maChucVu"));
                cv.setTenChucVu(rs.getString("tenChucVu"));
                
                rs.close();
                ps.close();
                return cv;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy chức vụ Nhân Viên: " + e.getMessage());
        }
        return null;
    }
}
