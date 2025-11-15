package iuh.fit.se.nhom10.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model đại diện cho Thanh Toán
 * Lưu thông tin thanh toán cho mỗi hóa đơn
 */
public class ThanhToan {
    private String maThanhToan;           // Mã thanh toán (khóa chính)
    private String maHD;                  // Mã hóa đơn (khóa ngoài - UNIQUE)
    private String phuongThuc;            // Phương thức thanh toán (Tiền mặt, MoMo, Banking...)
    private LocalDateTime ngayThanhToan;  // Ngày thanh toán
    private String trangThai;             // Trạng thái (Đã thanh toán, Chờ thanh toán, Thất bại...)

    public ThanhToan() {}

    public ThanhToan(String maThanhToan, String maHD, String phuongThuc, LocalDateTime ngayThanhToan, String trangThai) {
        this.maThanhToan = maThanhToan;
        this.maHD = maHD;
        this.phuongThuc = phuongThuc;
        this.ngayThanhToan = ngayThanhToan;
        this.trangThai = trangThai;
    }

    public ThanhToan(String maThanhToan, String maHD, String phuongThuc, String trangThai) {
        this.maThanhToan = maThanhToan;
        this.maHD = maHD;
        this.phuongThuc = phuongThuc;
        this.ngayThanhToan = LocalDateTime.now();
        this.trangThai = trangThai;
    }

    // ==================== GETTERS AND SETTERS ====================

    public String getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(String maThanhToan) {
        this.maThanhToan = maThanhToan;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThanhToan thanhToan = (ThanhToan) o;
        return Objects.equals(maThanhToan, thanhToan.maThanhToan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThanhToan);
    }

    @Override
    public String toString() {
        return "ThanhToan{" +
                "maThanhToan='" + maThanhToan + '\'' +
                ", maHD='" + maHD + '\'' +
                ", phuongThuc='" + phuongThuc + '\'' +
                ", ngayThanhToan=" + ngayThanhToan +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
