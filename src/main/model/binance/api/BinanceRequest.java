package main.model.binance.api;
/* ============================================================
 * java-test.resources.model.binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import lombok.Data;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.sellcom.core.Strings;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import javax.net.ssl.HttpsURLConnection;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import javax.net.ssl.X509TrustManager;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import javax.crypto.Mac;
import javax.xml.bind.DatatypeConverter;
import java.net.URL;
import java.util.*;
import java.io.*;




// Запрос Binance
@Data
//@Slf4j
public class BinanceRequest {
    private static final Logger log = LoggerFactory.getLogger(BinanceRequest.class);

    public String userAgent = "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0";
    public HttpsURLConnection conn = null;
    public String lastResponse = "";
    public String requestUrl = "";
    public String method = "GET";

    public String secretKey = "";
    public String apiKey = "";

    public Map<String, String> headers = new HashMap<>();

    // Internal JSON parser
    // Внутренний анализатор JSON
    private JsonParser jsonParser = new JsonParser();
    private String requestBody = "";



    // Creating public request
    // Создание публичного запроса
    public BinanceRequest(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public BinanceRequest() {
    }

    // HMAC encoding
    // Кодировка HMAC
    public static String encode(String key, String data) {
//        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
//        sha256_HMAC.init(secret_key);
//        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8"))); //////////////////////////////
//        return (Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8")))).toString(); /////////////////////////
//        return Arrays.toString((Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes("UTF-8")))));

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return DatatypeConverter.printHexBinary(sha256_HMAC.doFinal(data.getBytes()));
        } catch (Exception e) {
            new BinanceApiException("Что-то не то с кодированием HmacSHA256 "
                    + "-> class BinanceRequest -> method encode() -> "
                    + e.getStackTrace());
//            new BinanceApiException("Encryption error " + e.getMessage());
        }
        System.exit(0);
        return null;
    }

    /**
     * Requests signing - with public and secret key
     * Подписание заявки - с открытым и секретным ключом
     *
     * @param apiKey string of public API Key == строка открытого ключа API
     * @param secretKey string of secret Key == строка секретного ключа
     * @param options map of additional parameters to include == карта дополнительных параметров для включения
     * @return this request object == этот объект запроса
     * @throws BinanceApiException in case of any error == в случае любой ошибки
     */
    public BinanceRequest sign(String apiKey, String secretKey, Map<String, String> options) throws BinanceApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(apiKey))
            throw new BinanceApiException("Missing BINANCE_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(secretKey))
            throw new BinanceApiException("Missing BINANCE_SECRET_KEY. " + humanMessage);

        if (!Strings.isNullOrEmpty(secretKey) && !requestUrl.contains("&signature=")) {
            List<String> list = new LinkedList<>();
            if (options != null) {
                for (String key : options.keySet()) {
                    list.add(key + "=" + options.get(key));
                }
            }
            list.add("recvWindow=" + 5000);
            list.add("timestamp=" + String.valueOf(new Date().getTime()));
            String queryToAdd = String.join("&", list);
            String query = "";
            log.debug("Signature: RequestUrl = {}", requestUrl);
            if (requestUrl.contains("?")) {
                query = requestUrl.substring(requestUrl.indexOf('?') + 1) + "&";
            }
            query = query.concat(queryToAdd);

            log.debug("Signature: query to be included  = {} queryToAdd={}", query, queryToAdd);
//            try {
                // set the HMAC hash header
                String signature = encode(secretKey, query);
//                System.out.println(secretKey);
//                System.out.println(query);
//                System.out.println(signature);
                String concatenator = requestUrl.contains("?") ? "&" : "?";
                requestUrl += concatenator + queryToAdd + "&signature=" + signature;
//            } catch (Exception e ) {
//                throw new BinanceApiException("Encryption error " + e.getMessage());
//            }
        }
        headers.put("X-MBX-APIKEY", apiKey);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return this;
    }

    /**
     * Requests signing - just with a public key
     * Подписание запросов - просто открытым ключом
     *
     * @param apiKey public key string
     * @return this request object
     * @throws BinanceApiException in case of any error
     */
    public BinanceRequest sign(String apiKey) throws BinanceApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(apiKey))
            throw new BinanceApiException("Missing BINANCE_API_KEY. " + humanMessage);

        headers.put("X-MBX-APIKEY", apiKey);
        headers.put("Content-Type", " application/x-www-form-urlencoded");
        return this;
    }

    /**
     * Settings method as post, keeping interface fluid
     * Метод настройки как пост, сохраняя интерфейс жидкости
     *
     * @param
     * @return this request object == этот объект запроса
     */
    public BinanceRequest post() {
        this.setMethod("POST");
        return this;
    }

    /**
     * Settings method as PUT, keeping interface fluid
     * Метод настроек как PUT, сохраняющий интерфейс жидкости
     *
     * @return this request object == этот объект запроса
     */
    public BinanceRequest put() {
        this.setMethod("PUT");
        return this;
    }


    /**
     * Settings method as DELETE, keeping interface fluid
     * Метод настроек как УДАЛИТЬ, сохраняя интерфейсную жидкость
     *
     * @return this request object == этот объект запроса
     */
    public BinanceRequest delete() {
        this.setMethod("DELETE");
        return this;
    }

    /**
     * Opens HTTPS connection and save connection Handler
     * Открыть HTTPS-соединение и сохранение обработчика соединения
     *
     * @return this request object
     * @throws BinanceApiException in case of any error
     */
    public BinanceRequest connect() throws BinanceApiException {
        SSLContext sc = null;
        URL url = null;

        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    System.out.println("1");
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
//                    System.out.println("2");
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
//                    System.out.println("3");
                }
            }
        };
//        System.out.println("4");

        try {
            url = new URL(requestUrl);
            log.debug("{} {}", getMethod(), url);
//            System.out.println("5");
        } catch (MalformedURLException e) {
            throw new BinanceApiException("Mailformed URL " + e.getMessage()); //////////////////////////
        }

        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            System.out.println("6");
        } catch (NoSuchAlgorithmException e) {
            throw new BinanceApiException("SSL Error " + e.getMessage() );
        } catch (KeyManagementException e) {
            throw new BinanceApiException("Key Management Error " + e.getMessage() );
        }

        try {
            conn = (HttpsURLConnection)url.openConnection();
//            System.out.println(conn);

//            System.out.println("7");
        } catch (IOException e) {
            throw new BinanceApiException("HTTPS Connection error " + e.getMessage());
        }

        try {
            conn.setRequestMethod(method);
//            conn.setRequestMethod("POST");
            System.out.println(conn);
//            System.out.println("8");
        } catch (ProtocolException e) {
            throw new BinanceApiException("HTTP method error " + e.getMessage());
        }

        conn.setRequestProperty("User-Agent", getUserAgent());
//        System.out.println("9");
        for(String header: headers.keySet()) {
            conn.setRequestProperty(header, headers.get(header));
//            System.out.println("10");
        }
//        System.out.println("11");
        return this;
    }

    /**
     * Saving response into local string variable
     * Сохранение ответа в локальной строковой переменной
     *
     * @return this request object
     * @throws BinanceApiException in case of any error
     */
    public BinanceRequest read() throws BinanceApiException {
        if (conn == null) { connect(); }
        try {
            BufferedReader bufferedReader;
            InputStream inputStream;
            // posting payload it we do not have it yet
            // выкладываем полезную нагрузку у нас ее пока нет
            if (!Strings.isNullOrEmpty(getRequestBody())) { //////////////////////////////
                log.debug("Payload: {}", getRequestBody());
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getRequestBody());
                writer.close();
            }

            // вылетает ошибка 400
            if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = conn.getInputStream();
            } else {
                /* error from server */
                inputStream = conn.getErrorStream();
//                System.out.println(is);
//                System.out.println(conn);
//                System.out.println(conn.getResponseCode());
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            lastResponse = IOUtils.toString(bufferedReader);
            log.debug("Response: {}", lastResponse);
            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                // Try to parse JSON
                JsonObject obj = (JsonObject) jsonParser.parse(lastResponse);
                if (obj.has("code") && obj.has("msg")) {
                    throw new BinanceApiException("ERROR: " +
                            obj.get("code").getAsString() + ", " + obj.get("msg").getAsString() );
                }
            }
        } catch (IOException e) {
            throw new BinanceApiException("Error in reading response " + e.getMessage());
        } catch (Exception e) {
            throw new BinanceApiException("Error in reading response " + e.getMessage());
        }
        return this;
    }

    public BinanceRequest payload(JsonObject payload) {
        if (payload == null) return this; // this is a valid case
        // according to documentation we need to have this header if we have preload
        // согласно документации нам нужен этот заголовок, если у нас есть предварительная загрузка
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        this.requestBody = payload.toString();
        return this;
    }

    /**
     * Getting last response as google JsonObject
     * Получение последнего ответа как Google JsonObject
     *
     * @return response as Json Object
     */
    public JsonObject asJsonObject() {
        return (JsonObject)jsonParser.parse(getLastResponse());
    }
    /**
     * Getting last response as google GAON JsonArray
     * @return response as Json Array
     */
    public JsonArray asJsonArray() {
        return (JsonArray)jsonParser.parse(getLastResponse());
    }

    public HttpsURLConnection getConn() {
        return this.conn;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public String getMethod() {
        return this.method;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public JsonParser getJsonParser() {
        return this.jsonParser;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setConn(HttpsURLConnection conn) {
        this.conn = conn;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setJsonParser(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    /////////////////////////////////////    I had to add == пришлось добавить   ///////////////////////////////////////

    private void setMethod(String string) {
        this.method = string;
    }

    private String getUserAgent() {
        return this.userAgent;
    }

        // получить тело запроса
    private String getRequestBody() {
        return this.requestBody;
    }

        // получить последний ответ
    protected String getLastResponse() {
        return this.lastResponse;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof BinanceRequest)) return false;
        final BinanceRequest other = (BinanceRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$userAgent = this.getUserAgent();
        final Object other$userAgent = other.getUserAgent();
        if (this$userAgent == null ? other$userAgent != null : !this$userAgent.equals(other$userAgent)) return false;
        final Object this$conn = this.getConn();
        final Object other$conn = other.getConn();
        if (this$conn == null ? other$conn != null : !this$conn.equals(other$conn)) return false;
        final Object this$requestUrl = this.getRequestUrl();
        final Object other$requestUrl = other.getRequestUrl();
        if (this$requestUrl == null ? other$requestUrl != null : !this$requestUrl.equals(other$requestUrl))
            return false;
        final Object this$method = this.getMethod();
        final Object other$method = other.getMethod();
        if (this$method == null ? other$method != null : !this$method.equals(other$method)) return false;
        final Object this$lastResponse = this.getLastResponse();
        final Object other$lastResponse = other.getLastResponse();
        if (this$lastResponse == null ? other$lastResponse != null : !this$lastResponse.equals(other$lastResponse))
            return false;
        final Object this$apiKey = this.getApiKey();
        final Object other$apiKey = other.getApiKey();
        if (this$apiKey == null ? other$apiKey != null : !this$apiKey.equals(other$apiKey)) return false;
        final Object this$secretKey = this.getSecretKey();
        final Object other$secretKey = other.getSecretKey();
        if (this$secretKey == null ? other$secretKey != null : !this$secretKey.equals(other$secretKey)) return false;
        final Object this$headers = this.getHeaders();
        final Object other$headers = other.getHeaders();
        if (this$headers == null ? other$headers != null : !this$headers.equals(other$headers)) return false;
        final Object this$jsonParser = this.getJsonParser();
        final Object other$jsonParser = other.getJsonParser();
        if (this$jsonParser == null ? other$jsonParser != null : !this$jsonParser.equals(other$jsonParser))
            return false;
        final Object this$requestBody = this.getRequestBody();
        final Object other$requestBody = other.getRequestBody();
        if (this$requestBody == null ? other$requestBody != null : !this$requestBody.equals(other$requestBody))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BinanceRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $userAgent = this.getUserAgent();
        result = result * PRIME + ($userAgent == null ? 43 : $userAgent.hashCode());
        final Object $conn = this.getConn();
        result = result * PRIME + ($conn == null ? 43 : $conn.hashCode());
        final Object $requestUrl = this.getRequestUrl();
        result = result * PRIME + ($requestUrl == null ? 43 : $requestUrl.hashCode());
        final Object $method = this.getMethod();
        result = result * PRIME + ($method == null ? 43 : $method.hashCode());
        final Object $lastResponse = this.getLastResponse();
        result = result * PRIME + ($lastResponse == null ? 43 : $lastResponse.hashCode());
        final Object $apiKey = this.getApiKey();
        result = result * PRIME + ($apiKey == null ? 43 : $apiKey.hashCode());
        final Object $secretKey = this.getSecretKey();
        result = result * PRIME + ($secretKey == null ? 43 : $secretKey.hashCode());
        final Object $headers = this.getHeaders();
        result = result * PRIME + ($headers == null ? 43 : $headers.hashCode());
        final Object $jsonParser = this.getJsonParser();
        result = result * PRIME + ($jsonParser == null ? 43 : $jsonParser.hashCode());
        final Object $requestBody = this.getRequestBody();
        result = result * PRIME + ($requestBody == null ? 43 : $requestBody.hashCode());
        return result;
    }

    public String toString() {
        return "BinanceRequest(userAgent=" + this.getUserAgent() + ", conn=" + this.getConn()
                + ", requestUrl=" + this.getRequestUrl() + ", method=" + this.getMethod()
                + ", lastResponse=" + this.getLastResponse() + ", apiKey="
                + this.getApiKey() + ", secretKey=" + this.getSecretKey()
                + ", headers=" + this.getHeaders() + ", jsonParser="
                + this.getJsonParser() + ", requestBody="
                + this.getRequestBody() + ")";
    }
}
