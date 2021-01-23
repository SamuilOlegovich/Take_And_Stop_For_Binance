package main.model;

import java.util.ArrayList;
import java.util.Objects;


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
        try { inList.addAll(writerAndReadFile.readFile(filesAndPathCreator.getPathSettingsAndStatus()));
        } catch (Exception e) { writeKeysAndSettings.enterPatternForSettingsAndStates(); }

        if (inList.size() < 1 || !inList.get(0).equals(Enums.SETTINGS.toString())
                || !inList.get(inList.size() - 1).equals(Enums.END.toString())
                || !inList.contains(Enums.STATUS.toString())) {
            writeKeysAndSettings.enterPatternForSettingsAndStates();
            inList.clear();
            return;
        }

        ArrayList<ArrayList<String>> listStrategy = new ArrayList<>(Objects.requireNonNull(getStrategy(inList)));
        ArrayList<String> listSettings = new ArrayList<>(Objects.requireNonNull(getSettings(inList)));

        if (listStrategy.size() > 0) { new DecipherAndCreateStrategies(listStrategy);
        } else { writeKeysAndSettings.enterPatternForSettingsAndStates(); }
        if (listSettings.size() > 0) { new DecryptAndCustomize(listSettings);
        } else { writeKeysAndSettings.enterPatternForSettingsAndStates(); }

        listStrategy.clear();
        listSettings.clear();
        inList.clear();
    }


    // получаем лист строк с стратегиями без всякого разделительного мусора
    private ArrayList<ArrayList<String>> getStrategy(ArrayList<String> in) {
        ArrayList<ArrayList<String>> outList = new ArrayList<>();
        ArrayList<String> statusList = new ArrayList<>();
        ArrayList<String> inList = new ArrayList<>(in);

        int index = inList.indexOf(Enums.STATUS.toString());

        for (int i = index; i >= 0; i--) { inList.remove(i); }

        for (String s : inList) {
//            System.out.println(s);
            if (!s.equals(Enums.STATUS.toString()) && !s.equals(Enums.NEXT.toString()) && !s.equals(Enums.END.toString())) {
                statusList.add(s);
            } else if (s.equals(Enums.NEXT.toString())) {
                outList.add(statusList);
                statusList = new ArrayList<>();
            } else if (s.equals(Enums.END.toString())) {
                outList.add(statusList);
                break;
            }
        }

        if (outList.size() > 0) {
//            statusList.clear();
            inList.clear();
            return outList;
        }
        return null;
    }


    // получаем лист строк с настройками без всякого разделительного мусора
    private ArrayList<String> getSettings(ArrayList<String> in) {
        ArrayList<String> inList = new ArrayList<>(in);
        ArrayList<String> outList = new ArrayList<>();
        for (String s : inList) {
            if (!s.equals(Enums.SETTINGS.toString()) && !s.equals(Enums.STATUS.toString())) outList.add(s);
            if (s.equals(Enums.STATUS.toString())) break;
        }
        if (outList.size() > 0) {
            inList.clear();
            return outList;
        }
        return null;
    }



    private void readAPIAndSecretKeys() {
        ArrayList<String> arrayList = new ArrayList<>();
        try { arrayList.addAll(writerAndReadFile.readFile(filesAndPathCreator.getPathAPIAndSecretKeys())); }
        catch (Exception e) { writeKeysAndSettings.writePatternForKeys(); }

        if (arrayList.size() >= 4 && arrayList.get(0).equals(Enums.START.toString())
                && arrayList.get(arrayList.size() - 1).equals(Enums.END.toString())) {
            String start = arrayList.get(0).trim();
            String APIkey = arrayList.get(1).trim();
            String SecretKey = arrayList.get(2).trim();
            String end = arrayList.get(3).trim();


            if (start.equals(Enums.START.toString()) && end.equals(Enums.END.toString())) {
                if (APIkey.startsWith(APIandSecretKeys.API_KEY.toString())) {
                    String[] keys = APIkey.split(Lines.delimiter);
                    if (keys.length == 2) {
                        api.setAPI_KEY(keys[1]);
                    } else {
                        api.setAPI_KEY("NULL");

                    }
                }
                if (SecretKey.startsWith(APIandSecretKeys.SECRET_KEY.toString())) {
                    String[] secret = SecretKey.split(Lines.delimiter);
                    if (secret.length == 2) {
                        api.setSECRET_KEY(secret[1]);
                    } else {
                        api.setSECRET_KEY("NULL");
                    }
                }
            }
        }

        if (api.getAPI_KEY().length() > 20 && api.getSECRET_KEY().length() > 20)
            Agent.setYesOrNotAPIKey(true);
        else writeKeysAndSettings.writePatternForKeys();
        arrayList.clear();
    }
}
