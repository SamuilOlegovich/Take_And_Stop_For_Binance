package main.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.A;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (Agent.isYesOrNotAPIKey() == true) {
            Parent root = FXMLLoader.load(getClass().getResource("/main/view/main.fxml"));
            primaryStage.setTitle("Take and Loss for Binance");
            primaryStage.setScene(new Scene(root, 1200, 700));
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/main/view/authorization.fxml"));
            primaryStage.setTitle("Take and Loss for Binance");
            primaryStage.setScene(new Scene(root, 800, 500));
        }
        primaryStage.show();
    }


    public static void main(String[] args) {
        Agent.setApi(new API());
        // создаем класс хранения всех стратегий
        Agent.setArraysOfStrategies(new ArraysOfStrategies());
        Agent.setCreatesTemplatesAndData(new CreatesTemplatesAndData());
        // создаем все нужные папки и путя к ним если это надо
        Agent.setFilesAndPathCreator(new FilesAndPathCreator());
        Agent.setWriterAndReadFile(new WriterAndReadFile());
        Agent.setWriteKeysAndSettings(new WriteKeysAndSettings());
        // считываеи все файлы настроек, ключей и состояний
        Agent.setReadKeysAndSettings(new ReadKeysAndSettings());
        // Запускаем визуальную часть в зависимости от того что считалось из файлоф
        launch(args);
    }
}
