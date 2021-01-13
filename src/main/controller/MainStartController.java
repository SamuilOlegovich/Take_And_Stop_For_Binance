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
import main.model.*;



public class MainStartController {

    public MainStartController() {
        Agent.setMainStartController(this);
    }

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
        Thread thread = new Thread(new GetUpToDateDataOnPairs());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        enterButton.setOnAction(event -> {
            if (Agent.isGetUpToDateDataOnPairs() == true) {

            } else {
                openNewScene("/main/view/error_API_or_secret_key.fxml");
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
        stage.showAndWait();
    }
}
