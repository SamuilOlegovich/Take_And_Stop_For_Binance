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
import main.model.Agent;
import javafx.scene.text.Text;
import main.model.Lines;
import main.animations.*;


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
    private Text textField;

    @FXML
    void initialize() {
        enterButton.setOnAction(event -> {
            String APIKey = APIKeyField.getText().trim();
            String secretKey = secretKeyField.getText().trim();

            if ((!secretKey.equals("") && !APIKey.equals(""))
                    && (secretKey.length() > 10 && APIKey.length() > 10)) {
                Agent.getApi().setSECRET_KEY(secretKey);
                Agent.getApi().setAPI_KEY(APIKey);
                Agent.getWriteKeysAndSettings().writeNewKeys();
                openNewScene("/main/view/main.fxml");
            } else {
                // потрусить полями если что-то не верно
                Shake shakePassword = new Shake(secretKeyField);
                Shake shakeLogin = new Shake(APIKeyField);
                shakePassword.playAnim();
                shakeLogin.playAnim();

                textField.setText(Lines.s6);
                Agent.getWriteKeysAndSettings().enterPatternForKeys();
            }
        });
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
        stage.show();
    }
}
