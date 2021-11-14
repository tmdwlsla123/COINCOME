package com.example.coincome.WebSocket;

import android.util.Log;

import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.ViewModel.CoinRepository;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocket;

public class Binance {

    public void getBinanceData(String text, CoinRepository coinRepo){
        try {
            JSONObject jsonObject = new JSONObject(text);
            Coin coin = new Coin();
            int symbol = jsonObject.getString("s").indexOf("USDT");

            coin.setMarket("KRW-"+jsonObject.getString("s").substring(0,symbol));
            coin.setCoinOverseasPrice(jsonObject.getDouble("p"));

            coinRepo.updateOverseasList(coin);


        } catch (JSONException e) {

            e.printStackTrace();

        }
    }
    public void setSubscribe(WebSocket webSocket,StringBuilder str){
        //해외거래소
        webSocket.send("{\n" +
                "\"method\": \"SUBSCRIBE\",\n" +
                "\"params\":\n" +
//                   "[\"BTCUSDT@trade\"]"+",\n" +
                str+",\n"+
                "\"id\": 1\n" +
                "}");
//            webSocket.send("{\n" +
//                    "\"method\": \"SUBSCRIBE\",\n" +
//                    "\"params\":\n" +
//                    "[\n" +
//                    "\"btcusdt@trade\"\n" +
//                    "],\n" +
//                    "\"id\": 1\n" +
//                    "}");
        Log.v("onOpen","{\n" +
                "\"method\": \"SUBSCRIBE\",\n" +
                "\"params\":\n" +
                str+",\n" +
                "\"id\": 1\n" +
                "}");
    }
}
