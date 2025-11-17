package iuh.fit.se.nhom10.model;

/**
 * Model đại diện cho phim
 */
public class Phim {
    private String maPhim;
    private String tenPhim;
    private int maTheLoai;
    private int thoiLuong;
    private String maDD;

    // Constructor không tham số
    public Phim() {
    }

    // Constructor đầy đủ
    public Phim(String maPhim, String tenPhim, int maTheLoai, int thoiLuong, String maDD) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.maTheLoai = maTheLoai;
        this.thoiLuong = thoiLuong;
        this.maDD = maDD;
    }

    // Getters and Setters
    public String getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(String maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public int getMaTheLoai() {
        return maTheLoai;
    }

    public void setMaTheLoai(int maTheLoai) {
        this.maTheLoai = maTheLoai;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getMaDD() {
        return maDD;
    }

    public void setMaDD(String maDD) {
        this.maDD = maDD;
    }

    @Override
    public String toString() {
        return "Phim{" +
                "maPhim='" + maPhim + '\'' +
                ", tenPhim='" + tenPhim + '\'' +
                ", maTheLoai=" + maTheLoai +
                ", thoiLuong=" + thoiLuong +
                ", maDD='" + maDD + '\'' +
                '}';
    }

	public Object getNamPhatHanh() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getTheLoai() {
		// TODO Auto-generated method stub
		return null;
	}
}
