package main.model;


import com.google.gson.JsonObject;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.*;
import main.view.ConsoleHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;




public class SellStrategyObject implements Runnable {
    private final FilesAndPathCreator filesAndPathCreator;
    private final WriterAndReadFile writerAndReadFile;

    private BinanceOrderPlacement binanceOrderPlacement;
    private final ArraysOfStrategies arraysOfStrategies;
    private StrategyObject strategyObject;
    private BinanceSymbol binanceSymbol;
    private final BinanceAPI binanceAPI;

    private final Position preliminaryPosition;
    private Position position;

    private Double fractionsAmountCoins;
    private Double returnAmountCoins;
    private Double amountCoins;
    private Double price;

    private final String symbol;

    private int executedOrders;
    private final int fractions;
    private int attempts;



    public SellStrategyObject(StrategyObject strategyObject) {
        this.preliminaryPosition = strategyObject.getPreliminaryPosition();
        this.attempts = Agent.getNumberOfAttemptsToExecuteTrade();
        this.filesAndPathCreator = Agent.getFilesAndPathCreator();
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
        this.writerAndReadFile = Agent.getWriterAndReadFile();
        this.fractions = strategyObject.getFractionalParts();
        this.symbol = strategyObject.getTradingPair();
        this.binanceAPI = Agent.getBinanceAPI();
        this.strategyObject = strategyObject;
        this.returnAmountCoins = 0.0;
        this.binanceSymbol = null;
        this.executedOrders = 0;
        this.amountCoins = 0.0;
        new Thread(this).start();
    }



    @Override
    public void run() {
        determineWhatToDo();
        createBinanceOrderPlacement();
        if (fractions <= 1 ) {
            createdNewTrade();
        } else {
            createdNewTrades();
        }
    }



    private void determineWhatToDo() {
        position = strategyObject.getPosition();

        if (position.equals(Position.SELL_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            price = strategyObject.getPrice();
        } else if (position.equals(Position.SELL_TAKE_OR_STOP_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            price = strategyObject.getPrice();
            strategyObject.setBuyOrSellCoins(returnAmountCoins);
        } else if (position.equals(Position.SELL_TAKE_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getBuyOrSellCoins();
            price = strategyObject.getTakePrice();
        } else if (position.equals(Position.SELL_TRAILING_STOP_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            price = strategyObject.getPriceStopTrailing();
        }
    }


    // создаем обект - торгового ордеоа и запоняем его
    private void createBinanceOrderPlacement() {
        if (fractions > 1) {
            fractionsAmountCoins = amountCoins / fractions;
        } else {
            // если мы продаем то умножаем количество монет на цену и получаем итоговое количество
            returnAmountCoins = amountCoins * price;
        }

        try {
            binanceOrderPlacement = new BinanceOrderPlacement();
            binanceSymbol = new BinanceSymbol(symbol);

            binanceOrderPlacement.setQuantity(new BigDecimal(fractions > 1
                    ? fractionsAmountCoins.toString()
                    : returnAmountCoins.toString()
            ).setScale(5, RoundingMode.HALF_EVEN));

            binanceOrderPlacement.setTimeInForce(BinanceTimeInForce.GOOD_TILL_CANCELLED);
            binanceOrderPlacement.setNewClientOrderId(strategyObject.getClassID());
            binanceOrderPlacement.setPrice(new BigDecimal(price.toString()));
            binanceOrderPlacement.setType(BinanceOrderType.MARKET);
            binanceOrderPlacement.setSide(BinanceOrderSide.SELL);
            binanceOrderPlacement.setSymbol(binanceSymbol);
        } catch (BinanceApiException e) {
            ConsoleHelper.writeERROR(e.getMessage());
            errorException(e.getMessage());
        }
    }



    // отправляем сделку на биржу
    private void createdNewTrade() {
        attempts--;
        try {
            // если все хорошо то записываем в логи информацию о транзакциях.
            JsonObject jsonObject = binanceAPI.createOrder(binanceOrderPlacement);
            new TradeReport(strategyObject, jsonObject);
            writerAndReadFile.writerFile(getTransactionInformation(), filesAndPathCreator.getPathLogs(), true);
        } catch (BinanceApiException e) {
            // если что-то пошло не так, то записываем в лог информацию об ошибке
            // и начинаем обработку ошибки
            writerAndReadFile.writerFile("ERROR" + getTransactionERRORInformation(),
                    filesAndPathCreator.getPathLogs(), true);
            errorException(e.getMessage());
        }
    }



    // отправляем сделку на биржу, но частями
    private void createdNewTrades() {
        int fraction = 0;
        attempts--;
        for (int i = executedOrders; i < fractions; i++) {
            binanceOrderPlacement.setNewClientOrderId(strategyObject.getClassID() + "FR" + (i + 1));
            try {
                JsonObject jsonObject = binanceAPI.createOrder(binanceOrderPlacement);
                new TradeReport(strategyObject, jsonObject);
            } catch (BinanceApiException e) {
                // в ошибке прилетит количество выполненых! ордеров
                errorException(e.getMessage(), fraction);
                fraction = i + 1;
            }

            try { Thread.sleep(1000); } catch (InterruptedException e) {
                errorException(e.getMessage(), fraction);
            }
        }
    }



    // сделать так чтобы менялся статус ордера на неисполненый,
    // а так же состояние менялось и список если надо и добавлялся опять в список слушателей
    // ну или все это но в ручную
    // в данном случаи стратегия получает предыдущий статус и переходит список офлайн стратегий
    private void errorException(String string, int fraction) {
        if (attempts < 0) {
            understandAndExecute(string, fraction);
        } else {
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            executedOrders = fraction - 1;
            createdNewTrades();
        }
    }



    private void errorException(String string) {
        if (attempts < 0) {
            understandAndExecute(string);
        } else {
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
            createdNewTrade();
        }
    }



    // откатываем состояние стратегии в зависимости от ситуации с фракциями
    private void understandAndExecute(String string, int fraction) {
        // если перед этим былы нормальная позиция и не выполнилась
        if (preliminaryPosition.equals(Position.NORMAL_POSITION)) {
            // расчитываем оставшиеся количество монет
            Double remainder = (strategyObject.getAmountOfCoins() / fractions) * (fractions - fraction);
            // дописываем к имени стратегии ERROR и дальше сообщение об ошибке
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterErrorFR + fraction + Lines.delimiter + string);
            // так же указываем количество распроданых монет
            strategyObject.setBuyOrSellCoins(fractionsAmountCoins * fraction);
            // возвращаем ее предыдущую позицию - NORMAL_POSITION
            strategyObject.setPosition(preliminaryPosition);
            // вписываем количество невыполненых монет
            strategyObject.setAmountOfCoins(remainder);
            // переводим ее в список офлайн стратегий
            arraysOfStrategies.replaceStrategy(strategyObject);

        } else if (preliminaryPosition.equals(Position.EASY_POSITION)
                || preliminaryPosition.equals(Position.TRAILING_STOP_POSITION)) {
            // если была перед ошибкой легкая или трайлирующая позиция без стопов и тейков
            Double remainder = (strategyObject.getAmountOfCoins() / fractions) * (fractions - fraction);
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterErrorFR + fraction + Lines.delimiter + string);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setAmountOfCoins(remainder);
            arraysOfStrategies.replaceStrategy(strategyObject);

        } else if (preliminaryPosition.equals(Position.BUY_TAKE_OR_STOP_POSITION)) {
            // если была перед ошибкой позиция отработки стопа или тейка
            Double remainder = (strategyObject.getAmountOfCoins() / fractions) * (fractions - fraction);
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterErrorFR + fraction + Lines.delimiter + string);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setBuyOrSellCoins(remainder);
            arraysOfStrategies.replaceStrategy(strategyObject);
        }
    }



    // откатываем состояние стратегии в зависимости от ситуации
    private void understandAndExecute(String string) {
        // если перед этим былы нормальная позиция и не выполнилась
        if (preliminaryPosition.equals(Position.NORMAL_POSITION)) {
            // дописываем к имени стратегии ERROR и дальше сообщение об ошибке
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterError + Lines.delimiter + string);
            // возвращаем ее предыдущую позицию - NORMAL_POSITION
            strategyObject.setPosition(preliminaryPosition);
            // так же востанавливаем количество монет
            strategyObject.setAmountOfCoins(amountCoins);
            strategyObject.setBuyOrSellCoins(0.0);
            // переводим ее в список офлайн стратегий
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



    private String getTransactionInformation() {
        return DatesTimes.getDateLogs() + Lines.delimiter + symbol + Lines.delimiter + Enums.PRICE
                + Lines.delimiter + price + Lines.delimiter + Enums.AMOUNT_OF_COINS
                + Lines.delimiter + amountCoins + Lines.delimiter + Enums.RECEIVED_COINS
                + Lines.delimiter + returnAmountCoins + Lines.newline;
    }



    private String getTransactionERRORInformation() {
        return DatesTimes.getDateLogs() + Lines.delimiter + symbol + Lines.delimiter + Enums.PRICE
                + Lines.delimiter + price + Lines.delimiter + Enums.AMOUNT_OF_COINS
                + Lines.delimiter + amountCoins + Lines.delimiter + Enums.RECEIVED_COINS
                + Lines.delimiter + returnAmountCoins + Lines.newline;
    }




    //////////////////////// TEST ////////////////////////////



    public static void main(String[] args) {
        Agent.setApi(new API());

        // создаем класс хранения всех стратегий
        ArraysOfStrategies arraysOfStrategies = new ArraysOfStrategies();
        ArraysOfWebSockets arraysOfWebSockets = new ArraysOfWebSockets();
        arraysOfStrategies.setArraysOfWebSockets(arraysOfWebSockets);
        arraysOfWebSockets.setArraysOfStrategies(arraysOfStrategies);
        Agent.setArraysOfStrategies(arraysOfStrategies);
        Agent.setArraysOfWebSockets(arraysOfWebSockets);

        Agent.setCreatesTemplatesAndData(new CreatesTemplatesAndData());
        // создаем все нужные папки и путя к ним если это надо
        Agent.setFilesAndPathCreator(new FilesAndPathCreator());
        Agent.setWriterAndReadFile(new WriterAndReadFile());

        WriteKeysAndSettings writeKeysAndSettings = new WriteKeysAndSettings();
        arraysOfStrategies.setWriteKeysAndSettings(writeKeysAndSettings);
        Agent.setWriteKeysAndSettings(writeKeysAndSettings);
        // считываеи все файлы настроек, ключей и состояний
        Agent.setReadKeysAndSettings(new ReadKeysAndSettings());

        Thread thread = new Thread(new GetUpToDateDataOnPairs());
        thread.start();
        try { thread.join(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        StrategyObject sObject = new StrategyObject();
        sObject.setPosition(Position.SELL_COMPLETED_POSITION);
        sObject.setFractionalParts(-1);
        sObject.setAmountOfCoins(20.0);
        sObject.setTrailingStop(-1.0);
        sObject.setNameStrategy("Test");
        sObject.setTradingPair("ETHBUSD");
        sObject.setPrice(1341.0);
        sObject.setTakePrice(-1.0);
        sObject.setStopPrice(-1.0);
        sObject.setBuyOrSell(1);
        sObject.setOnOrOffFP(false);
        sObject.setOnOrOffTS(false);
        sObject.setPrice(1420.0);
        sObject.setWorks(true);
        sObject.setClassID();
        sObject.setAmountOfCoins(20.0);

        new SellStrategyObject(sObject);
    }
}
