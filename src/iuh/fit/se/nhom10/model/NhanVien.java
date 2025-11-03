package iuh.fit.se.nhom10.model;

import java.math.BigDecimal;

/**
 * Model đại diện cho nhân viên
 */
public class NhanVien {
    private String maNV;
    private String tenNV;
    private int maChucVu;
    private BigDecimal luong;
    private String soDienThoai;

    // Constructor không tham số
    public NhanVien() {
    }

    // Constructor đầy đủ
    public NhanVien(String maNV, String tenNV, int maChucVu, BigDecimal luong, String soDienThoai) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.maChucVu = maChucVu;
        this.luong = luong;
        this.soDienThoai = soDienThoai;
    }

    // Getters and Setters
    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public int getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(int maChucVu) {
        this.maChucVu = maChucVu;
    }

    public BigDecimal getLuong() {
        return luong;
    }

    public void setLuong(BigDecimal luong) {
        this.luong = luong;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", tenNV='" + tenNV + '\'' +
                ", maChucVu=" + maChucVu +
                ", soDienThoai='" + soDienThoai + '\'' +
                '}';
    }
}
