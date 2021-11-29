package Controller;

import DBConnection.DBConnection;
import Model.User;
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
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ManageEmployee implements Initializable {

    @FXML
    private TableColumn<User, String> addressColumn;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSua;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnXoa;

    @FXML
    private TextField tfLuong;

    @FXML
    private TableColumn<User, Date> dobColumn;

    @FXML
    private DatePicker dpNgaySinh;

    @FXML
    private TableColumn<User, String> gioiTinhColumn;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> luongColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TextField tfPassword;

    @FXML
    private TableColumn<User, String> phoneNumColumn;

    @FXML
    private RadioButton rbNam;

    @FXML
    private RadioButton rbNu;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfPNumber;

    @FXML
    private TextField tfSearch;

    @FXML
    private TextField tfTenNV;

    @FXML
    private TextField tfUsername;

    @FXML
    private TextField tfmaNV;

    @FXML
    private TextField tfIdCard;

    @FXML
    private TableView<User> tvEmployeeTable;


    @FXML
    private TableColumn<User, String> idCardColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private ToggleGroup gender;
    @FXML
    private Label error_address;

    @FXML
    private Label error_dob;

    @FXML
    private Label error_email;

    @FXML
    private Label error_gender;

    @FXML
    private Label error_id;

    @FXML
    private Label error_idCard;

    @FXML
    private Label error_name;

    @FXML
    private Label error_password;

    @FXML
    private Label error_phoneN;

    @FXML
    private Label error_salary;

    @FXML
    private Label error_username;

    @FXML
    void handleButtonAction(ActionEvent event) throws ParseException {
        if (event.getSource() == btnThem) {
            if(checkErrorText()){
                try{
                    insertUser();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Thêm thành công.");
                    alert.show();
                }catch (Exception ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Nhân viên đã tồn tại");
                    alert.show();
                }
            }
        } else if (event.getSource() == btnSua) {
            if(checkErrorText()){
                try{
                    updateUser();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Sửa thành công.");
                    alert.show();
                }catch (Exception ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Nhân viên đã tồn tại");
                    alert.show();
                }
            }
        } else if (event.getSource() == btnXoa) {
            deleteUser();

        } else if (event.getSource() == btnReset) {

            lamMoi();
            resetLabel();
        }
    }
    private void resetLabel(){
        error_name.setText(null);
        error_dob.setText(null);
        error_gender.setText(null);
        error_id.setText(null);
        error_salary.setText(null);
        error_username.setText(null);
        error_password.setText(null);
        error_email.setText(null);
        error_address.setText(null);
        error_phoneN.setText(null);
        error_idCard.setText(null);
    }
    public boolean checkErrorText(){
        boolean isIDEmpty=validation.Validation.isTextFieldNotEmpty(tfmaNV,error_id,"vui lòng nhập ID");
        boolean isNameEmpty=validation.Validation.isTextFieldNotEmpty(tfTenNV,error_name,"Vui lòng nhập tên.");
        boolean isDateEmpty=validation.Validation.isDatePickerNotEmpty(dpNgaySinh,error_dob,"Vui lòng chọn ngày.");
        boolean isGenderEmpty=validation.Validation.isGenderNotEmpty(rbNam,rbNu,error_gender,"vui lòng chọn giới tính");
        boolean isUsernameEmpty=validation.Validation.isTextFieldNotEmpty(tfUsername,error_username,"Vui lòng nhập username.");
        boolean isPasswordEmpty=validation.Validation.isTextFieldNotEmpty(tfPassword,error_password,"Vui lòng nhập password.");
        boolean isSalaryEmpty=validation.Validation.isTextFieldNotEmpty(tfLuong,error_salary,"Vui lòng nhập lương.");
        boolean isPhoneNumberEmpty=validation.Validation.isTextFieldNotEmpty(tfPNumber,error_phoneN,"Vui lòng nhập số điện thoại.");
        boolean isEmailEmpty=validation.Validation.isTextFieldNotEmpty(tfEmail,error_email,"Vui lòng nhập email.");
        boolean isAddressEmpty=validation.Validation.isTextFieldNotEmpty(tfAddress,error_address,"Vui lòng nhập quê quán.");
        boolean isIdCardEmpty=validation.Validation.isTextFieldNotEmpty(tfAddress,error_address,"Vui lòng nhập số CMND.");
        boolean isSalaryIsNumber=false;
        boolean isPNIsNumber=false;
        if(isSalaryEmpty){
            isSalaryIsNumber=validation.Validation.isNumber(tfLuong,error_salary,"Lương phải là 1 số.");
        }
        if(isPhoneNumberEmpty){
            isPNIsNumber=validation.Validation.isNumber(tfPNumber,error_phoneN,"Số đt phải là 1 số.");
        }
        if(isAddressEmpty && isEmailEmpty && isNameEmpty && isUsernameEmpty && isPasswordEmpty && isSalaryEmpty  && isPhoneNumberEmpty
                && isDateEmpty && isGenderEmpty && isSalaryIsNumber && isPNIsNumber && isIDEmpty && isIdCardEmpty){

            return true;
        }
        return false;
    }

    private ObservableList<User> userList;

    public ObservableList<User> getUsersList() {
        userList = FXCollections.observableArrayList();
        DBConnection dbc = new DBConnection();
        Connection conn = dbc.getConnection();
        String query = "SELECT * FROM [User]";
        Statement st;
        ResultSet rs;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            User user;
            while (rs.next()) {
                user = new User(rs.getString("id"), rs.getString("username"), rs.getString("password")
                        , rs.getString("tenNV"), rs.getDate("ngaySinh"), rs.getString("gioiTinh")
                        , rs.getString("soDT"), rs.getString("email")
                        , rs.getString("soCMND"), rs.getString("queQuan")
                        , rs.getInt("luong"));
                userList.add(user);


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList;
    }

    public void showUser() {
        ObservableList<User> list = getUsersList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        gioiTinhColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("queQuan"));
        phoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        luongColumn.setCellValueFactory(new PropertyValueFactory<>("luong"));
        idCardColumn.setCellValueFactory(new PropertyValueFactory<>("soCMND"));
        tvEmployeeTable.setItems(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showUser();
    }

    private void insertUser() throws ParseException {
        LocalDate date = dpNgaySinh.getValue();
        String s = date.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(s);
        String d2 = new SimpleDateFormat("yyyyMMdd").format(d1);

        String gioitinh = "";
        if (rbNam.isSelected()) gioitinh = "Nam";
        else gioitinh = "Nữ";
        String query = "INSERT INTO [User] VALUES ( N'"+ tfmaNV.getText() +"', N'" + tfUsername.getText() + "', N'"
                + tfPassword.getText() + "' , N'" + tfTenNV.getText() + "', N'" + d2 + "', N'" + gioitinh
                + "', N'"  + tfPNumber.getText() + "', N'" + tfEmail.getText() + "', N'" + tfIdCard.getText() + "', N'" + tfAddress.getText() + "', " + tfLuong.getText()+ ",'False'" + ")";

        try{
            executeQuery(query);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Thêm thành công.");
            alert.show();
        }catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nhân viên đã tồn tại");
            alert.show();
        }
//        executeQuery(query);
        showUser();
    }

    void lamMoi() {
        tfmaNV.setText("");
        tfTenNV.setText("");
        dpNgaySinh.setValue(null);
        tfUsername.setText("");
        tfPassword.setText("");
        rbNu.setSelected(false);
        rbNam.setSelected(false);
        tfLuong.setText("");
        tfEmail.setText("");
        tfPNumber.setText("");
        tfAddress.setText("");
        tfIdCard.setText("");
    }

    private void updateUser() throws ParseException {
        LocalDate date1 = dpNgaySinh.getValue();
        String s = date1.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(s);
        String d2 = new SimpleDateFormat("yyyyMMdd").format(d1);

        String gioitinh = "";
        if (rbNam.isSelected()) gioitinh = "Nam";
        else gioitinh = "Nữ";


        String query = "UPDATE [User] SET  id = N'"+ tfmaNV.getText() + "', username = N'" + tfUsername.getText() + "', password = N'"
                + tfPassword.getText() + "', tenNV = N'" + tfTenNV.getText() + "', ngaySinh = N'" + d2 + "', gioiTinh = N'" + gioitinh  +
                "', soDT = N'" + tfPNumber.getText() + "', email = N'" + tfEmail.getText() + "', soCMND = N'" + tfIdCard.getText() + "', queQuan = N'" + tfAddress.getText() + "', luong=" + tfLuong.getText()
                + " where id =N'" + tfmaNV.getText() + "'";
        executeQuery(query);
        showUser();
    }

    private void deleteUser() {

        String query = "DELETE FROM [User] WHERE id ='" + tfmaNV.getText() + "'";
        executeQuery(query);
        showUser();
    }

    private void executeQuery(String query) {
        DBConnection dbc = new DBConnection();
        Connection conn = dbc.getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleMouseAction(MouseEvent event) {
        User user = tvEmployeeTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            tfmaNV.setText("" + user.getMaNV());
            tfTenNV.setText("" + user.getTenNV());
            String date = new SimpleDateFormat("yyyy-MM-dd").format(user.getNgaySinh());
            LocalDate d = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            dpNgaySinh.setValue(d);
            if (user.getGioiTinh().equals("Nam")) {
                rbNam.fire();
            } else {
                rbNu.fire();
            }
            tfUsername.setText(user.getUsername());
            tfPassword.setText(user.getPassword());
            tfPNumber.setText(user.getSdt());
            tfEmail.setText(user.getEmail());
            tfAddress.setText(user.getQueQuan());
            tfLuong.setText("" + user.getLuong());
            tfIdCard.setText(user.getSoCMND());
        }
    }


    // TimKIem
    public void seachKeyReleased(KeyEvent keyEvent) {
        tfSearch.setOnKeyReleased(e -> {
            showInSearchBar();
            showUser();
        });
    }

    public void showInSearchBar() {
        userList.clear();
        if (tfSearch.getText().compareTo("") == 0) {
            addAll();
        } else {
            userList.clear();
            try {
                userList = FXCollections.observableArrayList();
                try {
                    ArrayList<User> p1 = fillCustomer(tfSearch.getText());
                    for (User i : p1) {
                        userList.add(i);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                tvEmployeeTable.setItems(userList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<User> fillCustomer(String s) throws Exception {
        String sql = "select * from dbo.[User] as nv where nv.name like '" + s + "%'" +
                "union select * from dbo.[User] as nv where nv.soCMND like '" + s + "%'" +
                "union select * from dbo.[User] as nv where nv.username like '" + s + "%'";
        DBConnection dbc = new DBConnection();
        ArrayList<User> kh = new ArrayList<>();
        try (Connection cnn = dbc.getConnection();
             PreparedStatement pstm = cnn.prepareStatement(sql);) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("id"), rs.getString("username"), rs.getString("password")
                        , rs.getString("tenNV"), rs.getDate("ngaySinh"), rs.getString("gioiTinh")
                        , rs.getString("soDT"), rs.getString("email")
                        , rs.getString("soCMND"), rs.getString("queQuan")
                        , rs.getInt("luong"));
                kh.add(user);
            }
        }
        return kh;
    }

    private void addAll() {
        userList.clear();
        try {
            ArrayList<User> kh1 = fillAll();
            for (User i : kh1) {
                userList.add(i);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        tvEmployeeTable.setItems(userList);
    }

    public ArrayList<User> fillAll() throws Exception {
        String sql = "select * from dbo.[User]";
        DBConnection dbc = new DBConnection();
        ArrayList<User> kh = new ArrayList<>();
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("id"), rs.getString("username"), rs.getString("password")
                        , rs.getString("tenNV"), rs.getDate("ngaySinh"), rs.getString("gioiTinh")
                        , rs.getString("soDT"), rs.getString("email")
                        , rs.getString("soCMND"), rs.getString("queQuan")
                        , rs.getInt("luong"));
            }
        }
        return kh;
    }
}
