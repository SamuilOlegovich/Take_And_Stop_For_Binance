package test.model.binance;


import com.google.gson.JsonObject;
import main.model.binance.api.BinanceApi;
import main.model.binance.api.BinanceApiException;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.slf4j.Logger;
import org.junit.Test;



//@Slf4j
public class SystemStatusTest {

    private static final Logger log = LoggerFactory.getLogger(SystemStatusTest.class);

    private BinanceApi binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
    }

    @Test
    public void testSystemStatus() throws Exception, BinanceApiException {
        JsonObject status = binanceApi.getSystemStatus();
        log.info("Status {}", status.toString() );
    }
}
