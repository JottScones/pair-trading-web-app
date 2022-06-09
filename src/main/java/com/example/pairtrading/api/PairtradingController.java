package com.example.pairtrading.api;

import com.example.pairtrading.model.Metric;
import com.example.pairtrading.model.Stock;
import com.example.pairtrading.model.StockProcessor;
import com.example.pairtrading.model.Trade;
import com.example.pairtrading.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("api")
@RestController
public class PairtradingController {
    private final StockService stockService;

    @Autowired
    public PairtradingController(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * This function implements the get functionality at the stored/distance endpoint. It takes two ticker strings as
     * request parameters and returns the double sum of square distances of their price histories.
     *
     * If either is stock is not found in the database, then we return a NOT_FOUND response.
     *
     * @param ticker1
     * @param ticker2
     * @return distance between the two given stocks (double)
     */
    @GetMapping("stored/distance")
    public ResponseEntity getDistance(@RequestParam("ticker1") String ticker1, @RequestParam("ticker2") String ticker2) {
        Optional<Stock> stock1 = stockService.getStockWithTicker(ticker1);
        Optional<Stock> stock2 = stockService.getStockWithTicker(ticker2);

        if (stock1.isEmpty() || stock2.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ticker not found please check spelling.");
        }

        return ResponseEntity.ok(StockProcessor.calculateStockDistance(stock1.get(), stock2.get()));
    }

    /**
     * This function implements the get functionality at the stored/pearson endpoint. It takes two ticker strings as
     * request parameters and returns the double pearson correlation coefficient of their price histories.
     *
     * If either stock is not found in the database, then we return a NOT_FOUND response.
     *
     * @param ticker1
     * @param ticker2
     * @return pearson correlation between the two given stocks (double)
     */
    @GetMapping("stored/pearson")
    public ResponseEntity getPearson(@RequestParam("ticker1") String ticker1, @RequestParam("ticker2") String ticker2) {
        Optional<Stock> stock1 = stockService.getStockWithTicker(ticker1);
        Optional<Stock> stock2 = stockService.getStockWithTicker(ticker2);

        if (stock1.isEmpty() || stock2.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ticker not found please check spelling.");
        }

        return ResponseEntity.ok(StockProcessor.calculatePearsonCoefficient(stock1.get(), stock2.get()));
    }

    /**
     * This function implements the get functionality at the stored/metrics endpoint. It takes two ticker strings as
     * request parameters and returns a Metric object converted into its json representation. The Metric object
     * holds information such as the rolling avg, rolling ratio and rolling standard deviations between the two stock
     * price histories.
     *
     * If either stock is not found in the database, then we return a NOT_FOUND response.
     * If the stored price history is less than 60 days long than we return an UNPROCESSABLE_ENTITY response.
     *
     * @param ticker1
     * @param ticker2
     * @return json representation of a Metric object
     */
    @GetMapping("stored/metrics")
    public ResponseEntity getPerformance(@RequestParam("ticker1") String ticker1, @RequestParam("ticker2") String ticker2) {
        Optional<Stock> stock1 = stockService.getStockWithTicker(ticker1);
        Optional<Stock> stock2 = stockService.getStockWithTicker(ticker2);

        if (stock1.isEmpty() || stock2.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ticker not found please check spelling.");
        }

        // If stock prices arrays are not long enough return error response
        Metric metric;
        try {
            metric = StockProcessor.calculateTradingMetrics(stock1.get(), stock2.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.toString());
        }
        return ResponseEntity.ok(metric);
    }

    /**
     * This  function implements the get functionality at the stored/profit endpoint. It takes two ticker strings as
     * request parameters and returns a double of profit/loss from using the pair trading algorithm with the two stocks
     * over the period of six months.
     *
     * If either stock is not found in the database, then we return a NOT_FOUND response.
     * If the stored price history is less than 60 days long than we return an UNPROCESSABLE_ENTITY response.
     *
     * @param ticker1
     * @param ticker2
     * @return profit or loss from pair trading both stocks (double)
     */
    @GetMapping("stored/profit")
    public ResponseEntity getTradingProfit(@RequestParam("ticker1") String ticker1, @RequestParam("ticker2") String ticker2) {
        Optional<Stock> stock1 = stockService.getStockWithTicker(ticker1);
        Optional<Stock> stock2 = stockService.getStockWithTicker(ticker2);

        if (stock1.isEmpty() || stock2.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ticker not found please check spelling.");
        }

        // If stock prices arrays are not long enough return error response
        double profit;
        try {
            profit = StockProcessor.calculateTradingProfit(stock1.get(), stock2.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.toString());
        }
        return ResponseEntity.ok(profit);
    }

    /**
     * This function implements the get functionality at the stored/trades endpoint. It takes two ticker strings as
     * request parameters and returns an array of Trade objects. The Trade objects represent a given trade decision
     * on a certain day and the current remaining budget (which starts at $1K). This endpoint is used to gain deeper
     * insight of the decisions the pair trading algorithm made when using the two chosen stocks.
     *
     * If either stock is not found in the database, then we return a NOT_FOUND response.
     * If the stored price history is less than 60 days long than we return an UNPROCESSABLE_ENTITY response.
     *
     * @param ticker1
     * @param ticker2
     * @return array of daily trades (represented as Trade objects)
     */
    @GetMapping("stored/trades")
    public ResponseEntity getTrades(@RequestParam("ticker1") String ticker1, @RequestParam("ticker2") String ticker2) {
        Optional<Stock> stock1 = stockService.getStockWithTicker(ticker1);
        Optional<Stock> stock2 = stockService.getStockWithTicker(ticker2);

        if (stock1.isEmpty() || stock2.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ticker not found please check spelling.");
        }

        // If stock prices arrays are not long enough return error response
        Trade[] trades;
        try {
            trades = StockProcessor.calculateTrades(stock1.get(), stock2.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.toString());
        }
        return ResponseEntity.ok(trades);
    }

    /**
     * This function implements the get functionality at the stored/stock endpoint. It takes a single ticker string as
     * a request parameter and, if the stock is found in the database, returns a Stock object containing price history
     * and the most recent stock date.
     *
     * If the stock is not found in the database, then we return a NOT_FOUND response.
     *
     * @param ticker
     * @return json representation of a Stock object
     */
    @GetMapping("stored/stock")
    public ResponseEntity getStock(@RequestParam("ticker") String ticker) {
        Optional<Stock> stock = stockService.getStockWithTicker(ticker);

        if (!stock.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Ticker not found please check spelling.");
        }

        return ResponseEntity.ok(stock.get());
    }

    /**
     * This function returns the get functionality at the root of the stored/ endpoint. It takes no request parameters and
     * return an array of Strings containing all the ticker symbols of the stored stocks in the database.
     *
     * @return array of ticker symbol strings
     */
    @GetMapping("stored")
    public ResponseEntity getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }
}
