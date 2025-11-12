package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho ghế ngồi
 */
public class GheNgoi {
    private String maGhe;
    private String hang;
    private int cot;
    private String trangThai;
    private String maPhong; // Added maPhong to link seat to room

    // Constructor không tham số
    public GheNgoi() {
    }

    // Constructor đầy đủ
    public GheNgoi(String maGhe, String hang, int cot, String trangThai) {
        this.maGhe = maGhe;
        this.hang = hang;
        this.cot = cot;
        this.trangThai = trangThai;
    }

    // Constructor với phòng chiếu
    public GheNgoi(String maGhe, String hang, int cot, String trangThai, String maPhong) {
        this.maGhe = maGhe;
        this.hang = hang;
        this.cot = cot;
        this.trangThai = trangThai;
        this.maPhong = maPhong;
    }

    // Getters and Setters
    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public int getCot() {
        return cot;
    }

    public void setCot(int cot) {
        this.cot = cot;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    @Override
    public String toString() {
        return "GheNgoi{" +
                "maGhe='" + maGhe + '\'' +
                ", hang='" + hang + '\'' +
                ", cot=" + cot +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
