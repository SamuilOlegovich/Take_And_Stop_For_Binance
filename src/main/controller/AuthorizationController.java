package main.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.model.binance.API;


public class AuthorizationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField secretKeyField;

    @FXML
    private TextField APIKeyField;

    @FXML
    private Button enterButton;

    @FXML
    void initialize() {
        enterButton.setOnAction(event -> {
            String APIKey = APIKeyField.getText().trim();
            String secretKey = secretKeyField.getText().trim();

            if ((!secretKey.equals("") && !APIKey.equals("")) && (secretKey.length() > 10 && APIKey.length() > 10)) {
                setAPIAndSecretKey(secretKey, APIKey);
                API.setSecretKey(secretKey);
                API.setApiKey(APIKey);
                openNewScene("/main/view/main_page.fxml");
            } else {
                openNewScene("/main/view/error_api_or_secret_key.fxml");
            }
        });

//        registerNowButton.setOnAction(event -> {
//            openNewScene("/sample/main.view/signUp.fxml");
//        });
    }



    private void setAPIAndSecretKey(String loginText, String passwordText) {

//        int counter = 0;
//        User user = new User();
//        user.setLogin(loginText);
//        user.setPassword(passwordText);
//        DatabaseHandler databaseHandler = new DatabaseHandler();
//        ResultSet resultSet = databaseHandler.getUser(user);

//        try {
//            while (resultSet.next()) counter++;
//        } catch (SQLException e) { e.printStackTrace(); }
//
//        if (counter >= 1) openNewScene("/sample/main.view/app.fxml");
//        else {
//            Shake shakeLogin = new Shake(loginField);
//            Shake shakePassword = new Shake(passwordField);
//            shakeLogin.playAnim();
//            shakePassword.playAnim();
//        }
    }



    private void openNewScene(String window) {
        // при нажатии на кнопку мы прячем окно
        // мы берем сцену на которой она находится
        // потом берем окно на которой она находится
        // и дальше уже это окно уже прячем
        enterButton.getScene().getWindow().hide();
        // далее нам нужно отобразить следующее нужное нам окно
        FXMLLoader fxmlLoader = new FXMLLoader();
        // устанавливаем локацию файла который нам надо загрузить
        fxmlLoader.setLocation(getClass().getResource(window));
        // а теперь запускаем его отображение
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent parent = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.showAndWait();
    }
}
