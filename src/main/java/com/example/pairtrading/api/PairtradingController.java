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

    @GetMapping("stored")
    public ResponseEntity getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }
}
