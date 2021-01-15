package main.model;

import java.util.ArrayList;

public class ArraysOfStrategies {
    private ArrayList<StrategySettingAndStatus> allStrategySettingAndStatusList;
    private StrategySettingAndStatus strategySettingAndStatus;



    public ArraysOfStrategies() {
        this.allStrategySettingAndStatusList = new ArrayList<>();
    }



    public void addToAllStrategySettingAndStatusList(StrategySettingAndStatus strategySettingAndStatus) {
        allStrategySettingAndStatusList.add(strategySettingAndStatus);
    }


    public void replaceStrategy(StrategySettingAndStatus strategySettingAndStatus) {

    }


    public StrategySettingAndStatus getStrategySettingAndStatus() {
        return strategySettingAndStatus;
    }

    public void setStrategySettingAndStatus(StrategySettingAndStatus strategySettingAndStatus) {
        this.strategySettingAndStatus = strategySettingAndStatus;
    }
}
