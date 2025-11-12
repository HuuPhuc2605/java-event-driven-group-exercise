package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho loại phòng chiếu
 */
public class LoaiPhong {
    private int maLoaiPhong;
    private String tenLoaiPhong;

    // Constructor không tham số
    public LoaiPhong() {
    }

    // Constructor đầy đủ
    public LoaiPhong(int maLoaiPhong, String tenLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
    }

    // Constructor chỉ tên (khi thêm mới)
    public LoaiPhong(String tenLoaiPhong) {
        this.tenLoaiPhong = tenLoaiPhong;
    }

    // Getters and Setters
    public int getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(int maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public String getTenLoaiPhong() {
        return tenLoaiPhong;
    }

    public void setTenLoaiPhong(String tenLoaiPhong) {
        this.tenLoaiPhong = tenLoaiPhong;
    }

    @Override
    public String toString() {
        return tenLoaiPhong;
    }
}
