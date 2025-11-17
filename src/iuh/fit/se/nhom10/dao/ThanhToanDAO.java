package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.ThanhToan;
import iuh.fit.se.nhom10.util.KetNoi;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ThanhToanDAO {
    private Connection connection;

    public ThanhToanDAO() throws SQLException {
        this.connection = KetNoi.getInstance().getConnection();
    }

    public List<ThanhToan> getAllThanhToan() throws SQLException {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM ThanhToan";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ThanhToan tt = mapResultSetToThanhToan(rs);
                list.add(tt);
            }
        }
        return list;
    }

    public ThanhToan getThanhToanByMa(String maThanhToan) throws SQLException {
        String sql = "SELECT * FROM ThanhToan WHERE maThanhToan = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maThanhToan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToThanhToan(rs);
                }
            }
        }
        return null;
    }

    public ThanhToan getThanhToanByMaHoaDon(String maHD) throws SQLException {
        String sql = "SELECT * FROM ThanhToan WHERE maHD = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToThanhToan(rs);
                }
            }
        }
        return null;
    }

    public boolean createThanhToan(ThanhToan thanhToan) throws SQLException {
        String sql = "INSERT INTO ThanhToan (maThanhToan, maHD, phuongThuc, ngayThanhToan, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, thanhToan.getMaThanhToan());
            pstmt.setString(2, thanhToan.getMaHD());
            pstmt.setString(3, thanhToan.getPhuongThuc());
            pstmt.setTimestamp(4, Timestamp.valueOf(thanhToan.getNgayThanhToan()));
            pstmt.setString(5, thanhToan.getTrangThai());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateThanhToan(ThanhToan thanhToan) throws SQLException {
        String sql = "UPDATE ThanhToan SET maHD = ?, phuongThuc = ?, ngayThanhToan = ?, trangThai = ? WHERE maThanhToan = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, thanhToan.getMaHD());
            pstmt.setString(2, thanhToan.getPhuongThuc());
            pstmt.setTimestamp(3, Timestamp.valueOf(thanhToan.getNgayThanhToan()));
            pstmt.setString(4, thanhToan.getTrangThai());
            pstmt.setString(5, thanhToan.getMaThanhToan());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteThanhToan(String maThanhToan) throws SQLException {
        String sql = "DELETE FROM ThanhToan WHERE maThanhToan = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maThanhToan);
            return pstmt.executeUpdate() > 0;
        }
    }

    public String getNextThanhToanId() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(maThanhToan, 3) AS INT)) as maxNum FROM ThanhToan WHERE maThanhToan LIKE 'TT%'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int nextNum = 1;
            if (rs.next()) {
                int maxNum = rs.getInt("maxNum");
                if (maxNum > 0) {
                    nextNum = maxNum + 1;
                }
            }
            return String.format("TT%03d", nextNum);
        }
    }

    private ThanhToan mapResultSetToThanhToan(ResultSet rs) throws SQLException {
        String maThanhToan = rs.getString("maThanhToan");
        String maHD = rs.getString("maHD");
        String phuongThuc = rs.getString("phuongThuc");
        Timestamp timestamp = rs.getTimestamp("ngayThanhToan");
        LocalDateTime ngayThanhToan = timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now();
        String trangThai = rs.getString("trangThai");
        
        return new ThanhToan(maThanhToan, maHD, phuongThuc, ngayThanhToan, trangThai);
    }

    public List<ThanhToan> getThanhToanByMaHD(String maHD) throws SQLException {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM ThanhToan WHERE maHD = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ThanhToan tt = mapResultSetToThanhToan(rs);
                    list.add(tt);
                }
            }
        }
        return list;
    }
}