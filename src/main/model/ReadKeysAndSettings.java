package main.model;

import java.util.ArrayList;



public class ReadKeysAndSettings {
    private WriteKeysAndSettings writeKeysAndSettings;
    private FilesAndPathCreator filesAndPathCreator;
    private WriterAndReadFile writerAndReadFile;
    private API api;


    public ReadKeysAndSettings() {
        this.writeKeysAndSettings = Agent.getWriteKeysAndSettings();
        this.filesAndPathCreator = Agent.getFilesAndPathCreator();
        this.writerAndReadFile = Agent.getWriterAndReadFile();
        this.api = Agent.getApi();
        readAPIAndSecretKeys();
        readSettingsAndStatus();
    }



    private void readSettingsAndStatus() {
        ArrayList<String> inList = new ArrayList<>();
        try {
            inList.addAll(writerAndReadFile.readFile(filesAndPathCreator.getPathSettingsAndStatus()));
        } catch (Exception e) { writeKeysAndSettings.enterPatternForSettingsAndStates(); }

        if (inList.size() < 1 || !inList.get(0).equals(Enums.SETTINGS.toString())
                || !inList.get(inList.size() - 1).equals(Enums.END)
                || !inList.contains(Enums.STATUS)) {
            writeKeysAndSettings.enterPatternForSettingsAndStates();
            inList.clear();
            return;
        }

        ArrayList<ArrayList<String>> listStrategy = getStrategy(inList);
        ArrayList<String> listSettings = getSettings(inList);
        if (listStrategy == null) {
            writeKeysAndSettings.enterPatternForSettingsAndStates();
        } else {
            new DecipherAndCreateStrategies(listStrategy);
        }
        if (listSettings == null) {
            writeKeysAndSettings.enterPatternForSettingsAndStates();
        } else {
            new DecryptAndCustomize(listSettings);
        }
        listSettings.clear();
        listStrategy.clear();
        inList.clear();
    }


    // получаем лист строк с стратегиями без всякого разделительного мусора
    private ArrayList<ArrayList<String>> getStrategy(ArrayList<String> inList) {
        ArrayList<ArrayList<String>> outList = new ArrayList<>();
        ArrayList<String> statusList = new ArrayList<>();
        int index = inList.indexOf(Enums.STATUS.toString());
        for (int i = index; i >= 0; i--) {
            inList.remove(i);
        }
        for (String s : inList) {
            if (!s.equals(Enums.STATUS.toString())
                    && !s.equals(Enums.NEXT.toString())
                    && !s.equals(Enums.END.toString())) {
                statusList.add(s);
            } else if (s.equals(Enums.NEXT)) {
                outList.add(statusList);
                statusList = new ArrayList<>();
            } else {
                outList.add(statusList);
                break;
            }
        }
        if (outList.size() > 0) return outList;
        return null;
    }


    // получаем лист строк с настройками без всякого разделительного мусора
    private ArrayList<String> getSettings(ArrayList<String> inList) {
        ArrayList<String> outList = new ArrayList<>();
        for (String s : inList) {
            if (!s.equals(Enums.SETTINGS) && !s.equals(Enums.STATUS)) outList.add(s);
            if (s.equals(Enums.STATUS)) break;
        }
        if (outList.size() > 0) return outList;
        return null;
    }



    private void readAPIAndSecretKeys() {
        ArrayList<String> arrayList = new ArrayList<>();
        try { arrayList.addAll(writerAndReadFile.readFile(filesAndPathCreator.getPathAPIAndSecretKeys())); }
        catch (Exception e) { writeKeysAndSettings.enterPatternForKeys(); }
        if (arrayList.size() >= 4) {
            String start = arrayList.get(0).trim();
            String APIkey = arrayList.get(1).trim();
            String SecretKey = arrayList.get(2).trim();
            String end = arrayList.get(3).trim();
            if (start.equals(Enums.START.toString()) && end.equals(Enums.END.toString())) {
                if (APIkey.startsWith(APIandSecretKeys.API_KEY.toString())) {
                    api.setAPI_KEY(APIkey.split("===")[1]);
                }
                if (SecretKey.startsWith(APIandSecretKeys.SECRET_KEY.toString())) {
                    api.setSECRET_KEY(SecretKey.split("===")[1]);
                }
            }
        }
        if (api.getAPI_KEY().length() > 20 && api.getSECRET_KEY().length() > 20)
            Agent.setYesOrNotAPIKey(true);
        else writeKeysAndSettings.enterPatternForKeys();
        arrayList.clear();
    }
}
