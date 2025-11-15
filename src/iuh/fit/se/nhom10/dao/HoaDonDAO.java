package iuh.fit.se.nhom10.dao;

import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.util.KetNoi;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HoaDonDAO {
    private Connection connection;

    public HoaDonDAO() throws SQLException {
        this.connection = KetNoi.getInstance().getConnection();
    }

    public List<HoaDon> getAllHoaDon() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY ngayLap DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HoaDon hd = mapResultSetToHoaDon(rs);
                list.add(hd);
            }
        }
        return list;
    }

    public HoaDon getHoaDonByMa(String maHD) throws SQLException {
        String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHoaDon(rs);
                }
            }
        }
        return null;
    }

    public List<HoaDon> searchHoaDon(String keyword) throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT DISTINCT h.* FROM HoaDon h " +
                    "LEFT JOIN KhachHang k ON h.maKH = k.maKH " +
                    "LEFT JOIN NhanVien n ON h.maNV = n.maNV " +
                    "WHERE h.maHD LIKE ? OR k.tenKH LIKE ? OR n.tenNV LIKE ? " +
                    "ORDER BY h.ngayLap DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchTerm = "%" + keyword + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = mapResultSetToHoaDon(rs);
                    list.add(hd);
                }
            }
        }
        return list;
    }

    public boolean createHoaDon(HoaDon hoaDon) throws SQLException {
        String sql = "INSERT INTO HoaDon (maHD, ngayLap, tongTien, giamGia, thanhToan, maNV, maKH, maKM) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, hoaDon.getMaHD());
            pstmt.setTimestamp(2, Timestamp.valueOf(hoaDon.getNgayLap()));
            pstmt.setBigDecimal(3, hoaDon.getTongTien());
            pstmt.setBigDecimal(4, hoaDon.getGiamGia());
            pstmt.setBigDecimal(5, hoaDon.getThanhToan());
            pstmt.setString(6, hoaDon.getMaNV());
            pstmt.setString(7, hoaDon.getMaKH());
            pstmt.setString(8, hoaDon.getMaKM());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateHoaDon(HoaDon hoaDon) throws SQLException {
        String sql = "UPDATE HoaDon SET ngayLap = ?, tongTien = ?, giamGia = ?, thanhToan = ?, maNV = ?, maKH = ?, maKM = ? WHERE maHD = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(hoaDon.getNgayLap()));
            pstmt.setBigDecimal(2, hoaDon.getTongTien());
            pstmt.setBigDecimal(3, hoaDon.getGiamGia());
            pstmt.setBigDecimal(4, hoaDon.getThanhToan());
            pstmt.setString(5, hoaDon.getMaNV());
            pstmt.setString(6, hoaDon.getMaKH());
            pstmt.setString(7, hoaDon.getMaKM());
            pstmt.setString(8, hoaDon.getMaHD());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteHoaDon(String maHD) throws SQLException {
        String sql = "DELETE FROM HoaDon WHERE maHD = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, maHD);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<HoaDon> getHoaDonByDate(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE ngayLap BETWEEN ? AND ? ORDER BY ngayLap DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = mapResultSetToHoaDon(rs);
                    list.add(hd);
                }
            }
        }
        return list;
    }

    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        String maHD = rs.getString("maHD");
        Timestamp timestamp = rs.getTimestamp("ngayLap");
        LocalDateTime ngayLap = timestamp != null ? timestamp.toLocalDateTime() : null;

        BigDecimal tongTien = rs.getBigDecimal("tongTien");
        BigDecimal giamGia = rs.getBigDecimal("giamGia");
        BigDecimal thanhToan = rs.getBigDecimal("thanhToan");

        String maNV = rs.getString("maNV");
        String maKH = rs.getString("maKH");
        String maKM = rs.getString("maKM");

        HoaDon hd = new HoaDon(
                maHD,
                ngayLap,
                tongTien != null ? tongTien : BigDecimal.ZERO,
                giamGia != null ? giamGia : BigDecimal.ZERO,
                thanhToan != null ? thanhToan : BigDecimal.ZERO,
                maNV,
                maKH,
                maKM
        );

        return hd;
    }

    public Map<java.time.LocalDate, BigDecimal[]> getRevenueByDay() throws SQLException {
        Map<java.time.LocalDate, BigDecimal[]> result = new LinkedHashMap<>();
        String sql = "SELECT CAST(ngayLap AS DATE) as ngay, COUNT(*) as soHD, SUM(thanhToan) as tongTien " +
                    "FROM HoaDon WHERE thanhToan IS NOT NULL GROUP BY CAST(ngayLap AS DATE) " +
                    "ORDER BY ngay DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                java.time.LocalDate ngay = rs.getDate("ngay").toLocalDate();
                BigDecimal tongTien = rs.getBigDecimal("tongTien");
                BigDecimal soHD = new BigDecimal(rs.getInt("soHD"));
                result.put(ngay, new BigDecimal[]{tongTien != null ? tongTien : BigDecimal.ZERO, soHD});
            }
        }
        return result;
    }

    public Map<String, BigDecimal[]> getRevenueByMonth() throws SQLException {
        Map<String, BigDecimal[]> result = new LinkedHashMap<>();
        String sql = "SELECT YEAR(ngayLap) as nam, MONTH(ngayLap) as thang, COUNT(*) as soHD, SUM(thanhToan) as tongTien " +
                    "FROM HoaDon WHERE thanhToan IS NOT NULL GROUP BY YEAR(ngayLap), MONTH(ngayLap) " +
                    "ORDER BY nam DESC, thang DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String key = "Tháng " + rs.getInt("thang") + "/" + rs.getInt("nam");
                BigDecimal tongTien = rs.getBigDecimal("tongTien");
                BigDecimal soHD = new BigDecimal(rs.getInt("soHD"));
                result.put(key, new BigDecimal[]{tongTien != null ? tongTien : BigDecimal.ZERO, soHD});
            }
        }
        return result;
    }

    public Map<Integer, BigDecimal[]> getRevenueByYear() throws SQLException {
        Map<Integer, BigDecimal[]> result = new LinkedHashMap<>();
        String sql = "SELECT YEAR(ngayLap) as nam, COUNT(*) as soHD, SUM(thanhToan) as tongTien " +
                    "FROM HoaDon WHERE thanhToan IS NOT NULL GROUP BY YEAR(ngayLap) ORDER BY nam DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int nam = rs.getInt("nam");
                BigDecimal tongTien = rs.getBigDecimal("tongTien");
                BigDecimal soHD = new BigDecimal(rs.getInt("soHD"));
                result.put(nam, new BigDecimal[]{tongTien != null ? tongTien : BigDecimal.ZERO, soHD});
            }
        }
        return result;
    }

    public Map<String, Object[]> getRevenueByFilmWithId() throws SQLException {
        Map<String, Object[]> result = new LinkedHashMap<>();
        String sql = "SELECT p.maPhim, p.tenPhim, COUNT(v.maVe) as soVe, SUM(ct.donGia) as doanh " +
                    "FROM Phim p LEFT JOIN LichChieu l ON p.maPhim = l.maPhim " +
                    "LEFT JOIN VeXemPhim v ON l.maLich = v.maLich " +
                    "LEFT JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe " +
                    "GROUP BY p.maPhim, p.tenPhim ORDER BY doanh DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String filmName = rs.getString("tenPhim");
                String maPhim = rs.getString("maPhim");
                BigDecimal doanh = rs.getBigDecimal("doanh");
                int soVe = rs.getInt("soVe");
                result.put(filmName, new Object[]{maPhim, filmName, soVe, doanh != null ? doanh : BigDecimal.ZERO});
            }
        }
        return result;
    }

    public Map<String, BigDecimal[]> getRevenueByFilm() throws SQLException {
        Map<String, BigDecimal[]> result = new LinkedHashMap<>();
        String sql = "SELECT p.maPhim, p.tenPhim, COUNT(v.maVe) as soVe, SUM(ct.donGia) as doanh " +
                    "FROM Phim p LEFT JOIN LichChieu l ON p.maPhim = l.maPhim " +
                    "LEFT JOIN VeXemPhim v ON l.maLich = v.maLich " +
                    "LEFT JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe " +
                    "GROUP BY p.maPhim, p.tenPhim ORDER BY doanh DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String filmName = rs.getString("tenPhim");
                BigDecimal doanh = rs.getBigDecimal("doanh");
                BigDecimal soVe = new BigDecimal(rs.getInt("soVe"));
                String maPhim = rs.getString("maPhim");
                result.put(filmName, new BigDecimal[]{doanh != null ? doanh : BigDecimal.ZERO, soVe, BigDecimal.valueOf(1)}); // Placeholder for compatibility
            }
        }
        return result;
    }

    public Map<String[], BigDecimal[]> getRevenueByScreening() throws SQLException {
        Map<String[], BigDecimal[]> result = new LinkedHashMap<>();
        String sql = "SELECT l.maLich, p.tenPhim, l.ngayChieu, l.gioBatDau, COUNT(v.maVe) as soVe, SUM(ct.donGia) as doanh " +
                    "FROM LichChieu l LEFT JOIN Phim p ON l.maPhim = p.maPhim " +
                    "LEFT JOIN VeXemPhim v ON l.maLich = v.maLich " +
                    "LEFT JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe " +
                    "GROUP BY l.maLich, p.tenPhim, l.ngayChieu, l.gioBatDau ORDER BY l.ngayChieu DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] key = {rs.getString("maLich"), rs.getString("tenPhim"), 
                               rs.getString("ngayChieu"), rs.getString("gioBatDau")};
                BigDecimal doanh = rs.getBigDecimal("doanh");
                BigDecimal soVe = new BigDecimal(rs.getInt("soVe"));
                result.put(key, new BigDecimal[]{doanh != null ? doanh : BigDecimal.ZERO, soVe});
            }
        }
        return result;
    }

    public Map<String, Object[]> getRevenueByEmployeeWithId() throws SQLException {
        Map<String, Object[]> result = new LinkedHashMap<>();
        String sql = "SELECT n.maNV, n.tenNV, COUNT(h.maHD) as soHD, SUM(h.thanhToan) as doanh " +
                    "FROM NhanVien n LEFT JOIN HoaDon h ON n.maNV = h.maNV AND h.thanhToan IS NOT NULL " +
                    "GROUP BY n.maNV, n.tenNV ORDER BY doanh DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tenNV = rs.getString("tenNV");
                String maNV = rs.getString("maNV");
                BigDecimal doanh = rs.getBigDecimal("doanh");
                int soHD = rs.getInt("soHD");
                result.put(tenNV, new Object[]{maNV, tenNV, soHD, doanh != null ? doanh : BigDecimal.ZERO});
            }
        }
        return result;
    }

    public Map<String, BigDecimal[]> getRevenueByEmployee() throws SQLException {
        Map<String, BigDecimal[]> result = new LinkedHashMap<>();
        String sql = "SELECT n.maNV, n.tenNV, COUNT(h.maHD) as soHD, SUM(h.thanhToan) as doanh " +
                    "FROM NhanVien n LEFT JOIN HoaDon h ON n.maNV = h.maNV AND h.thanhToan IS NOT NULL " +
                    "GROUP BY n.maNV, n.tenNV ORDER BY doanh DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tenNV = rs.getString("tenNV");
                BigDecimal doanh = rs.getBigDecimal("doanh");
                BigDecimal soHD = new BigDecimal(rs.getInt("soHD"));
                String maNV = rs.getString("maNV");
                result.put(tenNV, new BigDecimal[]{doanh != null ? doanh : BigDecimal.ZERO, soHD, BigDecimal.valueOf(1)}); // Placeholder for compatibility
            }
        }
        return result;
    }

    public List<Object[]> getTopSellingFilms(int limit) throws SQLException {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT TOP " + limit + " p.tenPhim, COUNT(v.maVe) as soVe, SUM(ct.donGia) as doanh " +
                    "FROM Phim p LEFT JOIN LichChieu l ON p.maPhim = l.maPhim " +
                    "LEFT JOIN VeXemPhim v ON l.maLich = v.maLich " +
                    "LEFT JOIN ChiTietHoaDon ct ON v.maVe = ct.maVe " +
                    "GROUP BY p.tenPhim ORDER BY soVe DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Object[]{
                    rs.getString("tenPhim"),
                    rs.getInt("soVe"),
                    rs.getBigDecimal("doanh") != null ? rs.getBigDecimal("doanh") : BigDecimal.ZERO
                });
            }
        }
        return result;
    }

    public List<Object[]> getTopCustomers() throws SQLException {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT TOP 10 k.maKH, k.tenKH, COUNT(h.maHD) as soMua, COALESCE(SUM(h.thanhToan), 0) as tongChi, MAX(h.ngayLap) as lanMuaCuoi " +
                    "FROM KhachHang k LEFT JOIN HoaDon h ON k.maKH = h.maKH AND h.thanhToan IS NOT NULL " +
                    "GROUP BY k.maKH, k.tenKH ORDER BY tongChi DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("lanMuaCuoi");
                String lanMua = ts != null ? ts.toLocalDateTime().toLocalDate().format(formatter) : "N/A";
                result.add(new Object[]{
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getInt("soMua"),
                    rs.getBigDecimal("tongChi") != null ? rs.getBigDecimal("tongChi") : BigDecimal.ZERO,
                    lanMua
                });
            }
        }
        return result;
    }

    public List<Object[]> getTopPromotions() throws SQLException {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT TOP 10 km.maKM, km.tenKM, COUNT(h.maHD) as soLan, COALESCE(SUM(h.giamGia), 0) as tongGiam, km.tiLeGiam " +
                    "FROM KhuyenMai km LEFT JOIN HoaDon h ON km.maKM = h.maKM AND h.maKM IS NOT NULL " +
                    "GROUP BY km.maKM, km.tenKM, km.tiLeGiam ORDER BY soLan DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Object[]{
                    rs.getString("maKM"),
                    rs.getString("tenKM"),
                    rs.getInt("soLan"),
                    rs.getBigDecimal("tongGiam") != null ? rs.getBigDecimal("tongGiam") : BigDecimal.ZERO,
                    rs.getBigDecimal("tiLeGiam") != null ? rs.getBigDecimal("tiLeGiam") : BigDecimal.ZERO
                });
            }
        }
        return result;
    }

    public Map<String, Object[]> getTicketSalesByDay() throws SQLException {
        Map<String, Object[]> result = new LinkedHashMap<>();
        String sql = "SELECT CAST(ngayLap AS DATE) as ngay, COUNT(*) as soVe, COALESCE(SUM(thanhToan), 0) as doanh " +
                    "FROM HoaDon WHERE thanhToan IS NOT NULL GROUP BY CAST(ngayLap AS DATE) " +
                    "ORDER BY ngay DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String ngay = rs.getString("ngay");
                long soVe = rs.getLong("soVe");
                BigDecimal doanh = rs.getBigDecimal("doanh");
                result.put(ngay, new Object[]{soVe, doanh != null ? doanh : BigDecimal.ZERO});
            }
        }
        return result;
    }
}
