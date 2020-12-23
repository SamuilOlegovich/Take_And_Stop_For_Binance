package main.model.binance.websocket;

/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */


import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventAggTrade;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





//@Slf4j
public abstract class BinanceWebSocketAdapterAggTrades extends WebSocketAdapter {

    private static final Logger log = LoggerFactory.getLogger(BinanceWebSocketAdapterAggTrades.class);

    @Override
    public void onWebSocketConnect(Session sess) {
        log.debug("onWebSocketConnect: {}", sess);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        log.error("onWebSocketError: {}", cause);
    }

    @Override
    public void onWebSocketText(String message) {
        log.debug("onWebSocketText message={}", message);
        JsonObject operation = (new Gson()).fromJson(message, JsonObject.class);
        try{
            onMessage(new BinanceEventAggTrade(operation));
        } catch ( BinanceApiException e ) {
            log.error("Error in websocket message {}", e.getMessage());
        }
    }
    public abstract void onMessage(BinanceEventAggTrade event) throws BinanceApiException;
}
