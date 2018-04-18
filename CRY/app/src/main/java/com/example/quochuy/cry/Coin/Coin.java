package com.example.quochuy.cry.Coin;

/**
 * Created by Quoc Huy on 4/2/2018.
 */




public class Coin {
    private String nameCoin;
    private String symbol;
    private String available_supply;
    private LastValues lastValues =new LastValues();
    private Max7Days_Values max7days_values=new Max7Days_Values();

    public Coin(String nameCoin, String symbol, String available_supply) {
        this.nameCoin = nameCoin;
        this.symbol = symbol;
        this.available_supply = available_supply;
    }

    public Coin() {
    }

    public String getNameCoin() {
        return nameCoin;
    }

    public void setNameCoin(String nameCoin) {
        this.nameCoin = nameCoin;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAvailable_supply() {
        return available_supply;
    }

    public void setAvailable_supply(String available_supply) {
        this.available_supply = available_supply;
    }

    public LastValues getLastValues() {
        return lastValues;
    }

    public void setLastValues(LastValues lastValues) {
        this.lastValues = lastValues;
    }

    public Max7Days_Values getMax7days_values() {
        return max7days_values;
    }

    public void setMax7days_values(Max7Days_Values max7days_values) {
        this.max7days_values = max7days_values;
    }
}
