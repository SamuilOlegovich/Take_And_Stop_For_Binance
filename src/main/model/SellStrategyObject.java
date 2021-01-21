package main.model;



public class SellStrategyObject implements Runnable {
    private StrategyObject strategyObject;

    public SellStrategyObject(StrategyObject strategyObject) {
        this.strategyObject = strategyObject;
//        new Thread(this).start();
    }

    @Override
    public void run() {

    }
}
