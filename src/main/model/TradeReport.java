package main.model;

import com.google.gson.JsonObject;




public class TradeReport extends Thread {
    private final StrategyObject strategyObject;
    private final JsonObject jsonObject;

    private String symbol;
    private String orderId;
    private String clientOrderId;
    private String transactTime;
    private String price;
    private String origQty;
    private String executedQty;
    private String cummulativeQuoteQty;
    private String status;
    private String timeInForce;
    private String type;
    private String side;
    private String fills;
    private String fillsPrice;
    private String fillsQty;
    private String fillsCommission;
    private String fillsCommissionAsset;
    private String fillsTradeId;



    public TradeReport(StrategyObject strategyObject, JsonObject jsonObject) {
        this.strategyObject = strategyObject;
        this.jsonObject = jsonObject;
        start();
    }



    @Override
    public void run() {
        parseJsonObject();
        nextParseJsonObject();
    }



    // Вошел в покупку или в продажу
    // {"symbol":"ETHBUSD","orderId":1162539234,"orderListId":-1,"clientOrderId":"1388834560",
    // "transactTime":1612369518163,"price":"0.00000000","origQty":"0.00629000","executedQty":"0.00629000",
    // "cummulativeQuoteQty":"9.98921190","status":"FILLED","timeInForce":"GTC","type":"MARKET","side":"BUY",
    // "fills":[{"price":"1588.11000000","qty":"0.00629000","commission":"0.00000629","commissionAsset":"ETH",
    // "tradeId":62138394}]}
    private void parseJsonObject() {
        this.cummulativeQuoteQty = jsonObject.get("cummulativeQuoteQty").getAsString();
        this.clientOrderId = jsonObject.get("clientOrderId").getAsString();
        this.transactTime = jsonObject.get("transactTime").getAsString();
        this.executedQty = jsonObject.get("executedQty").getAsString();
        this.timeInForce = jsonObject.get("timeInForce").getAsString();
        this.orderId = jsonObject.get("orderId").getAsString();
        this.origQty = jsonObject.get("origQty").getAsString();
        this.status = jsonObject.get("status").getAsString();
        this.symbol = jsonObject.get("symbol").getAsString();
        this.price = jsonObject.get("price").getAsString();
        this.fills = jsonObject.get("fills").getAsString();
        this.type = jsonObject.get("type").getAsString();
        this.side = jsonObject.get("side").getAsString();
    }


    private void nextParseJsonObject() {
        String[] strings = fills.replaceAll("\\[\\{", "")
                .replaceAll("}]", "")
                .replaceAll("\"", "")
                .split(",");

        for (String s : strings) {
            if (s.startsWith("price")) {
                this.fillsPrice = s.split(":")[1];
            } else if (s.startsWith("qty")) {
                this.fillsQty = s.split(":")[1];
            } else if (s.startsWith("commission")) {
                this.fillsCommission = s.split(":")[1];
            } else if (s.startsWith("commissionAsset")) {
                this.fillsCommissionAsset = s.split(":")[1];
            } else if (s.startsWith("tradeId")) {
                this.fillsTradeId = s.split(":")[1];
            }
        }
    }
}
