package main.model;

import com.google.gson.JsonObject;
import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.*;
import main.view.ConsoleHelper;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_FLOOR;




public class BuyStrategyObject implements Runnable {
    private final FilesAndPathCreator filesAndPathCreator;
    private final WriterAndReadFile writerAndReadFile;

    private BinanceOrderPlacement binanceOrderPlacement;
    private final ArraysOfStrategies arraysOfStrategies;
    private final StrategyObject strategyObject;
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
    private final int attempts;



    public BuyStrategyObject(StrategyObject strategyObject) {
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
        createdNewTrade();
    }



    // отправляем сделку на биржу
    private void createdNewTrade() {
        int fraction = 0;

        if (fractions <= 1) {
            try {
                JsonObject jsonObject = binanceAPI.createOrder(binanceOrderPlacement);
                System.out.println(jsonObject.toString());////////////////////
                writerAndReadFile.writerFile(getTransactionInformation(), filesAndPathCreator.getPathLogs(), true);
            } catch (BinanceApiException e) {
                System.out.println("ERROR");/////////////////////////////
                writerAndReadFile.writerFile("ERROR", filesAndPathCreator.getPathLogs(), true);////////////////
                errorException(e.getMessage(), 0);
            }
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



    // сделать так чтобы менялся статус ордера на неисполненый,
    // а так же состояние менялось и список если надо и добавлялся опять в список слушателей
    // ну или все это но в ручную
    // в данном случаи стратегия получает предыдущий статус и переходит список офлайн стратегий
    private void errorException(String string, int fraction) {
        if (fraction <= 1) {
            if (attempts < 0) {
                understandAndExecute(string);
            } else {
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                createdNewTrade();
            }
        } else {
            if (attempts < 0) { understandAndExecute(string, fraction); }
            else {
                try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                executedOrders = fraction - 1;
                createdNewTrade();
            }
        }
    }




    private void understandAndExecute(String string, int fraction) {
        if (preliminaryPosition.equals(Position.NORMAL_POSITION)) {
        // если была перед ошибкой нормальная позиция со стопами и тейками
            Double remainder = (strategyObject.getAmountOfCoins() / fractions) * (fractions - fraction);
            strategyObject.setNameStrategy(strategyObject.getNameStrategy()
                    + Lines.delimiterErrorFR + fraction + Lines.delimiter + string);
            strategyObject.setBuyOrSellCoins(fractionsAmountCoins * fraction);
            strategyObject.setPosition(preliminaryPosition);
            strategyObject.setAmountOfCoins(remainder);
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



    // посмотреть и поработать с setBuyOrSellCoins
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
        else { returnAmountCoins = amountCoins / price; }

        try {
            binanceSymbol = new BinanceSymbol(symbol);
            binanceOrderPlacement = new BinanceOrderPlacement();

            binanceOrderPlacement.setQuantity(new BigDecimal(fractions > 1
                    ? fractionsAmountCoins.toString()
                    : returnAmountCoins.toString()
            ).setScale(5, ROUND_FLOOR));

            binanceOrderPlacement.setTimeInForce(BinanceTimeInForce.GOOD_TILL_CANCELLED);
            binanceOrderPlacement.setNewClientOrderId(strategyObject.getClassID());
            binanceOrderPlacement.setPrice(new BigDecimal(price.toString()));
            binanceOrderPlacement.setType(BinanceOrderType.MARKET);
            binanceOrderPlacement.setSide(BinanceOrderSide.BUY);
            binanceOrderPlacement.setSymbol(binanceSymbol);
        } catch (BinanceApiException e) {
            ConsoleHelper.writeERROR(e.getMessage());
            errorException(e.getMessage(), 0);
        }
    }



    private void determineWhatToDo() {
        position = strategyObject.getPosition();

        if (position.equals(Position.BUY_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            price = strategyObject.getPrice();
        } else if (position.equals(Position.BUY_TAKE_OR_STOP_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            price = strategyObject.getPrice();
            strategyObject.setBuyOrSellCoins(returnAmountCoins);
        } else if (position.equals(Position.BUY_TAKE_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getBuyOrSellCoins();
            price = strategyObject.getTakePrice();
        } else if (position.equals(Position.BUY_TRAILING_STOP_COMPLETED_POSITION)) {
            amountCoins = strategyObject.getAmountOfCoins();
            price = strategyObject.getPriceStopTrailing();
        }
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
        sObject.setPosition(Position.BUY_COMPLETED_POSITION);
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

        new BuyStrategyObject(sObject);
    }
}
