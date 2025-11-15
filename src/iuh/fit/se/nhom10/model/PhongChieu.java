package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho phòng chiếu
 */
public class PhongChieu {
    private String maPhong;
    private String tenPhong;
    private int maLoaiPhong;

    // Constructor không tham số
    public PhongChieu() {
    }

    // Constructor đầy đủ
    public PhongChieu(String maPhong, String tenPhong, int maLoaiPhong) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.maLoaiPhong = maLoaiPhong;
    }

    // Getters and Setters
    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(int maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    @Override
    public String toString() {
        return "PhongChieu{" +
                "maPhong='" + maPhong + '\'' +
                ", tenPhong='" + tenPhong + '\'' +
                ", maLoaiPhong=" + maLoaiPhong +
                '}';
    }
}
