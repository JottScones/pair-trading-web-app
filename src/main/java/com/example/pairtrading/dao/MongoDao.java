package com.example.pairtrading.dao;

import com.example.pairtrading.model.Stock;

import java.util.Optional;

public class MongoDao implements StockDao{
    @Override
    public Optional<Stock> getStockWithTicker(String ticker) {
        return Optional.empty();
    }

    @Override
    public String[] getAllStocks() {
        return new String[0];
    }
}
