package com.example.coincome.WebSocket;

import android.util.Log;

import com.example.coincome.Exchange.Exchange;
import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.ViewModel.CoinRepository;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocket;

public class Bithumb {
    int multiply = 1;
    public void getBithumbData(String text, CoinRepository coinRepo){
        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("content"));
            Coin coin = new Coin();
            coin.setCoinName(jsonObject1.getString("symbol"));
            String[] array = jsonObject1.getString("symbol").replace("_","-").split("-");
            String symbolFormat = array[0]+"-"+array[1];
            coin.setMarket(symbolFormat);

            //                coin.getCoinDaytoday()/(coin.getCoinPrice()-coin.getCoinDaytoday())*100
            coin.setTradeVolume(jsonObject1.getDouble("value"));
            if(jsonObject1.getDouble("closePrice")>jsonObject1.getDouble("prevClosePrice")){
                coin.setCoinChange("RISE");
                multiply = 1;
            }else if(jsonObject1.getDouble("closePrice")<jsonObject1.getDouble("prevClosePrice")){
                coin.setCoinChange("FALL");
                multiply = -1;
            }else{
                coin.setCoinChange("EVEN");
                multiply = 1;
            }
            coin.setCoinPrice(jsonObject1.getDouble("closePrice"));
            double  daytoday = Math.abs(jsonObject1.getDouble("prevClosePrice")-jsonObject1.getDouble("closePrice"));
            coin.setCoinDaytoday((daytoday/(coin.getCoinPrice()-daytoday))*multiply);

//            Log.v("bithumb","symbol : "+jsonObject1.getString("symbol"));


            coinRepo.updateList(coin.getMarket(),coin);

        } catch (JSONException e) {
//            e.printStackTrace();
        }
    }
    public void setSubscribe(WebSocket webSocket, StringBuilder str){
        Log.v("setSubscriveBitumb",str.toString());
        webSocket.send("{\"type\":\"ticker\", \"symbols\": "+str+",\"tickTypes\": " +
                "[" +
//                "\"30M\", \"1H\", \"12H\", \"24H\", " +
                "\"MID\" " +
                "]}");
    }
}
