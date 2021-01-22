package main.model;

import java.util.ArrayList;

public class DecryptAndCustomize {
    private ArrayList<String> listSettings;



    public DecryptAndCustomize(ArrayList<String> inList) {
        this.listSettings = new ArrayList<>(inList);
        parseAndSetting();
    }



    private void parseAndSetting() {
        for (String s : listSettings) {
            if (s.startsWith(Enums.DATE_DIFFERENCE.toString())) {
                String value = s.split(Lines.delimiter)[1];
                Agent.setDateDifference(Integer.parseInt(value));
            }
        }
    }

    /*SETTINGS
        DATE_DIFFERENCE===0
        STATUS
        */
}
