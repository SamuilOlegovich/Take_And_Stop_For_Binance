package main.model.binance.api;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.common.collect.ImmutableSet;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import lombok.Data;
import lombok.Getter;
import main.model.Agent;
import main.model.binance.datatype.*;
import main.model.binance.websocket.*;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import org.sellcom.core.Strings;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.net.URISyntaxException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.net.URI;


//@Slf4j
@Data
public class BinanceAPI {

    @Getter
    private static final Logger log = LoggerFactory.getLogger(BinanceAPI.class);

    /* Actual API key and Secret Key that will be used == Фактический ключ API и секретный ключ,
    *       который будет использоваться
    * */
    public String secretKey;
    public String apiKey;

    /**
     * API Base URL
     */
    public String baseUrl = "https://www.binance.com/api/";
    /**
     * Old W-API Base URL. Might not function well at that moment, please use modern wapi3 API instead
     */
    public String baseWapiUrl = "https://www.binance.com/wapi/";
    /**
     * W-API3 Base URL.
     */
    public String baseWapi3 = "https://api.binance.com/wapi/v3";
    /**
     * Base URL for websockets
     */
    public String webSocketBaseUrl = "wss://stream.binance.com:9443/ws/";

    /**
     * Guava Class Instance for escaping
     */
    private Escaper esc = UrlEscapers.urlFormParameterEscaper();

    /**
     * Constructor of API when you exactly know the keys
     * @param apiKey Public API Key
     * @param secretKey Secret API Key
     * @throws BinanceApiException in case of any error
     */
    public BinanceAPI(String apiKey, String secretKey) throws BinanceApiException {
        this.secretKey = secretKey;
        this.apiKey = apiKey;
        validateCredentials();
    }

    /**
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     */
    public BinanceAPI() {
        BinanceConfig config = new BinanceConfig();
        this.secretKey = config.getVariable("BINANCE_SECRET_KEY");
        this.apiKey = config.getVariable("BINANCE_API_KEY");
        // если нет файла конфигураций (пропертис) то берем данные апи и ключей из класса API
        if (apiKey.length() == secretKey.length()) {
            this.secretKey = Agent.getApi().getSECRET_KEY();
            this.apiKey = Agent.getApi().getAPI_KEY();
        }
    }

    /**
     * Validation we have API keys set up
     * @throws BinanceApiException in case of any error
     */
    protected void  validateCredentials() throws BinanceApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new BinanceApiException("Missing BINANCE_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new BinanceApiException("Missing BINANCE_SECRET_KEY. " + humanMessage);
    }



    // - - - - - - - - - - - - - - - - - - - - - - - -
    // GENERAL ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -



    /**
     * Checking connectivity,
     * @return empty object
     * @throws BinanceApiException in case of any error
     */
    public JsonObject ping() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ping")).read().asJsonObject();
    }

    /**
     * Checking server time,
     * @return JsonObject, expected { serverTime: 00000 }
     * @throws BinanceApiException in case of any error
     */
    public JsonObject time() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/time")).read().asJsonObject();
    }



    // - - - - - - - - - - - - - - - - - - - - - - - -
    // MARKET ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -



    /**
     * 24hr ticker price change statistics
     * 24-часовая статистика изменения цен тикера
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @return result in JSON
     * @throws BinanceApiException in case of any error
     * глубина
     */
    public JsonObject depth(BinanceSymbol symbol) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/depth?symbol=" + symbol.get())).read().asJsonObject();
    }

    /**
     * 24hr ticker price change statistics, with limit explicitly set
     * 24-часовая статистика изменения цен тикера с явно установленным лимитом
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @param limit numeric limit of results
     * @return result in JSON
     * @throws BinanceApiException in case of any error
     * глубина
     */
    public JsonObject depth(BinanceSymbol symbol, int limit) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/depth?symbol=" + symbol.get() + "&limit=" + limit))
                .read().asJsonObject();
    }

    /**
     * Get compressed, aggregate trades and map result into BinanceAggregatedTrades type for better readability
     * Получите сжатые, агрегированные сделки и сопоставьте результат с типом BinanceAggregatedTrades для лучшей читаемости
     * Trades that fill at the time, from the same order, with the same price will have the quantity aggregated.
     * Сделки, которые выполняются одновременно из одного и того же ордера по одинаковой цене, будут агрегированы
     * Allowed options - fromId, startTime, endTime.
     * Допустимые параметры - fromId, startTime, endTime.
     * If both startTime and endTime are sent, limit should not be sent AND the distance between startTime and endTime must be less than 24 hours.
     * Если отправляются и startTime, и endTime, ограничение не должно отправляться И расстояние между startTime и endTime должно быть меньше 24 часов.
     * If fromId, startTime, and endTime are not sent, the most recent aggregate trades will be returned.
     * Если fromId, startTime и endTime не отправлены, будут возвращены самые последние совокупные сделки.
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @param limit numeric limit of results
     * @param options map of additional properties. leave null if not needed
     * @return list of aggregated trades
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol, int limit, Map<String, Long> options) throws BinanceApiException {
        String u = baseUrl + "v1/aggTrades?symbol=" + symbol.get() + "&limit=" + limit;
        if (options != null) {
            for (String optionKey : options.keySet()) {
                if (!optionKey.equals("fromId") && !optionKey.equals("startTime") && !optionKey.equals("endTime")) {
                    throw new BinanceApiException("Invalid aggTrades option, only fromId, startTime, endTime are allowed");
                }
                u += "&" + optionKey + "=" + options.get(optionKey);
            }
        }
        String lastResponse = (new BinanceRequest(u)).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceAggregatedTrades>>() {}.getType();
        return new Gson().fromJson(lastResponse, listType);
    }

    /**
     * short version of aggTrades with less parameters
     * короткая версия aggTrades с меньшим количеством параметров
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @param options map of additional properties. leave null if not needed
     * @return list of aggregated trades
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol, Map<String, Long> options) throws BinanceApiException {
        return this.aggTrades(symbol, 500, options);
    }

    /**
     * short version of aggTrades with less parameters
     * короткая версия aggTrades с меньшим количеством параметров
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @return list of aggregated trades
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol) throws BinanceApiException {
        return this.aggTrades(symbol, 500, null);
    }

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     * Клайн / свечные бары для символа. Klines однозначно идентифицируются по времени открытия.
     * if startTime and endTime are not sent, the most recent klines are returned.
     * если startTime и endTime не отправляются, возвращаются самые последние klines.
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @param interval valid time interval, see BinanceInterval enum
     * @param limit numeric limit of results
     * @param options options map of additional properties. leave null if not needed
     * @return list of candlesticks
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceCandlestick> klines(BinanceSymbol symbol, BinanceInterval interval, int limit, Map<String, Long> options) throws BinanceApiException {
        String u = baseUrl + "v1/klines?symbol=" + symbol.get() + "&interval=" + interval.toString() + "&limit=" + limit;
        if (options != null) {
            for (String optionKey : options.keySet()) {
                if (!optionKey.equals("startTime") && !optionKey.equals("endTime")) {
                    throw new BinanceApiException("Invalid klines option, only startTime, endTime are allowed");
                }
                u += "&" + optionKey + "=" + options.get(optionKey);
            }
        }
        JsonArray jsonElements = (new BinanceRequest(u)).read().asJsonArray();
        List<BinanceCandlestick> list = new LinkedList<>();
        for (JsonElement e : jsonElements) list.add(new BinanceCandlestick(e.getAsJsonArray()));
        return list;
    }

    /**
     * short version of klines() with less parameters
     * короткая версия klines () с меньшим количеством параметров
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @param interval  valid time interval, see BinanceInterval enum
     * @return list of candlesticks
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceCandlestick> klines(BinanceSymbol symbol, BinanceInterval interval) throws BinanceApiException {
        return klines(symbol, interval, 500, null);
    }


    /**
     * get public statistics on Binance markets
     * получать публичную статистику по рынкам Binance
     * This is stated to be a temporary solution - not a part of API documentation yet
     * Заявлено, что это временное решение - еще не часть документации API
     *
     * @return BinanceExchangeStat
     * @throws BinanceApiException in case of any error
     */
    public BinanceExchangeStats publicStats() throws BinanceApiException {
        JsonObject jsonObject = new BinanceRequest("https://www.binance.com/exchange/public/product")
                .read().asJsonObject();
        return new BinanceExchangeStats(jsonObject);
    }

    /**
     * Exchange info - information about open markets
     * Информация о бирже - информация об открытых рынках
     *
     * @return BinanceExchangeInfo
     * @throws BinanceApiException in case of any error
     */
    public BinanceExchangeInfo exchangeInfo() throws BinanceApiException {
        JsonObject jsonObject = (new BinanceRequest(baseUrl + "v1/exchangeInfo")).read().asJsonObject();
        return new BinanceExchangeInfo(jsonObject);
    }

    /**
     * 24hr ticker price change statistics
     * 24-часовая статистика изменения цен тикера
     *
     * @return json array with prices for all symbols
     * @throws BinanceApiException in case of any error
     */
    public JsonArray ticker24hr() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/24hr" )).read().asJsonArray();
    }

    /**
     * 24hr ticker price change statistics
     * 24-часовая статистика изменения цен тикера
     *
     * @param symbol Symbol pair, i.e. BNBBTC
     * @return json with prices
     * @throws BinanceApiException in case of any error
     */
    public JsonObject ticker24hr(BinanceSymbol symbol) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/24hr?symbol=" + symbol.get())).read().asJsonObject();
    }


    /**
     * Latest price for all symbols
     * Последняя цена для всех символов
     *
     * @return raw JSON Array of all prices
     * @throws BinanceApiException  in case of any error
     */
    public JsonArray allPrices() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/allPrices")).read().asJsonArray();
    }

    /**
     * Latest price for all symbols -
     * Последняя цена для всех символов
     *
     * @return Map of big decimals
     * @throws BinanceApiException in case of any error
     */
    public Map<String, BigDecimal> pricesMap() throws BinanceApiException {
        Map<String, BigDecimal> map = new ConcurrentHashMap<>();
        for (JsonElement elem : allPrices()) {
            JsonObject obj = elem.getAsJsonObject();
            map.put(obj.get("symbol").getAsString(), obj.get("price").getAsBigDecimal());
        }
        return map;
    }

    /**
     * Get best price/qty on the order book for all symbols.
     * Получите лучшую цену / количество в книге заказов для всех символов.
     *
     * @return JsonArray
     * @throws BinanceApiException in case of any error
     */
    public JsonArray allBookTickers() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/allBookTickers")).read().asJsonArray();
    }

    /**
     * Get best price/qty on the order book for all symbols.
     * Получите лучшую цену / количество в книге заказов для всех символов.
     *
     * @return map of BinanceTicker
     * @throws BinanceApiException in case of any error
     */
    public Map<String, BinanceTicker> allBookTickersMap() throws BinanceApiException {
        String lastResponse = (new BinanceRequest(baseUrl + "v1/ticker/allBookTickers")).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceTicker>>() {}.getType();

        Map<String, BinanceTicker> mapTickers = new ConcurrentHashMap<>();
        List<BinanceTicker> ticker = new Gson().fromJson(lastResponse, listType);
        for (BinanceTicker t : ticker) { mapTickers.put(t.getSymbol(), t); }
        return mapTickers;
    }

    // получить монеты
    public Set<String> getCoinsOf(String coin) {
        try {
            BinanceExchangeStats binanceExchangeStats = this.publicStats();
            return binanceExchangeStats.getCoinsOf(coin.toUpperCase());
        } catch (Exception e) { log.error("BINANCE UNCAUGHT EXCEPTION {}", e);
        } catch (BinanceApiException e) { log.warn("BINANCE ERROR {}", e.getMessage()); }
        return ImmutableSet.of();
    }



    // - - - - - - - - - - - - - - - - - - - - - - - -
    // ACCOUNT READ-ONLY ENDPOINTS
    // КОНЕЧНЫЕ ТОЧКИ ТОЛЬКО ДЛЯ ЧТЕНИЯ АККАУНТА
    // - - - - - - - - - - - - - - - - - - - - - - - -



    /**
     * Getting account information
     * Получение информации об аккаунте
     *
     * @return JsonObject
     * @throws BinanceApiException in case of any error
     */
    public JsonObject account() throws BinanceApiException {
        return new BinanceRequest(baseUrl + "v3/account")
                .sign(apiKey, secretKey, null).read().asJsonObject();
    }

    /**
     * Getting balances - part of account information
     * Получение остатков - часть информации о счете
     *
     * @return JsonArray
     * @throws BinanceApiException in case of any error
     */
    public JsonArray balances() throws BinanceApiException {
        return account().get("balances").getAsJsonArray();
    }

    /**
     * Getting balances - part of account information
     * Получение остатков - часть информации о счете
     *
     * @return map of wallet main.main.model.model.assets structure
     * @throws BinanceApiException  in case of any error
     */
    public Map<String, BinanceWalletAsset> balancesMap() throws BinanceApiException {
        Map<String, BinanceWalletAsset> mapAssets = new ConcurrentHashMap<>();
        for (JsonElement el : balances()) {
            BinanceWalletAsset w = new BinanceWalletAsset(el.getAsJsonObject());
            mapAssets.put(w.getAsset(), w);
        }
        return mapAssets;
    }

    /**
	 * Get all my open orders. <strong>Can use up a lot of Binance Weight. Use with caution.</strong>
     * Получите все мои открытые заказы. <strong> Может использовать много веса Binance. Используйте с осторожностью. </strong>
     *
	 * {@link //https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md#current-open-orders-user_data}
	 * 
	 * @return List of Orders
	 * @throws BinanceApiException in case of any error
	 */
	public List<BinanceOrder> allOpenOrders() throws BinanceApiException {
		String u = baseUrl + "v3/openOrders";
		String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
		Type listType = new TypeToken<List<BinanceOrder>>() {}.getType();
		return new Gson().fromJson(lastResponse, listType);
	}
    
    /**
     * Get all open orders on a symbol.
     * Получить все открытые ордера по символу.
     *
     * @param symbol i.e. BNBBTC
     * @return List of Orders
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceOrder> openOrders(BinanceSymbol symbol) throws BinanceApiException {
        String u = baseUrl + "v3/openOrders?symbol=" + symbol.toString();
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceOrder>>() {}.getType();
        return new Gson().fromJson(lastResponse, listType);
    }
    /**
     * Get all orders on a symbol; active, canceled, or filled.
     * If orderId is set (not null and greater than 0), it will get orders greater or equal than orderId.
     * Otherwise most recent orders are returned.
     * Получить все заказы по символу; активен, отменен или заполнен.
     * Если orderId установлен (не нуль и больше 0), он получит заказы больше или равные, чем orderId.
     * В противном случае возвращаются самые последние заказы.
     *
     * @param symbol i.e. BNBBTC
     * @param orderId numeric Order ID
     * @param limit numeric limit of orders in result
     * @return List of Orders
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceOrder> allOrders(BinanceSymbol symbol, Long orderId, int limit) throws BinanceApiException {
        String u = baseUrl + "v3/allOrders?symbol=" + symbol.toString() + "&limit=" + limit;
        if (orderId != null && orderId > 0) u += "&orderId=" + orderId;
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceOrder>>() {}.getType();
        return new Gson().fromJson(lastResponse, listType);
    }

    /**
     * short version of allOrders
     * краткая версия allOrders
     *
     * @param symbol i.e. BNBBTC
     * @return list of orders
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceOrder> allOrders(BinanceSymbol symbol) throws BinanceApiException {
        return allOrders(symbol, 0L, 500);
    }
    /**
     * Get trades for a specific account and symbol.
     * Получайте сделки для определенного счета и символа
     *
     * @param symbol i.e. BNBBTC
     * @param orderId numeric order ID
     * @param limit numeric limit of results
     * @return list of trades
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceTrade> myTrades(BinanceSymbol symbol, Long orderId, int limit) throws BinanceApiException {
        String u = baseUrl + "v3/myTrades?symbol=" + symbol.toString() + "&limit=" + limit;
        if (orderId != null && orderId > 0) u += "&orderId=" + orderId;
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceTrade>>() {}.getType();
        return new Gson().fromJson(lastResponse, listType);
    }

    /**
     * short version of myTrades(symbol, orderId, limit)
     * сокращенная версия myTrades (символ, orderId, лимит)
     *
     * @param symbol i.e. BNBBTC
     * @return list of trades
     * @throws BinanceApiException in case of any error
     */
    public List<BinanceTrade> myTrades(BinanceSymbol symbol) throws BinanceApiException {
        return myTrades(symbol, 0L, 500);
    }

    /**
     * Get Order Status
     * Получить статус заказа
     *
     * @param symbol i.e. BNBBTC
     * @param orderId numeric order ID
     * @return BinanceOrder object if success full
     * @throws BinanceApiException in case of any error
     */
    public BinanceOrder getOrderById(BinanceSymbol symbol, Long orderId ) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&orderId=" + orderId;
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        return (new Gson()).fromJson(lastResponse, BinanceOrder.class);
    }

    /**
     * @param symbol i.e. BNBBTC
     * @param origClientOrderId Custom Order ID, generated by client
     * @return BinanceOrder object if success full
     * @throws BinanceApiException in case of any error
     */
    public BinanceOrder getOrderByOrigClientId(BinanceSymbol symbol, String origClientOrderId)  throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&origClientOrderId=" + esc.escape(origClientOrderId);
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        return (new Gson()).fromJson(lastResponse, BinanceOrder.class);
    }

    /**
     * Getting order from order object. A wrapper for getOrderById(symbol, orderId)
     * In face, used to refresh information on existing order
     * Получение заказа от объекта заказа. Обертка для getOrderById (symbol, orderId)
     * Лицом к лицу, используется для обновления информации о существующем заказе
     *
     * @param order existing order structure
     * @return existing BinanceOrder record
     * @throws BinanceApiException  in case of any error
     */
    public BinanceOrder getOrder(BinanceOrder order)  throws BinanceApiException {
        return getOrderById(BinanceSymbol.valueOf(order.getSymbol()), order.getOrderId() );
    }



    // - - - - - - - - - - - - - - - - - - - - - - - -
    // TRADING ENDPOINTS
    // КОНЕЧНЫЕ ТОЧКИ ТОРГОВЛИ
    // - - - - - - - - - - - - - - - - - - - - - - - -



    /**
     * @param orderPlacement class for order placement
     * @return json result from order placement
     * @throws BinanceApiException in case of any error
     */
    public JsonObject createOrder(BinanceOrderPlacement orderPlacement)  throws BinanceApiException {
        String u = baseUrl + "v3/order?" + orderPlacement.getAsQuery();
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).post().read().asJsonObject();
    }

    /**
     * @param orderPlacement class for order placement
     * @return json result from order placement
     * @throws BinanceApiException in case of any error
     */
    public JsonObject testOrder(BinanceOrderPlacement orderPlacement)  throws BinanceApiException {
        String u = baseUrl + "v3/order/test?" + orderPlacement.getAsQuery();
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).post().read().asJsonObject();
    }

    /**
     * Deletes order by order ID
     * Удаляет заказ по идентификатору заказа
     *
     * @param symbol i.e. "BNBBTC"
     * @param orderId numeric Order ID
     * @return json result from order placement
     * @throws BinanceApiException in case of any error
     */
    public JsonObject deleteOrderById(BinanceSymbol symbol, Long orderId) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&orderId=" + orderId;
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).delete().read().asJsonObject();
    }
    /**
     * Deletes order by original client ID
     * Удаляет заказ по исходному идентификатору клиента
     *
     * @param symbol i.e. "BNBBTC"
     * @param origClientOrderId string order ID, generated by client
     * @return json result
     * @throws BinanceApiException in case of any error
     */
    public JsonObject deleteOrderByOrigClientId(BinanceSymbol symbol, String origClientOrderId) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&origClientOrderId=" + esc.escape(origClientOrderId);
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).delete().read().asJsonObject();
    }

    /**
     * Deletes order by new client ID
     * Удаляет заказ по новому идентификатору клиента
     *
     * @param symbol i.e. "BNBBTC"
     * @param newClientOrderId string order ID, generated by server
     * @return json result
     * @throws BinanceApiException in case of any error
     */
    public JsonObject deleteOrderByNewClientId(BinanceSymbol symbol, String newClientOrderId ) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&newClientOrderId=" + esc.escape(newClientOrderId);
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).delete().read().asJsonObject();
    }

    /**`
     * Deletes order by BinanceOrder object
     * Удалить ордер по объекту Binance Order
     *
     * @param order object of existing order
     * @return json result
     * @throws BinanceApiException in case of any error
     */
    public JsonObject deleteOrder(BinanceOrder order) throws BinanceApiException {
        BinanceSymbol symbol = BinanceSymbol.valueOf(order.getSymbol());
        if (!Strings.isNullOrEmpty(order.getClientOrderId())) {
            return deleteOrderByOrigClientId(symbol, order.getClientOrderId());
        }
        return deleteOrderById(symbol, order.getOrderId());
    }



    // - - - - - - - - - - - - - - - - - - - - - - - -
    // USER DATA STREAM
    // ПОТОК ДАННЫХ ПОЛЬЗОВАТЕЛЯ
    // - - - - - - - - - - - - - - - - - - - - - - - -



    /**
     * Start user data stream, get a key for web socket
     * Запустить поток данных пользователя, получить ключ для web socket
     *
     * @return listenKey - key that could be used to manage stream
     * @throws BinanceApiException in case of any error
     */
    public String startUserDataStream() throws BinanceApiException {
        JsonObject jsonObject = (new BinanceRequest(baseUrl + "v1/userDataStream"))
                .sign(apiKey).post().read().asJsonObject();
        return jsonObject.get("listenKey").getAsString();
    }

    /**
     * Keep user data stream alive
     * Поддерживайте поток пользовательских данных живым
     *
     * @param listenKey - key that could be used to manage stream
     *                 ключ, который можно использовать для управления потоком
     * @return json result
     * @throws BinanceApiException in case of any error
     */
    public JsonObject keepUserDataStream(String listenKey) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/userDataStream?listenKey=" + esc.escape(listenKey)))
                .sign(apiKey).put().read().asJsonObject();
    }

    /**
     * Close user data stream
     * Закрыть поток пользовательских данных
     *
     * @param listenKey key for user stream management
     * @return json result
     * @throws BinanceApiException in case of any error
     */
    public JsonObject deleteUserDataStream(String listenKey) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/userDataStream?listenKey=" + esc.escape(listenKey)))
                .sign(apiKey).delete().read().asJsonObject();
    }



    // - - - - - - - - - - - - - - - - - - - - - - - -
    // WEB-SOCKET ENDPOINTS
    // КОНЕЧНЫЕ ТОЧКИ ВЕБ-РОЗЕТКА
    // - - - - - - - - - - - - - - - - - - - - - - - -



    /**
     * Base method for all websockets streams
     * Базовый метод для всех потоков веб-сокетов
     *
     * @param url derived methods will use unique base url
     * @param adapter  class to handle the event
     * @return web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session getWebSocketSession(String url, WebSocketAdapter adapter) throws BinanceApiException {
        try {
            URI uri = new URI(webSocketBaseUrl + url);
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setTrustAll(true); // The magic
            WebSocketClient client = new WebSocketClient(sslContextFactory);
            client.start();
            return client.connect(adapter, uri).get();
        } catch (URISyntaxException e) { throw new BinanceApiException("URL Syntax error: " + e.getMessage());
        } catch (Throwable e) { throw new BinanceApiException("Websocket error: " + e.getMessage()); }
    }

    /**
     * Depth Websocket Stream Listener
     * Слушатель потока веб-сокетов глубины
     *
     * @param symbol i.e. "BNBBTC"
     * @param adapter class to handle the event
     * @return web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session webSocketDepth(BinanceSymbol symbol, BinanceWebSocketAdapterDepth adapter) throws BinanceApiException {
        return getWebSocketSession(symbol.toString().toLowerCase() + "@depth", adapter);
    }

    /**
     * Depth Web-Socket Stream Listener - best 20 levels
     * Слушатель потока Web-сокетов глубины - 20 лучших уровней
     *
     * @param symbol i.e. "BNBBTC"
     * @param adapter class to handle the events
     * @return web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session webSocketDepth20(BinanceSymbol symbol, BinanceWebSocketAdapterDepthLevel adapter) throws BinanceApiException {
        return getWebSocketSession(symbol.toString().toLowerCase() + "@depth20", adapter);
    }

    /**
     * Depth Websocket Stream Listener - best 10 levels
     * Слушатель потока Web-сокетов глубины - 10 лучших уровней
     *
     * @param symbol i.e. "BNBBTC"
     * @param adapter  class to handle the events
     * @return  web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session webSocketDepth10(BinanceSymbol symbol, BinanceWebSocketAdapterDepthLevel adapter) throws BinanceApiException {
        return getWebSocketSession(symbol.toString().toLowerCase() + "@depth10", adapter);
    }

    /**
     * Depth Websocket Stream Listener - best 5 lavels
     * Слушатель потока Web-сокетов глубины - 5 лучших уровней
     *
     * @param symbol i.e. "BNBBTC"
     * @param adapter class to handle the events
     * @return web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session webSocketDepth5(BinanceSymbol symbol, BinanceWebSocketAdapterDepthLevel adapter) throws BinanceApiException {
        return getWebSocketSession(symbol.toString().toLowerCase() + "@depth5", adapter);
    }

    /**
     * Klines Web-Socket Stream Listener
     * Кровать слушателя потока Web-сокетов
     *
     * @param symbol i.e. "BNBBTC"
     * @param interval  valid time interval, see BinanceInterval enum
     * @param adapter class to handle the events
     * @return web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session webSocketKlines(BinanceSymbol symbol, BinanceInterval interval, BinanceWebSocketAdapterKline adapter) throws BinanceApiException {
        return getWebSocketSession(symbol.toString().toLowerCase() + "@kline_" + interval.toString(), adapter);
    }

    /**
     * Trades Websocket Stream Listener
     * Торговый слушателя потока Web-сокетов
     *
     * @param symbol i.e. "BNBBTC"
     * @param adapter class to handle the events
     * @return web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session webSocketTrades(BinanceSymbol symbol, BinanceWebSocketAdapterAggTrades adapter) throws BinanceApiException {
        return getWebSocketSession(symbol.toString().toLowerCase() + "@aggTrade", adapter);
    }

    /**
     * User Data Websocket Stream Listener
     * Пользовательские данные слушателя потока Web-сокетов
     *
     * @param listenKey string, received in startUserDataStream()
     * @param adapter class to handle the event
     * @return web socket session
     * @throws BinanceApiException in case of any error
     */
    public Session webSocket(String listenKey, BinanceWebSocketAdapterUserData adapter) throws BinanceApiException {
        return getWebSocketSession(listenKey, adapter);
    }

    /**
     * Withdrawal APIs - might not work yet
     * API вывода средств - может еще не работать
     *
     * @param asset string of asset name, i.e. "BNB"
     * @param address string of destination wallet address
     * @param amount amount to be withdrawn
     * @param name label of destination address, can be left empty
     * @return empty json object if success
     * @throws BinanceApiException in case of any error
     */
    public JsonObject withdraw(String asset, String address, long amount, String name) throws BinanceApiException {
        String u = baseWapiUrl + "/v1/withdraw.html?asset=" + esc.escape(asset) +
                "&address=" + esc.escape(address) + "&amount=" + amount;
        if (!Strings.isNullOrEmpty(name)) { u += "name=" + esc.escape(name); }
        return (new BinanceRequest(u)).sign(apiKey).post().read().asJsonObject();
    }

    /**
     * Getting history of withdrawals - might not work yet.
     * So far response is string. at the moment of writing
     * there is a response in Chinese about parameter exception (which cannot be parsed by JSON),
     * and someone seems to still work on that part of server side
     * Получение истории выводов - может пока не работать.
     * Пока ответ - строка. на момент написания
     * есть ответ на китайском языке об исключении параметра (которое не может быть проанализировано JSON),
     * и кто-то, кажется, все еще работает на этой части серверной части
     *
     * @param historyFilter structure for user's history filtration
     * @return Temporary returns String until WAPI will be fixed
     * @throws BinanceApiException in case of any error
     */
    public String getWithdrawHistory(BinanceHistoryFilter historyFilter) throws BinanceApiException {
        String q = historyFilter.getAsQuery();
        String u = baseWapiUrl + "v1/getWithdrawHistory.html" + (Strings.isNullOrEmpty(q) ? "": ("?" + q));
        return (new BinanceRequest(u)).sign(apiKey).post().read().getLastResponse();
    }

    /**
     * Getting history of deposits - might not work yet
     * Получение истории вкладов - пока может не работать
     * So far response is string. at the moment of writing
     * there is a response in Chinese about parameter exception (which cannot be parsed by JSON),
     * and someone seems to still work on that part of server side
     *
     * @param historyFilter structure for user's history filtration
     * @return Temporary returns String until WAPI will be fixed
     * @throws BinanceApiException in case of any error
     */
    public String getDepositHistory(BinanceHistoryFilter historyFilter) throws BinanceApiException {
        String q = historyFilter.getAsQuery();
        String u = baseWapiUrl + "v1/getDepositHistory.html" + (Strings.isNullOrEmpty(q) ? "": ("?" + q));
        return (new BinanceRequest(u)).sign(apiKey).post().read().getLastResponse();
    }

    /**
     * Getting status of the system
     * Получение статуса системы
     * @return Temporary returns JsonObject
     * @throws BinanceApiException in case of any error
     */
    public JsonObject getSystemStatus() throws BinanceApiException {
        String u = baseWapi3 + "/systemStatus.html";
        return (new BinanceRequest(u)).read().asJsonObject();
    }

    protected boolean canEqual(final Object other) { return other instanceof BinanceAPI; }

    /////////////////////////////////////    I had to add == пришлось добавить   ///////////////////////////////////////

    private String getSecretKey() { return secretKey; }

    private String getApiKey() { return apiKey; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}


