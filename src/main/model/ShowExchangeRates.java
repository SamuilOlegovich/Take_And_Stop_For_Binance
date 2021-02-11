package main.model;

import main.controller.MainController;

import java.util.ArrayList;



public class ShowExchangeRates extends Thread {
    private MainController mainController;
    private final ArraysOfWebSockets webSockets;
    private final ArrayList<String> strings;
    private boolean stopThreads;


    public ShowExchangeRates(MainController mainController) {
        this.webSockets = Agent.getArraysOfWebSockets();
        this.mainController = mainController;
        this.strings = Agent.getViewPairSockets();
        this.stopThreads = true;
    }



    @Override
    public void run() {
        while (stopThreads) {
            try { Thread.sleep(1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
            mainController.setExchangeRates(getStingExchangeRates());
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

