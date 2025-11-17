package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.model.KhachHang;
import iuh.fit.se.nhom10.dao.KhachHangDAO;
import java.util.List;

/**
 * Service quản lý khách hàng cho nhân viên
 * Các chức năng: thêm, sửa, tìm kiếm khách hàng
 */
public class nvKhachHangService {
    private KhachHangDAO khachHangDAO;

    public nvKhachHangService() {
        this.khachHangDAO = new KhachHangDAO();
    }

    /**
     * Thêm khách hàng mới
     */
    public boolean themKhachHang(KhachHang kh) {
        if (kh == null || kh.getMaKH() == null || kh.getTenKH() == null) {
            System.out.println("Dữ liệu khách hàng không hợp lệ");
            return false;
        }
        return khachHangDAO.createKhachHang(kh);
    }

    /**
     * Sửa thông tin khách hàng (tên, số điện thoại)
     */
    public boolean suaKhachHang(KhachHang kh) {
        if (kh == null || kh.getMaKH() == null) {
            System.out.println("Mã khách hàng không hợp lệ");
            return false;
        }
        return khachHangDAO.updateKhachHang(kh);
    }

    /**
     * Tìm kiếm khách hàng theo mã hoặc tên
     */
    public List<KhachHang> timKiemKhachHang(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return khachHangDAO.getAllKhachHang();
        }
        return khachHangDAO.searchKhachHang(keyword.trim());
    }

    /**
     * Lấy tất cả khách hàng
     */
    public List<KhachHang> layTatCaKhachHang() {
        return khachHangDAO.getAllKhachHang();
    }

    /**
     * Lấy khách hàng theo mã
     */
    public KhachHang layKhachHangTheoMa(String maKH) {
        if (maKH == null || maKH.isEmpty()) {
            return null;
        }
        return khachHangDAO.getKhachHangByMa(maKH);
    }

    /**
     * Lấy mã khách hàng tiếp theo
     */
    public String getNextKhachHangId() {
        return khachHangDAO.getNextKhachHangId();
    }

	public boolean xoaKhachHang(String maKH) {
		// TODO Auto-generated method stub
		return false;
	}
}
