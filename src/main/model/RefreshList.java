package main.model;

import main.controller.MainController;

import java.util.ArrayList;

public class RefreshList extends Thread {
    private final CreatesTemplatesAndData createsTemplatesAndData;
    private final MainController mainController;

    public RefreshList() {
        this.createsTemplatesAndData = Agent.getCreatesTemplatesAndData();
        this.mainController = Agent.getMainPageController();
        start();
    }

    @Override
    public void run() {
        ArrayList<String> listOut = new ArrayList<>();
        listOut.add(Lines.tableOfContents);
        listOut.add(Enums.ON_LINE_STRATEGY.toString());
        listOut.addAll(createsTemplatesAndData.getTradedStrategyList());
        listOut.add(Enums.OFF_LINE_STRATEGY.toString());
        listOut.addAll(createsTemplatesAndData.getStoppedStrategyList());
        mainController.updateListView(listOut);
    }
}
