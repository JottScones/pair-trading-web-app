package com.example.pairtrading.dao;

import com.example.pairtrading.model.Stock;

import java.util.Optional;

public interface StockDao {
    Optional<Stock> getStockWithTicker(String ticker);

    String[] getAllStocks();
}
