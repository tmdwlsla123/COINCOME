package com.example.coincome.WebSocket;

import android.util.Log;

import com.example.coincome.Exchange.Exchange;
import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.ViewModel.CoinRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

import okhttp3.WebSocket;

public class Korbit {
    int multiply = 1;
    public void getKorbitData(String text, CoinRepository coinRepo){
        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
            //currency_pair = symbolName
            //last = 최근가격
            jsonObject1.getString("currency_pair");
            jsonObject1.getString("last");
            Coin coin = new Coin();
            int idx = jsonObject1.getString("currency_pair").indexOf("_");
            coin.setMarket("KRW-"+jsonObject1.getString("currency_pair").substring(0,idx).toUpperCase());
            coin.setCoinName(jsonObject1.getString("currency_pair"));
            coin.setCoinPrice(jsonObject1.getDouble("last"));

            String change;
            if(jsonObject1.getDouble("last")>jsonObject1.getDouble("open")){
                change = "RISE";
                multiply = 1;
            }else if(jsonObject1.getDouble("last")<jsonObject1.getDouble("open")){
                change = "FALL";
                multiply = -1;
            }else{
                change = "EVEN";
                multiply = 1;
            }

            double daytoday = Math.abs(jsonObject1.getDouble("last")-jsonObject1.getDouble("open"));
            coin.setCoinDaytoday((daytoday/(coin.getCoinPrice()-daytoday))*multiply);

            coin.setCoinChange(change);

            coinRepo.updateList(coin);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setSubscribe(WebSocket webSocket, StringBuilder str){
        long now = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(now);
        Log.v("korbitwebsocket",str.toString());
    webSocket.send("{\n" +
            "  \"accessToken\": null,\n" +
            "  \"timestamp\": "+timestamp.getTime()+",\n" +
            "  \"event\": \"korbit:subscribe\",\n" +
            "  \"data\": {\n" +
            "      \"channels\": [\"ticker:"+str+"\"]\n" +
            "  }\n" +
            "}");
    }
}
