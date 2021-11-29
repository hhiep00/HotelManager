package Controller;

import DBConnection.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class CheckOut implements Initializable {
    @FXML
    private ComboBox<String> cbMaThue;

    @FXML
    private TextField tfMaHoaDon;

    @FXML
    private TextField tfMaKhachHang;

    @FXML
    private TextField tfMaPhong;

    @FXML
    private Label lbNgayDen;

    @FXML
    private Label lbNgayDi;

    @FXML
    private Label lbSoNgay;

    @FXML
    private Label lbNgaySinh;

    @FXML
    private Label lbQueQuan;

    @FXML
    private Label lbQuocTich;

    @FXML
    private Label lbSoCMND;

    @FXML
    private Label lbSoDT;

    @FXML
    private Label lbTenKH;

    @FXML
    private Label lbGioiTinh;

    @FXML
    private Label lbLoaiPhong;

    @FXML
    private Label lbTienPhong;

    @FXML
    private Label lbTongTien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfMaHoaDon.setText(getMHD());
        cbMaThue.setVisibleRowCount(2);
        cbMaThue.setItems(getMaThue());
    }

    @FXML
    void handleActionCb(ActionEvent event) throws ParseException {
        String maThue = cbMaThue.getSelectionModel().getSelectedItem();
        String maPhong = getMP(maThue);
        String maKH = getMKH(maThue);
        String ngayDen = ngayDen(maThue);
        String ngayDi = getDateNow();
        tfMaHoaDon.setText(getMHD());
        tfMaPhong.setText(maPhong);
        tfMaKhachHang.setText(maKH);
        lbNgayDen.setText(ngayDen);
        lbNgayDi.setText(ngayDi);
        lbSoNgay.setText(String.valueOf(soNgay(ngayDen, ngayDi)));
        lbTenKH.setText(ten(maKH));
        lbGioiTinh.setText(gioiTinh(maKH));
        lbSoCMND.setText(soCMND(maKH));
        lbNgaySinh.setText(ngaySinh(maKH));
        lbQueQuan.setText(queQuan(maKH));
        lbQuocTich.setText(quocTich(maKH));
        lbSoDT.setText(soDT(maKH));
        lbLoaiPhong.setText(loaiPhong(Integer.parseInt(maPhong.substring(1))));
        lbTienPhong.setText(formatVND(tienPhong(Integer.parseInt(maPhong.substring(1)))));
        lbTongTien.setText(formatVND(tongTien(Integer.parseInt(tienPhong(Integer.parseInt(maPhong.substring(1)))), soNgay(ngayDen, ngayDi))));
    }

    private String getDateNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return sdf.format(date);
    }

    private ObservableList<String> getMaThue() {
        ObservableList<String> listMaThue = FXCollections.observableArrayList();
        DBConnection dbc = new DBConnection();
        Connection cn = dbc.getConnection();
        String query = "SELECT maThue FROM ThuePhong WHERE thanhToan = 0";
        Statement st;
        ResultSet rs;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listMaThue.add(rs.getString("maThue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMaThue;
    }

    private String getMHD() {
        DBConnection dbc = new DBConnection();
        Connection cn = dbc.getConnection();
        String query = "SELECT MAX(maHoaDon) FROM HoaDon";
        Statement st;
        ResultSet rs;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                return "HD" + String.format("%02d", rs.getInt(1) + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getMP(String ma) {
        String query = "SELECT maPhong FROM ThuePhong WHERE maThue = " + ma;
        return "P" + String.format("%02d", Integer.parseInt(DAO(query, "maPhong")));
    }

    private String getMKH(String ma) {
        String query = "SELECT maKH FROM ThuePhong WHERE maThue = " + ma;
        return DAO(query, "maKH");
    }

    private String ngayDen(String ma) {
        String query = "SELECT ngayDen FROM ThuePhong WHERE maThue = " + ma;
        return DAO(query, "ngayDen");
    }

//    private String ngayDi(String ma){
//        String query = "SELECT ngayDi FROM ThuePhong WHERE maThue = " + ma;
//        return DAO(query, "ngayDi");
//    }

    private String ten(String ma) {
        String query = "SELECT ten FROM KhachHang WHERE maKH = '" + ma + "'";
        return DAO(query, "ten");
    }

    private String gioiTinh(String ma) {
        String query = "SELECT gioiTinh FROM KhachHang WHERE maKH = '" + ma + "'";
        return DAO(query, "gioiTinh");
    }

    private String soCMND(String ma) {
        String query = "SELECT soCMND FROM KhachHang WHERE maKH = '" + ma + "'";
        return DAO(query, "soCMND");
    }

    private String ngaySinh(String ma) {
        String query = "SELECT ngaySinh FROM KhachHang WHERE maKH = '" + ma + "'";
        return DAO(query, "ngaySinh");
    }

    private String queQuan(String ma) {
        String query = "SELECT queQuan FROM KhachHang WHERE maKH = '" + ma + "'";
        return DAO(query, "queQuan");
    }

    private String quocTich(String ma) {
        String query = "SELECT quocTich FROM KhachHang WHERE maKH = '" + ma + "'";
        return DAO(query, "quocTich");
    }

    private String soDT(String ma) {
        String query = "SELECT soDT FROM KhachHang WHERE maKH = '" + ma + "'";
        return DAO(query, "soDT");
    }

    private String loaiPhong(int ma) {
        String query = "SELECT loaiPhong FROM Phong WHERE maPhong = '" + ma + "'";
        return DAO(query, "loaiPhong");
    }

    private String tienPhong(int ma) {
        String query = "SELECT gia FROM Phong WHERE maPhong = '" + ma + "'";
        return DAO(query, "gia");
    }

    private String DAO(String query, String columnLabel) {
        DBConnection dbc = new DBConnection();
        Connection cn = dbc.getConnection();
        Statement st;
        ResultSet rs;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                return rs.getString(columnLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private int soNgay(String ngayDen, String ngayDi) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(ngayDen);
        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(ngayDi);
        long diff = date2.getTime() - date1.getTime();
        if ((diff / (1000 * 60 * 60 * 24)) == 0) {
            return 1;
        }
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    private String tongTien(int donGia, int soNgay) {
        return String.valueOf(donGia * soNgay);
    }

    private String formatVND(String tien) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return currencyVN.format(Integer.parseInt(tien));
    }

    @FXML
    void handelBtThanhToan(ActionEvent event) throws ParseException {
        String maThue = cbMaThue.getSelectionModel().getSelectedItem();
        if (maThue != null) {
            String query = "INSERT INTO HotelManager.dbo.HoaDon VALUES (" + maThue + "," + tongTien(Integer.parseInt(tienPhong(Integer.parseInt(getMP(maThue).substring(1)))), soNgay(ngayDen(maThue), getDateNow())) + ", GETDATE())";
            ObservableList<String> listMTHD = getMTHoaDon();
            DBConnection dbc = new DBConnection();
            Connection cn = dbc.getConnection();
            Statement st;
            try {
                if (!listMTHD.contains(maThue)) {
                    st = cn.createStatement();
                    st.executeUpdate(query);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    updateNgayDi();
                    updateTTP();
                    alert.setHeaderText("Thông báo");
                    alert.setContentText("Xác nhận thành toán");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    updateNgayDi();
                    alert.setHeaderText("Chú ý");
                    alert.setContentText("Mã thuê đã thanh toán");
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Lỗi");
                alert.setContentText("Vui lòng thử lại");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Cảnh báo");
            alert.setContentText("Vui lòng chọn mã thuê");
            alert.show();
        }

    }

    private ObservableList<String> getMTHoaDon() {
        ObservableList<String> listMTHoaDon = FXCollections.observableArrayList();
        DBConnection dbc = new DBConnection();
        Connection cn = dbc.getConnection();
        String query = "SELECT maThue FROM HoaDon";
        Statement st;
        ResultSet rs;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listMTHoaDon.add(rs.getString("maThue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMTHoaDon;
    }

    private void updateNgayDi() {
        String query = "UPDATE ThuePhong SET ngayDi = GETDATE(), thanhToan = 1 WHERE maThue = " + cbMaThue.getSelectionModel().getSelectedItem();
        DBConnection dbc = new DBConnection();
        Connection cn = dbc.getConnection();
        Statement st;
        try {
            st = cn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTTP() {
        String query = "UPDATE Phong SET trangThai = 1 WHERE maPhong = " + Integer.parseInt(tfMaPhong.getText().substring(1));
        DBConnection dbc = new DBConnection();
        Connection cn = dbc.getConnection();
        Statement st;
        try {
            st = cn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
