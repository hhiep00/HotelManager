package Model;

import java.text.ParseException;
import java.util.Date;

public class ThuePhong {
    private String maThue;
    private String maKH;
    private String maPhong;
    private Date ngayDen;
    private Date ngayDi;
    private String thanhToan;

    public ThuePhong() {
    }

    public ThuePhong(String maThue, int maKH, int maPhong, Date ngayDen, Date ngayDi, String thanhToan) throws ParseException {
        this.maThue = maThue;
        this.maKH = "KH" + String.format("%02d", maKH);
        this.maPhong = "P" + String.format("%02d", maPhong);
        this.ngayDen = ngayDen;
        this.ngayDi = ngayDi;
        this.thanhToan = thanhToan;
    }

    public String getMaThue() {
        return maThue;
    }

    public String getMaKH() {
        return maKH;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public Date getNgayDen() {
        return ngayDen;
    }

    public Date getNgayDi() {
        return ngayDi;
    }

    public String getThanhToan() {
        if(thanhToan.contains("0")){
            return "Chưa thanh toán";
        } else {
            return "Đã thanh toán";
        }
    }
}
