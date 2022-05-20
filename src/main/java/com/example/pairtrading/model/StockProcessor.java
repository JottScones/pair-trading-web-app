package com.example.pairtrading.model;

import java.util.Arrays;

public class StockProcessor {
    private final static int ROLLING_WINDOW = 30;

    // Sum of square deviations
    public static double calculateStockDistance(Stock stock1, Stock stock2) {
        double[] priceHistory1 = stock1.getPreviousSixMonthPriceHistory();
        double[] priceHistory2 = stock2.getPreviousSixMonthPriceHistory();

        double initialP1 = priceHistory1[0];
        double initialP2 = priceHistory2[0];

        double distance = 0;
        double p1, p2;
        for (int i = 0; i < priceHistory1.length; i++) {
            p1 = priceHistory1[i];
            p2 = priceHistory2[i];
            distance += Math.pow((p1 / initialP1) - (p2 / initialP2), 2);
        }
        return distance;
    }

    // Pearson correlation of stock prices
    public static double calculatePearsonCoefficient(Stock stock1, Stock stock2) {
        double[] priceHistory1 = stock1.getPreviousSixMonthPriceHistory();
        double[] priceHistory2 = stock2.getPreviousSixMonthPriceHistory();

        double mean1 = 0;
        double mean2 = 0;

        for (int i = 0; i < priceHistory1.length; i++) {
            mean1 += priceHistory1[i] / priceHistory1.length;
            mean2 += priceHistory2[i] / priceHistory2.length;
        }

        double coVar = 0;
        double var1 = 0;
        double var2 = 0;

        for (int i = 0; i < priceHistory1.length; i++) {
            coVar += (priceHistory1[i] - mean1) * (priceHistory2[i] - mean2);
            var1  += Math.pow(priceHistory1[i] - mean1, 2);
            var2  += Math.pow(priceHistory2[i] - mean2, 2);
        }

        return coVar / Math.sqrt(var1 * var2);
    }

    // Calculate ratio array of stock1/stock2 at each index
    public static double[] calculateRatio(Stock stock1, Stock stock2) {
        double[] prices1 = stock1.getPriceHistory();
        double[] prices2 = stock2.getPriceHistory();

        double[] ratio = new double[prices1.length];
        for (int i = 0; i < prices1.length; i++) {
            ratio[i] = prices2[i] == 0 ? 0 :prices1[i] / prices2[i];
        }
        return ratio;
    }

    // Calculate rolling mean for right half of ratio array
    public static double[] calculateRollingMean(double[] ratio) throws IllegalArgumentException {

        if (ratio.length < 60) {throw new IllegalArgumentException("Ratio array must be greater than length 60 for a 30 day window.");}

        // Initialise avg with last 30 ratio values of left half
        double currAvg = 0;
        int half = (ratio.length + 1) / 2;
        for (int i = half - ROLLING_WINDOW; i < half; i++) {
            currAvg += ratio[i] / ROLLING_WINDOW;
        }

        double[] rollingAvg = new double[ratio.length - half];
        for (int i = half; i < ratio.length; i++) {
            currAvg += (ratio[i] - ratio[i - ROLLING_WINDOW]) / ROLLING_WINDOW;
            rollingAvg[i-half] = currAvg;
        }

        return rollingAvg;
    }

    // Calculate rolling standard deviation for right half of ratio array
    public static double[] calculateRollingSD(double[] ratio) throws IllegalArgumentException {

        if (ratio.length < 60) {throw new IllegalArgumentException("Ratio array must be greater than length 60 for a 30 day window.");}

        // Initialise avg with last 30 ratio values of left half
        double currAvg = 0;
        int half = (ratio.length + 1) / 2;
        for (int i = half - ROLLING_WINDOW; i < half; i++) {
            currAvg += ratio[i] / ROLLING_WINDOW;
        }

        double currVar = 0;
        for (int i = half - ROLLING_WINDOW; i < half; i++) {
            currVar += Math.pow(ratio[i] - currAvg, 2) / ROLLING_WINDOW;
        }

        double[] rollingSD = new double[ratio.length - half];
        double prevAvg = 0;
        for (int i = half; i < ratio.length; i++) {
            prevAvg  = currAvg;
            currAvg += (ratio[i] - ratio[i - ROLLING_WINDOW]) / ROLLING_WINDOW;

            currVar += (ratio[i] - currAvg + ratio[i - ROLLING_WINDOW] - prevAvg) * (ratio[i] - ratio[i - ROLLING_WINDOW]) / (ROLLING_WINDOW - 1);
            rollingSD[i-half] = Math.sqrt(currVar);
        }

        return rollingSD;
    }

    // Calculates and returns Metrics object with data needed for trading strategies
    public static Metric calculateTradingMetrics(Stock stock1, Stock stock2) throws IllegalArgumentException {
        double[] ratio = calculateRatio(stock1, stock2);
        try {
            double[] mean = calculateRollingMean(ratio);
            double[] sd = calculateRollingSD(ratio);


            double[] positiveTwoSD = new double[sd.length];
            double[] negativeTwoSD = new double[sd.length];

            for (int i = 0; i < sd.length; i++) {
                positiveTwoSD[i] = mean[i] + 2 * sd[i];
                negativeTwoSD[i] = mean[i] - 2 * sd[i];
            }

            ratio = Arrays.copyOfRange(ratio, (ratio.length + 1) / 2, ratio.length);
            return new Metric(ratio, mean, positiveTwoSD, negativeTwoSD);
        } catch (IllegalArgumentException e){
            throw e;
        }
    }

    // Helper function to calculate profit/loss from a short and long position (we always split our budget 50/50)
    private static double calculateTradeProfitLoss(double budget, double shortStart, double shortEnd, double longStart, double longEnd) {
        return ((budget / 2) * (shortStart - shortEnd) / shortStart) + ((budget / 2) * (longEnd - longStart) / longStart);
    }

    // Trading strategy implementation checks if ratio greater or less than 2 standard deviations, if so buy shorts and longs
    public static double calculateTradingProfit(Stock stock1, Stock stock2) throws IllegalArgumentException {
        Metric metrics;
        try {
            metrics = calculateTradingMetrics(stock1, stock2);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        double[] ratio         = metrics.getRatio();
        double[] positiveTwoSD = metrics.getPosSD();
        double[] negativeTwoSD = metrics.getNegSD();


        double budget    = 1000;
        double[] prices1 = stock1.getSixMonthPriceHistory();
        double[] prices2 = stock2.getSixMonthPriceHistory();
        for (int i = 1; i < ratio.length; i++) {
            // If stock1 > stock2 significantly short stock1 and long stock2
            if (ratio[i-1] > positiveTwoSD[i-1]) {
                budget += calculateTradeProfitLoss(budget, prices1[i-1], prices1[i], prices2[i-1], prices2[i]);
            // If stock1 < stock2 significantly short stock2 and long stock1
            } else if (ratio[i-1] < negativeTwoSD[i-1]) {
                budget += calculateTradeProfitLoss(budget, prices2[i-1], prices2[i], prices1[i-1], prices1[i]);
            }
            // Else do nothing
        }
        return budget-1000;
    }

    // Trading strategy implementation that returns Trade object representing the traded decision of that day
    public static Trade[] calculateTrades(Stock stock1, Stock stock2) throws IllegalArgumentException {
        Metric metrics;
        try {
            metrics = calculateTradingMetrics(stock1, stock2);
        } catch (IllegalArgumentException e) {
            throw e;
        }
        double[] ratio         = metrics.getRatio();
        double[] positiveTwoSD = metrics.getPosSD();
        double[] negativeTwoSD = metrics.getNegSD();


        double budget    = 1000;
        double[] prices1 = stock1.getSixMonthPriceHistory();
        double[] prices2 = stock2.getSixMonthPriceHistory();

        Trade[] trades = new Trade[ratio.length-1];
        for (int i = 1; i < ratio.length; i++) {
            // If stock1 > stock2 significantly short stock1 and long stock2
            if (ratio[i-1] > positiveTwoSD[i-1]) {
                trades[i-1] = new Trade(1, budget, prices1[i-1], prices1[i], prices2[i-1], prices2[i]);

                budget += calculateTradeProfitLoss(budget, prices1[i-1], prices1[i], prices2[i-1], prices2[i]);
                // If stock1 < stock2 significantly short stock2 and long stock1
            } else if (ratio[i-1] < negativeTwoSD[i-1]) {
                trades[i-1] = new Trade(-1, budget, prices1[i-1], prices1[i], prices2[i-1], prices2[i]);

                budget += calculateTradeProfitLoss(budget, prices2[i-1], prices2[i], prices1[i-1], prices1[i]);
            } else {
                // Else don't trade
                trades[i-1] = new Trade(0, budget, prices1[i-1], prices1[i], prices2[i-1], prices2[i]);
            }

        }
        return trades;
    }

}
