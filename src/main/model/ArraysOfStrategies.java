package main.model;

import java.util.ArrayList;

public class ArraysOfStrategies {
    private ArrayList<StrategyObject> stoppedStrategyList;
    private ArrayList<StrategyObject> tradedStrategyList;

    private StrategyObject strategyObject;



    public ArraysOfStrategies() {
        this.stoppedStrategyList = new ArrayList<>();
        this.tradedStrategyList = new ArrayList<>();
    }



    public void addToAllStrategyList(StrategyObject in) {
        // в зависимости от объекта добавим его в тот или иной лист
    }


    public void replaceStrategy(StrategyObject in) {
        // находит по стратегие - strategyObject - удаляем е и вставляем новую
        // для новой будет новый айди - его находим и все ее обслуживание переводим теперь на него
        // это делаем везде где она может быть

    }


    public StrategyObject getStrategySettingAndStatus() {
        return strategyObject;
    }

//    public void setStrategySettingAndStatus(StrategyObject strategyObject) {
//        this.strategyObject = strategyObject;
//    }

    // найти стратегию
    public void findStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию

    }

    // удалить стратегию
    public void removeStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию и удаляем ее отовсюду
    }

    public ArrayList<StrategyObject> getStoppedStrategyList() {
        return stoppedStrategyList;
    }

    public ArrayList<StrategyObject> getTradedStrategyList() {
        return tradedStrategyList;
    }
}
