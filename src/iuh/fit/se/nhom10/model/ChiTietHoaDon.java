package iuh.fit.se.nhom10.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Model đại diện cho Chi Tiết Hóa Đơn
 * Lưu chi tiết các vé trong mỗi hóa đơn
 */
public class ChiTietHoaDon {
    private String maHD;       // Mã hóa đơn (khóa chính, khóa ngoài)
    private String maVe;       // Mã vé (khóa chính, khóa ngoài - UNIQUE)
    private BigDecimal donGia; // Đơn giá của vé

    public ChiTietHoaDon() {}

    public ChiTietHoaDon(String maHD, String maVe, BigDecimal donGia) {
        this.maHD = maHD;
        this.maVe = maVe;
        this.donGia = donGia;
    }

    // ==================== GETTERS AND SETTERS ====================

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDon that = (ChiTietHoaDon) o;
        return Objects.equals(maVe, that.maVe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maVe);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maVe='" + maVe + '\'' +
                ", donGia=" + donGia +
                '}';
    }
}
