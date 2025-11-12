package iuh.fit.se.nhom10.service;

import iuh.fit.se.nhom10.model.PhongChieu;
import iuh.fit.se.nhom10.model.LoaiPhong;
import iuh.fit.se.nhom10.model.GheNgoi;
import iuh.fit.se.nhom10.dao.PhongChieuDAO;
import iuh.fit.se.nhom10.dao.LoaiPhongDAO;
import iuh.fit.se.nhom10.dao.GheNgoiDAO;

import java.util.List;

/**
 * Service cho quản lý phòng chiếu
 */
public class PhongChieuService {
    private PhongChieuDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private GheNgoiDAO gheDAO;

    public PhongChieuService() {
        this.phongDAO = new PhongChieuDAO();
        this.loaiPhongDAO = new LoaiPhongDAO();
        this.gheDAO = new GheNgoiDAO();
    }

    // ===== LOẠI PHÒNG =====
    
    public boolean createLoaiPhong(LoaiPhong loaiPhong) {
        if (loaiPhong == null || loaiPhong.getTenLoaiPhong() == null || loaiPhong.getTenLoaiPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại phòng không được trống");
        }
        return loaiPhongDAO.createLoaiPhong(loaiPhong);
    }

    public boolean addLoaiPhong(String tenLoaiPhong) {
        if (tenLoaiPhong == null || tenLoaiPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại phòng không được trống");
        }
        return loaiPhongDAO.createLoaiPhong(new LoaiPhong(tenLoaiPhong));
    }

    public boolean updateLoaiPhong(LoaiPhong loaiPhong) {
        if (loaiPhong == null || loaiPhong.getTenLoaiPhong() == null || loaiPhong.getTenLoaiPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại phòng không được trống");
        }
        return loaiPhongDAO.updateLoaiPhong(loaiPhong);
    }

    public boolean deleteLoaiPhong(String maLoaiPhong) {
        try {
            int id = Integer.parseInt(maLoaiPhong);
            return loaiPhongDAO.deleteLoaiPhong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Mã loại phòng không hợp lệ");
        }
    }

    public List<LoaiPhong> getAllLoaiPhong() {
        return loaiPhongDAO.getAllLoaiPhong();
    }

    public LoaiPhong getLoaiPhongByMa(String maLoaiPhong) {
        try {
            int id = Integer.parseInt(maLoaiPhong);
            return loaiPhongDAO.getLoaiPhongByMa(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Mã loại phòng không hợp lệ");
        }
    }

    // ===== PHÒNG CHIẾU =====
    
    public boolean createPhongChieu(PhongChieu phong) {
        if (phong == null || phong.getMaPhong() == null || phong.getMaPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được trống");
        }
        if (phong.getTenPhong() == null || phong.getTenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được trống");
        }
        if (phong.getSoGhe() <= 0) {
            throw new IllegalArgumentException("Số ghế phải lớn hơn 0");
        }
        return phongDAO.createPhongChieu(phong);
    }

    public boolean addPhongChieu(String maPhong, String tenPhong, int soGhe, int maLoaiPhong) {
        if (maPhong == null || maPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được trống");
        }
        if (tenPhong == null || tenPhong.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được trống");
        }
        if (soGhe <= 0) {
            throw new IllegalArgumentException("Số ghế phải lớn hơn 0");
        }
        
        PhongChieu phong = new PhongChieu(maPhong, tenPhong, soGhe, maLoaiPhong);
        return phongDAO.createPhongChieu(phong);
    }

    public boolean updatePhongChieu(PhongChieu phong) {
        if (phong == null || phong.getTenPhong() == null || phong.getTenPhong().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được trống");
        }
        if (phong.getSoGhe() <= 0) {
            throw new IllegalArgumentException("Số ghế phải lớn hơn 0");
        }
        return phongDAO.updatePhongChieu(phong);
    }

    public boolean deletePhongChieu(String maPhong) {
        return phongDAO.deletePhongChieu(maPhong);
    }

    public List<PhongChieu> getAllPhongChieu() {
        return phongDAO.getAllPhongChieu();
    }

    public PhongChieu getPhongChieuByMa(String maPhong) {
        return phongDAO.getPhongByMa(maPhong);
    }

    // ===== GHẾ NGỒI =====
    
    public boolean createGheNgoi(GheNgoi ghe) {
        if (ghe == null || ghe.getMaGhe() == null || ghe.getMaGhe().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã ghế không được trống");
        }
        if (ghe.getHang() == null || ghe.getHang().trim().isEmpty()) {
            throw new IllegalArgumentException("Hàng không được trống");
        }
        if (ghe.getCot() <= 0) {
            throw new IllegalArgumentException("Cột phải lớn hơn 0");
        }
        return gheDAO.createGheNgoi(ghe);
    }

    public boolean addGheNgoi(String maGhe, String hang, int cot, String trangThai) {
        if (maGhe == null || maGhe.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã ghế không được trống");
        }
        if (hang == null || hang.trim().isEmpty()) {
            throw new IllegalArgumentException("Hàng không được trống");
        }
        if (cot <= 0) {
            throw new IllegalArgumentException("Cột phải lớn hơn 0");
        }
        
        GheNgoi ghe = new GheNgoi(maGhe, hang, cot, trangThai);
        return gheDAO.createGheNgoi(ghe);
    }

    public boolean updateGheNgoi(GheNgoi ghe) {
        if (ghe == null || ghe.getHang() == null || ghe.getHang().trim().isEmpty()) {
            throw new IllegalArgumentException("Hàng không được trống");
        }
        if (ghe.getCot() <= 0) {
            throw new IllegalArgumentException("Cột phải lớn hơn 0");
        }
        return gheDAO.updateGheNgoi(ghe);
    }

    public boolean updateGheStatus(String maGhe, String trangThai) {
        GheNgoi ghe = gheDAO.getGheByMa(maGhe);
        if (ghe == null) {
            throw new IllegalArgumentException("Ghế không tồn tại");
        }
        ghe.setTrangThai(trangThai);
        return gheDAO.updateGheNgoi(ghe);
    }

    public boolean deleteGheNgoi(String maGhe) {
        return gheDAO.deleteGheWithReferences(maGhe);
    }

    public List<GheNgoi> getAllGheNgoi() {
        return gheDAO.getAllGheNgoi();
    }

    public GheNgoi getGheNgoiByMa(String maGhe) {
        return gheDAO.getGheByMa(maGhe);
    }
}
