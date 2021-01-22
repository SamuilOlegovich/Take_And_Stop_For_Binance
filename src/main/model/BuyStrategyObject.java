package main.model;

import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.*;

import java.math.BigDecimal;




public class BuyStrategyObject implements Runnable {
    private BinanceOrderPlacement binanceOrderPlacement;
    private ArraysOfStrategies arraysOfStrategies;
    private StrategyObject strategyObject;
    private BinanceSymbol binanceSymbol;
    private BinanceAPI binanceAPI;

    private Position preliminaryPosition;
    private Position position;

    private String symbol;

    private Double fractionsAmountCoins;
    private Double returnAmountCoins;
    private Double amountCoins;
    private Double price;

    private int executedOrders;
    private int fractions;
    private int attempts;



    public BuyStrategyObject(StrategyObject strategyObject) {
        this.preliminaryPosition = strategyObject.getPreliminaryPosition();
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
        this.binanceAPI = Agent.getBinanceAPI();
        this.strategyObject = strategyObject;
        this.returnAmountCoins = 0.0;
        this.binanceSymbol = null;
        this.executedOrders = 0;
        this.amountCoins = 0.0;
        this.attempts = 10;
        new Thread(this).start();
    }


    @Override
    public void run() {
        determineWhatToDo();
        createBinanceOrderPlacement();
        createdNewTrade();
    }


    // отправляем сделку на биржу
    private void createdNewTrade() {
        int fraction = 0;
        if (fractions <= 1) {
            try { binanceAPI.createOrder(binanceOrderPlacement); }
            catch (BinanceApiException e) { errorException(e.getMessage(), 0); }
        } else {
            // если нужно разбить сделку
            // в ошибке прилетит количество выполненых! ордеров
            for (int i = executedOrders; i < fractions; i++) {
                binanceOrderPlacement.setNewClientOrderId(strategyObject.getClassID() + "FR" + (i + 1));
                try { binanceAPI.createOrder(binanceOrderPlacement); }
                catch (BinanceApiException e) { errorException(e.getMessage(), fraction); }
                fraction = i + 1;
                try { Thread.sleep(500); } catch (InterruptedException e) { errorException(e.getMessage(), fraction); }
            }
        }
    }



    private void errorException(String string, int fraction) {
        // сделать так чтобы менялся статус ордера на неисполненый,
        // а так же состояние менялось и список если надо и добавлялся опять в список слушателей
        // ну или все это но в ручную
        if (fraction <= 1) {
            if (attempts <= 0) {
                strategyObject.setNameStrategy(strategyObject.getNameStrategy() + Lines.delimiterError + string);
                strategyObject.setBuyOrSellCoins(0.0);
                strategyObject.setPosition(preliminaryPosition);
                arraysOfStrategies.replaceStrategy(strategyObject);
            } else {
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                createdNewTrade();
            }
        } else {
            if (attempts <= 0) {
                understandAndExecute(string, fraction);
            } else {
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                executedOrders = fraction - 1;
                createdNewTrade();
            }
        }
    }


    private void understandAndExecute(String string, int fraction) {
        if (preliminaryPosition.equals(Position.NORMAL_POSITION)) {
            Double remainder = (strategyObject.getAmountOfCoins() / fractions) * (fractions - fraction);
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterErrorFR + fraction + Lines.delimiter + string);
            strategyObject.setBuyOrSellCoins(fractionsAmountCoins * fraction);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setAmountOfCoins(remainder);
            arraysOfStrategies.replaceStrategy(strategyObject);
        } else if (preliminaryPosition.equals(Position.EASY_POSITION)
                || preliminaryPosition.equals(Position.TRAILING_STOP_POSITION)) {
            Double remainder = (strategyObject.getAmountOfCoins() / fractions) * (fractions - fraction);
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterErrorFR + fraction + Lines.delimiter + string);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setAmountOfCoins(remainder);
            arraysOfStrategies.replaceStrategy(strategyObject);
        } else if (preliminaryPosition.equals(Position.BUY_TAKE_OR_STOP_POSITION)) {
            Double remainder = (strategyObject.getAmountOfCoins() / fractions) * (fractions - fraction);
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterErrorFR + fraction + Lines.delimiter + string);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setBuyOrSellCoins(remainder);
            arraysOfStrategies.replaceStrategy(strategyObject);
        }
    }


    private void understandAndExecute(String string) {
        if (preliminaryPosition.equals(Position.NORMAL_POSITION)) {
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterError + Lines.delimiter + string);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setAmountOfCoins(amountCoins);
            strategyObject.setBuyOrSellCoins(0.0);
            arraysOfStrategies.replaceStrategy(strategyObject);
        } else if (preliminaryPosition.equals(Position.EASY_POSITION)
                || preliminaryPosition.equals(Position.TRAILING_STOP_POSITION)) {
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterError + Lines.delimiter + string);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setAmountOfCoins(amountCoins);
            arraysOfStrategies.replaceStrategy(strategyObject);
        } else if (preliminaryPosition.equals(Position.BUY_TAKE_OR_STOP_POSITION)) {
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterError + Lines.delimiter + string);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setBuyOrSellCoins(amountCoins);
            arraysOfStrategies.replaceStrategy(strategyObject);
        }
    }


    private void createBinanceOrderPlacement() {
        if (fractions > 1) { fractionsAmountCoins = amountCoins / fractions; }
        try {
            binanceSymbol = new BinanceSymbol(symbol);
            binanceOrderPlacement = new BinanceOrderPlacement();
            binanceOrderPlacement.setQuantity(new BigDecimal(fractions > 1
                    ? fractionsAmountCoins.toString()
                    : returnAmountCoins.toString()));
            binanceOrderPlacement.setTimeInForce(BinanceTimeInForce.GOOD_TILL_CANCELLED);
            binanceOrderPlacement.setNewClientOrderId(strategyObject.getClassID());
            binanceOrderPlacement.setPrice(new BigDecimal(price.toString()));
            binanceOrderPlacement.setType(BinanceOrderType.MARKET);
            binanceOrderPlacement.setSide(BinanceOrderSide.BUY);
            binanceOrderPlacement.setSymbol(binanceSymbol);
        } catch (BinanceApiException e) { errorException(e.getMessage(), 0);}
    }



    private void determineWhatToDo() {
        position = strategyObject.getPosition();
        if (position.equals(Position.BUY_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            fractions = strategyObject.getFractionalParts();
            symbol = strategyObject.getTradingPair();
            price = strategyObject.getPrice();
        } else if (position.equals(Position.BUY_TAKE_OR_STOP_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            fractions = strategyObject.getFractionalParts();
            symbol = strategyObject.getTradingPair();
            price = strategyObject.getPrice();
            returnAmountCoins = amountCoins / price;
            strategyObject.setBuyOrSellCoins(returnAmountCoins);
        } else if (position.equals(Position.BUY_TAKE_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getBuyOrSellCoins();
            fractions = strategyObject.getFractionalParts();
            symbol = strategyObject.getTradingPair();
            price = strategyObject.getTakePrice();
        } else if (position.equals(Position.BUY_TRAILING_STOP_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            fractions = strategyObject.getFractionalParts();
            symbol = strategyObject.getTradingPair();
            price = strategyObject.getPrice();
        }
    }
}
