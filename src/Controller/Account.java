package Controller;

import DBConnection.DBConnection;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Account implements Initializable {
    @FXML
    private Label labelAddress;

    @FXML
    private Label labelDob;

    @FXML
    private Label labelEmail;

    @FXML
    private Label labelGender;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPhone;

    @FXML
    private Label labelUsername;

    public void setInfor(String username) {
        DBConnection db = new DBConnection();
        Connection cn = db.getConnection();
        String query = "SELECT * FROM [User] WHERE username = '" + username + "'";
        Statement st;
        ResultSet rs;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                labelUsername.setText(rs.getString("username"));
                labelName.setText(rs.getString("tenNV"));
                labelDob.setText(String.valueOf(rs.getDate("ngaySinh")));
                labelGender.setText(rs.getString("gioiTinh"));
                labelPhone.setText(rs.getString("soDT"));
                labelEmail.setText(rs.getString("email"));
                labelAddress.setText(rs.getString("diaChi"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MenuBarBorderPane menuBarBorderPane = new MenuBarBorderPane();
//        setInfor(menuBarBorderPane.displayUsername());
//        setInfor(menuBarBorderPane.getUsername());
//        System.out.println(menuBarBorderPane.getUsername());
    }
}
