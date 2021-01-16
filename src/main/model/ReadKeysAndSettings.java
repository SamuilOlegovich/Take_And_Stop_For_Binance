package main.model;

import java.util.ArrayList;

public class ReadKeysAndSettings {
    private WriterAndReadFile writerAndReadFile;



    public ReadKeysAndSettings() {
        this.writerAndReadFile = new WriterAndReadFile();
        Agent.setWriterAndReadFile(writerAndReadFile);
        readAPIAndSecretKeys();
        readSettingsAndStatus();
    }



    private void readSettingsAndStatus() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            arrayList.addAll(writerAndReadFile.readFile(Agent.getFilesAndPathCreator().getPathSettingsAndStatus()));
        } catch (Exception e) {
            enterPatternForSettingsAndStates();
        }
        if (arrayList.size() < 1) {
            enterPatternForSettingsAndStates();
        }

        /////////
        /////////
        /////////

        arrayList.clear();
    }


    // вписать шаблон настроек и состояния
    private void enterPatternForSettingsAndStates() {
        String string = Enums.SETTINGS + "\n"
                + Enums.DATE_DIFFERENCE + "===" + Agent.getDateDifference() + "\n"
                + Enums.STATUS + "\n"
                + Enums.ID + "===" + Enums.IF_IT_WAS_NOT_CREATED_THROUGH_THE_PROGRAM_THEM_WE_WRITE_DONE_BY_HAND + "\n"
                + Enums.WORKS + "===" + Enums.FALSE + "\n"
                + Enums.TRADING_PAIR + "===ETHBTC\n"
                + Enums.BUY_OR_SELL + "===" + Enums.BUY + "\n"
                + Enums.AMOUNT_OF_COINS + "===12.890\n"
                + Enums.PRICE + "===0.03498738\n"
                + Enums.TAKE_PRICE + "===0.04889889\n"
                + Enums.STOP_PRICE + "===0.03389889\n"
                + Enums.TRAILING_STOP + "===3.7\n"
                + Enums.ON_OR_OFF_TRAILING_STOP + "===" + Enums.OFF + "\n"
                + Enums.FRACTIONAL_PARTS + "===5\n"
                + Enums.ON_OR_OFF_FRACTIONAL_PARTS + "===" + Enums.OFF + "\n"
                + Enums.BUY_OR_SELL_COINS + "===123.608\n"
                + Enums.POSITION + "===" + Position.TAKE_OR_STOP_POSITION + "\n"
                + Enums.NAME_STRATEGY + "===" + Enums.AN_EXAMPLE_OF_CREATING_AND_FILLING_IN_STRATEGIES_IN_MANUAL_MODE_IN_A_FILE + "\n"
                + Enums.NEXT + "\n"
                + Enums.ID + "===" + Enums.IF_IT_WAS_NOT_CREATED_THROUGH_THE_PROGRAM_THEM_WE_WRITE_DONE_BY_HAND + "\n"
                + Enums.WORKS + "===" + Enums.TRUE + "\n"
                + Enums.TRADING_PAIR + "===LTCBTC\n"
                + Enums.BUY_OR_SELL + "===" + Enums.SELL + "\n"
                + Enums.AMOUNT_OF_COINS + "===0.890\n"
                + Enums.PRICE + "===0.00349873\n"
                + Enums.TAKE_PRICE + "===0.00304889\n"
                + Enums.STOP_PRICE + "===0.03589889\n"
                + Enums.TRAILING_STOP + "===0.7\n"
                + Enums.ON_OR_OFF_TRAILING_STOP + "===" + Enums.OFF + "\n"
                + Enums.FRACTIONAL_PARTS + "===0\n"
                + Enums.ON_OR_OFF_FRACTIONAL_PARTS + "===" + Enums.OFF + "\n"
                + Enums.BUY_OR_SELL_COINS + "===0.0\n"
                + Enums.POSITION + "===" + Position.STARTED_POSITION + "\n"
                + Enums.NAME_STRATEGY + "===" + Enums.AN_EXAMPLE_OF_CREATING_AND_FILLING_IN_STRATEGIES_IN_MANUAL_MODE_IN_A_FILE + "\n"
                + Enums.END.toString() + "\n";
        writerAndReadFile.writerFile(string, Agent.getFilesAndPathCreator().getPathSettingsAndStatus(),false);
    }



    private void readAPIAndSecretKeys() {
        ArrayList<String> arrayList = new ArrayList<>();
        try { arrayList.addAll(writerAndReadFile.readFile(Agent.getFilesAndPathCreator().getPathAPIAndSecretKeys())); }
        catch (Exception e) { enterPatternForKeys(); }
        if (arrayList.size() >= 4) {
            String start = arrayList.get(0).trim();
            String APIkey = arrayList.get(1).trim();
            String SecretKey = arrayList.get(2).trim();
            String end = arrayList.get(3).trim();
            if (start.equals(Enums.START.toString()) && end.equals(Enums.END.toString())) {
                if (APIkey.startsWith(APIandSecretKeys.API_KEY.toString())) {
                    Agent.getApi().setAPI_KEY(APIkey.split("===")[1]);
                }
                if (SecretKey.startsWith(APIandSecretKeys.SECRET_KEY.toString())) {
                    Agent.getApi().setSECRET_KEY(SecretKey.split("===")[1]);
                }
            }
        }
        if (Agent.getApi().getAPI_KEY().length() > 20 && Agent.getApi().getSECRET_KEY().length() > 20)
            Agent.setYesOrNotAPIKey(true);
        else enterPatternForKeys();
        arrayList.clear();
    }



    // вписать шаблон для ключей
    public void enterPatternForKeys() {
        Agent.setYesOrNotAPIKey(false);
        String string = Enums.START.toString() + "\n"
                + APIandSecretKeys.API_KEY.toString() + "===\n"
                + APIandSecretKeys.SECRET_KEY.toString() + "===\n"
                + Enums.END.toString() + "\n";
        writerAndReadFile.writerFile(string, Agent.getFilesAndPathCreator().getPathAPIAndSecretKeys(),false);
    }



    // записать новые ключи
    public void writeNewKeys() {
        Agent.setYesOrNotAPIKey(true);
        String string = Enums.START.toString() + "\n"
                + APIandSecretKeys.API_KEY.toString() + "===" + Agent.getApi().getAPI_KEY() + "\n"
                + APIandSecretKeys.SECRET_KEY.toString() + "===" + Agent.getApi().getSECRET_KEY() + "\n"
                + Enums.END.toString() + "\n";
        writerAndReadFile.writerFile(string, Agent.getFilesAndPathCreator().getPathAPIAndSecretKeys(), false);
    }
}
