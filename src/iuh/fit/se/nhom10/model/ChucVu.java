package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho chức vụ
 */
public class ChucVu {
    private int maChucVu;
    private String tenChucVu;

    public ChucVu() {
    }

    public ChucVu(int maChucVu, String tenChucVu) {
        this.maChucVu = maChucVu;
        this.tenChucVu = tenChucVu;
    }

    public int getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(int maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    @Override
    public String toString() {
        return tenChucVu;
    }
}
