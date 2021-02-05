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
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.model.*;
import javafx.scene.text.Text;




public class MainPageController {
    private ShowExchangeRates showExchangeRates;
    private CreatesTemplatesAndData createsTemplatesAndData;
    private ArraysOfWebSockets arraysOfWebSockets;
    private ArraysOfStrategies arraysOfStrategies;
    private ObservableList<String> observableList;
    private Clocks clocks;


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
    private Text startStopText;

    @FXML
    private Text pricesText;



    @FXML
    void initialize() {
        Agent.getArraysOfStrategies().setMainPageController(this);
        Agent.setMainPageController(this);

        showExchangeRates = new ShowExchangeRates(this);
        clocks = new Clocks(this);
        showExchangeRates.start();
        clocks.start();

        if (Agent.isStartAllOrStopAll()) { startStopText.setText(Enums.START.toString()); }
        else { startStopText.setText(Enums.STOP.toString()); }

        observableList = FXCollections.observableArrayList();
        arraysOfWebSockets = Agent.getArraysOfWebSockets();
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
                        && !stringOfList.equals(Lines.thereAreNoStrategiesNow)
                        && !stringOfList.equals(Lines.tableOfContents)) {
                    arraysOfStrategies.findStrategy(stringOfList);
                    openNewScene("/main/view/info.fxml");
                }
            }
        });

        // запускаем все рабочие стратегии и разрешаем подачу на них котировок
        startAllButton.setOnAction(event -> {
            Agent.setStartAllOrStopAll(true);
            startStopText.setText(Enums.START.toString());
            if (Agent.isStartAllOrStopAll()) {
                arraysOfWebSockets.runAllWebSocketsForWorkingCouples();
                Agent.setOneStatr(false);
            }
        });

        // останавливает передачу данных котировок на все рабочие стратегии, но сокеты не отключаем
        stopAllButton.setOnAction(event -> {
            Agent.setStartAllOrStopAll(false);
            startStopText.setText(Enums.STOP.toString());
        });

        startButton.setOnAction(event -> {
            String string = getSelectedItem();
            if (string.length() > 5) { arraysOfStrategies.launchStrategy(string); }
        });

        stopButton.setOnAction(event -> {
            String string = getSelectedItem();
            if(string.length() > 5) { arraysOfStrategies.stopStrategy(string); }
        });

        addButton.setOnAction(event -> {
            openNewScene("/main/view/setting.fxml");
        });

        editButton.setOnAction(event -> {
            String stringOfList = getSelectedItem();
            if (!stringOfList.equals(Enums.ON_LINE_STRATEGY.toString())
                    && !stringOfList.equals(Enums.OFF_LINE_STRATEGY.toString())
                    && !stringOfList.equals(Lines.thereAreNoStrategiesNow)
                    && !stringOfList.equals(Lines.tableOfContents)
                    && !stringOfList.equals("")) {
                arraysOfStrategies.findStrategy(stringOfList);
                if (!arraysOfStrategies.getStrategySettingAndStatus().getWorks()) {
                    openNewScene("/main/view/edit.fxml");
                }
            }
        });

        deleteButton.setOnAction(event -> {
            String string = getSelectedItem();
            if (string.length() > 5) { arraysOfStrategies.removeStrategy(string); }
        });

        timeText.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) { openNewScene("/main/view/time.fxml"); }
        });


        Agent.getWriterAndReadFile().writerFile(DatesTimes.getDateLogs(),
                Agent.getFilesAndPathCreator().getPathLogs(), true);
    }



    private void openNewScene(String window) {
        showExchangeRates.stopThreads(false);
        clocks.stopThreads(false);
        // при нажатии на кнопку мы прячем окно
        // мы берем сцену на которой она находится
        // потом берем окно на которой она находится
        // и дальше уже это окно прячем
        stopButton.getScene().getWindow().hide();
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



    private void getAListOfStrategy() {
        if (observableList.size() > 0) { observableList.clear(); }
        observableList.addAll(Lines.tableOfContents);
        observableList.addAll(Enums.ON_LINE_STRATEGY.toString());
        observableList.addAll(createsTemplatesAndData.getTradedStrategyList());
        observableList.add(Enums.OFF_LINE_STRATEGY.toString());
        observableList.addAll(createsTemplatesAndData.getStoppedStrategyList());
        listViewInMainPage.refresh();
    }



    private void getAddressesOfUsedClasses() {
        arraysOfStrategies = Agent.getArraysOfStrategies();
        createsTemplatesAndData = Agent.getCreatesTemplatesAndData();
        arraysOfStrategies.setWriteKeysAndSettings(Agent.getWriteKeysAndSettings());
    }



    // дать строку выбранную в списке
    private String getSelectedItem() {
        return listViewInMainPage.getSelectionModel().getSelectedItems().toString()
                .replaceAll("\\[", "").replaceAll("]", "");
    }


    private synchronized void setStartStopPriceText(String in) {
        pricesText.setText(in);
    }



    public synchronized void updateListView() {
        getAListOfStrategy();
    }



    // обновляет время
    public synchronized void serClocks(String in) {
        timeText.setText(in);
    }



    // обновляет курсы валют и строку состояния
    public synchronized void setExchangeRates(String in) {
        pricesText.setText(in);
    }

}
