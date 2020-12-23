package main.model.binance.websocket;

import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceEventDepthLevelUpdate;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import org.slf4j.Logger;




//@Slf4j
public abstract class BinanceWebSocketAdapterDepthLevel extends WebSocketAdapter {

    private static final Logger log = LoggerFactory.getLogger(BinanceWebSocketAdapterDepthLevel.class);

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
            onMessage(new BinanceEventDepthLevelUpdate(operation));
        } catch ( BinanceApiException e ) {
            log.error("Error in websocket message {}", e.getMessage());
        }
    }

    public abstract void onMessage(BinanceEventDepthLevelUpdate event) throws BinanceApiException;

}
