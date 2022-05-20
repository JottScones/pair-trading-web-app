package com.example.pairtrading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metric {
    @JsonProperty("ratio")
    private double[] ratio;
    @JsonProperty("mean")
    private double[] mean;
    @JsonProperty("posSD")
    private double[] posSD;
    @JsonProperty("negSD")
    private double[] negSD;

    public Metric(@JsonProperty("ratio") double[] ratio, @JsonProperty("mean") double[] mean, @JsonProperty("posSD") double[] posSD, @JsonProperty("negSD") double[] negSD) {
        this.ratio = ratio;
        this.mean = mean;
        this.posSD = posSD;
        this.negSD = negSD;
    }

    public double[] getRatio() {
        return ratio;
    }

    public double[] getMean() {
        return mean;
    }

    public double[] getPosSD() {
        return posSD;
    }

    public double[] getNegSD() {
        return negSD;
    }
}
