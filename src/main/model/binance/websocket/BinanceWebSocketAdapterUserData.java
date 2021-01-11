package main.model.binance.websocket;

/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */


import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventExecutionReport;
import main.model.binance.datatype.BinanceEventOutboundAccountInfo;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




//@Slf4j
// Пользовательские данные адаптера веб-сокета Binance
public abstract class BinanceWebSocketAdapterUserData  extends WebSocketAdapter {

    private static final Logger log = LoggerFactory.getLogger(BinanceWebSocketAdapterUserData.class);

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

        try {
            String eventType = operation.get("e").getAsString();
            if (eventType.equals("outboundAccountInfo")) {
                onOutboundAccountInfo(new BinanceEventOutboundAccountInfo(operation));
            } else if (eventType.equals("executionReport")) {
                onExecutionReport(new BinanceEventExecutionReport(operation));
            } else {
                log.error("Error in websocket message - unknown event Type");
            }
        } catch (BinanceApiException e) {
            log.error("Error in websocket message {}", e.getMessage());
        }
    }

    public abstract void onOutboundAccountInfo(BinanceEventOutboundAccountInfo event) throws BinanceApiException;
    public abstract void onExecutionReport(BinanceEventExecutionReport event) throws BinanceApiException;
}
