package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.NhanVien;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho nhân viên
 */
public class NhanVienDAO {
    private Connection connection;

    public NhanVienDAO() {
        this.connection = KetNoi.getInstance().getConnection();
    }

    /**
     * Lấy nhân viên theo mã
     */
    public NhanVien getNhanVienByMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maNV);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setMaChucVu(rs.getInt("maChucVu"));
                nv.setLuong(rs.getBigDecimal("luong"));
                nv.setSoDienThoai(rs.getString("soDienThoai"));
                
                rs.close();
                ps.close();
                return nv;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy nhân viên: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy tất cả nhân viên
     */
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setMaChucVu(rs.getInt("maChucVu"));
                nv.setLuong(rs.getBigDecimal("luong"));
                nv.setSoDienThoai(rs.getString("soDienThoai"));
                list.add(nv);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách nhân viên: " + e.getMessage());
        }
        return list;
    }

    /**
     * Thêm nhân viên mới
     */
    public boolean createNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNV, tenNV, maChucVu, luong, soDienThoai) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setInt(3, nv.getMaChucVu());
            ps.setBigDecimal(4, nv.getLuong());
            ps.setString(5, nv.getSoDienThoai());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm nhân viên: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin nhân viên
     */
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET tenNV = ?, maChucVu = ?, luong = ?, soDienThoai = ? WHERE maNV = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nv.getTenNV());
            ps.setInt(2, nv.getMaChucVu());
            ps.setBigDecimal(3, nv.getLuong());
            ps.setString(4, nv.getSoDienThoai());
            ps.setString(5, nv.getMaNV());
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật nhân viên: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa nhân viên
     */
    public boolean deleteNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNV = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, maNV);
            
            int result = ps.executeUpdate();
            ps.close();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa nhân viên: " + e.getMessage());
        }
        return false;
    }

    /**
     * Tìm kiếm nhân viên theo tên
     */
    public List<NhanVien> searchNhanVien(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE tenNV LIKE ? OR maNV LIKE ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien();
                nv.setMaNV(rs.getString("maNV"));
                nv.setTenNV(rs.getString("tenNV"));
                nv.setMaChucVu(rs.getInt("maChucVu"));
                nv.setLuong(rs.getBigDecimal("luong"));
                nv.setSoDienThoai(rs.getString("soDienThoai"));
                list.add(nv);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm nhân viên: " + e.getMessage());
        }
        return list;
    }
}
