package main.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.model.Agent;
import javafx.scene.text.Text;
import main.animations.*;



public class TimeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField APIKeyField;

    @FXML
    private Button enterButton;

    @FXML
    private Text textField;

    @FXML
    void initialize() {
        enterButton.setOnAction(event -> {
            String timeZone = APIKeyField.getText().trim();
            try {
                int time = Integer.parseInt(timeZone);
                if (time > -12 && time < 12) {
                    Agent.setDateDifference(time);
                    openNewScene("/main/view/main.fxml");
                } else { wrongInput(); }
            } catch (Exception e) {
                wrongInput();
            }
        });
    }

    private void wrongInput() {
        // потрусить полями если что-то не верно
        textField.setText("Incorrect input format");
        Shake shakeLogin = new Shake(APIKeyField);
        shakeLogin.playAnim();
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
