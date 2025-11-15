package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.HoaDonDAO;
import iuh.fit.se.nhom10.dao.PhimDAO;
import iuh.fit.se.nhom10.dao.NhanVienDAO;
import iuh.fit.se.nhom10.dao.KhachHangDAO;
import iuh.fit.se.nhom10.dao.LichChieuDAO;
import iuh.fit.se.nhom10.model.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Service để lấy dữ liệu dashboard
 */
public class DashboardService {
    private HoaDonDAO hoaDonDAO;
    private PhimDAO phimDAO;
    private NhanVienDAO nhanVienDAO;
    private KhachHangDAO khachHangDAO;
    private LichChieuDAO lichChieuDAO;

    public DashboardService() throws Exception {
        this.hoaDonDAO = new HoaDonDAO();
        this.phimDAO = new PhimDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.lichChieuDAO = new LichChieuDAO();
    }

    /**
     * Lấy doanh thu hôm nay
     */
    public BigDecimal getRevenueTodayFull() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        List<HoaDon> hoaDons = hoaDonDAO.getHoaDonByDate(startOfDay, endOfDay);
        BigDecimal total = BigDecimal.ZERO;
        
        for (HoaDon hd : hoaDons) {
            if (hd.getThanhToan() != null) {
                total = total.add(hd.getThanhToan());
            }
        }
        return total;
    }

    /**
     * Lấy số vé bán hôm nay
     */
    public long getTicketsSoldTodayFull() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        List<HoaDon> hoaDons = hoaDonDAO.getHoaDonByDate(startOfDay, endOfDay);
        long totalTickets = 0;
        
        for (HoaDon hd : hoaDons) {
            // Tính tổng vé từ chi tiết hóa đơn nếu cần
            // Ở đây giả sử mỗi hóa đơn có 1 giao dịch = 1 lần mua vé
            totalTickets += 1; // Có thể cộng thêm từ ChiTietHoaDon nếu cần
        }
        return totalTickets;
    }

    /**
     * Lấy tổng số phim
     */
    public int getTotalMovies() throws Exception {
        return phimDAO.getAllPhim().size();
    }

    /**
     * Lấy tổng số nhân viên
     */
    public int getTotalEmployees() throws Exception {
        return nhanVienDAO.getAllNhanVien().size();
    }

    /**
     * Lấy tổng số khách hàng
     */
    public int getTotalCustomers() throws Exception {
        return khachHangDAO.getAllKhachHang().size();
    }

    /**
     * Lấy tổng số phòng chiếu
     */
    public int getTotalScreens() throws Exception {
        return lichChieuDAO.getAllLichChieu().size();
    }
}
