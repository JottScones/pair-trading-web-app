package com.example.pairtrading;

import com.example.pairtrading.model.Stock;
import com.example.pairtrading.model.StockProcessor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockProcessorTests {
    Stock mockStock1 = mock(Stock.class);
    Stock mockStock2 = mock(Stock.class);


    @Test
    void shouldReturnCorrectDistance() {
        double[] prices1 = {1,4,3};
        double[] prices2 = {1,2,9};
        when(mockStock1.getPreviousSixMonthPriceHistory()).thenReturn(prices1);
        when(mockStock2.getPreviousSixMonthPriceHistory()).thenReturn(prices2);

        assert StockProcessor.calculateStockDistance(mockStock1, mockStock2) == 40;
    }

    @Test
    void shouldReturnPositivePearsonRatio() {
        double[] prices1 = {1,2,3,4,5,6};
        double[] prices2 = {2,3,4,5,6,7};

        when(mockStock1.getPreviousSixMonthPriceHistory()).thenReturn(prices1);
        when(mockStock2.getPreviousSixMonthPriceHistory()).thenReturn(prices2);

        assert StockProcessor.calculatePearsonCoefficient(mockStock1, mockStock2) == 1;
    }

    @Test
    void shouldReturnNegativePearsonRatio() {
        double[] prices1 = {1,2,3,4,5,6};
        double[] prices2 = {6,5,4,3,2,1};

        when(mockStock1.getPreviousSixMonthPriceHistory()).thenReturn(prices1);
        when(mockStock2.getPreviousSixMonthPriceHistory()).thenReturn(prices2);

        assert StockProcessor.calculatePearsonCoefficient(mockStock1, mockStock2) == -1;
    }

    @Test
    void shouldReturnZeroPearsonRatio() {
        double[] prices1 = {1,2,3,4,5,6};
        double[] prices2 = {1,1,1,1,1,1};

        when(mockStock1.getPreviousSixMonthPriceHistory()).thenReturn(prices1);
        when(mockStock2.getPreviousSixMonthPriceHistory()).thenReturn(prices2);

        assert StockProcessor.calculatePearsonCoefficient(mockStock1, mockStock2) == 0;
    }

    @Test
    void shouldReturnCorrectRatio() {
        double[] prices1 = {1,4,9};
        double[] prices2 = {1,2,3};
        double[] ratio = {1, 2, 3};

        when(mockStock1.getPriceHistory()).thenReturn(prices1);
        when(mockStock2.getPriceHistory()).thenReturn(prices2);

        assert Arrays.equals(StockProcessor.calculateRatio(mockStock1, mockStock2), ratio);
    }

    @Test
    void rollingMeanShouldReturnThrowExceptionIfRatioSmallerThanSixty() {
        double[] ratio = {1, 2, 3};
        Throwable e = null;
        try {
            StockProcessor.calculateRollingMean(ratio);
        } catch (Exception ex) {
            e = ex;
        }
        assert e instanceof IllegalArgumentException;
    }

    @Test
    void rollingMeanShouldReturnCorrect() {
        double EPSILON = 0.001;

        double[] ratio = {5,9,12,7,54,65,42,72,80,56,3,1,60,34,46,82,58,8,75,33,88,49,4,39,43,81,48,99,87,24,86,23,27,70,14,21,17,47,66,35,59,62,100,71,85,57,22,2,11,77,45,55,63,51,19,96,79,18,98,38};
        double[] targetAvg = {48.167,48.633,49.133,51.233,49.9,48.433,47.6,46.767,46.3,45.6,47.467,49.5,50.833,52.067,53.367,52.533,51.333,51.133,49,50.467,49.033,49.233,51.2,51.6,50.8,51.3,52.333,49.633,50,50.467};

        double[] calcAvg = StockProcessor.calculateRollingMean(ratio);
        boolean roughlyEquals = targetAvg.length == calcAvg.length;
        if (roughlyEquals) {
            for (int i = 0; i < targetAvg.length; i++) {
                roughlyEquals = roughlyEquals && Math.abs(targetAvg[i]-calcAvg[i]) < EPSILON;
            }
        }

        assert roughlyEquals;
    }

    @Test
    void rollingSDShouldThrowExceptionIfRatioSmallerThanSixty() {
        double[] ratio = {1, 2, 3};
        Throwable e = null;
        try {
            StockProcessor.calculateRollingSD(ratio);
        } catch (Exception ex) {
            e = ex;
        }
        assert e instanceof IllegalArgumentException;
    }

    @Test
    void rollingSDShouldReturnCorrect() {
        double EPSILON = 0.2;

        double[] ratio = {5,9,12,7,54,65,42,72,80,56,3,1,60,34,46,82,58,8,75,33,88,49,4,39,43,81,48,99,87,24,86,23,27,70,14,21,17,47,66,35,59,62,100,71,85,57,22,2,11,77,45,55,63,51,19,96,79,18,98,38};
        double[] targetSD = {29.316756679793592, 28.796392520977733, 28.28159982902114, 27.40034468558541, 28.194975911794405, 28.513953699119938, 29.050071715344636, 28.694579433908576, 28.260868115941992, 28.27201207319116, 27.227110672187667, 25.927784324928346, 27.419072842741336, 27.46626213294331, 28.064786160279617, 27.568984183115795, 28.083605339929004, 28.410952504663094, 28.93671255228094, 29.202435210478978, 28.368390076907, 28.388593170888583, 27.20588171701112, 27.111621124528874, 27.701263509089255, 28.369173410587766, 28.791588277751462, 28.07784812900653, 28.629821282478634, 28.314582030387722};

        double[] calcSD = StockProcessor.calculateRollingSD(ratio);
        boolean roughlyEquals = targetSD.length == calcSD.length;
        if (roughlyEquals) {
            for (int i = 0; i < targetSD.length; i++) {
                roughlyEquals = roughlyEquals && Math.abs(targetSD[i]-calcSD[i]) < EPSILON;
            }
        }

        assert roughlyEquals;
    }
}
