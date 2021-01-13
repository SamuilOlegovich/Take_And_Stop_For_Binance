package main.model;

import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.model.binance.API;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceSymbol;
import main.model.binance.datatype.BinanceTicker;
import main.model.binance.websocket.WebSocketAdapterDepth;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Map;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (Agent.isYesOrNotAPIKey() == true) {
            Parent root = FXMLLoader.load(getClass().getResource("/main/view/main_start_controller.fxml"));
            primaryStage.setTitle("Take and Loss for Binance");
            primaryStage.setScene(new Scene(root, 1200, 700));
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/main/view/start_controller.fxml"));
            primaryStage.setTitle("Take and Loss for Binance");
            primaryStage.setScene(new Scene(root, 800, 500));
        }
        primaryStage.show();
    }


    public static void main(String[] args) {
        // считываеи все файлы настроек, ключей и состояний
        ReadKeysAndSettings readKeysAndSettings = new ReadKeysAndSettings();
        // Запускаем визуальную часть в зависимости от того что считалось из файлоф
        launch(args);
    }
}
