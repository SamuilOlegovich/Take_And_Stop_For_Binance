package main.model;

import main.controller.MainPageController;

import java.util.ArrayList;



public class ArraysOfStrategies {
    private final ArrayList<StrategyObject> stoppedStrategyList;
    private final ArrayList<StrategyObject> tradedStrategyList;

    private final MainPageController mainPageController;
    private StrategyObject strategyObject;



    public ArraysOfStrategies() {
        this.mainPageController = Agent.getMainPageController();
        this.stoppedStrategyList = new ArrayList<>();
        this.tradedStrategyList = new ArrayList<>();
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

        for (StrategyObject s : works ? tradedStrategyList : stoppedStrategyList) {
            if (id.equals(s.getClassID())) {
                if (works) tradedStrategyList.indexOf(s);
                else stoppedStrategyList.indexOf(s);
            }
        }

        if (works) tradedStrategyList.remove(index);
        else stoppedStrategyList.remove(index);

        in.setClassID();
        in.setWorks(false);
        stoppedStrategyList.add(in);
        mainPageController.updateListView();
        strategyObject = null;
    }



//    public void setStrategySettingAndStatus(StrategyObject strategyObject) {
//        this.strategyObject = strategyObject;
//    }



    // найти стратегию
    public void findStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id = in.split(Lines.space)[0];
        for (StrategyObject s : tradedStrategyList) {
            if (id.equals(s.getClassID())) {
                strategyObject = s;
                return;
            }
        }
        for (StrategyObject s : stoppedStrategyList) {
            if (id.equals(s.getClassID())) {
                strategyObject = s;
                return;
            }
        }
    }



    // запустить стратегию
    public void launchStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию
        String id = in.split(Lines.space)[0];
        int index = -1;
        for (StrategyObject s : stoppedStrategyList) {
            if (id.equals(s.getClassID())) {
                index = stoppedStrategyList.indexOf(s);
                strategyObject = s;
                break;
            }
        }
        stoppedStrategyList.remove(index);
        strategyObject.setWorks(true);
        tradedStrategyList.add(strategyObject);
        mainPageController.updateListView();
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
    }






    // удалить стратегию
    public void removeStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию и удаляем ее отовсюду
        String id = in.split(Lines.space)[0];
        int index = -1;

        for (StrategyObject s : tradedStrategyList) {
            if (id.equals(s.getClassID())) {
                tradedStrategyList.indexOf(s);
                break;
            }
        }
        if (index >= 0 ) {
            tradedStrategyList.remove(index);
            mainPageController.updateListView();
            return;
        }
        for (StrategyObject s : stoppedStrategyList) {
            if (id.equals(s.getClassID())) {
                stoppedStrategyList.indexOf(s);
                break;
            }
        }
        if (index >= 0 ) {
            tradedStrategyList.remove(index);
            mainPageController.updateListView();
        }
    }



    public StrategyObject getStrategySettingAndStatus() { return strategyObject; }

    public ArrayList<StrategyObject> getStoppedStrategyListObject() { return stoppedStrategyList; }

    public ArrayList<StrategyObject> getTradedStrategyListObject() { return tradedStrategyList; }
}
