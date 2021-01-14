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
import main.model.SettingsAndStatus;
import javafx.scene.text.Text;
import main.model.WedgeLines;


public class SettingPageController {
    private SettingsAndStatus settingsAndStatus;

    private String nameStrategy;
    private String tradingPair;

    private Double numberOfCoins;
    private Double trailingStop;
    private Double takePrice;
    private Double stopPrice;
    private Double price;

    private int fractionalParts;
    private int buyOrSell;
    private int onOrOffTS;
    private int onOrOffFP;


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
        ObservableList<String> observableList = FXCollections.observableArrayList(Agent.getListAllCoinPair());
        listViewInSettingPage.setItems(observableList);

        // установка группы
        ToggleGroup groupBuyOrSel = new ToggleGroup();
        buyRadioButton.setToggleGroup(groupBuyOrSel);
        sellRadioButton.setToggleGroup(groupBuyOrSel);

        ToggleGroup groupOnOrOffFp = new ToggleGroup();
        onFPRadioButton.setToggleGroup(groupOnOrOffFp);
        offFPRadioButton.setToggleGroup(groupOnOrOffFp);

        ToggleGroup groupOnOrOffTs = new ToggleGroup();
        onTSRadioButton.setToggleGroup(groupOnOrOffTs);
        offTSRadioButton.setToggleGroup(groupOnOrOffTs);

        buyOrSell = 0;
        onOrOffTS = 0;
        onOrOffFP = 0;

        buyRadioButton.setOnAction(event -> {
            buyOrSell = 1;
        });

        sellRadioButton.setOnAction(event -> {
            buyOrSell = -1;
        });

        okButton.setOnAction(event -> {
            collectAndProcessData();
            if (checkIfEverythingIsOk()) {
                createAndFillAnObject();
                addTheFinishedObjectToTheListOfStrategies();
                openNewScene("/main/view/main_page.fxml");
            }

            textInfoERROR.setText("nhfv nfhf nfhf nfhf nfrjdjkjj wkjcwcrkcu fkvevhebvhbvd khevhbewivbeiw kjewnvjnewivbids");





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


    private boolean checkIfEverythingIsOk() {
        String blankFields;





        textInfoERROR.setText(WedgeLines.s4);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }



    private void createAndFillAnObject() {
        settingsAndStatus = new SettingsAndStatus();
    }


    private void addTheFinishedObjectToTheListOfStrategies() {

    }
}

