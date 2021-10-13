package com.example.coincome.WebSocket;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.coincome.Exchange.Exchange;
import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.RecyclerView.QuoteAdapter;

import com.example.coincome.ViewModel.CoinRepository;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class WebSocketListener extends okhttp3.WebSocketListener {

    Exchange exchange;
    StringBuilder str;
    QuoteAdapter quoteAdapter;
    Context context;
    CoinRepository coinRepo;
    ArrayList arrayList;
    public WebSocketListener(Exchange exchange, QuoteAdapter quoteAdapter,Context context) {
    this.exchange = exchange;
    this.quoteAdapter = quoteAdapter;
    this.context = context;
        str = new StringBuilder();
        coinRepo = CoinRepository.getInstance();
        marketList();
    }

    private void marketList(){
         arrayList = new ArrayList();

         for(int i=0; i<exchange.upbit_Market.length(); i++){
             try {
                 arrayList.add(exchange.upbit_Market.getJSONObject(i).getString("cd"));
                 coinRepo.setCoin(exchange.upbit_Market.getJSONObject(i));

                 //
                 Coin coin = new Coin();
                 coin.setMarket(exchange.upbit_Market.getJSONObject(i).getString("cd"));
                 coin.setCoinName(exchange.upbit_Market.getJSONObject(i).getString("cn"));
                 coinRepo.add(coin);
                         //
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }

        str.append(arrayList);
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.v("socket", "str : "+str);




        webSocket.send("[{\"ticket\":\"test\"},{\"type\":\"trade\",\"codes\":"+str+"},{\"format\":\"SIMPLE\"}]");

//        webSocket.close(1000, null); //없을 경우 끊임없이 서버와 통신함
        Log.d("Socket",response.toString());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {

        Log.d("Socket","Receiving :"+ text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {

        try {
            String str = new String(bytes.toByteArray());
            Log.v("bytes",str);
            JSONObject jsonObject = new JSONObject(str);
            Log.v("socket", "market : " + jsonObject.getString("cd"));
            coinRepo.setMerge(jsonObject);
            Coin coin = new Coin();
            DecimalFormat df = new DecimalFormat("###,###");
            if(jsonObject.getInt("tp")>100){
                coin.setCoinPrice(df.format(jsonObject.getInt("tp")));
            }else{
                coin.setCoinPrice(jsonObject.getString("tp"));
            }

            coin.setMarket(jsonObject.getString("cd"));
            coinRepo.updateList(coin);


        } catch (JSONException e) {
            e.printStackTrace();

        }
//        Log.v("retrofit2",coin.getJSONArray().toString());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d("Socket","Receiving : reason" + reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d("Socket","Receiving : text" + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        Log.d("Socket","Receiving : text" + response);
    }
}
