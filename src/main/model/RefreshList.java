package main.model;

import main.controller.MainPageController;

import java.util.ArrayList;

public class RefreshList extends Thread {
    private final CreatesTemplatesAndData createsTemplatesAndData;
    private final MainPageController mainPageController;

    public RefreshList() {
        this.createsTemplatesAndData = Agent.getCreatesTemplatesAndData();
        this.mainPageController = Agent.getMainPageController();
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
        mainPageController.updateListView(listOut);
    }
}
