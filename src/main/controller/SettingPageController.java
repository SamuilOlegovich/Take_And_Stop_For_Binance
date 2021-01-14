package main.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.model.Agent;



public class SettingPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> listViewInSettingPage;

    @FXML
    private Button addPairButton;

    @FXML
    private Button okButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField tradingPairField;

    @FXML
    void initialize() {
        ObservableList<String> observableList = FXCollections.observableArrayList(Agent.getListAllCoinPair());
        listViewInSettingPage.setItems(observableList);

        okButton.setOnAction(event -> {
            String tradingPair = tradingPairField.getText();





//            openNewScene("/main/view/main_page.fxml");
        });

        backButton.setOnAction(event -> {
            openNewScene("/main/view/main_page.fxml");
        });

        addPairButton.setOnAction(event -> {
            tradingPairField.insertText(0, "");
            tradingPairField.insertText(0, getSelectedItem());
        });
    }


    private void openNewScene(String window) {
        // при нажатии на кнопку мы прячем окно
        // мы берем сцену на которой она находится
        // потом берем окно на которой она находится
        // и дальше уже это окно уже прячем
        backButton.getScene().getWindow().hide();
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



    private String getSelectedItem() {
        return listViewInSettingPage.getSelectionModel().getSelectedItems().toString()
                .replaceAll("\\[", "").replaceAll("]", "");
    }
}

