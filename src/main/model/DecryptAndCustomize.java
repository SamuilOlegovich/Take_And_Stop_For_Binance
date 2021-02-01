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
                Agent.setDateDifference(Integer.parseInt(s.split(Lines.delimiter)[1]));
            } else if (s.startsWith(Enums.NUMBER_OF_ATTEMPTS_TO_EXECUTE_A_TRADE.toString())) {
                Agent.setNumberOfAttemptsToExecuteTrade(Integer.parseInt(s.split(Lines.delimiter)[1]));
            }
        }
    }
}
