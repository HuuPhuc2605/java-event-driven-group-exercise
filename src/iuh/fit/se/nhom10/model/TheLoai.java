package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho thể loại phim
 */
public class TheLoai {
    private int maTheLoai;
    private String tenTheLoai;

    // Constructor không tham số
    public TheLoai() {
    }

    // Constructor đầy đủ
    public TheLoai(int maTheLoai, String tenTheLoai) {
        this.maTheLoai = maTheLoai;
        this.tenTheLoai = tenTheLoai;
    }

    // Getters and Setters
    public int getMaTheLoai() {
        return maTheLoai;
    }

    public void setMaTheLoai(int maTheLoai) {
        this.maTheLoai = maTheLoai;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    @Override
    public String toString() {
        return "TheLoai{" +
                "maTheLoai=" + maTheLoai +
                ", tenTheLoai='" + tenTheLoai + '\'' +
                '}';
    }
}
