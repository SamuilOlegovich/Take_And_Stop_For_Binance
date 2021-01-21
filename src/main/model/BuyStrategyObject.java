package main.model;

public class BuyStrategyObject implements Runnable {
    private StrategyObject strategyObject;

    public BuyStrategyObject(StrategyObject strategyObject) {
        this.strategyObject = strategyObject;
//        new Thread(this).start();
    }


    @Override
    public void run() {

    }
}
