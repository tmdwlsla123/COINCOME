package com.example.coincome.RecyclerView;

public class Coin {


    private String coinName;
    private String market;
    private Double coinPrice;
    private Double coinPremium;
    private Double coinOverseasPrice;
    private String coinChange;
    private Double coinDaytoday;
    private Double tradeVolume;
    private String symbol;
    private String exchange;
    private boolean isChecked;
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Double getCoinOverseasPrice() {
        return coinOverseasPrice;
    }

    public void setCoinOverseasPrice(Double coinOverseasPrice) {
        this.coinOverseasPrice = coinOverseasPrice;
    }


    public Double getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(Double tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public String getCoinChange() {
        return coinChange;
    }

    public void setCoinChange(String coinChange) {
        this.coinChange = coinChange;
    }



    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Double getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(Double coinPrice) {
        this.coinPrice = coinPrice;
    }

    public Double getCoinPremium() {
        return coinPremium;
    }

    public void setCoinPremium(Double coinPremium) {
        this.coinPremium = coinPremium;
    }

    public Double getCoinDaytoday() {
        return coinDaytoday;
    }

    public void setCoinDaytoday(Double coinDaytoday) {
        this.coinDaytoday = coinDaytoday;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }




}
