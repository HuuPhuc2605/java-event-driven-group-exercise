package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho tài khoản nhân viên (Admin)
 */
public class TaiKhoanNhanVien {
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;
    private String maNV;
    private NhanVien nhanVien; // Tham chiếu đến nhân viên

    // Constructor không tham số
    public TaiKhoanNhanVien() {
    }

    // Constructor đầy đủ
    public TaiKhoanNhanVien(String tenDangNhap, String matKhau, String vaiTro, String maNV) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.maNV = maNV;
    }

    // Getters and Setters
    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    @Override
    public String toString() {
        return "TaiKhoanNhanVien{" +
                "tenDangNhap='" + tenDangNhap + '\'' +
                ", vaiTro='" + vaiTro + '\'' +
                ", maNV='" + maNV + '\'' +
                '}';
    }
}
