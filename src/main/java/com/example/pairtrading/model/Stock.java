package com.example.pairtrading.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.Date;

@Document("stocks")
public class Stock {
    @Id
    @Field("ticker")
    @JsonProperty("ticker")
    private final String tickerSymbol;

    @Field("date")
    @JsonProperty("date")
    private final String lastDate;

    @Field("prices")
    @JsonProperty("prices")
    private final double[] priceHistory; // Should store 1 yrs worth of close prices

    public Stock(@JsonProperty("ticker") String tickerSymbol,
                 @JsonProperty("date") String lastDate,
                 @JsonProperty("prices") double[] priceHistory) {
        this.tickerSymbol = tickerSymbol;
        this.lastDate = lastDate;
        this.priceHistory = priceHistory;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public double[] getPriceHistory() {
        return priceHistory;
    }

    public double[] getSixMonthPriceHistory() {
        return Arrays.copyOfRange(priceHistory, (priceHistory.length + 1) / 2, priceHistory.length);
    }

    public double[] getPreviousSixMonthPriceHistory() {
        return Arrays.copyOfRange(priceHistory, 0, (priceHistory.length + 1) / 2);

    }

}
