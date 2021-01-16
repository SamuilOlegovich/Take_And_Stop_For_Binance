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
import main.model.Agent;
import main.model.EndPair;
import javafx.scene.text.Text;
import main.model.StrategySettingAndStatus;
import main.model.WedgeLines;




public class SettingPageController {
    private StrategySettingAndStatus strategySettingAndStatus;

    private String nameStrategy;
    private String tradingPair;

    private Double numberOfCoins;
    private Double trailingStop;
    private Double takePrice;
    private Double stopPrice;
    private Double price;

    private int fractionalParts;
    private int buyOrSell;
    private int onOrOffFP;
    private int onOrOffTS;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text textInfoERROR;

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
    private RadioButton buyRadioButton;

    @FXML
    private RadioButton sellRadioButton;

    @FXML
    private TextField numberOfCoinsField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField takePriceField;

    @FXML
    private TextField stopPriceField;

    @FXML
    private TextField fractionalPartsField;

    @FXML
    private TextField trailingStopField;

    @FXML
    private TextField nameStrategyField;

    @FXML
    private RadioButton onFPRadioButton;

    @FXML
    private RadioButton offFPRadioButton;

    @FXML
    private RadioButton onTSRadioButton;

    @FXML
    private RadioButton offTSRadioButton;

    @FXML
    void initialize() {
        // получаем и выводим список торговых пар
        getAListOfTradingPairs();
        // установка группы
        setGroupsOfRadioButtons();
        buyOrSell = 0;

        buyRadioButton.setOnAction(event -> {
            buyOrSell = 1;
        });

        sellRadioButton.setOnAction(event -> {
            buyOrSell = -1;
        });

        onTSRadioButton.setOnAction(event -> {
            onOrOffTS = 1;
        });

        offTSRadioButton.setOnAction(event -> {
            onOrOffTS = -1;
        });

        onFPRadioButton.setOnAction(event -> {
            onOrOffFP = 1;
        });

        offFPRadioButton.setOnAction(event -> {
            onOrOffFP = -1;
        });

        okButton.setOnAction(event -> {
            collectAndProcessData();
            if (checkIfEverythingIsOk()) {
                createAndFillAnObject();
                addTheFinishedObjectToTheListOfStrategies();
                openNewScene("/main/view/main.fxml");
            }
        });

        backButton.setOnAction(event -> {
            openNewScene("/main/view/main.fxml");
        });

        addPairButton.setOnAction(event -> {
            tradingPairField.undo();
            tradingPairField.insertText(0, getSelectedItem());
        });
    }

    private void getAListOfTradingPairs() {
        ObservableList<String> observableList = FXCollections.observableArrayList(Agent.getAllCoinPairList());
        listViewInSettingPage.setItems(observableList);
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


    private void setGroupsOfRadioButtons() {
        ToggleGroup groupBuyOrSel = new ToggleGroup();
        buyRadioButton.setToggleGroup(groupBuyOrSel);
        sellRadioButton.setToggleGroup(groupBuyOrSel);

        ToggleGroup groupOnOrOffFp = new ToggleGroup();
        onFPRadioButton.setToggleGroup(groupOnOrOffFp);
        offFPRadioButton.setToggleGroup(groupOnOrOffFp);
        offFPRadioButton.setSelected(true);

        ToggleGroup groupOnOrOffTs = new ToggleGroup();
        onTSRadioButton.setToggleGroup(groupOnOrOffTs);
        offTSRadioButton.setToggleGroup(groupOnOrOffTs);
        offTSRadioButton.setSelected(true);
    }



    private String getSelectedItem() {
        return listViewInSettingPage.getSelectionModel().getSelectedItems().toString()
                .replaceAll("\\[", "").replaceAll("]", "");
    }



    // считываем данные в полях
    private void collectAndProcessData() {
        String tradingPairText = tradingPairField.getText();
        String nameStrategyText = nameStrategyField.getText();
        String numberOfCoinsText = numberOfCoinsField.getText().replaceAll(",", ".");
        String priceText = priceField.getText().replaceAll(",", ".");
        String takePriceText = takePriceField.getText().replaceAll(",", ".");
        String stopPriceText = stopPriceField.getText().replaceAll(",", ".");
        String fractionalPartsText = fractionalPartsField.getText();
        String trailingStopText = trailingStopField.getText().replaceAll(",", ".");

        if (tradingPairText.length() >= 5) {
            boolean flag = false;
            EndPair[] endPairs = EndPair.values();
            for (EndPair e : endPairs) {
                if (tradingPairText.endsWith(e.toString())) {
                    flag = true;
                    break;
                }
            }
            if (flag) tradingPair = tradingPairText;
            else tradingPair = null;
        } else {
            tradingPair = null;
        }

        if (nameStrategyText.length() >= 1) {
            nameStrategy = nameStrategyText;
        } else {
            nameStrategy = null;
        }

        if (numberOfCoinsText.length() >= 1) {
            numberOfCoins = Double.parseDouble(numberOfCoinsText);
        } else {
            numberOfCoins = -1.0;
        }

        if (priceText.length() >= 1) {
            price = Double.parseDouble(priceText);
        } else {
            price = -1.0;
        }

        if (takePriceText.length() > 1) {
            takePrice = Double.parseDouble(takePriceText);
        } else {
            takePrice = -1.0;
        }

        if (stopPriceText.length() > 1) {
            stopPrice = Double.parseDouble(stopPriceText);
        } else {
            stopPrice = -1.0;
        }

        if (fractionalPartsText.length() > 1) {
            int i = Integer.parseInt(fractionalPartsText);
            if (i > 1 && i < 11) {
                fractionalParts = i;
            } else {
                fractionalParts = -1;
            }
        } else {
            fractionalParts = -1;
        }

        if (trailingStopText.length() > 3) {
            trailingStop = Double.parseDouble(trailingStopText);
        } else {
            trailingStop = -1.0;
        }
    }


    // проверяем все ли заполнено правильно для создания объекта если нет то сообщаем об этом
    private boolean checkIfEverythingIsOk() {
        boolean flag = true;
        // Вы не заполнили!
        StringBuilder blankFields = new StringBuilder("You have not completed!\n");
        if (tradingPair == null) {
            blankFields.append("· Trading pair\n");
            flag = false;
        }
        if (buyOrSell == 0) {
            blankFields.append("· Not chosen BUY or SELL\n");
            flag = false;
        }
        if (nameStrategy == null) {
            blankFields.append("· Name strategy\n");
            flag = false;
        }
        if (numberOfCoins == -1) {
            blankFields.append("· Number of coins\n");
            flag = false;
        }
        if (price == -1 && onOrOffTS != 1) {
            blankFields.append("· Price\n");
            flag = false;
        }
        // Уберите цену, включен трайлирующий стоп, или выключите стоп
        if (price > 0 && onOrOffTS == 1) {
            blankFields.append("· Remove price, turn on trailing stop, or turn off stop\n");
            flag = false;
        }
        // проверить что-то тут не так ----------------------------------------------------
        if (trailingStop == -1 && onOrOffTS == 1) {
            blankFields.append("· Trailing stop\n");
            flag = false;
        }
        // указан но выключен
        if (trailingStop > 0 && onOrOffTS <= 0) {
            blankFields.append("· Trailing stop specified but disabled\n");
            flag = false;
        }

        if (flag == false) {
            textInfoERROR.setText("");
            textInfoERROR.setText(blankFields.toString());
            return false;
        }

        textInfoERROR.setText("");
        textInfoERROR.setText(WedgeLines.s4);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }



    // создаем и заполняем обект стратегии
    private void createAndFillAnObject() {
        strategySettingAndStatus = new StrategySettingAndStatus();
        strategySettingAndStatus.setNameStrategy(nameStrategy);
        strategySettingAndStatus.setTradingPair(tradingPair);

        strategySettingAndStatus.setAmountOfCoins(numberOfCoins);
        strategySettingAndStatus.setTrailingStop(trailingStop);
        strategySettingAndStatus.setTakePrice(takePrice);
        strategySettingAndStatus.setStopPrice(stopPrice);
        strategySettingAndStatus.setPrice(price);

        strategySettingAndStatus.setFractionalParts(fractionalParts);
        strategySettingAndStatus.setBuyOrSell(buyOrSell);
        strategySettingAndStatus.setOnOrOffFP(onOrOffFP);
        strategySettingAndStatus.setOnOrOffTS(onOrOffTS);

        strategySettingAndStatus.setClassID();
    }


    // добавляем торговый объект в коллекцию стратегий
    private void addTheFinishedObjectToTheListOfStrategies() {
        Agent.getArraysOfStrategies().addToAllStrategyList(strategySettingAndStatus);
    }
}

