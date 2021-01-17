package main.model;

import java.util.ArrayList;

public class DecipherAndCreateStrategies {
    private ArrayList<ArrayList<String>> listStrategy;

    public DecipherAndCreateStrategies(ArrayList<ArrayList<String>> inList) {
        this.listStrategy = new ArrayList<>(inList);
    }


    private void parseAndCreateStrategyObjects() {

    }

    /*SETTINGS
        DATE_DIFFERENCE===0
        STATUS
        ID===IF_IT_WAS_NOT_CREATED_THROUGH_THE_PROGRAM_THEM_WE_WRITE_DONE_BY_HAND
        WORKS===FALSE
        TRADING_PAIR===ETHBTC
        BUY_OR_SELL===BUY
        AMOUNT_OF_COINS===12.890
        PRICE===0.03498738
        TAKE_PRICE===0.04889889
        STOP_PRICE===0.03389889
        TRAILING_STOP===3.7
        ON_OR_OFF_TRAILING_STOP===OFF
        FRACTIONAL_PARTS===5
        ON_OR_OFF_FRACTIONAL_PARTS===OFF
        BUY_OR_SELL_COINS===123.608
        POSITION===TAKE_OR_STOP_POSITION
        NAME_STRATEGY===AN_EXAMPLE_OF_CREATING_AND_FILLING_IN_STRATEGIES_IN_MANUAL_MODE_IN_A_FILE
        NEXT
        ID===IF_IT_WAS_NOT_CREATED_THROUGH_THE_PROGRAM_THEM_WE_WRITE_DONE_BY_HAND
        WORKS===TRUE
        TRADING_PAIR===LTCBTC
        BUY_OR_SELL===SELL
        AMOUNT_OF_COINS===0.890
        PRICE===0.00349873
        TAKE_PRICE===0.00304889
        STOP_PRICE===0.03589889
        TRAILING_STOP===0.7
        ON_OR_OFF_TRAILING_STOP===OFF
        FRACTIONAL_PARTS===0
        ON_OR_OFF_FRACTIONAL_PARTS===OFF
        BUY_OR_SELL_COINS===0.0
        POSITION===STARTED_POSITION
        NAME_STRATEGY===AN_EXAMPLE_OF_CREATING_AND_FILLING_IN_STRATEGIES_IN_MANUAL_MODE_IN_A_FILE
        END*/
}
