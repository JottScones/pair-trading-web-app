package com.example.pairtrading.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Date;

@Document("stocks")
public class Stock {
    @Id
    @JsonProperty("ticker")
    private final String tickerSymbol;

    @JsonFormat(pattern="yyyy-MM-dd") @JsonProperty("date")
    private final Date lastDate;

    @JsonProperty("prices")
    private final double[] priceHistory; // Should store 1 yrs worth of close prices

    public Stock(@JsonProperty("ticker") String tickerSymbol,
                 @JsonFormat(pattern="yyyy-MM-dd") @JsonProperty("date") Date lastDate,
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
