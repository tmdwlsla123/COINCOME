package com.example.coincome.CoinSymbol;

import org.json.JSONObject;

public final class UpbitAPI implements CoinAPIInterface {

    @Override
    public String getCoinSymbol() {
        if(market.contains("KRW")){
            int idx = market.indexOf("-");
            String symbol = market.substring(idx+1).toLowerCase()+"usdt@trade";
            return symbol;
        }
        return null;
    }
    public JSONObject getWebsocketSubscribeList(){

        return new JSONObject();
    }
    private final String market; //BTC-SEI, KRW-SEI
    private final String korean_name; //비트코인, 이더리움
    private final String english_name; // Tron, Polymesh, Siacoin, Bitcoin
    public UpbitAPI(String market, String korean_name, String english_name) {
        this.market = market;
        this.korean_name = korean_name;
        this.english_name = english_name;
    }
    public String getMarket() {
        return market;
    }

    public String getKorean_name() {
        return korean_name;
    }

    public String getEnglish_name() {
        return english_name;
    }
}
