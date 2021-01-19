package main.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.Locale;
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
import main.model.*;
import javafx.scene.text.Text;




public class MainPageController {
    private CreatesTemplatesAndData createsTemplatesAndData;
    private ArraysOfStrategies arraysOfStrategies;
    private ObservableList<String> observableList;
    private Thread threadShow;



//    public MainPageController() { Agent.setMainPageController(this); }



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text timeText;

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
        Thread clock = new Thread(new Clock());

        thread.start();
        clock.start();

        try { thread.join();
        } catch (InterruptedException e) { e.printStackTrace(); }

        Agent.getArraysOfStrategies().setMainPageController(this);
        Agent.setMainPageController(this);

        observableList = FXCollections.observableArrayList();
        listViewInMainPage.setItems(observableList);

        // получить адреса используемых классов
        getAddressesOfUsedClasses();

        // получаем и выводим список торговых пар
        getAListOfStrategy();

        // позволяет выбирать несколько элементов из списка
//        listViewInMainPage.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // двойное нажатие мыши - переходим на страницу детальной информации
        listViewInMainPage.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                String stringOfList = getSelectedItem();
                if (!stringOfList.equals(Enums.ON_LINE_STRATEGY.toString())
                        && !stringOfList.equals(Enums.OFF_LINE_STRATEGY.toString())
                        && !stringOfList.equals(Lines.thereAreNoStrategiesNow)) {
                    arraysOfStrategies.findStrategy(stringOfList);
                    openNewScene("/main/view/info.fxml");
                }
            }
        });

        startAllButton.setOnAction(event -> {
            if (!Agent.isGetUpToDateDataOnPairs()) {
                openNewScene("/main/view/error_api_or_secret_key.fxml");
            }
        });

        stopAllButton.setOnAction(event -> {

        });

        timeText.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                openNewScene("/main/view/time.fxml");
            }
        });

        startButton.setOnAction(event -> {
            arraysOfStrategies.launchStrategy(getSelectedItem());
        });

        stopButton.setOnAction(event -> {
            arraysOfStrategies.stopStrategy(getSelectedItem());
        });

        addButton.setOnAction(event -> {
            openNewScene("/main/view/setting.fxml");
        });

        editButton.setOnAction(event -> {
            String stringOfList = getSelectedItem();
            if (!stringOfList.equals(Enums.ON_LINE_STRATEGY.toString())
                    && !stringOfList.equals(Enums.OFF_LINE_STRATEGY.toString())
                    && !stringOfList.equals(Lines.thereAreNoStrategiesNow)) {
                arraysOfStrategies.findStrategy(getSelectedItem());
                openNewScene("/main/view/edit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            arraysOfStrategies.removeStrategy(getSelectedItem());
        });
    }



    private void getAListOfStrategy() {
//        RefreshListView refreshListView = new RefreshListView();
//        Agent.setRefreshListView(refreshListView);

//        refreshListView.updateListView();
        observableList.clear();
        observableList.addAll(Enums.ON_LINE_STRATEGY.toString());
        observableList.addAll(createsTemplatesAndData.getTradedStrategyList());
        observableList.add(Enums.OFF_LINE_STRATEGY.toString());
        observableList.addAll(createsTemplatesAndData.getStoppedStrategyList());
        listViewInMainPage.refresh();


//        observableList.clear();
//        observableList.addAll(Enums.ON_LINE_STRATEGY.toString());
//        observableList.addAll(createsTemplatesAndData.getTradedStrategyList());
//        observableList.add(Enums.OFF_LINE_STRATEGY.toString());
//        observableList.addAll(createsTemplatesAndData.getStoppedStrategyList());
//        listViewInMainPage.refresh();
//        System.out.println("refresh");

//        ObservableList<String> observableList = FXCollections.observableArrayList(Enums.ON_LINE_STRATEGY.toString());
//        observableList.addAll(createsTemplatesAndData.getTradedStrategyList());
//        observableList.add(Enums.OFF_LINE_STRATEGY.toString());
//        observableList.addAll(createsTemplatesAndData.getStoppedStrategyList());
//        listViewInMainPage.setItems(observableList);
    }



    public class RefreshListView {
        public void updateListView() {
            observableList.clear();
            observableList.addAll(Enums.ON_LINE_STRATEGY.toString());
            observableList.addAll(createsTemplatesAndData.getTradedStrategyList());
            observableList.add(Enums.OFF_LINE_STRATEGY.toString());
            observableList.addAll(createsTemplatesAndData.getStoppedStrategyList());
            listViewInMainPage.refresh();
            System.out.println("refresh");
//        getAListOfStrategy();
        }
    }



    private void getAddressesOfUsedClasses() {
        createsTemplatesAndData = Agent.getCreatesTemplatesAndData();
        arraysOfStrategies = Agent.getArraysOfStrategies();
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
//        stage.showAndWait();
        stage.show();
    }



    private String getSelectedItem() {
        return listViewInMainPage.getSelectionModel().getSelectedItems().toString()
                .replaceAll("\\[", "").replaceAll("]", "");
    }





    private class Clock implements Runnable {
        DateFormat dateFormat;

        @Override
        public void run() {
            dateFormat = new SimpleDateFormat("EEEE HH:mm:ss", Locale.ENGLISH);
            while (true) {
            timeText.setText(getDate());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getDate() {
            Date date = new Date();
            dateFormat.format(date);
            return dateFormat.format(date);
        }
    }

    public void updateListView() {
        getAListOfStrategy();
    }
}
