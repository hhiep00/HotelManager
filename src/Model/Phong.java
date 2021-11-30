package Model;

public class Phong {
    private String maPhong;
    private String loaiPhong;
    private String trangThai;
    private int gia;

    public Phong() {
    }

    public Phong(String maPhong, String loaiPhong, String trangThai, int gia) {
        this.maPhong = maPhong;
        this.loaiPhong = loaiPhong;
        this.trangThai = trangThai;
        this.gia = gia;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public int getGia() {
        return gia;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }
}
