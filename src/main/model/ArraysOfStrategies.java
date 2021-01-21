package main.model;

import main.controller.MainPageController;

import java.util.ArrayList;



public class ArraysOfStrategies {
    private final ArrayList<StrategyObject> stoppedStrategyList;
    private final ArrayList<StrategyObject> tradedStrategyList;

    private final ArraysOfWebSockets arraysOfWebSockets;
    private WriteKeysAndSettings writeKeysAndSettings;
    private MainPageController mainPageController;
    private StrategyObject strategyObject;



    public ArraysOfStrategies() {
        this.arraysOfWebSockets = Agent.getArraysOfWebSockets();
        this.stoppedStrategyList = new ArrayList<>();
        this.tradedStrategyList = new ArrayList<>();
        this.writeKeysAndSettings = null;
        this.mainPageController = null;
    }



    public void addToAllStrategyList(StrategyObject in) {
        // в зависимости от объекта добавим его в тот или иной лист
        if (in.getWorks()) tradedStrategyList.add(in);
        else stoppedStrategyList.add(in);
    }



    public void replaceStrategy(StrategyObject in) {
        // находит по стратегие - strategyObject - удаляем е и вставляем новую
        // для новой будет новый айди - его находим и все ее обслуживание переводим теперь на него
        // это делаем везде где она может быть
        String id = in.getClassID();
        boolean works = in.getWorks();
        int index = -1;

        for (StrategyObject object : works ? tradedStrategyList : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                if (works) index = tradedStrategyList.indexOf(object);
                else index = stoppedStrategyList.indexOf(object);
            }
        }

        if (works) tradedStrategyList.remove(index);
        else stoppedStrategyList.remove(index);

        in.setClassID();
        in.setWorks(false);
        stoppedStrategyList.add(in);
        mainPageController.updateListView();
        strategyObject = null;
        writeKeysAndSettings.writeNewSettingsAndStates();
    }



//    public void setStrategySettingAndStatus(StrategyObject strategyObject) {
//        this.strategyObject = strategyObject;
//    }



    // найти стратегию
    public void findStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id = in.split(Lines.space)[0];
        for (StrategyObject object : tradedStrategyList) {
            if (id.equals(object.getClassID())) {
                strategyObject = object;
                return;
            }
        }
        for (StrategyObject object : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                strategyObject = object;
                return;
            }
        }
    }



    // запустить стратегию
    public void launchStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id = in.split(Lines.space)[0];
        int index = -1;
        for (StrategyObject object : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                index = stoppedStrategyList.indexOf(object);
                strategyObject = object;
                break;
            }
        }
        strategyObject.setWorks(true);
        stoppedStrategyList.remove(index);
        tradedStrategyList.add(strategyObject);
        mainPageController.updateListView();
        arraysOfWebSockets.addOneStrategyToWiretap(strategyObject);
        writeKeysAndSettings.writeNewSettingsAndStates();
    }



    // остановить стратегию
    public void stopStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id = in.split(Lines.space)[0];
        int index = -1;
        for (StrategyObject s : tradedStrategyList) {
            if (id.equals(s.getClassID())) {
                index = tradedStrategyList.indexOf(s);
                strategyObject = s;
                break;
            }
        }
        tradedStrategyList.remove(index);
        strategyObject.setWorks(false);
        stoppedStrategyList.add(strategyObject);
        mainPageController.updateListView();
        writeKeysAndSettings.writeNewSettingsAndStates();
    }



    // удалить стратегию
    public void removeStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию и удаляем ее отовсюду
        String id = in.split(Lines.space)[0];
        int index = -1;

        for (StrategyObject object : tradedStrategyList) {
            if (id.equals(object.getClassID())) {
                index = tradedStrategyList.indexOf(object);
                object.setWorks(false);
                break;
            }
        }
        if (index >= 0 ) {
            tradedStrategyList.remove(index);
            mainPageController.updateListView();
            return;
        }
        for (StrategyObject object : stoppedStrategyList) {
            if (id.equals(object.getClassID())) {
                index = stoppedStrategyList.indexOf(object);
                break;
            }
        }
        if (index >= 0 ) {
            stoppedStrategyList.remove(index);
            mainPageController.updateListView();
        }
        writeKeysAndSettings.writeNewSettingsAndStates();
    }




    public void setMainPageController(MainPageController mainPageController) { this.mainPageController = mainPageController; }

    public void setWriteKeysAndSettings(WriteKeysAndSettings in) { this.writeKeysAndSettings = in; }

    public ArrayList<StrategyObject> getStoppedStrategyListObject() { return stoppedStrategyList; }

    public ArrayList<StrategyObject> getTradedStrategyListObject() { return tradedStrategyList; }

    public StrategyObject getStrategySettingAndStatus() { return strategyObject; }
}
