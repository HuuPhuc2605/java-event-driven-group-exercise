package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.model.HoaDon;
import iuh.fit.se.nhom10.model.ChiTietHoaDon;
import iuh.fit.se.nhom10.model.KhuyenMai;
import iuh.fit.se.nhom10.dao.HoaDonDAO;
import iuh.fit.se.nhom10.dao.ChiTietHoaDonDAO;
import iuh.fit.se.nhom10.dao.KhuyenMaiDAO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý hóa đơn cho nhân viên
 * Chức năng: tạo hóa đơn, thêm chi tiết, áp dụng khuyến mãi
 */
public class nvHoaDonService {
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private KhuyenMaiDAO khuyenMaiDAO;

    public nvHoaDonService() throws SQLException {
        this.hoaDonDAO = new HoaDonDAO();
        this.chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        this.khuyenMaiDAO = new KhuyenMaiDAO();
    }

    /**
     * Tạo hóa đơn mới
     * @throws SQLException 
     */
    public boolean taoHoaDon(HoaDon hoaDon) throws SQLException {
        if (hoaDon == null || hoaDon.getMaHD() == null) {
            try {
				System.out.println("Dữ liệu hóa đơn không hợp lệ");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return false;
        }
        return hoaDonDAO.createHoaDon(hoaDon);
    }

    /**
     * Thêm chi tiết hóa đơn
     */
    public boolean themChiTietHoaDon(ChiTietHoaDon chiTiet) {
        if (chiTiet == null || chiTiet.getMaHD() == null) {
            System.out.println("Dữ liệu chi tiết hóa đơn không hợp lệ");
            return false;
        }
        // Tự động tính thanhTien = donGia * soLuong
        if (chiTiet.getDonGia() != null && chiTiet.getSoLuong() > 0) {
            BigDecimal thanhTien = chiTiet.getDonGia().multiply(
                new BigDecimal(chiTiet.getSoLuong())
            );
            chiTiet.setThanhTien(thanhTien);
        }
        return chiTietHoaDonDAO.createChiTietHoaDon(chiTiet);
    }

    /**
     * Áp dụng khuyến mãi cho hóa đơn
     * @throws SQLException 
     */
    public boolean apDungKhuyenMai(String maHD, String maKM) throws SQLException {
        if (maHD == null || maKM == null) {
            return false;
        }
        HoaDon hoaDon = hoaDonDAO.getHoaDonByMa(maHD);
        if (hoaDon != null) {
            hoaDon.setMaKM(maKM);
            return hoaDonDAO.updateHoaDon(hoaDon);
        }
        return false;
    }

    /**
     * Lấy danh sách khuyến mãi
     */
    public List<KhuyenMai> layDanhSachKhuyenMai() {
        return khuyenMaiDAO.getAllKhuyenMai();
    }

    /**
     * Lấy hóa đơn theo mã
     * @throws SQLException 
     */
    public HoaDon layHoaDonTheoMa(String maHD) throws SQLException {
        if (maHD == null || maHD.isEmpty()) {
            return null;
        }
        return hoaDonDAO.getHoaDonByMa(maHD);
    }

    /**
     * Lấy chi tiết hóa đơn
     */
    public List<ChiTietHoaDon> layChiTietHoaDon(String maHD) {
        if (maHD == null || maHD.isEmpty()) {
            return null;
        }
        return chiTietHoaDonDAO.getChiTietHoaDonByMaHD(maHD);
    }

    /**
     * Added method to get all invoices for employee view
     */
    public List<HoaDon> layTatCaHoaDon() {
        try {
            return hoaDonDAO.getAllHoaDon();
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách hóa đơn: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}