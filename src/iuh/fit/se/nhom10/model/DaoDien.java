package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho đạo diễn
 */
public class DaoDien {
    private String maDD;
    private String tenDD;
    private String quocTich;

    // Constructor không tham số
    public DaoDien() {
    }

    // Constructor đầy đủ
    public DaoDien(String maDD, String tenDD, String quocTich) {
        this.maDD = maDD;
        this.tenDD = tenDD;
        this.quocTich = quocTich;
    }

    // Getters and Setters
    public String getMaDD() {
        return maDD;
    }

    public void setMaDD(String maDD) {
        this.maDD = maDD;
    }

    public String getTenDD() {
        return tenDD;
    }

    public void setTenDD(String tenDD) {
        this.tenDD = tenDD;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    @Override
    public String toString() {
        return "DaoDien{" +
                "maDD='" + maDD + '\'' +
                ", tenDD='" + tenDD + '\'' +
                ", quocTich='" + quocTich + '\'' +
                '}';
    }
}
