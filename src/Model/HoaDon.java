package Model;

public class HoaDon {
    private int maHoaDon;
    private int maThue;
    private float tongTien;

    public HoaDon(int maHoaDon, int maThue, float tongTien) {
        this.maHoaDon = maHoaDon;
        this.maThue = maThue;
        this.tongTien = tongTien;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public int getMaThue() {
        return maThue;
    }

    public float getTongTien() {
        return tongTien;
    }
}
