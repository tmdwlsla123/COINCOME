package com.example.coincome.WebSocket;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.coincome.Exchange.Exchange;
import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.RecyclerView.QuoteAdapter;

import com.example.coincome.Room.RoomDB;
import com.example.coincome.ViewModel.CoinRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;


public class WebSocketListener extends okhttp3.WebSocketListener {

    Binance binance;
    Bithumb bithumb;
    Upbit upbit;
    Korbit korbit;
    Exchange exchange;
    StringBuilder str;

    Context context;
    CoinRepository coinRepo;
    ArrayList arrayList;
    public WebSocket webSocket;
    public boolean isConnected;
    String exchangeName;
//    private static WebSocketListener instance = new WebSocketListener();
//    private static WebSocketListener instance1 = new WebSocketListener();
    private static final HashMap<WebsocketType,WebSocketListener> instances = new HashMap<WebsocketType,WebSocketListener>();
public enum WebsocketType{
    domestic,
    overseas
}
    private WebSocketListener(Context context) {

        this.context = context;


        coinRepo = CoinRepository.getInstance();

        binance = new Binance();
        upbit = new Upbit();
        bithumb = new Bithumb();
        korbit = new Korbit();
        isConnected = false;

    }
    public static WebSocketListener getInstance(Exchange exchange,Context context,WebsocketType websocketType){
        if(instances.get(websocketType) == null){
            instances.put(websocketType,new WebSocketListener(context));
        }

        return instances.get(websocketType);
    }

    private void marketList(String exchangeName){
    int multiply;
         arrayList = new ArrayList();
         str = new StringBuilder();
         if(exchangeName.equals("업비트")){
             for(int i=0; i<exchange.upbitMarket.length(); i++){
                 try {
                     String[] array = exchange.upbitMarket.getJSONObject(i).getString("cd").split("-");
                     arrayList.add(exchange.upbitMarket.getJSONObject(i).getString("cd"));
                     Coin coin = new Coin();
                     coin.setMarket(array[1]+"-"+array[0]);
                     coin.setSymbol(array[1]);
                     coin.setCoinName(exchange.upbitMarket.getJSONObject(i).getString("cn"));
                     coin.setExchange("upbit");
                     coin.setChecked(RoomDB.getDatabase(context).DatabaseDao().favoriteExist(array[1],"upbit"));
//                     coin.setTradeVolume(exchange.upbitMarket.getJSONObject(i).getDouble("atp24h"));
                     coinRepo.add(coin);
                     //
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         }else if(exchangeName.equals("바이낸스")){

             for(int i=0; i<exchange.binanceMarket.length(); i++){
                 try {
                     arrayList.add("\""+exchange.binanceMarket.getJSONObject(i).getString("overseas")+"\"");
                     Log.v("overseas",exchange.binanceMarket.getJSONObject(i).getString("overseas"));

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         }else if(exchangeName.equals("빗썸")){
             for(int i = 0; i < exchange.bithumbMarket.length(); i++){

                 try {
                     arrayList.add("\""+exchange.bithumbMarket.getJSONObject(i).getString("symbol")+"\"");
                     Coin coin = new Coin();
                     String[] array = exchange.bithumbMarket.getJSONObject(i).getString("symbol").replace("_","-").split("-");
                     String symbolFormat = array[0]+"-"+array[1];
                     coin.setMarket(symbolFormat);
                     coin.setSymbol(array[0]);
                     coin.setCoinName(exchange.bithumbMarket.getJSONObject(i).getString("symbol"));
                     coin.setCoinPrice(exchange.bithumbMarket.getJSONObject(i).getDouble("coinPrice"));
                     coin.setCoinChange(exchange.bithumbMarket.getJSONObject(i).getString("change"));
                     coin.setChecked(RoomDB.getDatabase(context).DatabaseDao().favoriteExist(array[0],"bithumb"));
                     coin.setExchange("bithumb");
                     if(coin.getCoinChange().equals("FALL")){
                          multiply = -1;
                     }else{
                          multiply = 1;
                     }
                     double daytoday = exchange.bithumbMarket.getJSONObject(i).getDouble("changePrice")/(coin.getCoinPrice()-exchange.bithumbMarket.getJSONObject(i).getDouble("changePrice"));
                     coin.setCoinDaytoday(daytoday*multiply);
                     coin.setTradeVolume(exchange.bithumbMarket.getJSONObject(i).getDouble("trade_volume"));
                     Log.v("bithumb",exchange.bithumbMarket.getJSONObject(i).getString("symbol").replace("_","-"));
                     coinRepo.add(coin);

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }

             }
         }else if(exchangeName.equals("코빗")){
             for(int i = 0; i < exchange.korbitMarket.length(); i++){
                 try {
                     arrayList.add(exchange.korbitMarket.getJSONObject(i).getString("symbol"));
                     Coin coin = new Coin();
                     int idx = exchange.korbitMarket.getJSONObject(i).getString("symbol").indexOf("_");
                     String symbol = exchange.korbitMarket.getJSONObject(i).getString("symbol").substring(0,idx).toUpperCase();
                     coin.setMarket(symbol+"-KRW");
                     coin.setSymbol(symbol);
                     coin.setCoinName(exchange.korbitMarket.getJSONObject(i).getString("symbol"));
                     coin.setCoinPrice(exchange.korbitMarket.getJSONObject(i).getDouble("last"));
                     coin.setCoinChange(exchange.korbitMarket.getJSONObject(i).getString("change"));
                     coin.setChecked(RoomDB.getDatabase(context).DatabaseDao().favoriteExist(symbol,"korbit"));
                     coin.setExchange("korbit");
                     if(coin.getCoinChange().equals("FALL")){
                         multiply = -1;
                     }else{
                         multiply = 1;
                     }
                     double daytoday = exchange.korbitMarket.getJSONObject(i).getDouble("changePrice")/(coin.getCoinPrice()-exchange.korbitMarket.getJSONObject(i).getDouble("changePrice"));
                     coin.setCoinDaytoday(daytoday*multiply);
                     coinRepo.add(coin);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         }


        str.append(arrayList);

        Log.v("socket", "str : append() :"+exchange.upbitMarket.length());
        Log.v("socket", "str : append() :"+exchangeName.equals("업비트"));
    }
    public void addExchangeName(String exchangeName,Exchange exchange){
    this.exchangeName = exchangeName;
    this.exchange = exchange;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

        marketList(exchangeName);
        Log.v("socket", "str : "+exchangeName);
        Log.v("socket", "str : "+str);
        Log.v("socket", "str : "+response);
        this.webSocket = webSocket;
        if(exchangeName.equals("업비트")){
            upbit.setSubscribe(webSocket,str);
        }else if(exchangeName.equals("바이낸스")){
            binance.setSubscribe(webSocket,str);
        }else if(exchangeName.equals("빗썸")){
            bithumb.setSubscribe(webSocket,str);
        }else if(exchangeName.equals("코빗")){
            korbit.setSubscribe(webSocket,str);
        }
//        webSocket.close(1000,null);







//        webSocket.send(" {\"stream\":\"<!miniTicker@arr>\",\"data\":<rawPayload>}");
//        webSocket.close(1000, null); //없을 경우 끊임없이 서버와 통신함
//        Log.d("Socket",response.toString());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        isConnected =true;
        if(exchangeName.equals("바이낸스")){

            binance.getBinanceData(text,coinRepo);
//                    Log.d("Socket","Receiving :"+ text);
        }else if(exchangeName.equals("빗썸")){
            bithumb.getBithumbData(text,coinRepo);
//                    Log.d("Socket","Receiving :"+ text);
        }else if(exchangeName.equals("코빗")){
            korbit.getKorbitData(text,coinRepo);
        }

        Log.d("Socket","Receiving text:"+ text);

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        isConnected =true;
        upbit.getUpbitData(bytes,coinRepo);
        Log.d("Socket","Receiving ByteString:"+ bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d("Socket","onClosing : reason" + reason);
        Log.d("Socket","onClosing : reason" + exchangeName);
        isConnected = false;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d("Socket","onClosed : text" + reason);
        Log.d("Socket","onClosed : text" + exchangeName);
        isConnected = false;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        Log.d("Socket","onFailure : text" + response);
        Log.d("Socket","onFailure : text" + exchangeName);
        isConnected = false;
    }



}
