package main.model;

public class StrategyObject {
    private String nameStrategy;
    private String tradingPair;
    private String classID;

    // заполняется только после совершения основной сделки если есть указаны стопы и тейки
    private Double buyOrSellCoins;
    private Double amountOfCoins;
    private Double trailingStop;
    private Double takePrice;
    private Double stopPrice;
    private Double price;

    private Position position;

    private int fractionalParts;
    private int buyOrSell;

    private boolean onOrOffFP;
    private boolean onOrOffTS;
    private boolean works;





    public StrategyObject() {
        this.position = Position.STARTED_POSITION;
        this.buyOrSellCoins = 0.0;
        this.onOrOffFP = false;
        this.onOrOffTS = false;
        this.works = false;
        this.buyOrSell = 0;
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

    public Double getAmountOfCoins() {
        return amountOfCoins;
    }

    public void setAmountOfCoins(Double amountOfCoins) {
        this.amountOfCoins = amountOfCoins;
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

    public boolean getOnOrOffTS() {
        return onOrOffTS;
    }

    public void setOnOrOffTS(boolean onOrOffTS) {
        this.onOrOffTS = onOrOffTS;
    }

    public boolean getOnOrOffFP() {
        return onOrOffFP;
    }

    public void setOnOrOffFP(boolean onOrOffFP) {
        this.onOrOffFP = onOrOffFP;
    }

    public void setClassID() {
        classID = hashCode() + "";
    }

    public String getClassID() {
        return classID;
    }

    public Double getBuyOrSellCoins() {
        return buyOrSellCoins;
    }

    public boolean getWorks() {
        return works;
    }

    public Position getPosition() {
        return position;
    }

    public void setBuyOrSellCoins(Double buyOrSellCoins) {
        this.buyOrSellCoins = buyOrSellCoins;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setWorks(boolean works) {
        this.works = works;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    @Override
    public int hashCode() {
        return  (nameStrategy.hashCode()
                + tradingPair.hashCode()
                + amountOfCoins.hashCode()
                + trailingStop.hashCode()
                + takePrice.hashCode()
                + stopPrice.hashCode()
                + price.hashCode()
                + fractionalParts
                + buyOrSell
                + (onOrOffFP ? 1 : 0)
                + (onOrOffTS ? 1 : 0))
                * 39;
    }
}
