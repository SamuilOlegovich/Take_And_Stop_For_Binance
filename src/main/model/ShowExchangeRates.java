package main.model;

import main.controller.MainPageController;

import java.util.ArrayList;



public class ShowExchangeRates extends Thread {
    private MainPageController mainPageController;
    private final ArraysOfWebSockets webSockets;
    private final ArrayList<String> strings;
    private boolean stopThreads;


    public ShowExchangeRates(MainPageController mainPageController) {
        this.webSockets = Agent.getArraysOfWebSockets();
        this.mainPageController = mainPageController;
        this.strings = Agent.getViewPairSockets();
        this.stopThreads = true;
    }



    @Override
    public void run() {
        while (stopThreads) {
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
            mainPageController.setExchangeRates(getStingExchangeRates());
        }
    }

    private String getStingExchangeRates() {
        StringBuilder out = new StringBuilder("");

        for (String s : strings) { out.append(s).append(" -> ").append(webSockets.getPriceNow(s)).append("   "); }

        if (out.length() > 10) { out.delete(out.length() - 3, out.length()); }
        return out.toString();
    }


    public void stopThreads(boolean in) { stopThreads = in; }
}

