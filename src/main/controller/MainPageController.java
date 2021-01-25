package main.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_GREENPeer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import main.model.*;
import javafx.scene.text.Text;




public class MainPageController {
    private CreatesTemplatesAndData createsTemplatesAndData;
    private ArraysOfWebSockets arraysOfWebSockets;
    private ArraysOfStrategies arraysOfStrategies;
    private ObservableList<String> observableList;
    private volatile boolean stopThreads;


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
    private Text startStopPriceText;



    @FXML
    void initialize() {
        stopThreads = true;
//        if (!Agent.isGetUpToDateDataOnPairs()) {
//            System.out.println("000");
//            Thread thread = new Thread(new GetUpToDateDataOnPairs());
//            thread.start();
//            try { thread.join(); }
//            catch (InterruptedException e) { e.printStackTrace(); }
//        }
        Thread clock = new Thread(new Clock());
        clock.start();
        Thread showExchangeRatesAndStatusBar = new Thread(new ShowExchangeRatesAndStatusBar());
        showExchangeRatesAndStatusBar.start();

        Agent.getArraysOfStrategies().setMainPageController(this);
        Agent.setMainPageController(this);

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
                        && !stringOfList.equals(Lines.thereAreNoStrategiesNow)) {
                    arraysOfStrategies.findStrategy(stringOfList);
                    openNewScene("/main/view/info.fxml");
                }
            }
        });

        startAllButton.setOnAction(event -> {
            new StartAllButton().start();
//            if (!Agent.isGetUpToDateDataOnPairs()) {
//                openNewScene("/main/view/error_api_or_secret_key.fxml");
//            } else {
//                Agent.setStartAllOrStopAll(true);
//                arraysOfWebSockets.runAllWebSocketsForWorkingCouples();
//            }
        });

        stopAllButton.setOnAction(event -> {
            new StopAllButton().start();
//            Agent.setStartAllOrStopAll(false);
//            arraysOfWebSockets.stopAllWebSocketsForWorkingCouples();
        });

        timeText.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) { openNewScene("/main/view/time.fxml"); }
        });

        startButton.setOnAction(event -> {
            new StartButton().start();
//            String string = getSelectedItem();
//            if (string.length() > 5) arraysOfStrategies.launchStrategy(string);
        });

        stopButton.setOnAction(event -> {
            new StopButton().start();
//            String string = getSelectedItem();
//            if(string.length() > 5) {
//                arraysOfStrategies.stopStrategy(string);
//            }
        });

        addButton.setOnAction(event -> {
            openNewScene("/main/view/setting.fxml");
        });

        editButton.setOnAction(event -> {
            String stringOfList = getSelectedItem();
            if (!stringOfList.equals(Enums.ON_LINE_STRATEGY.toString())
                    && !stringOfList.equals(Enums.OFF_LINE_STRATEGY.toString())
                    && !stringOfList.equals(Lines.thereAreNoStrategiesNow)
                    && !stringOfList.equals("")) {
                arraysOfStrategies.findStrategy(getSelectedItem());
                openNewScene("/main/view/edit.fxml");
            }
        });

        deleteButton.setOnAction(event -> {
            String string = getSelectedItem();
            if (string.length() > 5) arraysOfStrategies.removeStrategy(string);
        });


//        if (!Agent.isGetUpToDateDataOnPairs()) {
//            openNewScene("/main/view/authorization.fxml");
//        }
    }



    private void getAListOfStrategy() {
        observableList.clear();
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



    private void openNewScene(String in) {
        String window = new String(in);
        stopThreads = false;
        // при нажатии на кнопку мы прячем окно
        // мы берем сцену на которой она находится
        // потом берем окно на которой она находится
        // и дальше уже это окно уже прячем
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


//        Alert alert = new Alert(AlertType.CONFIRMATION);
//        alert.setTitle("Select");
//        alert.setHeaderText("Choose the sport you like:");
//
//        ButtonType football = new ButtonType("Football");
//        ButtonType badminton = new ButtonType("Badminton");
//        ButtonType volleyball = new ButtonType("Volleyball");
//
//        // Remove default ButtonTypes
//        alert.getButtonTypes().clear();
//
//        alert.getButtonTypes().addAll(football, badminton, volleyball);
//
//        // option != null.
//        Optional<ButtonType> option = alert.showAndWait();

//        if (option.get() == null) {
//            this.label.setText("No selection!");
//        } else if (option.get() == football) {
//            this.label.setText("You like Football");
//        } else if (option.get() == badminton) {
//            this.label.setText("You like Badminton");
//        } else if (option.get() == volleyball) {
//            this.label.setText("You like Volleyball");
//        } else {
//            this.label.setText("-");
//        }
    }



    private String getSelectedItem() {
        return listViewInMainPage.getSelectionModel().getSelectedItems().toString()
                .replaceAll("\\[", "").replaceAll("]", "");
    }





    private class StartAllButton extends Thread {
        @Override
        public synchronized void start() {
            if (!Agent.isGetUpToDateDataOnPairs()) {
                openNewScene("/main/view/error_api_or_secret_key.fxml");
            } else {
                Agent.setStartAllOrStopAll(true);
                arraysOfWebSockets.runAllWebSocketsForWorkingCouples();
            }
        }
    }



    private class StopAllButton extends Thread {
        @Override
        public synchronized void start() {
            Agent.setStartAllOrStopAll(false);
            arraysOfWebSockets.stopAllWebSocketsForWorkingCouples();
        }
    }



    private class StartButton extends Thread {
        @Override
        public synchronized void start() {
            String string = getSelectedItem();
            if (string.length() > 5) {
                arraysOfStrategies.launchStrategy(string);
            }
        }
    }



    private class StopButton extends Thread {
        @Override
        public synchronized void start() {
            String string = getSelectedItem();
            if(string.length() > 5) {
                arraysOfStrategies.stopStrategy(string);
            }
        }
    }



    private class Clock implements Runnable {
        DateFormat dateFormat;

        @Override
        public void run() {
            dateFormat = new SimpleDateFormat("EEEE HH:mm:ss", Locale.ENGLISH);
            while (stopThreads) {
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
            date.setTime(Agent.getDateDifference() > 0
                    ? date.getTime() + (1000 * 60 * 60 * Math.abs(Agent.getDateDifference()))
                    : date.getTime() - (1000 * 60 * 60 * Math.abs(Agent.getDateDifference())));
            dateFormat.format(date);
            return dateFormat.format(date);
        }
    }


    // Показать курсы валют и строку состояния
    private class ShowExchangeRatesAndStatusBar implements Runnable {
        private final ArraysOfWebSockets webSockets;

        public ShowExchangeRatesAndStatusBar() { this.webSockets = Agent.getArraysOfWebSockets(); }

        @Override
        public void run() {
            while (stopThreads) {
                try { Thread.sleep(1000);
                } catch (InterruptedException e) { e.printStackTrace(); }
                startStopPriceText.setText(getStingExchangeRatesAndStatus());
            }
        }

        private String getStingExchangeRatesAndStatus() {
            StringBuilder out = new StringBuilder();
            out.append(Agent.isStartAllOrStopAll() ? Enums.START.toString() : Enums.STOP.toString());
            out.append("   ");
            for (String s : Agent.getViewPairSockets()) {
                out.append("  ").append(s).append(" -> ").append(webSockets.getPriceNow(s));
            }
            return out.toString();
        }
    }


    public void updateListView() { getAListOfStrategy(); }
}
