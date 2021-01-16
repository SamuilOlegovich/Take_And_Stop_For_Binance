package main.model;

import java.util.ArrayList;

public class ArraysOfStrategies {
    private ArrayList<StrategySettingAndStatus> stoppedStrategyList;
    private ArrayList<StrategySettingAndStatus> tradedStrategyList;

    private StrategySettingAndStatus strategySettingAndStatus;



    public ArraysOfStrategies() {
        this.stoppedStrategyList = new ArrayList<>();
        this.tradedStrategyList = new ArrayList<>();
    }



    public void addToAllStrategyList(StrategySettingAndStatus in) {
        // в зависимости от объекта добавим его в тот или иной лист
    }


    public void replaceStrategy(StrategySettingAndStatus in) {
        // находит по стратегие - strategySettingAndStatus - удаляем е и вставляем новую
        // для новой будет новый айди - его находим и все ее обслуживание переводим теперь на него
        // это делаем везде где она может быть

    }


    public StrategySettingAndStatus getStrategySettingAndStatus() {
        return strategySettingAndStatus;
    }

//    public void setStrategySettingAndStatus(StrategySettingAndStatus strategySettingAndStatus) {
//        this.strategySettingAndStatus = strategySettingAndStatus;
//    }

    // найти стратегию
    public void findStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию

    }

    // удалить стратегию
    public void removeStrategy(String in) {
        // разбираем строку и по основным данным находим стратегию и удаляем ее отовсюду
    }
}
