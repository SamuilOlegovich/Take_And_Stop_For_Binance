package main.controller;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.model.*;



public class InfoPageController {
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
    private Button okButton;

    @FXML
    private Button backButton;

    @FXML
    private Text tradingPairField;

    @FXML
    private Text numberOfCoinsField;

    @FXML
    private Text nameStrategyField;

    @FXML
    private RadioButton buyRadioButton;

    @FXML
    private RadioButton sellRadioButton;

    @FXML
    private Text priceField;

    @FXML
    private RadioButton higherButton;

    @FXML
    private RadioButton lowerButton;

    @FXML
    private Text takePriceField;

    @FXML
    private Text stopPriceField;

    @FXML
    private Text fractionalPartsField;

    @FXML
    private RadioButton onFPRadioButton;

    @FXML
    private RadioButton offFPRadioButton;

    @FXML
    private Text trailingStopField;

    @FXML
    private RadioButton onTSRadioButton;

    @FXML
    private RadioButton offTSRadioButton;




    @FXML
    void initialize() {
        arraysOfStrategies = Agent.getArraysOfStrategies();
        // получаем объект из которого надо получить все данные
        getAnObjectAndDataFromIt();
        // установка группы радио кнопок и т д
        setGroupsOfRadioButtons();
        // Вывести все имеющиеся данные в поля
        displayAllAvailableDataInFields();


        buyRadioButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        sellRadioButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        higherButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        lowerButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        onTSRadioButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        offTSRadioButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        onFPRadioButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        offFPRadioButton.setOnAction(event -> {
            setGroupsOfRadioButtons();
        });

        okButton.setOnAction(event -> {
                String stringID = strategyObject.getClassID();
                arraysOfStrategies.findStrategy(stringID);
                openNewScene("/main/view/edit.fxml");
        });

        backButton.setOnAction(event -> {
            openNewScene("/main/view/main.fxml");
        });
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
        try { fxmlLoader.load(); }
        catch (IOException e) { e.printStackTrace(); }

        Parent parent = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
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



    private void setGroupsOfRadioButtons() {
        ToggleGroup groupBuyOrSel = new ToggleGroup();
        buyRadioButton.setToggleGroup(groupBuyOrSel);
        sellRadioButton.setToggleGroup(groupBuyOrSel);
        if (buyOrSell == 1) buyRadioButton.setSelected(true);
        else if (buyOrSell == -1) sellRadioButton.setSelected(true);

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



    private void displayAllAvailableDataInFields() {
        tradingPairField.setText("Trading pair  -> " + strategyObject.getTradingPair());

        nameStrategyField.setText("Name strategy -> " + strategyObject.getNameStrategy());

        numberOfCoinsField.setText("Amount of coins -> " + (numberOfCoins != -1 ? numberOfCoins.toString() : ""));
        trailingStopField.setText("Trailing stop -> " + (trailingStop != -1 ? trailingStop.toString() : ""));
        takePriceField.setText("Take price -> " + (takePrice != -1 ? takePrice.toString() : ""));
        stopPriceField.setText("Stop price -> " + (stopPrice != -1 ? stopPrice.toString() : ""));
        priceField.setText("Price -> " + (price != -1 ? price.toString() : ""));

        fractionalPartsField.setText("Fractional parts -> " + (fractionalParts != -1 ? fractionalParts + "" : ""));
    }
}





