package Controller;

import DBConnection.DBConnection;
import Model.KhachHang;
import Model.Phong;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ManageCustomer implements Initializable {

    @FXML
    private Label error_CMND;

    @FXML
    private Label error_dob;

    @FXML
    private Label error_gender;

    @FXML
    private Label error_idKH;

    @FXML
    private Label error_nameKH;

    @FXML
    private Label error_phoneN;

    @FXML
    private Label error_que;

    @FXML
    private Label error_quocTich;

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField idCardText;

    @FXML
    private TextField numberPhoneText;

    @FXML
    private TextField nativePlaceText;

    @FXML
    private TextField nationalityText;

    @FXML
    private DatePicker dboDatePicker;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private ToggleGroup genderGroup;

    //Thanh Tim Kiem
    @FXML
    private TextField seachText;

    // Bảng
    @FXML
    private TableView<KhachHang> customerTable;

    @FXML
    private TableColumn<KhachHang, String> idColumn;

    @FXML
    private TableColumn<KhachHang, String> nameColumn;

    @FXML
    private TableColumn<KhachHang, Date> dobColumn;

    @FXML
    private TableColumn<KhachHang, String> genderColumn;

    @FXML
    private TableColumn<KhachHang, String> idCardColumn;

    @FXML
    private TableColumn<KhachHang, String> numberPhoneColumn;

    @FXML
    private TableColumn<KhachHang, String> nativePlaceColumn;

    @FXML
    private TableColumn<KhachHang, String> nationalityColumn;

    private ObservableList<KhachHang> customersList;
    // Hết Bảng
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellTable();
        customersList= FXCollections.observableArrayList();
        addAll();
    }
    private boolean checkErrorText(){
        boolean isDobNotEmpty=validation.Validation.isDatePickerNotEmpty(dboDatePicker,error_dob,"Vui lòng chọn ngày sinh.");
        boolean isCMNDNotEmpty=validation.Validation.isTextFieldNotEmpty(idCardText,error_CMND,"Vui lòng nhập số CMND.");
        boolean isHomeNotEmpty=validation.Validation.isTextFieldNotEmpty(nativePlaceText,error_que,"Vui lòng nhập quê quán.");
        boolean isNameNotEmpty=validation.Validation.isTextFieldNotEmpty(nameText,error_nameKH,"Vui lòng nhập tên KH.");
        boolean isGenderNotEmpty=validation.Validation.isGenderNotEmpty(maleRadioButton,femaleRadioButton,error_gender,"Vui lòng chọn giới tính.");
        boolean isPhoneNNotEmpty=validation.Validation.isTextFieldNotEmpty(numberPhoneText,error_phoneN,"Vui lòng nhập số ĐT.");
        boolean isNationalityNotEmpty=validation.Validation.isTextFieldNotEmpty(nationalityText,error_quocTich,"Vui lòng nhập quốc tịch.");
        boolean phoneNIsNumber=false;
        boolean CMNDisNumber=false;
        if(isCMNDNotEmpty){
            CMNDisNumber=validation.Validation.isNumber(idCardText,error_CMND,"CMND phải là 1 dãy các chữ số");
        }
        if(isPhoneNNotEmpty){
            phoneNIsNumber=validation.Validation.isNumber(numberPhoneText,error_phoneN,"Số đt phải là 1 dãy các chữ số");
        }
        if(isDobNotEmpty && isCMNDNotEmpty && isHomeNotEmpty && isNameNotEmpty && isGenderNotEmpty && isPhoneNNotEmpty && isNationalityNotEmpty && phoneNIsNumber && CMNDisNumber){
            return true;
        }
        return false;
    }
    private void setCellTable(){
        idColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,String>("maKH"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,String>("ten"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,Date>("ngaySinh"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,String>("gioiTinh"));
        idCardColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,String>("soCMND"));
        numberPhoneColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,String>("soDT"));
        nativePlaceColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,String>("queQuan"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<KhachHang,String>("quocTich"));
    }

    private void addAll(){
        customersList.clear();
        try {
            ArrayList<KhachHang> kh1=fillAll();
            for(KhachHang i:kh1){
                customersList.add(i);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        customerTable.setItems(customersList);
    }


    // Hiển thị tất cả trong DB
    public ArrayList<KhachHang> fillAll() throws Exception {
        String sql = "select * from dbo.KhachHang";
        DBConnection dbc=new DBConnection();
        ArrayList<KhachHang> kh=new ArrayList<>();
        try (Connection cnn = dbc.getConnection(); PreparedStatement pstm = cnn.prepareStatement(sql);) {
            ResultSet rs=pstm.executeQuery();
            while(rs.next()){
                KhachHang kh1=new KhachHang();
                kh1.setMaKH(rs.getInt("maKH"));
                kh1.setTen(rs.getString("ten"));
                kh1.setNgaySinh(rs.getDate("ngaySinh"));
                kh1.setGioiTinh(rs.getString("gioiTinh"));
                kh1.setSoCMND(rs.getString("soCMND"));
                kh1.setSoDT(rs.getString("soDT"));
                kh1.setQueQuan(rs.getString("queQuan"));
                kh1.setQuocTich(rs.getString("quocTich"));
                kh.add(kh1);
            }
        }
        return kh;
    }

    //Sửa dữ liệu.
    // Update trong database;
    public boolean updateDB(KhachHang kh) throws Exception {
        DBConnection dbc=new DBConnection();
        String sql ="update dbo.KhachHang set ten=?, ngaySinh=?, gioiTinh=?, soCMND=?, soDT=?, queQuan=?, quocTich=? where maKH=?";
        try ( Connection cnn = dbc.getConnection();  PreparedStatement pstm = cnn.prepareStatement(sql);) {
            pstm.setString(1,kh.getTen());
            LocalDate d=dboDatePicker.getValue();
            Date d1=kh.getNgaySinh();
            String d2=new SimpleDateFormat("yyyyMMdd").format(d1);
            pstm.setString(2,d2);
            pstm.setString(3,kh.getGioiTinh());
            pstm.setString(4,kh.getSoCMND());
            pstm.setString(5,kh.getSoDT());
            pstm.setString(6,kh.getQueQuan());
            pstm.setString(7,kh.getQuocTich());
            StringBuilder sd=new StringBuilder(kh.getMaKH());
            sd.deleteCharAt(0);sd.deleteCharAt(0);
            String s1=sd.toString();
            int t=Integer.parseInt(s1);
            pstm.setInt(8,t);
            boolean check=false;
            try{
                pstm.executeUpdate();
                check=true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Update thành công");
                alert.show();
            }catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Số CMND hoặc sđt đã tồn tại.");
                alert.show();
            }
            return check;
        }
    }

    //Thao Tác với nut sửa
    public void update(ActionEvent event) throws ParseException {
        if(checkErrorText()){
            KhachHang kh=new KhachHang();
            KhachHang kh1=kh;
            kh.setMaKH(Integer.parseInt(idText.getText()));
            kh.setTen(nameText.getText());
            // Xử lý ngày tháng.
            LocalDate d=dboDatePicker.getValue();
            String s=d.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d1=sdf.parse(s);
            kh.setNgaySinh(d1);
            //Xử lý giới tính
            if(maleRadioButton.isSelected()){
                kh.setGioiTinh("Nam");
            }
            else {
                kh.setGioiTinh("Nữ");
            }
            //Xử lý phần còn lại
            kh.setSoCMND(idCardText.getText());
            kh.setSoDT(numberPhoneText.getText());
            kh.setQueQuan(nativePlaceText.getText());
            kh.setQuocTich(nationalityText.getText());
            try {
                updateDB(kh);

            } catch (Exception e) {
                e.printStackTrace();
            }
            addAll();
        }
    }

    // Thao tác khi nhấn chọn một đối tượng trong bảng.
    public void handleMouseAction(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount()==2) {
            KhachHang kh = customerTable.getSelectionModel().getSelectedItem();
            StringBuilder sd = new StringBuilder(kh.getMaKH());
            //Hiển thị mã-Tên
            sd.deleteCharAt(0);
            sd.deleteCharAt(0);
            String s1 = sd.toString();
            idText.setText(s1);
            nameText.setText(kh.getTen());
            //Hiển thị ngày sinh
            String sns = new SimpleDateFormat("yyyy-MM-dd").format(kh.getNgaySinh());
            LocalDate d = LocalDate.parse(sns, DateTimeFormatter.ISO_LOCAL_DATE);
            dboDatePicker.setValue(d);
            //Hiện thị giới tính
            if (kh.getGioiTinh().equals("Nam")) {
                maleRadioButton.fire();
            } else {
                femaleRadioButton.fire();
            }
            //SoCMND-Sodt-Que Quan-QuocTich.
            idCardText.setText(kh.getSoCMND());
            numberPhoneText.setText(kh.getSoDT());
            nativePlaceText.setText(kh.getQueQuan());
            nationalityText.setText(kh.getQuocTich());
        }
    }

    @FXML
    void resetInfor(ActionEvent event) {
        idText.setText("");
        nameText.setText("");
        dboDatePicker.setValue(null);
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        idCardText.setText("");
        numberPhoneText.setText("");
        nativePlaceText.setText("");
        nationalityText.setText("");

        error_CMND.setText(null);
        error_dob.setText(null);
        error_phoneN.setText(null);
        error_gender.setText(null);
        error_idKH.setText(null);
        error_nameKH.setText(null);
        error_que.setText(null);
        error_quocTich.setText(null);

    }
    //Xoa dữ liệu trong db.
    public void delete(ActionEvent event) {
        DBConnection db=new DBConnection();
        String sql ="delete dbo.KhachHang where maKH=?";
        try ( Connection cnn =db.getConnection();  PreparedStatement pstm = cnn.prepareStatement(sql);) {

            int i=Integer.parseInt(idText.getText());
            pstm.setInt(1,i);
            pstm.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        addAll();
    }

    // TimKIem
    public void seachKeyReleased(KeyEvent keyEvent) {
        seachText.setOnKeyReleased(e->{
            showInSearchBar();
        });
    }

    public void showInSearchBar(){
        customersList.clear();
        if(seachText.getText().compareTo("")==0){
            addAll();
        }
        else{
            customersList.clear();
            try{
                customersList= FXCollections.observableArrayList();
                try {
                    ArrayList<KhachHang> p1=fillCustomer(seachText.getText());
                    for(KhachHang i:p1){
                        customersList.add(i);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                customerTable.setItems(customersList);
            } catch (Exception ex) {
                Logger.getLogger(ManageRoom.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
    }


    public ArrayList<KhachHang> fillCustomer(String s) throws Exception {
        s=s.toLowerCase();
        String s1=removeAccent(s);
        ArrayList<KhachHang> kh=new ArrayList<>();
        ArrayList<KhachHang> kh1=fillAll();
        for(KhachHang i:kh1){
            if(removeAccent(i.getTen().toLowerCase()).contains(s1) || i.getTen().toLowerCase().contains(s)
                    || i.getSoCMND().contains(s1) || i.getSoDT().contains(s1)){
                kh.add(i);
            }
        }
        return kh;
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }

}
