package test.model.binance;

import main.model.binance.api.BinanceAPI;
import main.model.binance.api.BinanceApiException;
import main.model.binance.datatype.BinanceHistoryFilter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Calendar;



//@Slf4j
// Проверка истории аккаунта
public class AccountHistoryTest {

    private static final Logger log = LoggerFactory.getLogger(AccountInfoTest.class);

    private BinanceAPI binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceAPI();
    }

    @Test
    public void testAccountDepositHistory() throws Exception, BinanceApiException {
        BinanceHistoryFilter historyFilter = new BinanceHistoryFilter("ETH");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        historyFilter.setStartTime(cal.getTime());
        log.info("DEPOSIT={}", binanceApi.getDepositHistory(historyFilter));
    }

    @Test
    public void testAccountWithdrawalHistory() throws Exception, BinanceApiException {
        BinanceHistoryFilter historyFilter = new BinanceHistoryFilter();
        log.info("WITHDRAWALS={}", binanceApi.getWithdrawHistory(historyFilter));
    }
}
