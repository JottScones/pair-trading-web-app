package com.example.pairtrading.dao;

import com.example.pairtrading.model.Stock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Repository("file")
public class FileDao implements StockDao {
    private JSONArray memoryStocks;
    private String cachePath = "classpath:json/store.json";

    public FileDao() {
    }

    public FileDao(String cachePath) {
        this.cachePath = cachePath;
    }

    private Date formatDateString(String dateString) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(dateString);
    }

    private void loadMemory() {
        try {
            // Load Cached Stocks from file
            if (memoryStocks == null) {
                File file = ResourceUtils.getFile(cachePath);
                String content = Files.readString(file.toPath());

                JSONObject jsonObject = new JSONObject(content);
                memoryStocks = jsonObject.getJSONArray("stocks");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getAllStocks() {
        loadMemory();
        String[] stocks = new String[memoryStocks.length()];
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            for (int i = 0; i < memoryStocks.length(); i++) {
                JSONObject stock = memoryStocks.getJSONObject(i);
                stocks[i] = stock.getString("ticker");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return stocks;
    }

    @Override
    public Optional<Stock> getStockWithTicker(String ticker) {
        loadMemory();
        if (memoryStocks == null) {
            return Optional.empty();
        }

        try {
            for (int i = 0; i < memoryStocks.length(); i++) {
                JSONObject stock = memoryStocks.getJSONObject(i);
                if (stock.getString("ticker").equals(ticker)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return Optional.of(objectMapper.readValue(stock.toString(), Stock.class));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
