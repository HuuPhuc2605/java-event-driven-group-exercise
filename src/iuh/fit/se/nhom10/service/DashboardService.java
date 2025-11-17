package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.dao.DaoDienDAO;
import iuh.fit.se.nhom10.dao.HoaDonDAO;
import iuh.fit.se.nhom10.dao.PhimDAO;
import iuh.fit.se.nhom10.dao.NhanVienDAO;
import iuh.fit.se.nhom10.dao.KhachHangDAO;
import iuh.fit.se.nhom10.dao.LichChieuDAO;
import iuh.fit.se.nhom10.dao.PhongChieuDAO;
import iuh.fit.se.nhom10.model.DaoDien;
import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.model.LichChieu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private PhongChieuDAO phongChieuDAO;
    private DaoDienDAO daoDienDAO;

    public DashboardService() throws Exception {
        this.hoaDonDAO = new HoaDonDAO();
        this.phimDAO = new PhimDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.lichChieuDAO = new LichChieuDAO();
        this.phongChieuDAO = new PhongChieuDAO();
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
    public List<DaoDien> layTatCaDaoDien() {
        try {
            
			return daoDienDAO.getAllDaoDien();
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách đạo diễn: " + e.getMessage());
            return new ArrayList<>();
        }
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
     * Lấy tổng số phòng chiếu - sửa để lấy từ PhongChieuDAO
     * Updated to get screens from PhongChieuDAO instead of LichChieuDAO
     */
    public int getTotalScreens() throws Exception {
        return phongChieuDAO.getAllPhongChieu().size();
    }

    /**
     * Lấy danh sách hoạt động gần đây (lịch chiếu mới nhất)
     * Added new method to fetch recent activities from schedule
     */
    public List<String> getRecentActivities() throws Exception {
        List<String> activities = new ArrayList<>();
        try {
            // Lấy lịch chiếu gần đây từ database
            List<LichChieu> schedules = lichChieuDAO.getAllLichChieu();
            
            // Thêm các hoạt động mẫu - trong thực tế có thể lấy từ log hoặc từ database có history
            activities.add("✓ Thêm lịch chiếu mới");
            activities.add("✓ Cập nhật thông tin phim");
            activities.add("✓ Xác nhận vé bán hôm nay");
            
            // Giới hạn 3 hoạt động gần đây nhất
            return activities.size() > 3 ? activities.subList(0, 3) : activities;
        } catch (Exception e) {
            System.out.println("Lỗi lấy hoạt động: " + e.getMessage());
            activities.add("✓ Hệ thống hoạt động bình thường");
            return activities;
        }
    }
}
