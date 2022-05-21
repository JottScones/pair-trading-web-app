package com.example.pairtrading.dao;

import com.example.pairtrading.model.Stock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Repository("file")
public class FileDao implements StockDao {
    private JSONArray memoryStocks;
    private String cachePath = "json/store.json";

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
                InputStream stream = new ClassPathResource(cachePath).getInputStream();
                String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);;

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
    public void updateEntry(Stock stock) {
        // Do nothing can't actually write to file
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
