package com.example.pairtrading.dao;

import com.example.pairtrading.datastore.StockRepository;
import com.example.pairtrading.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("mongo")
public class MongoDao implements StockDao{

    @Autowired
    StockRepository stockRepository;

    @Override
    public Optional<Stock> getStockWithTicker(String ticker) {
        return Optional.of(stockRepository.findStockByTicker(ticker));
    }

    @Override
    public String[] getAllStocks() {
        List<Stock> stocks   = stockRepository.findAll();
        Stream<String> tickers =  stocks.stream().map(Stock::getTickerSymbol);
        return tickers.toArray(String[]::new);
    }
}
