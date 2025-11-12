package iuh.fit.se.nhom10.model;

import java.sql.Date;
import java.sql.Time;

public class LichChieu {
    private String maLich;
    private Date ngayChieu;
    private Time gioBatDau;
    private Time gioKetThuc;
    private String maPhim;
    private String maPhong;

    public LichChieu() {}

    public LichChieu(String maLich, Date ngayChieu, Time gioBatDau, Time gioKetThuc, String maPhim, String maPhong) {
        this.maLich = maLich;
        this.ngayChieu = ngayChieu;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.maPhim = maPhim;
        this.maPhong = maPhong;
    }

    public String getMaLich() { return maLich; }
    public void setMaLich(String maLich) { this.maLich = maLich; }

    public Date getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(Date ngayChieu) { this.ngayChieu = ngayChieu; }

    public Time getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(Time gioBatDau) { this.gioBatDau = gioBatDau; }

    public Time getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(Time gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public String getMaPhim() { return maPhim; }
    public void setMaPhim(String maPhim) { this.maPhim = maPhim; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    @Override
    public String toString() {
        return "LichChieu{" +
                "maLich='" + maLich + '\'' +
                ", ngayChieu=" + ngayChieu +
                ", maPhim='" + maPhim + '\'' +
                '}';
    }
}
