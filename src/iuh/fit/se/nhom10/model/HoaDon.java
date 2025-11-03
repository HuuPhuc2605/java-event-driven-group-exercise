package iuh.fit.se.nhom10.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model đại diện cho Hóa Đơn
 * Hóa đơn là ghi chép giao dịch bán vé xem phim cho khách hàng
 */
public class HoaDon {
    private String maHD;                // Mã hóa đơn (khóa chính)
    private LocalDateTime ngayLap;      // Ngày lập hóa đơn
    private BigDecimal tongTien;        // Tổng tiền của hóa đơn
    private String maNV;                // Mã nhân viên lập hóa đơn (khóa ngoài - bắt buộc)
    private String maKH;                // Mã khách hàng (khóa ngoài - có thể null)
    private String maKM;                // Mã khuyến mãi (khóa ngoài - có thể null)

    /**
     * Constructor không tham số
     */
    public HoaDon() {
    }

    /**
     * Constructor đầy đủ
     */
    public HoaDon(String maHD, LocalDateTime ngayLap, BigDecimal tongTien, String maNV, String maKH, String maKM) {
        this.maHD = maHD;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
    }

    /**
     * Constructor với tham số cơ bản
     * NgayLap sẽ được set thành thời gian hiện tại
     */
    public HoaDon(String maHD, BigDecimal tongTien, String maNV, String maKH, String maKM) {
        this.maHD = maHD;
        this.ngayLap = LocalDateTime.now();
        this.tongTien = tongTien;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
    }

    /**
     * Constructor cho trường hợp lập hóa đơn không có khuyến mãi
     */
    public HoaDon(String maHD, BigDecimal tongTien, String maNV, String maKH) {
        this(maHD, tongTien, maNV, maKH, null);
    }

    // ==================== GETTERS AND SETTERS ====================

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    // ==================== BUSINESS METHODS ====================

    /**
     * Kiểm tra xem hóa đơn có khách hàng hay không
     * @return true nếu có mã khách hàng, false nếu không
     */
    public boolean coKhachHang() {
        return maKH != null && !maKH.trim().isEmpty();
    }

    /**
     * Kiểm tra xem hóa đơn có áp dụng khuyến mãi hay không
     * @return true nếu có mã khuyến mãi, false nếu không
     */
    public boolean coKhuyenMai() {
        return maKM != null && !maKM.trim().isEmpty();
    }

    // ==================== EQUALS, HASHCODE, TOSTRING ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", ngayLap=" + ngayLap +
                ", tongTien=" + tongTien +
                ", maNV='" + maNV + '\'' +
                ", maKH='" + maKH + '\'' +
                ", maKM='" + maKM + '\'' +
                '}';
    }
}