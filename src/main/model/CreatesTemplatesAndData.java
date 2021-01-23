package main.model;

import java.util.ArrayList;



public class CreatesTemplatesAndData {
    private ArraysOfStrategies arraysOfStrategies;


    public CreatesTemplatesAndData() {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
    }



    // шаблон для ключей
    public String getPatternForKeys() {
        return Enums.START.toString() + Lines.newline
                + APIandSecretKeys.API_KEY.toString() + Lines.delimiter + Lines.newline
                + APIandSecretKeys.SECRET_KEY.toString() + Lines.delimiter + Lines.newline
                + Enums.END.toString() + Lines.newline;
    }


    public String getStringPatternForSettingsAndStates() {
        return Enums.SETTINGS + Lines.newline
                + Enums.DATE_DIFFERENCE + Lines.delimiter + Agent.getDateDifference() + Lines.newline
                + Enums.STATUS + Lines.newline
                + Enums.ID + Lines.delimiter + Enums.IF_IT_WAS_NOT_CREATED_THROUGH_THE_PROGRAM_THEM_WE_WRITE_DONE_BY_HAND + Lines.newline
                + Enums.POSITION + Lines.delimiter + Position.EXAMPLE_POSITION + Lines.newline
                + Enums.WORKS + Lines.delimiter + Enums.FALSE + Lines.newline
                + Enums.TRADING_PAIR + Lines.delimiter +"ETHBTC" + Lines.newline
                + Enums.BUY_OR_SELL + Lines.delimiter + Enums.BUY + Lines.newline
                + Enums.LOWER_OR_HIGHER_PRICE + Lines.delimiter + Enums.HIGHER + Lines.newline
                + Enums.AMOUNT_OF_COINS + Lines.delimiter + "12.890" + Lines.newline
                + Enums.PRICE + Lines.delimiter + "0.03498738" + Lines.newline
                + Enums.TAKE_PRICE + Lines.delimiter + "0.04889889" + Lines.newline
                + Enums.STOP_PRICE + Lines.delimiter + "0.03389889" + Lines.newline
                + Enums.TRAILING_STOP + Lines.delimiter + "3.7" + Lines.newline
                + Enums.ON_OR_OFF_TRAILING_STOP + Lines.delimiter + Enums.FALSE + Lines.newline
                + Enums.FRACTIONAL_PARTS + Lines.delimiter + "5" + Lines.newline
                + Enums.ON_OR_OFF_FRACTIONAL_PARTS + Lines.delimiter + Enums.FALSE + Lines.newline
                + Enums.BUY_OR_SELL_COINS + Lines.delimiter + "123.608" + Lines.newline
                + Enums.NAME_STRATEGY + Lines.delimiter + Enums.AN_EXAMPLE_OF_CREATING_AND_FILLING_IN_STRATEGIES_IN_MANUAL_MODE_IN_A_FILE + Lines.newline
                + Enums.NEXT + Lines.newline
                + Enums.ID + Lines.delimiter + Enums.IF_IT_WAS_NOT_CREATED_THROUGH_THE_PROGRAM_THEM_WE_WRITE_DONE_BY_HAND + Lines.newline
                + Enums.POSITION + Lines.delimiter + Position.EXAMPLE_POSITION + Lines.newline
                + Enums.WORKS + Lines.delimiter + Enums.FALSE + Lines.newline
                + Enums.TRADING_PAIR + Lines.delimiter + "LTCBTC" + Lines.newline
                + Enums.BUY_OR_SELL + Lines.delimiter + Enums.SELL + Lines.newline
                + Enums.LOWER_OR_HIGHER_PRICE + Lines.delimiter + Enums.LOWER + Lines.newline
                + Enums.AMOUNT_OF_COINS + Lines.delimiter + "0.890" + Lines.newline
                + Enums.PRICE + Lines.delimiter + "0.00349873" + Lines.newline
                + Enums.TAKE_PRICE + Lines.delimiter + "0.00304889" + Lines.newline
                + Enums.STOP_PRICE + Lines.delimiter + "0.03589889" + Lines.newline
                + Enums.TRAILING_STOP + Lines.delimiter + "0.7" + Lines.newline
                + Enums.ON_OR_OFF_TRAILING_STOP + Lines.delimiter + Enums.TRUE + Lines.newline
                + Enums.FRACTIONAL_PARTS + Lines.delimiter +"0" + Lines.newline
                + Enums.ON_OR_OFF_FRACTIONAL_PARTS + Lines.delimiter + Enums.TRUE + Lines.newline
                + Enums.BUY_OR_SELL_COINS + Lines.delimiter + "0.0" + Lines.newline
                + Enums.NAME_STRATEGY + Lines.delimiter + Enums.AN_EXAMPLE_OF_CREATING_AND_FILLING_IN_STRATEGIES_IN_MANUAL_MODE_IN_A_FILE + Lines.newline
                + Enums.END.toString() + Lines.newline;
    }



    public String getRealSettingsAndStates() {
        ArrayList<StrategyObject> stoppedStrategyList = new ArrayList<>(arraysOfStrategies.getStoppedStrategyListObject());
        ArrayList<StrategyObject> tradedStrategyList = new ArrayList<>(arraysOfStrategies.getTradedStrategyListObject());

        StringBuilder sB = new StringBuilder(getStringPatternForSettingsAndStates());
        sB.delete(sB.length() - 4, sB.length());
        sB.append(Enums.NEXT).append(Lines.newline);

        if (stoppedStrategyList.size() > 0) {
            for (StrategyObject s : stoppedStrategyList) {
                sB.append(Enums.ID).append(Lines.delimiter).append(s.getClassID()).append(Lines.newline);
                sB.append(Enums.POSITION).append(Lines.delimiter).append(s.getPosition()).append(Lines.newline);
                sB.append(Enums.WORKS).append(Lines.delimiter).append(s.getWorks() ? Enums.TRUE : Enums.FALSE).append(Lines.newline);
                sB.append(Enums.TRADING_PAIR).append(Lines.delimiter).append(s.getTradingPair()).append(Lines.newline);
                sB.append(Enums.BUY_OR_SELL).append(Lines.delimiter).append(s.getBuyOrSell() == 1 ? Enums.BUY : Enums.SELL).append(Lines.newline);
                sB.append(Enums.LOWER_OR_HIGHER_PRICE).append(Lines.delimiter).append(s.isLowerOrHigherPrices() ? Enums.HIGHER : Enums.LOWER).append(Lines.newline);
                sB.append(Enums.AMOUNT_OF_COINS).append(Lines.delimiter).append(s.getAmountOfCoins()).append(Lines.newline);
                sB.append(Enums.PRICE).append(Lines.delimiter).append(s.getPrice()).append(Lines.newline);
                sB.append(Enums.TAKE_PRICE).append(Lines.delimiter).append(s.getTakePrice()).append(Lines.newline);
                sB.append(Enums.STOP_PRICE).append(Lines.delimiter).append(s.getStopPrice()).append(Lines.newline);
                sB.append(Enums.TRAILING_STOP).append(Lines.delimiter).append(s.getTrailingStop()).append(Lines.newline);
                sB.append(Enums.ON_OR_OFF_TRAILING_STOP).append(Lines.delimiter).append(s.getOnOrOffTS() ? Enums.TRUE : Enums.FALSE).append(Lines.newline);
                sB.append(Enums.FRACTIONAL_PARTS).append(Lines.delimiter).append(s.getFractionalParts()).append(Lines.newline);
                sB.append(Enums.ON_OR_OFF_FRACTIONAL_PARTS).append(Lines.delimiter).append(s.getOnOrOffFP() ? Enums.TRUE : Enums.FALSE).append(Lines.newline);
                sB.append(Enums.BUY_OR_SELL_COINS).append(Lines.delimiter).append(s.getBuyOrSellCoins()).append(Lines.newline);
                sB.append(Enums.NAME_STRATEGY).append(Lines.delimiter).append(s.getNameStrategy()).append(Lines.newline);
                sB.append(Enums.NEXT).append(Lines.newline);
            }
        }
        if (tradedStrategyList.size() > 0) {
            for (StrategyObject s : tradedStrategyList) {
                sB.append(Enums.ID).append(Lines.delimiter).append(s.getClassID()).append(Lines.newline);
                sB.append(Enums.POSITION).append(Lines.delimiter).append(s.getPosition()).append(Lines.newline);
                sB.append(Enums.WORKS).append(Lines.delimiter).append(s.getWorks() ? Enums.TRUE : Enums.FALSE).append(Lines.newline);
                sB.append(Enums.TRADING_PAIR).append(Lines.delimiter).append(s.getTradingPair()).append(Lines.newline);
                sB.append(Enums.BUY_OR_SELL).append(Lines.delimiter).append(s.getBuyOrSell() == 1 ? Enums.BUY : Enums.SELL).append(Lines.newline);
                sB.append(Enums.LOWER_OR_HIGHER_PRICE).append(Lines.delimiter).append(s.isLowerOrHigherPrices() ? Enums.HIGHER : Enums.LOWER).append(Lines.newline);
                sB.append(Enums.AMOUNT_OF_COINS).append(Lines.delimiter).append(s.getAmountOfCoins()).append(Lines.newline);
                sB.append(Enums.PRICE).append(Lines.delimiter).append(s.getPrice()).append(Lines.newline);
                sB.append(Enums.TAKE_PRICE).append(Lines.delimiter).append(s.getTakePrice()).append(Lines.newline);
                sB.append(Enums.STOP_PRICE).append(Lines.delimiter).append(s.getStopPrice()).append(Lines.newline);
                sB.append(Enums.TRAILING_STOP).append(Lines.delimiter).append(s.getTrailingStop()).append(Lines.newline);
                sB.append(Enums.ON_OR_OFF_TRAILING_STOP).append(Lines.delimiter).append(s.getOnOrOffTS() ? Enums.TRUE : Enums.FALSE).append(Lines.newline);
                sB.append(Enums.FRACTIONAL_PARTS).append(Lines.delimiter).append(s.getFractionalParts()).append(Lines.newline);
                sB.append(Enums.ON_OR_OFF_FRACTIONAL_PARTS).append(Lines.delimiter).append(s.getOnOrOffFP() ? Enums.TRUE : Enums.FALSE).append(Lines.newline);
                sB.append(Enums.BUY_OR_SELL_COINS).append(Lines.delimiter).append(s.getBuyOrSellCoins()).append(Lines.newline);
                sB.append(Enums.NAME_STRATEGY).append(Lines.delimiter).append(s.getNameStrategy()).append(Lines.newline);
                sB.append(Enums.NEXT).append(Lines.newline);
            }
        }

        sB.delete(sB.length() - 5, sB.length()).append(Enums.END).append(Lines.newline);
        stoppedStrategyList.clear();
        tradedStrategyList.clear();
        return sB.toString();
    }



    public ArrayList<String> getTradedStrategyList() {
        ArrayList<String> out = new ArrayList<>();
        for (StrategyObject s : arraysOfStrategies.getTradedStrategyListObject()) {
            out.add(transformObjectToString(s));
        }
        if (out.size() < 1) out.add(Lines.thereAreNoStrategiesNow);
        return out;
    }



    public ArrayList<String> getStoppedStrategyList() {
        ArrayList<String> out = new ArrayList<>();
        for (StrategyObject s : arraysOfStrategies.getStoppedStrategyListObject()) {
            out.add(transformObjectToString(s));
        }
        if (out.size() < 1) out.add(Lines.thereAreNoStrategiesNow);
        return out;
    }


    private String transformObjectToString(StrategyObject in) {
        StringBuilder sB = new StringBuilder()
                .append(in.getClassID()).append(Lines.delimiter).append(in.getTradingPair()).append(Lines.delimiter)
                .append(in.getBuyOrSell() == 1 ? Enums.BUY : Enums.SELL).append(Lines.delimiter)
                .append(in.getPosition()).append(Lines.delimiter).append(in.getNameStrategy());
        return sB.toString();
    }
}
