package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.ChiTietHoaDon;
import iuh.fit.se.nhom10.util.KetNoi;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    private Connection connection;

    public ChiTietHoaDonDAO() throws SQLException {
        this.connection = KetNoi.getInstance().getConnection();
    }

    public List<ChiTietHoaDon> getChiTietByMaHoaDon(String maHD) throws SQLException {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHD = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = mapResultSetToChiTietHoaDon(rs);
                    list.add(ct);
                }
            }
        }
        return list;
    }

    public ChiTietHoaDon getChiTietByMaVe(String maVe) throws SQLException {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maVe = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maVe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToChiTietHoaDon(rs);
                }
            }
        }
        return null;
    }

    public boolean createChiTiet(ChiTietHoaDon chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (maHD, maVe, donGia) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, chiTiet.getMaHD());
            pstmt.setString(2, chiTiet.getMaVe());
            pstmt.setBigDecimal(3, chiTiet.getDonGia());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteChiTiet(String maHD, String maVe) throws SQLException {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHD = ? AND maVe = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            pstmt.setString(2, maVe);
            return pstmt.executeUpdate() > 0;
        }
    }

    private ChiTietHoaDon mapResultSetToChiTietHoaDon(ResultSet rs) throws SQLException {
        String maHD = rs.getString("maHD");
        String maVe = rs.getString("maVe");
        BigDecimal donGia = rs.getBigDecimal("donGia");
        
        return new ChiTietHoaDon(maHD, maVe, donGia);
    }

	public boolean createChiTietHoaDon(ChiTietHoaDon chiTiet) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<ChiTietHoaDon> getChiTietHoaDonByMaHD(String maHD) {
		// TODO Auto-generated method stub
		return null;
	}
}
