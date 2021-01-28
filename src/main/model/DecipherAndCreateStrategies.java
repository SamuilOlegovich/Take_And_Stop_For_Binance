package main.model;

import java.util.ArrayList;

public class DecipherAndCreateStrategies {
    private final ArrayList<ArrayList<String>> listStrategy;
    private final ArraysOfStrategies arraysOfStrategies;

    public DecipherAndCreateStrategies(ArrayList<ArrayList<String>> inList) {
        this.arraysOfStrategies = Agent.getArraysOfStrategies();
        this.listStrategy = new ArrayList<>(inList);
        parseAndCreateStrategyObjects();
    }


    private void parseAndCreateStrategyObjects() {
        for (ArrayList<String> arrayList : listStrategy) {
            StrategyObject strategyObject = new StrategyObject();
            for (String s : arrayList) {
                String name = s.split(Lines.delimiter)[0];
                String value = s.split(Lines.delimiter)[1];
                if (name.equals(Enums.ID.toString())) {
                    strategyObject.setClassID(value);
                } else if (name.equals(Enums.WORKS.toString())) {
                    strategyObject.setWorks(value.equals(Enums.TRUE.toString()));
                } else if (name.equals(Enums.TRADING_PAIR.toString())) {
                    strategyObject.setTradingPair(value);
                } else if (name.equals(Enums.BUY_OR_SELL.toString())) {
                    strategyObject.setBuyOrSell(value.equals(Enums.BUY.toString()) ? 1 : -1);
                } else if (name.equals(Enums.AMOUNT_OF_COINS.toString())) {
                    strategyObject.setAmountOfCoins(Double.parseDouble(value));
                } else if (name.equals(Enums.PRICE.toString())) {
                    strategyObject.setPrice(Double.parseDouble(value));
                } else if (name.equals(Enums.TAKE_PRICE.toString())) {
                    strategyObject.setTakePrice(Double.parseDouble(value));
                } else if (name.equals(Enums.STOP_PRICE.toString())) {
                    strategyObject.setStopPrice(Double.parseDouble(value));
                } else if (name.equals(Enums.TRAILING_STOP.toString())) {
                    strategyObject.setTrailingStop(Double.parseDouble(value));
                } else if (name.equals(Enums.ON_OR_OFF_TRAILING_STOP.toString())) {
                    strategyObject.setOnOrOffTS(value.equals(Enums.TRUE.toString()));
                } else if (name.equals(Enums.FRACTIONAL_PARTS.toString())) {
                    strategyObject.setFractionalParts(Integer.parseInt(value));
                } else if (name.equals(Enums.ON_OR_OFF_FRACTIONAL_PARTS.toString())) {
                    strategyObject.setOnOrOffFP(value.equals(Enums.TRUE.toString()));
                } else if (name.equals(Enums.BUY_OR_SELL_COINS.toString())) {
                    strategyObject.setBuyOrSellCoins(Double.parseDouble(value));
                } else if (name.equals(Enums.LOWER_OR_HIGHER_PRICE.toString())) {
                    strategyObject.setLowerOrHigherPrices(value.endsWith(Enums.HIGHER.toString()));
                } else if (name.equals(Enums.POSITION.toString())) {
                    for (Position p : Position.values()) {
                        if (value.equals(p.toString())) {
                            strategyObject.setPosition(p);
                            break;
                        }
                    }
                } else if (name.equals(Enums.NAME_STRATEGY.toString())) { strategyObject.setNameStrategy(value); }
            }

            if (strategyObject.getClassID().equals(Enums.DONE_BY_HAND.toString())) {
                strategyObject.setPosition(Position.STARTED_POSITION);
                strategyObject.setClassID();
            }

            if (!strategyObject.getPosition().equals(Position.EXAMPLE_POSITION)) {
                arraysOfStrategies.addToAllStrategyList(strategyObject, false);
            }
        }
    }
}
