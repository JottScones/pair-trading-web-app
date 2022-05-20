package com.example.pairtrading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade {
    @JsonProperty("tradeSignal")
    private int tradeSignal;
    @JsonProperty("budget")
    private double budget;
    @JsonProperty("price1Start")
    private double price1Start;
    @JsonProperty("price1End")
    private double price1End;

    @JsonProperty("price2Start")
    private double price2Start;
    @JsonProperty("price2End")
    private double price2End;

    public Trade(int tradeSignal, double budget, double price1Start, double price1End, double price2Start, double price2End) {
        this.tradeSignal = tradeSignal;
        this.budget = budget;
        this.price1Start = price1Start;
        this.price1End = price1End;
        this.price2Start = price2Start;
        this.price2End = price2End;
    }
}
