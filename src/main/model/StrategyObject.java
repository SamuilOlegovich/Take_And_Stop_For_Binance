package main.model;




public class StrategyObject {
    private final WriteKeysAndSettings writeKeysAndSettings;
    private String nameStrategy;
    private String tradingPair;
    private String classID;

    // заполняется только после совершения основной сделки если есть указаны стопы и тейки
    private Double priceStopTrailing;
    private Double buyOrSellCoins;
    private Double amountOfCoins;
    private Double trailingStop;
    private Double takePrice;
    private Double stopPrice;
    private Double maxPrice;
    private Double price;

    private Position preliminaryPosition;
    private Position position;

    private volatile Double priceAskNow;
    private volatile Double priceBidNow;

    private int fractionalParts;
    private int buyOrSell;

    // ниже или выше цены
    private boolean lowerOrHigherPrices;
    private boolean onOrOffFP;
    private boolean onOrOffTS;
    private boolean count;

    private volatile boolean works;



    public StrategyObject() {
        this.writeKeysAndSettings = Agent.getWriteKeysAndSettings();
        this.position = Position.STARTED_POSITION;
        this.lowerOrHigherPrices = false;
        this.preliminaryPosition = null;
        this.priceStopTrailing = 0.0;
        this.buyOrSellCoins = 0.0;
        this.onOrOffFP = false;
        this.onOrOffTS = false;
        this.stopPrice = 0.0;
        this.takePrice = 0.0;
        this.maxPrice = 0.0;
        this.works = false;
        this.buyOrSell = 0;
        this.count = true;
    }



    public synchronized void setPriceAskAndBidNow(Double ask, Double bid) {
        if (count) { determineThePosition(); }
        System.out.println(tradingPair + "===" + bid.toString());///////////////////////////////////
        this.priceAskNow = ask;
        this.priceBidNow = bid;
        if (works) { doSomethingOrNot();}
    }



    // определить позицию
    private void determineThePosition() {
        count = false;
        if (position.equals(Position.STARTED_POSITION)) {
            if (onOrOffTS && trailingStop > 0.0) { position = Position.TRAILING_STOP_POSITION; }
            else if (price > 0.0 && stopPrice > 0.0 && takePrice > 0.0) { position = Position.NORMAL_POSITION; }
            else if (price > 0.0 && stopPrice <= 0.0 && takePrice <= 0.0) { position = Position.EASY_POSITION; }
            writeKeysAndSettings.writeNewSettingsAndStates();
        }
    }



    // делаем что-то или нет
    private void doSomethingOrNot() {
        if (position.equals(Position.NORMAL_POSITION)) { normalPosition(); }
        else if (position.equals(Position.EASY_POSITION)) { easyPosition(); }
        else if (position.equals(Position.SELL_TAKE_OR_STOP_POSITION)) { sellTakeOrStopPosition(); }
        else if (position.equals(Position.BUY_TAKE_OR_STOP_POSITION)) { buyTakeOrStopPosition(); }
        else if (position.equals(Position.TRAILING_STOP_POSITION)) { trailingStopPosition(); }
    }



    private void trailingStopPosition() {
        if (maxPrice < priceBidNow) {
            maxPrice = priceBidNow;
            priceStopTrailing = (maxPrice - ((maxPrice / 100.0) * trailingStop));
        }
        if (buyOrSell == 1 && priceStopTrailing <= priceAskNow) {
            preliminaryPosition = position;
            position = Position.BUY_TRAILING_STOP_COMPLETED_POSITION;
            works = false;
            BUY();
        } else if (buyOrSell == -1 && priceStopTrailing >= priceBidNow) {
            preliminaryPosition = position;
            position = Position.SELL_TRAILING_STOP_COMPLETED_POSITION;
            works = false;
            SELL();
        }
    }



    private void buyTakeOrStopPosition() {
        if (takePrice <= priceAskNow) {
            preliminaryPosition = position;
            position = Position.SELL_TAKE_COMPLETED_POSITION;
            works = false;
            SELL();
        } else if (stopPrice >= priceAskNow) {
            preliminaryPosition = position;
            position = Position.SELL_STOP_COMPLETED_POSITION;
            works = false;
            SELL();
        }
    }



    private void sellTakeOrStopPosition() {
        if (takePrice >= priceBidNow) {
            preliminaryPosition = position;
            position = Position.BUY_TAKE_COMPLETED_POSITION;
            works = false;
            BUY();
        } else if (stopPrice <= priceBidNow) {
            preliminaryPosition = position;
            position = Position.BUY_STOP_COMPLETED_POSITION;
            works = false;
            BUY();
        }
    }



    private void easyPosition() {
        if (buyOrSell == 1) {
            if (lowerOrHigherPrices && price <= priceBidNow) {
                preliminaryPosition = position;
                position = Position.BUY_COMPLETED_POSITION;
                works = false;
                BUY();
            } else if (price >= priceBidNow) {
                preliminaryPosition = position;
                position = Position.BUY_COMPLETED_POSITION;
                works = false;
                BUY();
            }
        } else if (buyOrSell == -1) {
            if (lowerOrHigherPrices && price <= priceBidNow) {
                preliminaryPosition = position;
                position = Position.SELL_COMPLETED_POSITION;
                works = false;
                SELL();
            } else if (price >= priceBidNow) {
                preliminaryPosition = position;
                position = Position.SELL_COMPLETED_POSITION;
                works = false;
                SELL();
            }
        }
    }



    private void  normalPosition() {
        if (buyOrSell == 1) {
            if (lowerOrHigherPrices && price <= priceBidNow) {
                preliminaryPosition = position;
                position = Position.BUY_TAKE_OR_STOP_POSITION;
                BUY();
            } else if (price >= priceBidNow) {
                preliminaryPosition = position;
                position = Position.BUY_TAKE_OR_STOP_POSITION;
                BUY();
            }
        } else if (buyOrSell == -1) {
            if (lowerOrHigherPrices && price <= priceBidNow) {
                preliminaryPosition = position;
                position = Position.SELL_TAKE_OR_STOP_POSITION;
                SELL();
            } else if (price >= priceBidNow) {
                preliminaryPosition = position;
                position = Position.SELL_TAKE_OR_STOP_POSITION;
                SELL();
            }
        }
    }



    private void BUY() {
        new BuyStrategyObject(this);
        writeKeysAndSettings.writeNewSettingsAndStates();
    }

    private void SELL() {
        new SellStrategyObject(this);
        writeKeysAndSettings.writeNewSettingsAndStates();
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


    public void setPreliminaryPosition(Position preliminaryPosition) { this.preliminaryPosition = preliminaryPosition; }
    public void setLowerOrHigherPrices(boolean lowerOrHigherPrices) { this.lowerOrHigherPrices = lowerOrHigherPrices; }
    public void setFractionalParts(int fractionalParts) { this.fractionalParts = fractionalParts; }
    public void setBuyOrSellCoins(Double buyOrSellCoins) { this.buyOrSellCoins = buyOrSellCoins; }
    public void setAmountOfCoins(Double amountOfCoins) { this.amountOfCoins = amountOfCoins; }
    public void setNameStrategy(String nameStrategy) { this.nameStrategy = nameStrategy; }
    public void setTrailingStop(Double trailingStop) { this.trailingStop = trailingStop; }
    public void setTradingPair(String tradingPair) { this.tradingPair = tradingPair; }
    public void setOnOrOffTS(boolean onOrOffTS) { this.onOrOffTS = onOrOffTS; }
    public void setOnOrOffFP(boolean onOrOffFP) { this.onOrOffFP = onOrOffFP; }
    public void setTakePrice(Double takePrice) { this.takePrice = takePrice; }
    public void setStopPrice(Double stopPrice) { this.stopPrice = stopPrice; }
    public Position getPreliminaryPosition() { return preliminaryPosition; }
    public void setPosition(Position position) { this.position = position; }
    public void setBuyOrSell(int buyOrSell) { this.buyOrSell = buyOrSell; }
    public boolean isLowerOrHigherPrices() { return lowerOrHigherPrices; }
    public void setClassID(String classID) { this.classID = classID; }
    public Double getBuyOrSellCoins() { return buyOrSellCoins; }
    public void setWorks(boolean works) { this.works = works; }
    public int getFractionalParts() { return fractionalParts; }
    public void setPrice(Double price) { this.price = price; }
    public Double getAmountOfCoins() { return amountOfCoins; }
    public Double getTrailingStop() { return trailingStop; }
    public String getNameStrategy() { return nameStrategy; }
    public void setClassID() { classID = hashCode() + ""; }
    public String getTradingPair() { return tradingPair; }
    public boolean getOnOrOffTS() { return onOrOffTS; }
    public boolean getOnOrOffFP() { return onOrOffFP; }
    public Double getTakePrice() { return takePrice; }
    public Double getStopPrice() { return stopPrice; }
    public Position getPosition() { return position; }
    public int getBuyOrSell() { return buyOrSell; }
    public String getClassID() { return classID; }
    public boolean getWorks() { return works; }
    public Double getPrice() { return price; }
}
