package com.example.coincome.WebSocket;

import android.util.Log;

import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.ViewModel.CoinRepository;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.WebSocket;
import okio.ByteString;

public class Upbit {

    public void getUpbitData(ByteString bytes, CoinRepository coinRepo){
        String str = new String(bytes.toByteArray());
//        if(num==1){
            try {

//                Log.v("onMessage : bytes",str);

                JSONObject jsonObject = new JSONObject(str);

//                coinRepo.setMerge(jsonObject);
                Coin coin = new Coin();

                //24시간 거래량

                coin.setTradeVolume(jsonObject.getDouble("atp24h"));
                coin.setCoinChange(jsonObject.getString("c"));
                coin.setCoinPrice(jsonObject.getDouble("tp"));

                coin.setCoinDaytoday(jsonObject.getDouble("scp")/(coin.getCoinPrice()-jsonObject.getDouble("scp")));
                String[] array = jsonObject.getString("cd").split("-");
                coin.setMarket(array[1]+"-"+array[0]);

                coinRepo.updateList(coin.getMarket(),coin);

            } catch (JSONException e) {
                e.printStackTrace();

            }
//        }else{
//            //해외거래소일경우
//            Log.v("onMessage : bytes","Binance : "+str);
//        }
    }
    public void setSubscribe(WebSocket webSocket,StringBuilder str){
        //국내거래소
        //업비트
        webSocket.send("[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":"+str+"},{\"format\":\"SIMPLE\"}]");
        Log.v("onOpen","[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":"+str+"},{\"format\":\"SIMPLE\"}]");

    }
}
