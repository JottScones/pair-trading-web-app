package com.example.pairtrading;

import com.example.pairtrading.dao.FileDao;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class FileDaoTests {
    private final static String TEST_PATH = "classpath:json/testStock.json";
    private static FileDao fileDao = new FileDao(TEST_PATH);

    @Test
    void shouldFindTicker() {
        assert fileDao.getStockWithTicker("APPL").isPresent();
    }

    @Test
    void shouldNotFindTicker() {
        assert fileDao.getStockWithTicker("AL").isEmpty();
    }

    @Test
    void shouldReturnAllStoredStockTickers() {
        String[] tickers = {"APPL", "AMZ"};
        assert Arrays.equals(fileDao.getAllStocks(), tickers);
    }
}
