package main.model;


import main.view.ConsoleHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;



public class FilesAndPathCreator {
    private String pathSettingsAndStatus;
    private String pathAPIAndSecretKeys;
    private String pathLogs;


    public FilesAndPathCreator() {
        createdPath();
        createdFileLog();
        isTheFileInPlace();
        showPath();
    }



    private void createdPath() {
        String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String[] stringsSplit = path.split("/");
        path = stringsSplit[stringsSplit.length - 1];

        String[] strings = getClass().getResource("").getPath().split(path);
        String finish = strings[0].replaceAll("file:", "");


        if (System.getProperty("os.name").startsWith("Windows")) {
            finish = finish.replaceFirst("/", "").replaceAll("/", "\\\\");
        }

        if (strings.length == 2) {
            if (System.getProperty("os.name").startsWith("Windows")) {

                Path settingsAndStatus = Paths.get(finish + "SettingsAndStatus");
                // действия, если папка существует
                if (!Files.exists(settingsAndStatus)) {
                    try { Files.createDirectories(Paths.get("SettingsAndStatus")); }
                    catch (IOException e) { e.printStackTrace(); }
                }

                Path  apiAndSecretKeys = Paths.get(finish + "APIAndSecretKeys");
                // действия, если папка существует
                if (!Files.exists(apiAndSecretKeys)) {
                    try { Files.createDirectories(Paths.get("APIAndSecretKeys")); }
                    catch (IOException e) { e.printStackTrace(); }
                }

                Path logs = Paths.get(finish + "Logs");
                // действия, если папка существует
                if (!Files.exists(logs)) {
                    try { Files.createDirectories(Paths.get("Logs")); }
                    catch (IOException e) { e.printStackTrace(); }
                }

                pathLogs = finish + "Logs\\" + DatesTimes.getDateLogs().replaceAll(":", "-")
                        + " Log.txt";
                pathSettingsAndStatus = finish + "SettingsAndStatus\\SettingsAndStatus.txt";
                pathAPIAndSecretKeys = finish + "APIAndSecretKeys\\APIAndSecretKeys.txt";

            } else {

                Path settingsAndStatus = Paths.get(strings[0] + "SettingsAndStatus");
                // действия, если папка существует
                if (!Files.exists(settingsAndStatus)) {
                    try { Files.createDirectories(Paths.get("SettingsAndStatus")); }
                    catch (IOException e) { e.printStackTrace(); }
                }

                Path apiAndSecretKeys = Paths.get(strings[0] + "APIAndSecretKeys");
                // действия, если папка существует
                if (!Files.exists(apiAndSecretKeys)) {
                    try { Files.createDirectories(Paths.get("APIAndSecretKeys")); }
                    catch (IOException e) { e.printStackTrace(); }
                }

                Path logs = Paths.get(strings[0] + "Logs");
                // действия, если папка существует
                if (!Files.exists(logs)) {
                    try { Files.createDirectories(Paths.get("Logs")); }
                    catch (IOException e) { e.printStackTrace(); }
                }

                pathSettingsAndStatus = finish + "SettingsAndStatus/SettingsAndStatus.txt";
                pathAPIAndSecretKeys = finish + "APIAndSecretKeys/APIAndSecretKeys.txt";
                pathLogs = finish + "Logs/" + DatesTimes.getDateLogs() + " Log.txt";
            }
        } else {
            // создаст файлы в .../out/production/...
            // папки прийдется создать самому или дать разрешение на их создание
            // это при запуске программы с IDEA
            String string = getClass().getResource("").getPath()
                    .replaceAll("target/classes", "src/main")
                    .replaceAll("model/", "");

            pathSettingsAndStatus = string + "SettingsAndStatus/SettingsAndStatus.txt";
            pathAPIAndSecretKeys = string + "APIAndSecretKeys/APIAndSecretKeys.txt";
            pathLogs = string + "Logs/" + DatesTimes.getDateLogs() + "===Log.txt";
        }

        if (System.getProperty("os.name").startsWith("Windows")) {
            pathSettingsAndStatus = pathSettingsAndStatus
                    .replaceFirst("/", "").replaceAll("/", "\\\\");
            pathAPIAndSecretKeys = pathAPIAndSecretKeys
                    .replaceFirst("/", "").replaceAll("/", "\\\\");
            pathLogs = pathLogs
                    .replaceFirst("/", "").replaceAll("/", "\\\\");
        }
    }



    private void showPath() {
        System.out.println(DatesTimes.getDateTerminal() + " --- " + pathSettingsAndStatus);
        System.out.println(DatesTimes.getDateTerminal() + " --- " + pathAPIAndSecretKeys);
        System.out.println(DatesTimes.getDateTerminal() + " --- " + pathLogs);
    }



    private void isTheFileInPlace() {
        if (!Files.exists(Paths.get(pathSettingsAndStatus))) { createdFileSettingsAndStatus(); }
        if (!Files.exists(Paths.get(pathAPIAndSecretKeys))) { createdFileAPIAndSecretKeys(); }
    }



    private void createdFileSettingsAndStatus() {
        File file = new File(pathSettingsAndStatus);
        try {
            boolean newFile = file.createNewFile();
            System.out.println(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл SettingsAndStatus успешно создан.");
        } catch (IOException ex) {
            System.out.println(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл SettingsAndStatus.");
        }
    }



    private void createdFileAPIAndSecretKeys() {
        File file = new File(pathAPIAndSecretKeys);
        try {
            boolean newFile = file.createNewFile();
            System.out.println(DatesTimes.getDateTerminal() + " --- "
                    + "Новый файл для APIAndSecretKeys успешно создан.");
        } catch (IOException ex) {
            System.out.println(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать файл APIAndSecretKeys.");
        }
    }



    private void createdFileLog() {
        File file = new File(pathLogs);
        try {
            boolean newFile = file.createNewFile();
            System.out.println(DatesTimes.getDateTerminal() + " --- "
                    + "Новый Logs файл успешно создан.");
        } catch (IOException ex) {
            System.out.println(DatesTimes.getDateTerminal() + " --- "
                    + "Не удалось создать Logs файл.");
        }
    }


    public String getPathSettingsAndStatus() { return pathSettingsAndStatus; }
    public String getPathAPIAndSecretKeys() { return pathAPIAndSecretKeys; }
    public String getPathLogs() { return pathLogs; }
}
