package main.model;

import main.controller.MainPageController;

public class WriteKeysAndSettings {
    private final CreatesTemplatesAndData createsTemplatesAndData;
    private final FilesAndPathCreator filesAndPathCreator;
    private final WriterAndReadFile writerAndReadFile;
    private final API api;



    public WriteKeysAndSettings() {
        this.createsTemplatesAndData = Agent.getCreatesTemplatesAndData();
        this.filesAndPathCreator = Agent.getFilesAndPathCreator();
        this.writerAndReadFile = Agent.getWriterAndReadFile();
        this.api = Agent.getApi();
    }



    // записать новые ключи
    public void writeNewKeys() {
        Agent.setYesOrNotAPIKey(true);
        String string = Enums.START.toString() + Lines.newline
                + APIandSecretKeys.API_KEY.toString() + Lines.delimiter + api.getAPI_KEY() + Lines.newline
                + APIandSecretKeys.SECRET_KEY.toString() + Lines.delimiter + api.getSECRET_KEY() + Lines.newline
                + Enums.END.toString() + Lines.newline;
        writerAndReadFile.writerFile(string, filesAndPathCreator.getPathAPIAndSecretKeys(), false);
    }



    // вписать шаблон для ключей
    public void writePatternForKeys() {
        Agent.setYesOrNotAPIKey(false);
        writerAndReadFile.writerFile(createsTemplatesAndData.getPatternForKeys(),
                filesAndPathCreator.getPathAPIAndSecretKeys(),false);
    }



    // вписать шаблон настроек и состояния
    public void enterPatternForSettingsAndStates() {
        writerAndReadFile.writerFile(createsTemplatesAndData.getStringPatternForSettingsAndStates(),
                filesAndPathCreator.getPathSettingsAndStatus(),false);
    }



    // вписать новые настройки и состояния
    public void writeNewSettingsAndStates() {
        writerAndReadFile.writerFile(createsTemplatesAndData.getRealSettingsAndStates(),
                filesAndPathCreator.getPathSettingsAndStatus(),false);
    }
}
