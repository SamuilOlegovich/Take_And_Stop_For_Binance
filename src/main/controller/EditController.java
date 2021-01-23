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




public class EditController {
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
    private int buyOrSell;

    private boolean lowerOrHigher;
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
        arraysOfStrategies = Agent.getArraysOfStrategies();
        // получаем и выводим список торговых пар
        getAListOfTradingPairs();
        // получаем объект из которого надо получить все данные
        getAnObjectAndDataFromIt();
        // установка группы радио кнопок и т д
        setGroupsOfRadioButtons();
        // Вывести все имеющиеся данные в поля
        displayAllAvailableDataInFields();


        buyRadioButton.setOnAction(event -> {
            buyOrSell = 1;
        });

        sellRadioButton.setOnAction(event -> {
            buyOrSell = -1;
        });

        higherButton.setOnAction(event -> {
            lowerOrHigher = true;
        });

        lowerButton.setOnAction(event -> {
            lowerOrHigher = false;
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

        okButton.setOnAction(event -> {
            collectAndProcessData();
            if (checkIfEverythingIsOk()) {
                createAndFillAnObject();
                replaceStrategy();
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



    private void displayAllAvailableDataInFields() {
        tradingPairField.undo();
        tradingPairField.insertText(0, getSelectedItem());

        nameStrategyField.undo();
        nameStrategyField.insertText(0, nameStrategy);
        tradingPairField.undo();
        tradingPairField.insertText(0, tradingPair);

        numberOfCoinsField.undo();
        numberOfCoinsField.insertText(0, numberOfCoins != -1 ? numberOfCoins.toString() : "");
        trailingStopField.undo();
        trailingStopField.insertText(0, trailingStop != -1 ? trailingStop.toString() : "");
        takePriceField.undo();
        takePriceField.insertText(0, takePrice != -1 ? takePrice.toString() : "");
        stopPriceField.undo();
        stopPriceField.insertText(0, stopPrice != -1 ? stopPrice.toString() : "");
        priceField.undo();
        priceField.insertText(0, price != -1 ? price.toString() : "");

        fractionalPartsField.undo();
        fractionalPartsField.insertText(0, fractionalParts != -1 ? fractionalParts + "" : "");
    }

    private void setGroupsOfRadioButtons() {
        ToggleGroup groupBuyOrSel = new ToggleGroup();
        buyRadioButton.setToggleGroup(groupBuyOrSel);
        sellRadioButton.setToggleGroup(groupBuyOrSel);
        if (buyOrSell == 1) buyRadioButton.setSelected(true);
        else sellRadioButton.setSelected(true);

        ToggleGroup groupLowerOrHigher = new ToggleGroup();
        lowerButton.setToggleGroup(groupLowerOrHigher);
        higherButton.setToggleGroup(groupLowerOrHigher);
        if (lowerOrHigher) higherButton.setSelected(true);
        else lowerButton.setSelected(true);

        ToggleGroup groupOnOrOffFp = new ToggleGroup();
        onFPRadioButton.setToggleGroup(groupOnOrOffFp);
        offFPRadioButton.setToggleGroup(groupOnOrOffFp);
        if (onOrOffFP) onFPRadioButton.setSelected(true);
        else offFPRadioButton.setSelected(true);

        ToggleGroup groupOnOrOffTs = new ToggleGroup();
        onTSRadioButton.setToggleGroup(groupOnOrOffTs);
        offTSRadioButton.setToggleGroup(groupOnOrOffTs);
        if (onOrOffTS) onTSRadioButton.setSelected(true);
        else offTSRadioButton.setSelected(true);
    }


    private void openNewScene(String in) {
        String window = new String(in);
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
        stage.show();
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

        if (nameStrategyText.length() >= 1) { nameStrategy = nameStrategyText; }
        else { nameStrategy = null; }

        if (numberOfCoinsText.length() >= 1) { numberOfCoins = Double.parseDouble(numberOfCoinsText); }
        else { numberOfCoins = -1.0; }

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
        } else { fractionalParts = -1; }

        if (trailingStopText.length() > 3) { trailingStop = Double.parseDouble(trailingStopText); }
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
        if (trailingStop == -1 && onOrOffTS) {
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



    private void getAListOfTradingPairs() {
        ObservableList<String> observableList = FXCollections.observableArrayList(Agent.getAllCoinPairList());
        listViewInSettingPage.setItems(observableList);
    }



    // создаем и заполняем обект стратегии
    private void createAndFillAnObject() {
        strategyObject.setFractionalParts(fractionalParts);
        strategyObject.setAmountOfCoins(numberOfCoins);
        strategyObject.setNameStrategy(nameStrategy);
        strategyObject.setTrailingStop(trailingStop);
        strategyObject.setTradingPair(tradingPair);
        strategyObject.setTakePrice(takePrice);
        strategyObject.setStopPrice(stopPrice);
        strategyObject.setBuyOrSell(buyOrSell);
        strategyObject.setOnOrOffFP(onOrOffFP);
        strategyObject.setOnOrOffTS(onOrOffTS);
        strategyObject.setPrice(price);
    }


    // Заменить стратегию
    private void replaceStrategy() {
        arraysOfStrategies.replaceStrategy(strategyObject);
    }



    // получить объект и данные из него
    private void getAnObjectAndDataFromIt() {
        strategyObject = Agent.getArraysOfStrategies().getStrategySettingAndStatus();
        if (strategyObject != null) {
            nameStrategy = strategyObject.getNameStrategy();
            tradingPair = strategyObject.getTradingPair();

            numberOfCoins = strategyObject.getAmountOfCoins();
            trailingStop = strategyObject.getTrailingStop();
            takePrice = strategyObject.getTakePrice();
            stopPrice = strategyObject.getStopPrice();
            price = strategyObject.getPrice();

            lowerOrHigher = strategyObject.isLowerOrHigherPrices();
            fractionalParts = strategyObject.getFractionalParts();
            buyOrSell = strategyObject.getBuyOrSell();
            onOrOffFP = strategyObject.getOnOrOffFP();
            onOrOffTS = strategyObject.getOnOrOffTS();
        }
    }
}
