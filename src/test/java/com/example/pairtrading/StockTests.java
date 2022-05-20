package com.example.pairtrading;
import com.example.pairtrading.model.Stock;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

public class StockTests {
    Date date = new Date();
    private final double[] history = {1,2,3};
    private final double[] prevSixHistory = {1, 2};
    private final double[] sixHistory = {3};
    private final Stock testStock = new Stock("TEST", date, history);

    @Test
    void canGetStockTickerSymbol() {
        assert testStock.getTickerSymbol().equals("TEST");
    }

    @Test
    void canGetStockPriceHistory() {
        assert Arrays.equals(testStock.getPriceHistory(), history);
    }

    @Test
    void canRetrieveLeftHalfOfPriceHistory() {
        assert Arrays.equals(testStock.getPreviousSixMonthPriceHistory(), prevSixHistory);
    }

    @Test
    void canRetrieveRightHalfOfPriceHistory() {
        assert Arrays.equals(testStock.getSixMonthPriceHistory(), sixHistory);
    }


}
