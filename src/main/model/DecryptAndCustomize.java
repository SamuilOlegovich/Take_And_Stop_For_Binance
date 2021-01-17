package main.model;

import java.util.ArrayList;

public class DecryptAndCustomize {
    private ArrayList<String> listSettings;

    public DecryptAndCustomize(ArrayList<String> inList) {
        this.listSettings = new ArrayList<>(inList);
    }

    private void parseAndSetting() {
        for (String s : listSettings) {
            String name = s.split(Lines.delimiter)[0];
            String value = s.split(Lines.delimiter)[1];
            if (name.equals(Enums.DATE_DIFFERENCE.toString())) Agent.setDateDifference(Integer.parseInt(value));
        }
    }

    /*SETTINGS
        DATE_DIFFERENCE===0
        STATUS
        */
}
