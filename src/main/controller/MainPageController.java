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
import javafx.stage.Stage;
import main.model.Agent;
import main.model.GetUpToDateDataOnPairs;



public class MainPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> listViewInMainPage;

    @FXML
    private Button startAllButton;

    @FXML
    private Button stopAllButton;

    @FXML
    private Button startButton;

    @FXML
    private Button editButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    void initialize() {
        Thread thread = new Thread(new GetUpToDateDataOnPairs());
        thread.start();
        try { thread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }

        ObservableList<String> observableList = FXCollections.observableArrayList(Agent.getAllCoinPairList());
        listViewInMainPage.setItems(observableList);


        startAllButton.setOnAction(event -> {
            if (Agent.isGetUpToDateDataOnPairs() == true) {

            } else {
                openNewScene("/main/view/error_api_or_secret_key.fxml");
            }
        });

        stopAllButton.setOnAction(event -> {

        });

        startButton.setOnAction(event -> {
            String keySymbol = getSelectedItem();
        });

        editButton.setOnAction(event -> {
            String keySymbol = getSelectedItem();

        });

        stopButton.setOnAction(event -> {
            String keySymbol = getSelectedItem();
        });

        addButton.setOnAction(event -> {
            openNewScene("/main/view/setting_page.fxml");
        });

        deleteButton.setOnAction(event -> {
            String keySymbol = getSelectedItem();
        });
    }



    private void openNewScene(String window) {
        // при нажатии на кнопку мы прячем окно
        // мы берем сцену на которой она находится
        // потом берем окно на которой она находится
        // и дальше уже это окно уже прячем
        startAllButton.getScene().getWindow().hide();
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
        return listViewInMainPage.getSelectionModel().getSelectedItems().toString()
                    .replaceAll("\\[", "").replaceAll("]", "");
    }

}
