package com.example.pairtrading.service;

import com.example.pairtrading.dao.StockDao;
import com.example.pairtrading.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {
    private final StockDao stockDao;

    @Autowired
    public StockService(@Qualifier("mongoDao") StockDao stockDao) {
        this.stockDao = stockDao;
    }

    public Optional<Stock> getStockWithTicker(String ticker) {
        return stockDao.getStockWithTicker(ticker);
    }

    public String[] getAllStocks() {return stockDao.getAllStocks();}
}
