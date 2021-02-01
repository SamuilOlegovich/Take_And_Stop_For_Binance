package main.model;

import main.controller.MainPageController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Clocks extends Thread{
    private MainPageController mainPageController;
    private DateFormat dateFormat;
    private boolean stopThreads;


    public Clocks(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.stopThreads = true;
    }


    @Override
    public void run() {
        dateFormat = new SimpleDateFormat("EEEE HH:mm:ss", Locale.ENGLISH);

        while (stopThreads) {
            mainPageController.serClocks(getDate());
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }


    private String getDate() {
        Date date = new Date();
        date.setTime(Agent.getDateDifference() > 0
                ? date.getTime() + (1000 * 60 * 60 * Math.abs(Agent.getDateDifference()))
                : date.getTime() - (1000 * 60 * 60 * Math.abs(Agent.getDateDifference())));
        dateFormat.format(date);
        return dateFormat.format(date);
    }


    public void stopThreads(boolean in) { stopThreads = in; }
}
