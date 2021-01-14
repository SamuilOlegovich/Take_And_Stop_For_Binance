package main.model;

public class StrategySettingAndStatus {
    private String nameStrategy;
    private String tradingPair;

    private Double numberOfCoins;
    private Double trailingStop;
    private Double takePrice;
    private Double stopPrice;
    private Double price;

    private int fractionalParts;
    private int buyOrSell;
    private int onOrOffFP;
    private int onOrOffTS;

    private Long classID;



    public StrategySettingAndStatus() {
    }



    public String getNameStrategy() {
        return nameStrategy;
    }

    public void setNameStrategy(String nameStrategy) {
        this.nameStrategy = nameStrategy;
    }

    public String getTradingPair() {
        return tradingPair;
    }

    public void setTradingPair(String tradingPair) {
        this.tradingPair = tradingPair;
    }

    public Double getNumberOfCoins() {
        return numberOfCoins;
    }

    public void setNumberOfCoins(Double numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
    }

    public Double getTrailingStop() {
        return trailingStop;
    }

    public void setTrailingStop(Double trailingStop) {
        this.trailingStop = trailingStop;
    }

    public Double getTakePrice() {
        return takePrice;
    }

    public void setTakePrice(Double takePrice) {
        this.takePrice = takePrice;
    }

    public Double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(Double stopPrice) {
        this.stopPrice = stopPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getFractionalParts() {
        return fractionalParts;
    }

    public void setFractionalParts(int fractionalParts) {
        this.fractionalParts = fractionalParts;
    }

    public int getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(int buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public int getOnOrOffTS() {
        return onOrOffTS;
    }

    public void setOnOrOffTS(int onOrOffTS) {
        this.onOrOffTS = onOrOffTS;
    }

    public int getOnOrOffFP() {
        return onOrOffFP;
    }

    public void setOnOrOffFP(int onOrOffFP) {
        this.onOrOffFP = onOrOffFP;
    }

    public void setClassID() {
    }
}
