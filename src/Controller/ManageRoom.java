package Controller;

import DBConnection.*;
import Model.Phong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ManageRoom implements Initializable {

    private ObservableList<Phong> phongList;

    @FXML
    private TableView<Phong> table;

    @FXML
    private TableColumn<Phong, String> idColumn;

    @FXML
    private TableColumn<Phong, String> typeColumn;

    @FXML
    private TableColumn<Phong, String> statusColumn;

    @FXML
    private TableColumn<Phong, Integer> priceColumn;

    @FXML
    private TextField idText;

    @FXML
    private TextField typeText;

    @FXML
    private TextField statusText;

    @FXML
    private TextField priceText;

    @FXML
    private TextField seachText;

    @FXML
    private RadioButton roomAvailable;

    @FXML
    private RadioButton roomNotAvailable;

    @FXML
    private ToggleGroup trangThaiPhong;
    @FXML
    private Label error_idRoom;

    @FXML
    private Label error_price;

    @FXML
    private Label error_tinhTrang;

    @FXML
    private Label error_type;
    private boolean checkErrorText(){
        boolean isIdRoomNotEmpty=validation.Validation.isTextFieldNotEmpty(idText,error_idRoom,"Vui lòng nhập mã phòng.");
        boolean isTypeNotEmpty=validation.Validation.isTextFieldNotEmpty(typeText,error_type,"Vui lòng nhập loại phòng.");
        boolean isTinhTrangNotEmpty=validation.Validation.isGenderNotEmpty(roomAvailable,roomNotAvailable,error_tinhTrang,"Vui lòng chọn trạng thái.");
        boolean isPriceNotEmpty=validation.Validation.isTextFieldNotEmpty(priceText,error_price,"Vui lòng nhập giá tiền.");
        boolean isPriceisNumber=false;
        if(isPriceNotEmpty){
            isPriceisNumber=validation.Validation.isNumber(priceText,error_price,"Giá tiền phải là 1 số");
        }
        if(isIdRoomNotEmpty && isPriceNotEmpty && isTypeNotEmpty && isTinhTrangNotEmpty && isPriceisNumber){
            return true;
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        phongList = FXCollections.observableArrayList();
        setCellTable();
        addAll();
        keyReased();
    }

    private void setCellTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Phong, String>("maPhong"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Phong, String>("loaiPhong"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Phong, String>("trangThai"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Phong, Integer>("gia"));
    }

    private void addAll() {
        phongList.clear();
        try {
            ArrayList<Phong> p1 = fillAll();
            for (Phong i : p1) {
                phongList.add(i);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        table.setItems(phongList);
    }

    // Lấy toàn bộ danh sách từ trong db
    public ArrayList<Phong> fillAll() throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "select * from dbo.Phong";
        ArrayList<Phong> p = new ArrayList<>();
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Phong p1 = new Phong();
                p1.setMaPhong(rs.getString("maPhong"));
                p1.setLoaiPhong(rs.getString("loaiPhong"));
                if(rs.getBoolean("trangThai")) p1.setTrangThai("Trống");
                else p1.setTrangThai("Đã thuê");
                p1.setGia(rs.getInt("gia"));
                p.add(p1);
            }
        }
        return p;
    }

    // Thao tác với nút thêm
    public void insert(ActionEvent e)  {
        if(checkErrorText()){
            Phong p = new Phong();
            int t = 0;
            if (phongList.size() != 0) {
                Phong p1 = phongList.get(phongList.size() - 1);
                String s = p1.getMaPhong();
                String s1 = "" + s.charAt(1) + s.charAt(2);
                t = Integer.parseInt(s1);
            }
            p.setMaPhong(idText.getText());
            p.setLoaiPhong(typeText.getText());
            if(roomAvailable.isSelected()) p.setTrangThai("Trống");
            else p.setTrangThai("Đã thuê");
            p.setGia(Integer.parseInt(priceText.getText()));
            phongList.add(p);
            try {
                insertDB(p);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Thêm dữ liệu vào DB
    public boolean insertDB(Phong p) throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "insert dbo.Phong (maPhong,loaiphong,trangThai,gia) values (?,?,?,?)";
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
          pstm.setString(1, p.getMaPhong());
            pstm.setString(2, p.getLoaiPhong());
            String s = p.getTrangThai();
            int th;
            if (s.equals("Trống")) th = 1;
            else th = 0;
            pstm.setInt(3, th);
            pstm.setInt(4, p.getGia());
            boolean check=false;
            try{
                pstm.executeUpdate();
                check=true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Thông báo:");
                alert.setContentText("Thành công.");
                alert.show();
            }catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Thông báo:");
                alert.setContentText("Không thành công\nMã phòng đã tồn tại.");
                alert.show();
            }
            return check;
        }
    }

    // Xóa dữ liệu
    // thao tác với nút xóa
    public void delete(ActionEvent e) {

        Phong selected = table.getSelectionModel().getSelectedItem();
        phongList.remove(selected);
        try {
            deleteDB(selected);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Xóa trong db
    public boolean deleteDB(Phong o) throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "delete dbo.Phong where MaPhong=?";
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
            String s = o.getMaPhong();
            pstm.setString(1, s);
            return pstm.executeUpdate() > 0;
        }
    }

    //Thao tác sửa dữ liệu
    // Thao tác nút sửa
    public void update(ActionEvent e) {
        if(checkErrorText()){
            Phong p = new Phong();
            p.setMaPhong(idText.getText());
            p.setLoaiPhong(typeText.getText());
            if(roomAvailable.isSelected()) p.setTrangThai("Trống");
            else p.setTrangThai("Đã thuê");
            p.setGia(Integer.parseInt(priceText.getText()));
            try {
                updateDB(p);
                addAll();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Cập nhật trong DB
    public boolean updateDB(Phong p) throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "update dbo.Phong set loaiPhong=?, trangThai=?, gia=? where maPhong=?";
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
//            pstm.setString(1, p.getLoaiPhong());
//            String s = p.getTrangThai();
//            int t;
//            if (s.equals("Trống")) t = 1;
//            else t = 0;
//            pstm.setInt(2, t);
//            pstm.setInt(3, p.getGia());
//            String s1 = p.getMaPhong();
//            pstm.setString(4, s1);
//            return pstm.executeUpdate() > 0;
            pstm.setString(1, typeText.getText());
            int t;
            if (roomAvailable.isSelected()) t = 1;
            else t = 0;
            pstm.setInt(2, t);
            pstm.setInt(3, Integer.parseInt(priceText.getText()));
            String s1 = p.getMaPhong();
            pstm.setString(4, idText.getText());
            boolean check=false;
            try{
                pstm.executeUpdate();
                check=true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Thông báo:");
                alert.setContentText("Update thành công.");
                alert.show();
            }catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Thông báo:");
                alert.setContentText("Update không thành công.");
                alert.show();
            }
            return check;
        }
    }

    // Thao tác khi nhấn chọn một dòng trong bảng
    public void handleMouseAction(MouseEvent mouseEvent) {
        Phong p = table.getSelectionModel().getSelectedItem();
        if (mouseEvent.getClickCount() == 1 && p != null) {
            String s1 = p.getMaPhong();
            idText.setText(s1);
            typeText.setText(p.getLoaiPhong());
            String s = p.getTrangThai();
            if (s.equals("Trống")) roomAvailable.setSelected(true);
            else roomNotAvailable.setSelected(true);
            priceText.setText(String.format("%d", p.getGia()));
        }
    }

    // Tìm Kiếm trong DB
    public ArrayList<Phong> seach(String s) throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "";
            sql = sql + "select * from dbo.Phong as p where p.maPhong like '" + s + "%'"
                    + "select * from dbo.Phong as p where p.loaiPhong like '" + s + "%'";

        ArrayList<Phong> p = new ArrayList<>();
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Phong p1 = new Phong();
                p1.setMaPhong(rs.getString("maPhong"));
                p1.setLoaiPhong(rs.getString("loaiPhong"));
                p1.setTrangThai(rs.getString("trangThai"));
                p1.setGia(rs.getInt("gia"));
                p.add(p1);
            }
        }
        return p;
    }



    // Thao tác với thanh tìm kiếm
    public void keyReased() {
        seachText.setOnKeyReleased(e -> {
            showInSearchBar();
        });
    }

    //Hirnt thị thanh tìm kiếm
    public void showInSearchBar() {
        phongList.clear();
        if (seachText.getText().compareTo("") == 0) {
            addAll();
        } else {
            phongList.clear();
            try {
                phongList = FXCollections.observableArrayList();
                try {
                    ArrayList<Phong> p1 = seach(seachText.getText());
                    for (Phong i : p1) {
                        phongList.add(i);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                table.setItems(phongList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Thao tác với nút tạo mới
    public void refresh(ActionEvent e) {
        typeText.setText("");
        priceText.setText("");
        seachText.clear();
        idText.setText("");
        roomNotAvailable.setSelected(false);
        roomAvailable.setSelected(false);
        error_idRoom.setText(null);
        error_price.setText(null);
        error_type.setText(null);
        error_tinhTrang.setText(null);
        addAll();
    }
}
