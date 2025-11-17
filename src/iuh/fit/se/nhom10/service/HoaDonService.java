package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.HoaDonDAO;
import iuh.fit.se.nhom10.model.HoaDon;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service layer cho quản lý Hóa Đơn
 * Cung cấp các phương thức business logic liên quan đến hóa đơn
 */
public class HoaDonService {
    private HoaDonDAO hoaDonDAO;

    public HoaDonService() throws SQLException {
        this.hoaDonDAO = new HoaDonDAO();
    }

    public List<HoaDon> getAllHoaDon() throws SQLException {
        return hoaDonDAO.getAllHoaDon();
    }

    public HoaDon getHoaDonByMa(String maHD) throws SQLException {
        if (maHD == null || maHD.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống");
        }
        return hoaDonDAO.getHoaDonByMa(maHD);
    }

    public List<HoaDon> searchHoaDon(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllHoaDon();
        }
        return hoaDonDAO.searchHoaDon(keyword);
    }

    public boolean createHoaDon(HoaDon hoaDon) throws SQLException {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không được null");
        }
        if (hoaDon.getMaHD() == null || hoaDon.getMaHD().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống");
        }
        if (hoaDon.getMaNV() == null || hoaDon.getMaNV().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống");
        }
        if (hoaDon.getTongTien() == null || hoaDon.getTongTien().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Tổng tiền phải lớn hơn hoặc bằng 0");
        }

        return hoaDonDAO.createHoaDon(hoaDon);
    }

    public boolean updateHoaDon(HoaDon hoaDon) throws SQLException {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không được null");
        }
        if (hoaDon.getMaHD() == null || hoaDon.getMaHD().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống");
        }

        HoaDon existingHoaDon = hoaDonDAO.getHoaDonByMa(hoaDon.getMaHD());
        if (existingHoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại");
        }

        return hoaDonDAO.updateHoaDon(hoaDon);
    }

    public boolean deleteHoaDon(String maHD) throws SQLException {
        if (maHD == null || maHD.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống");
        }

        HoaDon hoaDon = hoaDonDAO.getHoaDonByMa(maHD);
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không tồn tại");
        }

        return hoaDonDAO.deleteHoaDon(maHD);
    }

    public List<HoaDon> getHoaDonByDate(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Ngày bắt đầu và kết thúc không được null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        return hoaDonDAO.getHoaDonByDate(startDate, endDate);
    }

    public Map<java.time.LocalDate, BigDecimal[]> getRevenueByDay() throws SQLException {
        return hoaDonDAO.getRevenueByDay();
    }

    public Map<String, BigDecimal[]> getRevenueByMonth() throws SQLException {
        return hoaDonDAO.getRevenueByMonth();
    }

    public Map<Integer, BigDecimal[]> getRevenueByYear() throws SQLException {
        return hoaDonDAO.getRevenueByYear();
    }

    public Map<String, BigDecimal[]> getRevenueByFilm() throws SQLException {
        return hoaDonDAO.getRevenueByFilm();
    }

    public Map<String, Object[]> getRevenueByFilmWithId() throws SQLException {
        return hoaDonDAO.getRevenueByFilmWithId();
    }

    public Map<String, BigDecimal[]> getRevenueByEmployee() throws SQLException {
        return hoaDonDAO.getRevenueByEmployee();
    }

    public Map<String, Object[]> getRevenueByEmployeeWithId() throws SQLException {
        return hoaDonDAO.getRevenueByEmployeeWithId();
    }

    public Map<String[], BigDecimal[]> getRevenueByScreening() throws SQLException {
        return hoaDonDAO.getRevenueByScreening();
    }

    public List<Object[]> getTopSellingFilms(int limit) throws SQLException {
        if (limit <= 0) {
            throw new IllegalArgumentException("Số lượng phim phải lớn hơn 0");
        }
        return hoaDonDAO.getTopSellingFilms(limit);
    }

    public List<Object[]> getTopCustomers() throws SQLException {
        return hoaDonDAO.getTopCustomers();
    }

    public List<Object[]> getTopPromotions() throws SQLException {
        return hoaDonDAO.getTopPromotions();
    }

    public Map<String, Object[]> getTicketSalesByDay() throws SQLException {
        return hoaDonDAO.getTicketSalesByDay();
    }

    public BigDecimal calculateRemainingAmount(HoaDon hoaDon) throws SQLException {
        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không được null");
        }

        BigDecimal tongTien = hoaDon.getTongTien() != null ? hoaDon.getTongTien() : BigDecimal.ZERO;
        BigDecimal giamGia = hoaDon.getGiamGia() != null ? hoaDon.getGiamGia() : BigDecimal.ZERO;
        BigDecimal thanhToan = hoaDon.getThanhToan() != null ? hoaDon.getThanhToan() : BigDecimal.ZERO;

        return tongTien.subtract(giamGia).subtract(thanhToan);
    }

    public boolean isFullyPaid(HoaDon hoaDon) throws SQLException {
        return calculateRemainingAmount(hoaDon).compareTo(BigDecimal.ZERO) <= 0;
    }
}
