package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarBorderPane implements Initializable {
    @FXML
    private BorderPane borderPane;

    @FXML
    private Label labelUsername;

    @FXML
    void handleMenuItem(ActionEvent event) throws IOException {
        MenuItem mItem = (MenuItem) event.getSource();
        String side = mItem.getText();
        Stage stage = (Stage) borderPane.getScene().getWindow();
        if ("Đặt phòng".equalsIgnoreCase(side)) {
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("../View/CheckIn.fxml"));
            try {
                stage.setHeight(550);
                stage.setWidth(720);
                stage.centerOnScreen();
                borderPane.setCenter(menuLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("Thanh toán".equalsIgnoreCase(side)) {
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("../View/CheckOut.fxml"));

            StackPane secondLayout = new StackPane();
            secondLayout.getChildren().add(menuLoader.load());

            Scene secondScene = new Scene(secondLayout);

            Stage newWindow = new Stage();
            newWindow.setTitle("Check Out");
            newWindow.setScene(secondScene);
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.initOwner(stage);

            newWindow.setX(stage.getX() + 0);
            newWindow.setY(stage.getY() + 100);
            newWindow.show();

        } else if ("Quản lý phòng".equalsIgnoreCase(side)){
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("../View/RoomManager.fxml"));
            try {
                StackPane secondLayout = new StackPane();
                secondLayout.getChildren().add(menuLoader.load());
                Scene secondScene = new Scene(secondLayout);

                Stage newWindow = new Stage();
                newWindow.setTitle("Quản lý phòng");
                newWindow.setScene(secondScene);

                newWindow.initModality(Modality.WINDOW_MODAL);
                newWindow.initOwner(stage);

                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                newWindow.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                newWindow.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                newWindow.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if("Quản lý khách hàng".equalsIgnoreCase(side)){
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("../View/CustomerManager.fxml"));
            try {
                StackPane secondLayout = new StackPane();
                secondLayout.getChildren().add(menuLoader.load());
                Scene secondScene = new Scene(secondLayout);

                Stage newWindow = new Stage();
                newWindow.setTitle("Quản lý khách hàng");
                newWindow.setScene(secondScene);

                newWindow.initModality(Modality.WINDOW_MODAL);
                newWindow.initOwner(stage);

                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                newWindow.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                newWindow.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                newWindow.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if("Quản lý nhân viên".equalsIgnoreCase(side)){
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("../View/EmployeeManager.fxml"));
            try {
                StackPane secondLayout = new StackPane();
                secondLayout.getChildren().add(menuLoader.load());
                Scene secondScene = new Scene(secondLayout);

                Stage newWindow = new Stage();
                newWindow.setTitle("Quản lý nhân viên");
                newWindow.setScene(secondScene);

                newWindow.initModality(Modality.WINDOW_MODAL);
                newWindow.initOwner(stage);

                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                newWindow.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                newWindow.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                newWindow.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if("Thông tin".equalsIgnoreCase(side)){
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("../View/InforUser.fxml"));
            StackPane secondLayout = new StackPane();
            secondLayout.getChildren().add(menuLoader.load());
            Scene secondScene = new Scene(secondLayout);
            Stage newWindow = new Stage();
            newWindow.setTitle("Thông tin tài khoản");
            newWindow.setScene(secondScene);
            Account inforUser = menuLoader.getController();
            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.initOwner(stage);
            newWindow.setX(stage.getX() + 200);
            newWindow.setY(stage.getY() + 100);
            inforUser.setInfor(labelUsername.getText());
            newWindow.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("../View/CheckIn.fxml"));
        try {
            borderPane.setCenter(menuLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getusername(String username){
        labelUsername.setText(username);
        labelUsername.setVisible(false);
    }
}
