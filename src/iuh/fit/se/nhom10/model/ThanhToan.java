package iuh.fit.se.nhom10.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model đại diện cho thanh toán hóa đơn
 * Mỗi hóa đơn có tối đa 1 bản ghi thanh toán
 */
public class ThanhToan {
    private String maThanhToan;         // Mã thanh toán (khóa chính)
    private String maHD;                // Mã hóa đơn (khóa ngoài - unique)
    private String phuongThuc;          // Phương thức thanh toán (Tiền mặt, Chuyển khoản, Thẻ, v.v.)
    private LocalDateTime ngayThanhToan; // Ngày và giờ thanh toán
    private String trangThai;           // Trạng thái (Chưa thanh toán, Đã thanh toán, Hủy, v.v.)

    // Constructor không tham số
    public ThanhToan() {
    }

    // Constructor đầy đủ
    public ThanhToan(String maThanhToan, String maHD, String phuongThuc, LocalDateTime ngayThanhToan, String trangThai) {
        this.maThanhToan = maThanhToan;
        this.maHD = maHD;
        this.phuongThuc = phuongThuc;
        this.ngayThanhToan = ngayThanhToan;
        this.trangThai = trangThai;
    }

    // Constructor cho thêm mới
    public ThanhToan(String maHD, String phuongThuc, String trangThai) {
        this.maHD = maHD;
        this.phuongThuc = phuongThuc;
        this.ngayThanhToan = LocalDateTime.now();
        this.trangThai = trangThai;
    }

    // Getters and Setters
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

    // Business methods
    public boolean isThanhToan() {
        return "Đã thanh toán".equals(trangThai);
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
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
