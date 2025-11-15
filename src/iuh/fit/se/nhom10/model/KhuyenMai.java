package iuh.fit.se.nhom10.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Model đại diện cho chương trình khuyến mãi
 * Khuyến mãi dùng để áp dụng giảm giá cho hóa đơn
 */
public class KhuyenMai {
    private String maKM;                // Mã khuyến mãi (khóa chính)
    private String tenKM;               // Tên khuyến mãi
    private String moTa;                // Mô tả chi tiết
    private double tiLeGiam;            // Tỉ lệ giảm (%) - từ 0-100
    private LocalDate ngayBatDau;       // Ngày bắt đầu áp dụng
    private LocalDate ngayKetThuc;      // Ngày kết thúc áp dụng

    // Constructor không tham số
    public KhuyenMai() {
    }

    // Constructor đầy đủ
    public KhuyenMai(String maKM, String tenKM, String moTa, double tiLeGiam, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.moTa = moTa;
        this.tiLeGiam = tiLeGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    // Constructor cho thêm mới
    public KhuyenMai(String tenKM, String moTa, double tiLeGiam, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this.tenKM = tenKM;
        this.moTa = moTa;
        this.tiLeGiam = tiLeGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    // Getters and Setters
    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getTiLeGiam() {
        return tiLeGiam;
    }

    public void setTiLeGiam(double tiLeGiam) {
        this.tiLeGiam = tiLeGiam;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    // Business methods
    public boolean isHoatDong() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(ngayBatDau) && !today.isAfter(ngayKetThuc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhuyenMai khuyenMai = (KhuyenMai) o;
        return Objects.equals(maKM, khuyenMai.maKM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKM);
    }

    @Override
    public String toString() {
        return tenKM + " (" + tiLeGiam + "%)";
    }
}
