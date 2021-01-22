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
import main.animations.Shake;
import main.model.*;
import javafx.scene.text.Text;
import main.model.binance.datatype.BinanceEventDepthUpdate;


public class SettingPageController {
    private ArraysOfStrategies arraysOfStrategies;
    private StrategyObject strategyObject;

    private String nameStrategy;
    private String tradingPair;

    private Double numberOfCoins;
    private Double trailingStop;
    private Double takePrice;
    private Double stopPrice;
    private Double price;

    private int fractionalParts;
    private int lowerOrHigher;
    private int buyOrSell;

    private boolean onOrOffFP;
    private boolean onOrOffTS;


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
    private RadioButton higherButton;

    @FXML
    private RadioButton lowerButton;

    @FXML
    void initialize() {
        // получить адреса используемых классов
        getAddressesOfUsedClasses();
        // получаем и выводим список торговых пар
        getAListOfTradingPairs();
        // установка группы
        setGroupsOfRadioButtons();
        lowerOrHigher = 0;
        buyOrSell = 0;

        buyRadioButton.setOnAction(event -> {
            buyOrSell = 1;
        });

        sellRadioButton.setOnAction(event -> {
            buyOrSell = -1;
        });

        onTSRadioButton.setOnAction(event -> {
            onOrOffTS = true;
        });

        offTSRadioButton.setOnAction(event -> {
            onOrOffTS = false;
        });

        onFPRadioButton.setOnAction(event -> {
            onOrOffFP = true;
        });

        offFPRadioButton.setOnAction(event -> {
            onOrOffFP = false;
        });

        higherButton.setOnAction(event -> {
            lowerOrHigher = 1;
        });

        lowerButton.setOnAction(event -> {
            lowerOrHigher = -1;
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

        listViewInSettingPage.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) { getSelectedItem(); }
        });

        addPairButton.setOnAction(event -> {
            tradingPairField.undo();
            tradingPairField.insertText(0, getSelectedItem());
        });
    }



    private void getAddressesOfUsedClasses() {
        arraysOfStrategies = Agent.getArraysOfStrategies();
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
        okButton.getScene().getWindow().hide();
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


    private void setGroupsOfRadioButtons() {
        ToggleGroup groupBuyOrSel = new ToggleGroup();
        buyRadioButton.setToggleGroup(groupBuyOrSel);
        sellRadioButton.setToggleGroup(groupBuyOrSel);


        ToggleGroup lowerOrHigher = new ToggleGroup();
        higherButton.setToggleGroup(lowerOrHigher);
        lowerButton.setToggleGroup(lowerOrHigher);

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
        } else { tradingPair = null; }

        if (nameStrategyText.length() >= 1) { nameStrategy = nameStrategyText;
        } else { nameStrategy = null; }

        if (numberOfCoinsText.length() >= 1) { numberOfCoins = Double.parseDouble(numberOfCoinsText);
        } else { numberOfCoins = -1.0; }

        if (priceText.length() >= 1) { price = Double.parseDouble(priceText); }
        else { price = -1.0; }

        if (takePriceText.length() > 1) { takePrice = Double.parseDouble(takePriceText); }
        else { takePrice = -1.0; }

        if (stopPriceText.length() > 1) { stopPrice = Double.parseDouble(stopPriceText); }
        else { stopPrice = -1.0; }

        if (fractionalPartsText.length() > 1) {
            int i = Integer.parseInt(fractionalPartsText);
            if (i > 1 && i < 11) { fractionalParts = i; }
            else { fractionalParts = -1; }
        } else {
            fractionalParts = -1;
        }

        if (trailingStopText.length() > 3) { trailingStop = Double.parseDouble(trailingStopText); }
        else { trailingStop = -1.0; }
    }


    // проверяем все ли заполнено правильно для создания объекта если нет то сообщаем об этом
    private boolean checkIfEverythingIsOk() {
        boolean flag = true;
        // Вы не заполнили!
        StringBuilder blankFields = new StringBuilder("You have not completed!\n");
        if (tradingPair == null) {
            blankFields.append("· Trading pair\n");
            Shake shake = new Shake(tradingPairField);
            shake.playAnim();
            flag = false;
        }

        if (buyOrSell == 0) {
            blankFields.append("· Not chosen BUY or SELL\n");
            Shake shake2 = new Shake(sellRadioButton);
            Shake shake = new Shake(buyRadioButton);
            shake2.playAnim();
            shake.playAnim();
            flag = false;
        }

        if (nameStrategy == null) {
            blankFields.append("· Name strategy\n");
            Shake shake = new Shake(nameStrategyField);
            shake.playAnim();
            flag = false;
        }

        if (numberOfCoins == -1) {
            blankFields.append("· Number of coins\n");
            Shake shake = new Shake(numberOfCoinsField);
            shake.playAnim();
            flag = false;
        }

        if (price == -1 && !onOrOffTS) {
            blankFields.append("· Price\n");
            Shake shake = new Shake(priceField);
            shake.playAnim();
            flag = false;
        }
        // Уберите цену, включен трайлирующий стоп, или выключите стоп
        if (price > 0 && onOrOffTS) {
            blankFields.append("· Remove price, turn on trailing stop, or turn off stop\n");
            Shake shake3 = new Shake(offTSRadioButton);
            Shake shake2 = new Shake(onTSRadioButton);
            Shake shake = new Shake(priceField);
            shake3.playAnim();
            shake2.playAnim();
            shake.playAnim();
            flag = false;
        }
        // проверить что-то тут не так ----------------------------------------------------
        if (trailingStop == -1 && onOrOffTS ) {
            blankFields.append("· Trailing stop\n");
            Shake shake3 = new Shake(offTSRadioButton);
            Shake shake2 = new Shake(onTSRadioButton);
            Shake shake = new Shake(trailingStopField);
            shake3.playAnim();
            shake2.playAnim();
            shake.playAnim();
            flag = false;
        }
        // указан но выключен
        if (trailingStop > 0 && !onOrOffTS) {
            blankFields.append("· Trailing stop specified but disabled\n");
            Shake shake3 = new Shake(offTSRadioButton);
            Shake shake2 = new Shake(onTSRadioButton);
            Shake shake = new Shake(trailingStopField);
            shake3.playAnim();
            shake2.playAnim();
            shake.playAnim();
            flag = false;
        }

        // Не выбрано выше или ниже текущей цены
        if (lowerOrHigher == 0 && !onOrOffTS) {
            blankFields.append("· Not selected above or below current price\n");
            Shake shake2 = new Shake(higherButton);
            Shake shake = new Shake(lowerButton);
            shake2.playAnim();
            shake.playAnim();
            flag = false;
        }

        if ((buyOrSell == 1 && (price > takePrice && takePrice > 0))
                || (buyOrSell == 1 && (price < stopPrice && stopPrice > 0))) {
            blankFields.append("· Something is wrong with the price, stop and take\n");
            Shake shake3 = new Shake(stopPriceField);
            Shake shake2 = new Shake(takePriceField);
            Shake shake = new Shake(priceField);
            shake3.playAnim();
            shake2.playAnim();
            shake.playAnim();
            flag = false;
        }

        if ((buyOrSell == -1 && (price < takePrice && takePrice > 0))
                || (buyOrSell == -1 && (price > stopPrice && stopPrice > 0))) {
            blankFields.append("· Something is wrong with the price, stop and take\n");
            Shake shake3 = new Shake(stopPriceField);
            Shake shake2 = new Shake(takePriceField);
            Shake shake = new Shake(priceField);
            shake3.playAnim();
            shake2.playAnim();
            shake.playAnim();
            flag = false;
        }


        if (!flag) {
            textInfoERROR.setText("");
            textInfoERROR.setText(blankFields.toString());
            return false;
        }

        textInfoERROR.setText("");
        textInfoERROR.setText(Lines.s4);
        try { Thread.sleep(2000);
        } catch (InterruptedException e) { e.printStackTrace(); }
        return true;
    }



    // создаем и заполняем обект стратегии
    private void createAndFillAnObject() {
        strategyObject = new StrategyObject();
        strategyObject.setPosition(Position.STARTED_POSITION);
        strategyObject.setFractionalParts(fractionalParts);
        strategyObject.setAmountOfCoins(numberOfCoins);
        strategyObject.setTrailingStop(trailingStop);
        strategyObject.setNameStrategy(nameStrategy);
        strategyObject.setTradingPair(tradingPair);
        strategyObject.setTakePrice(takePrice);
        strategyObject.setStopPrice(stopPrice);
        strategyObject.setBuyOrSell(buyOrSell);
        strategyObject.setOnOrOffFP(onOrOffFP);
        strategyObject.setOnOrOffTS(onOrOffTS);
        strategyObject.setPrice(price);
        strategyObject.setWorks(false);
        strategyObject.setClassID();
    }



    // добавляем торговый объект в коллекцию стратегий
    private void addTheFinishedObjectToTheListOfStrategies() {
        arraysOfStrategies.addToAllStrategyList(strategyObject, true);

    }
}

