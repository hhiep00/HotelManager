package Controller;

import DBConnection.*;
import Model.Phong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
                p1.setMaPhong(rs.getInt("maPhong"));
                p1.setLoaiPhong(rs.getString("loaiPhong"));
                p1.setTrangThai(rs.getString("trangThai"));
                p1.setGia(rs.getInt("gia"));
                p.add(p1);
            }
        }
        return p;
    }

    // Thao tác với nút thêm
    public void insert(ActionEvent e) throws SQLException {
        Phong p = new Phong();
        int t = 0;
        if (phongList.size() != 0) {
            Phong p1 = phongList.get(phongList.size() - 1);
            String s = p1.getMaPhong();
            String s1 = "" + s.charAt(1) + s.charAt(2);
            t = Integer.parseInt(s1);
        }
        p.setMaPhong(t + 1);
        p.setLoaiPhong(typeText.getText());
        p.setTrangThai(statusText.getText());
        p.setGia(Integer.parseInt(priceText.getText()));
        phongList.add(p);
        try {
            insertDB(p);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Thêm dữ liệu vào DB
    public boolean insertDB(Phong p) throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "insert dbo.Phong (loaiphong,trangThai,gia) values (?,?,?)";
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
//          pstm.setString(1, p.getMaPhong());
            pstm.setString(1, p.getLoaiPhong());
            String s = p.getTrangThai();
            int th;
            if (s.equals("Trống")) th = 1;
            else th = 0;
            pstm.setInt(2, th);
            pstm.setInt(3, p.getGia());
            return pstm.executeUpdate() > 0;
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
            String s1 = "" + s.charAt(1) + s.charAt(2);
            int t = Integer.parseInt(s1);
            pstm.setInt(1, t);
            return pstm.executeUpdate() > 0;
        }
    }

    //Thao tác sửa dữ liệu
    // Thao tác nút sửa
    public void update(ActionEvent e) {
        Phong p = new Phong();
        p.setMaPhong(Integer.parseInt(idText.getText()));
        p.setLoaiPhong(typeText.getText());
        p.setTrangThai(statusText.getText());
        p.setGia(Integer.parseInt(priceText.getText()));
        try {
            updateDB(p);
            showInSearchBar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Cập nhật trong DB
    public boolean updateDB(Phong p) throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "update dbo.Phong set loaiPhong=?, trangThai=?, gia=? where maPhong=?";
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
            pstm.setString(1, p.getLoaiPhong());
            String s = p.getTrangThai();
            int t;
            if (s.equals("Trống")) t = 1;
            else t = 0;
            pstm.setInt(2, t);
            pstm.setInt(3, p.getGia());
            String s1 = p.getMaPhong();
            String s2 = "" + s1.charAt(1) + s1.charAt(2);
            int t1 = Integer.parseInt(s2);
            pstm.setInt(4, t1);
            return pstm.executeUpdate() > 0;
        }
    }

    // Thao tác khi nhấn chọn một dòng trong bảng
    public void handleMouseAction(MouseEvent mouseEvent) {
        Phong p = table.getSelectionModel().getSelectedItem();
        if (mouseEvent.getClickCount() == 1 && p != null) {
            String s1 = p.getMaPhong();
            String s2 = "" + s1.charAt(1) + s1.charAt(2);
            idText.setText(s2);
            typeText.setText(p.getLoaiPhong());
            String s = p.getTrangThai();
            int t;
            if (s.equals("Trống")) t = 1;
            else t = 0;
            statusText.setText(String.format("%d", t));
            priceText.setText(String.format("%d", p.getGia()));
        }
    }

    // Tìm Kiếm trong DB
    public ArrayList<Phong> seach(String s) throws Exception {
        DBConnection dbc = new DBConnection();
        String sql = "";
        if (isNumber(s) == true) {
            int t1 = Integer.parseInt(s);
            sql = sql + "select * from dbo.Phong as p where p.maPhong like " + String.format("%d", t1) +
                    "union select * from dbo.Phong as p where p.gia like " + String.format("%d", t1);
        } else {
            sql = sql + "select * from dbo.Phong as p where p.loaiPhong like '" + s + "%'";
        }
        ArrayList<Phong> p = new ArrayList<>();
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Phong p1 = new Phong();
                p1.setMaPhong(rs.getInt("maPhong"));
                p1.setLoaiPhong(rs.getString("loaiPhong"));
                p1.setTrangThai(rs.getString("trangThai"));
                p1.setGia(rs.getInt("gia"));
                p.add(p1);
            }
        }
        return p;
    }

    //Kiểm tra có phải số hay không.
    public static boolean isNumber(String s) {
        try {
            int t = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
                Logger.getLogger(ManageRoom.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Thao tác với nút tạo mới
    public void refresh(ActionEvent e) {
        typeText.setText(null);
        statusText.setText(null);
        priceText.setText(null);
        seachText.clear();
        addAll();
    }
}
