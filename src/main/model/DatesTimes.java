package main.model;

import main.view.ConsoleHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatesTimes {
    public static String getDateTerminal() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);
        if (Agent.getDateDifference() != 0) {
            date.setTime(Agent.getDateDifference() > 0
                    ? date.getTime() + (1000 * 60 * 60 * Math.abs(Agent.getDateDifference()))
                    : date.getTime() - (1000 * 60 * 60 * Math.abs(Agent.getDateDifference())));
        } else {
            date.setTime(date.getTime());
        }
        return dateFormat.format(date);
    }


    public static long getDateTerminalLong() {
        Date date = new Date();
        if (Agent.getDateDifference() != 0) {
            date.setTime(Agent.getDateDifference() > 0
                    ? date.getTime() + (1000 * 60 * 60 * Math.abs(Agent.getDateDifference()))
                    : date.getTime() - (1000 * 60 * 60 * Math.abs(Agent.getDateDifference())));
        } else {
            date.setTime(date.getTime());
        }
        return date.getTime();
    }


    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);
        return dateFormat.format(date);
    }


    public static String getDateLogs() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);
        return dateFormat.format(date);
    }



    public static synchronized Date getDate(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateFromString = null;

        if (string.startsWith("\"") && string.endsWith("\"")) {
            String stringIn = string.replaceAll("\"", "").replace("time: ", "");

            try {
                dateFromString = simpleDateFormat.parse(stringIn);
            } catch (Exception e) {
                ConsoleHelper.writeMessage("неверный формат даты --- " + stringIn);
            }
            return dateFromString;

        } else {

            try {
                dateFromString = simpleDateFormat.parse(string);
            } catch (Exception e) {
                ConsoleHelper.writeMessage("неверный формат даты --- " + string);
            }

            return dateFromString;
        }
    }
}
