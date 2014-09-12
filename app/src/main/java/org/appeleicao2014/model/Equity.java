package org.appeleicao2014.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thaleslima on 8/20/14.
 */
public class Equity {

    @SerializedName("bem")
    private String equity;

    @SerializedName("montante")
    private String amount;

    @SerializedName("ano")
    private String year;

    private int color;

    public Equity(String year, int color) {
        this.year = year;
        this.color = color;
    }

    public Equity() {
    }

    public String getEquity() {
        return equity;
    }

    public void setEquity(String equity) {
        this.equity = equity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
