package main.model;


import java.math.BigDecimal;

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
    private volatile boolean count;
    private boolean onOrOffFP;
    private boolean onOrOffTS;

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



    public synchronized void setPriceAskAndBidNow(BigDecimal ask, BigDecimal bid) {
        // при первом запуске стратегии определяем ее назначение и дальнейшую работу
        if (count) { determineThePosition(); }
        this.priceAskNow = ask.doubleValue();
        this.priceBidNow = bid.doubleValue();
        System.out.println(tradingPair + "===" + priceBidNow);///////////////////////////////////
        // если стратегия запушена, то сверяем ее данные с котировками
        if (works) { doSomethingOrNot();}
    }



    // определить позицию
    private synchronized void determineThePosition() {
        count = false;
        if (position.equals(Position.STARTED_POSITION)) {
            preliminaryPosition = Position.STARTED_POSITION;
            if (onOrOffTS && trailingStop > 0.0) { position = Position.TRAILING_STOP_POSITION; }
            else if (price > 0.0 && stopPrice > 0.0 && takePrice > 0.0) { position = Position.NORMAL_POSITION; }
            else if (price > 0.0 && stopPrice <= 0.0 && takePrice <= 0.0) { position = Position.EASY_POSITION; }
            // закомментировать для теста 2 строки
            writeKeysAndSettings.writeNewSettingsAndStates();
            Agent.getMainPageController().updateListView();
        }
    }



    // делаем что-то или нет в зависимости от позиции
    private synchronized void doSomethingOrNot() {
        if (position.equals(Position.NORMAL_POSITION)) { normalPosition(); }
        else if (position.equals(Position.EASY_POSITION)) { easyPosition(); }
        else if (position.equals(Position.SELL_TAKE_OR_STOP_POSITION)) { sellTakeOrStopPosition(); }
        else if (position.equals(Position.BUY_TAKE_OR_STOP_POSITION)) { buyTakeOrStopPosition(); }
        else if (position.equals(Position.TRAILING_STOP_POSITION)) { trailingStopPosition(); }
    }



    // при трайлирующей стратегии в зависимости от направления игры
    // следим за обновлением пиковой цены и дальнейшим отклонением от неё,
    // сравниваем цены с текущими и если условие срабатывает
    // - меняем назначение стратегии и передаем ее на сделку,
    // а также переводим стратегию в выключеное состояние
    private void trailingStopPosition() {
//        if (lowerOrHigherPrices) {
            // в будущем переделать чтобы можно было выбирать
            // траллирующий стоп как от вершины - так и от низа
//        }
        if (maxPrice < priceBidNow) {
            maxPrice = priceBidNow.doubleValue();
            priceStopTrailing = (maxPrice - ((maxPrice / 100.0) * trailingStop));
        }

        if (buyOrSell == 1 && priceStopTrailing >= priceAskNow) {
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



    // при второй части нормальной стратегии в зависимости от направления игры
    // сравниваем цены с текущими и если условие срабатывает
    // - меняем назначение стратегии и передаем ее на сделку,
    // а также переводим стратегию в выключеное состояние
    private void buyTakeOrStopPosition() {
        if (takePrice >= priceAskNow) {
            preliminaryPosition = position;
            position = Position.SELL_TAKE_COMPLETED_POSITION;
            works = false;
            SELL();
        } else if (stopPrice <= priceAskNow) {
            preliminaryPosition = position;
            position = Position.SELL_STOP_COMPLETED_POSITION;
            works = false;
            SELL();
        }
    }



    // при второй части нормальной стратегии в зависимости от направления игры
    // сравниваем цены с текущими и если условие срабатывает
    // - меняем назначение стратегии и передаем ее на сделку,
    // а также переводим стратегию в выключеное состояние
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



    // при простой (легкой) стратегии в зависимости от направления игры
    // сравниваем цены с текущими и если условие срабатывает
    // - меняем назначение стратегии и передаем ее на сделку,
    // а также переводим стратегию в выключеное состояние
    private void easyPosition() {
        if (buyOrSell == 1) {
            if (lowerOrHigherPrices && price <= priceBidNow) {
                preliminaryPosition = position;
                position = Position.BUY_COMPLETED_POSITION;
                works = false;
                BUY();
            } else if (!lowerOrHigherPrices && price >= priceBidNow) {
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
            } else if (!lowerOrHigherPrices && price >= priceBidNow) {
                preliminaryPosition = position;
                position = Position.SELL_COMPLETED_POSITION;
                works = false;
                SELL();
            }
        }
    }



    // при обычной стратегии в зависимости от направления игры
    // сравниваем цены с текущими и если условие срабатывает
    // - меняем назначение стратегии и передаем ее на сделку
    private void  normalPosition() {
        if (buyOrSell == 1) {
            if (lowerOrHigherPrices && price <= priceBidNow) {
                preliminaryPosition = position;
                position = Position.BUY_TAKE_OR_STOP_POSITION;
                BUY();
            } else if (!lowerOrHigherPrices && price >= priceBidNow) {
                preliminaryPosition = position;
                position = Position.BUY_TAKE_OR_STOP_POSITION;
                BUY();
            }
        } else if (buyOrSell == -1) {
            if (lowerOrHigherPrices && price <= priceBidNow) {
                preliminaryPosition = position;
                position = Position.SELL_TAKE_OR_STOP_POSITION;
                SELL();
            } else if (!lowerOrHigherPrices &&  price >= priceBidNow) {
                preliminaryPosition = position;
                position = Position.SELL_TAKE_OR_STOP_POSITION;
                SELL();
            }
        }
    }



    private void BUY() {
        // закомментировать для теста 2 строки ниже
        new BuyStrategyObject(this);
        writeKeysAndSettings.writeNewSettingsAndStates();
        // раскоментировать для теста
//        System.out.println("BUY");
    }

    private void SELL() {
        // закомментировать для теста 2 строки ниже
        new SellStrategyObject(this);
        writeKeysAndSettings.writeNewSettingsAndStates();
        // раскоментировать для теста
//        System.out.println("SELL");
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
    public Double getPriceStopTrailing() { return priceStopTrailing; }
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
    public Double getMaxPrice() { return maxPrice; }
    public int getBuyOrSell() { return buyOrSell; }
    public String getClassID() { return classID; }
    public boolean getWorks() { return works; }
    public Double getPrice() { return price; }



    /////////////////////////// TEST ///////////////////////////

    public static void main(String[] args) {
        StrategyObject.saleAbovePrice();
        StrategyObject.saleBelowPrice();
        StrategyObject.saleBelowPriceTake();
        StrategyObject.saleBelowPriceStop();
        StrategyObject.buyAbovePriceTake();
        StrategyObject.buyAbovePriceStop();
        StrategyObject.saleAbovePrice2();
        StrategyObject.saleBelowPrice2();
        StrategyObject.saleBelowPriceTake2();
        StrategyObject.saleBelowPriceStop2();
        StrategyObject.buyAbovePriceTake2();
        StrategyObject.buyAbovePriceStop2();
        StrategyObject.buyTrStop();
        StrategyObject.sellTrStop();
    }

    // продажа выше цены
    private static void saleAbovePrice() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(true);
        strategyObject.setBuyOrSell(-1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setClassID();

        Double price = 1450.0;

        while (strategyObject.getWorks()) {
            price++;
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // продажа ниже цены
    private static void saleBelowPrice() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(-1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setClassID();

        Double price = 1510.0;

        while (strategyObject.getWorks()) {
            price--;
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // продажа ниже цены c тейкпрофитом
    private static void saleBelowPriceTake() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(-1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1450.0);
        strategyObject.setStopPrice(1510.0);
        strategyObject.setClassID();

        Double price = 1510.0;

        while (strategyObject.getWorks()) {
            price--;
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // продажа ниже цены c стопом
    private static void saleBelowPriceStop() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(-1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1450.0);
        strategyObject.setStopPrice(1520.0);
        strategyObject.setClassID();

        Double price = 1510.0;
        boolean flag = true;

        while (strategyObject.getWorks()) {
            if (flag) {
                price--;
                if(price == 1490) flag = false;
            } else {
                price++;
            }

            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // продажа выше цены c тейкпрофитом
    private static void buyAbovePriceTake() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(true);
        strategyObject.setBuyOrSell(-1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1490.0);
        strategyObject.setStopPrice(1520.0);
        strategyObject.setClassID();

        Double price = 1480.0;
        boolean flag = true;

        while (strategyObject.getWorks()) {
            if (flag) {
                price++;
                if (price == 1510) {
                    flag = false;
                }
            } else {
                price--;
            }
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // продажа выше цены c стопом
    private static void buyAbovePriceStop() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(true);
        strategyObject.setBuyOrSell(-1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1450.0);
        strategyObject.setStopPrice(1520.0);
        strategyObject.setClassID();

        Double price = 1510.0;

        while (strategyObject.getWorks()) {
            price++;

            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////////////////////////

    // покупка выше цены
    private static void saleAbovePrice2() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(true);
        strategyObject.setBuyOrSell(1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setClassID();

        Double price = 1450.0;

        while (strategyObject.getWorks()) {
            price++;
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // покупка ниже цены
    private static void saleBelowPrice2() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setClassID();

        Double price = 1510.0;

        while (strategyObject.getWorks()) {
            price--;
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // покупка ниже цены c тейкпрофитом
    private static void saleBelowPriceTake2() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1450.0);
        strategyObject.setStopPrice(1510.0);
        strategyObject.setClassID();

        Double price = 1510.0;

        while (strategyObject.getWorks()) {
            price--;
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // покупка ниже цены c стопом
    private static void saleBelowPriceStop2() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1450.0);
        strategyObject.setStopPrice(1520.0);
        strategyObject.setClassID();

        Double price = 1510.0;
        boolean flag = true;

        while (strategyObject.getWorks()) {
            if (flag) {
                price--;
                if(price == 1490) flag = false;
            } else {
                price++;
            }

            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // покупка выше цены c тейкпрофитом
    private static void buyAbovePriceTake2() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(true);
        strategyObject.setBuyOrSell(1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1490.0);
        strategyObject.setStopPrice(1520.0);
        strategyObject.setClassID();

        Double price = 1480.0;
        boolean flag = true;

        while (strategyObject.getWorks()) {
            if (flag) {
                price++;
                if (price == 1510) {
                    flag = false;
                }
            } else {
                price--;
            }
            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // покупка выше цены c стопом
    private static void buyAbovePriceStop2() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1500.0);
        strategyObject.setLowerOrHigherPrices(true);
        strategyObject.setBuyOrSell(1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(-1.0);
        strategyObject.setTakePrice(1450.0);
        strategyObject.setStopPrice(1520.0);
        strategyObject.setClassID();

        Double price = 1490.0;

        while (strategyObject.getWorks()) {
            price++;

            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // проверка трайлирующего стопа на продажу
    private static void buyTrStop() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(-1.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(-1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(1.0);
        strategyObject.setOnOrOffTS(true);
        strategyObject.setTakePrice(-1.0);
        strategyObject.setStopPrice(-1.0);
        strategyObject.setClassID();

        Double price = 1490.0;
        boolean flag = true;

        while (strategyObject.getWorks()) {
            if (flag) {
                price++;
                if (price == 1500) flag = false;
            } else {
                price--;
            }

            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // проверка трайлирующего стопа на покупку
    private static void sellTrStop() {
        StrategyObject strategyObject = new StrategyObject();
        strategyObject.setWorks(true);
        strategyObject.setPrice(1.0);
        strategyObject.setLowerOrHigherPrices(false);
        strategyObject.setBuyOrSell(1);
        strategyObject.setAmountOfCoins(0.001);
        strategyObject.setTradingPair("ETHUSDT");
        strategyObject.setNameStrategy("TESTED jsljflsljf");
        strategyObject.setTrailingStop(1.0);
        strategyObject.setOnOrOffTS(true);
        strategyObject.setTakePrice(-1.0);
        strategyObject.setStopPrice(-1.0);
        strategyObject.setClassID();

        Double price = 1490.0;
        boolean flag = true;

        while (strategyObject.getWorks()) {
            if (flag) {
                price++;
                if (price == 1500) flag = false;
            } else {
                price--;
            }

            strategyObject.setPriceAskAndBidNow(new BigDecimal(price), new BigDecimal(price));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
