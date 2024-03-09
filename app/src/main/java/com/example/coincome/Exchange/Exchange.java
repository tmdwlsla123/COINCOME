package com.example.coincome.Exchange;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.Room.RoomDB;
import com.example.coincome.ViewModel.CoinRepository;
import com.example.coincome.WebSocket.WebSocketListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Exchange {
    public JSONArray upbitMarket;
    public JSONArray bithumbMarket;
    public JSONArray coinoneMarket;
    public JSONArray korbitMarket;
    public JSONArray binanceMarket;
    int abc;
    public Exchange(){
        upbitMarket = new JSONArray();
        bithumbMarket = new JSONArray();
        coinoneMarket = new JSONArray();
        korbitMarket = new JSONArray();
        binanceMarket = new JSONArray();
    }


        //심볼 추가
        public void AddUpbitList(JSONArray jsonArray,JSONArray exchange){
            try {
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject =  jsonArray.getJSONObject(i);
                    if(jsonObject.getString("market").contains("KRW")){
                        JSONObject upbit = new JSONObject();
                        upbit.put("cd",jsonObject.getString("market"));
                        upbit.put("cn",jsonObject.getString("korean_name"));
                        exchange.put(upbit);
                    }
                }
                Log.v("retrofit2","거래소 : "+exchange.length());
                Log.v("retrofit2","거래소 : "+exchange.get(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }
    public void AddBithumbList(JSONObject jsonObject,JSONArray exchange){

        try {
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
            Iterator i = jsonObject1.keys();

            String key;
            while (i.hasNext()){
                key = i.next().toString();
                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString(key));
                JSONObject bithumb = new JSONObject();
                if(Character.isUpperCase(key.charAt(0))){
                    bithumb.put("coinPrice",jsonObject2.getDouble("closing_price"));
                    bithumb.put("changePrice",Math.abs(jsonObject2.getDouble("closing_price")-jsonObject2.getDouble("prev_closing_price")));
                    Log.v("빗썸변동금액", String.valueOf(Math.abs(jsonObject2.getDouble("closing_price")-jsonObject2.getDouble("prev_closing_price"))));
                    String change;
                    if(jsonObject2.getDouble("closing_price")>jsonObject2.getDouble("prev_closing_price")){
                        change = "RISE";
                    }else if(jsonObject2.getDouble("closing_price")<jsonObject2.getDouble("prev_closing_price")){
                        change = "FALL";
                    }else{
                        change = "EVEN";
                    }
                    bithumb.put("trade_volume",jsonObject2.getDouble("units_traded"));
                    bithumb.put("change",change);
                    bithumb.put("symbol",key+"_KRW");
                    exchange.put(bithumb);
                }
            }
            Log.v("retrofit2","거래소 : "+exchange.length());
            Log.v("retrofit2","거래소 : "+exchange.get(0));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    해외거래소
    public void AddBinanceList(Object object,JSONArray exchange,String exchangeName){
        Log.v("object",object.toString());

        try {
            String firstChar = object.toString();


            if (exchangeName.equals("업비트")) {
                Log.v("업비트","jsonobject");
                JSONArray jsonArray = new JSONArray(firstChar);
                Log.v("object",jsonArray.toString());
            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject =  jsonArray.getJSONObject(i);
                if(jsonObject.getString("market").contains("KRW")){
                    JSONObject binance = new JSONObject();

                    int idx = jsonObject.getString("market").indexOf("-");
                    String market = jsonObject.getString("market").substring(idx+1).toLowerCase()+"usdt@trade";
                    Log.v("binancesymbol",market);
                    binance.put("overseas",market);
                    exchange.put(binance);
                }
              }
            }else if(exchangeName.equals("빗썸")){
                Log.v("빗썸","jsonobject");

                JSONObject jsonObject = new JSONObject(firstChar);
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                Iterator i = jsonObject1.keys();
                String key;
                while (i.hasNext()){
                    key = i.next().toString();
                    if(Character.isUpperCase(key.charAt(0))){
                        JSONObject binance = new JSONObject();
                        String market = key.toLowerCase()+"usdt@trade";
                        binance.put("overseas",market);
                        exchange.put(binance);
                    }
                }
                Log.v("retrofit2","거래소 : "+exchange.length());
            }else if(exchangeName.equals("코빗")){
                JSONObject jsonObject = new JSONObject(firstChar);
                Iterator i = jsonObject.keys();
                String key;
                int idx;
                while (i.hasNext()){
                    key = i.next().toString();
                    JSONObject binance = new JSONObject();
                    idx = key.indexOf('_');
                    String market = key.substring(0,idx)+"usdt@trade";;
                    Log.v("korbitTobinance",market);
                    binance.put("overseas",market);
                    exchange.put(binance);
                }
            }else{

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void AddKorbitList(JSONObject jsonObject,JSONArray exchange){
        Iterator i = jsonObject.keys();
        String key;
        while (i.hasNext()){
            try {

                key =  i.next().toString();
                JSONObject jsonObject1 = new JSONObject(jsonObject.getString(key));
                JSONObject korbit = new JSONObject();
                korbit.put("symbol",key);
                korbit.put("changePercent",jsonObject1.getDouble("changePercent"));
                korbit.put("last",jsonObject1.getDouble("last"));
                korbit.put("open",jsonObject1.getDouble("open"));
                String change;
                if(jsonObject1.getDouble("last")>jsonObject1.getDouble("open")){
                    change = "RISE";
                }else if(jsonObject1.getDouble("last")<jsonObject1.getDouble("open")){
                    change = "FALL";
                }else{
                    change = "EVEN";
                }
                korbit.put("change",change);
                Log.v("korbit", String.valueOf(jsonObject1.getDouble("last")-jsonObject1.getDouble("open")));
                korbit.put("changePrice",Math.abs(jsonObject1.getDouble("last")-jsonObject1.getDouble("open")));

                exchange.put(korbit);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void AddCoinoneList(String response, Request request, WebSocketListener overseasListener, OkHttpClient client, String binanceSocketUri, Context context){



        try {
            int multiply = 1;
            JSONObject jsonObject  = new JSONObject(response);

            Iterator i = jsonObject.keys();
            String key;
            Log.v("coinone", String.valueOf(CoinRepository.getInstance().getAllList()));
            Log.v("coinone", String.valueOf(jsonObject));
            if(CoinRepository.getInstance().getAllList().isEmpty()){

            while(i.hasNext()){

                key = i.next().toString();
                if(!(key.equals("result") ^ key.equals("errorCode") ^ key.equals("timestamp"))){
                    JSONObject jsonObject1 = new JSONObject();
//                           코인 이름 영문 ex) xec doge
                    String coinName = jsonObject.getJSONObject(key).getString("currency");
                    Double coinPrice = jsonObject.getJSONObject(key).getDouble("last");
                    Double coinChange = jsonObject.getJSONObject(key).getDouble("yesterday_last");

//                            String coinName = jsonObject.getJSONObject(key).getString("currency");
//                            String coinName = jsonObject.getJSONObject(key).getString("currency");
//                            String coinName = jsonObject.getJSONObject(key).getString("currency");
                    String change;
                    if(coinPrice>coinChange){
                        change = "RISE";
                        multiply = 1;
                    }else if(coinPrice<coinChange){
                        change = "FALL";
                        multiply = -1;
                    }else{
                        change = "EVEN";
                        multiply = 1;
                    }
                    double daytoday = Math.abs(coinPrice-coinChange);
                    Coin coin = new Coin();
                    coin.setCoinPrice(coinPrice);
                    coin.setCoinName(coinName.toUpperCase()+"-KRW");
                    coin.setCoinDaytoday((daytoday/(coinPrice-daytoday))*multiply);
                    coin.setCoinChange(change);
                    coin.setMarket(coinName.toUpperCase()+"-KRW");
                    coin.setSymbol(coinName.toUpperCase());
                    coin.setExchange("coinone");
                    jsonObject1.put("overseas",coinName+"usdt@trade");
//                    coin.setChecked(RoomDB.getDatabase(context).DatabaseDao().favoriteExist(coinName.toUpperCase(),"coinone"));
                    CoinRepository.getInstance().add(coin.getMarket(),coin);
                    binanceMarket.put(jsonObject1);
                    }

                }

                AddBinanceList(jsonObject,binanceMarket,"코인원");
                CoinRepository.getInstance().getListliveData().postValue(CoinRepository.getInstance().getAllList());
                //해외거래소 웹소켓
                request = new Request.Builder()
                        .url(binanceSocketUri)
                        .build();
                overseasListener = WebSocketListener.getInstance(this,context, WebSocketListener.WebsocketType.overseas);
                overseasListener.addExchangeName("바이낸스",this);
                client.newWebSocket(request, overseasListener);
            }else{
                while(i.hasNext()){
                    key = i.next().toString();
                    if(!(key.equals("result") ^ key.equals("errorCode") ^ key.equals("timestamp"))){
//                           코인 이름 영문 ex) xec doge
                        String coinName = jsonObject.getJSONObject(key).getString("currency");
                        Double coinPrice = jsonObject.getJSONObject(key).getDouble("last");
                        Double coinChange = jsonObject.getJSONObject(key).getDouble("yesterday_last");
                        String change;
                        if(coinPrice>coinChange){
                            change = "RISE";
                            multiply = 1;
                        }else if(coinPrice<coinChange){
                            change = "FALL";
                            multiply = -1;
                        }else{
                            change = "EVEN";
                            multiply = 1;
                        }
                        double daytoday = Math.abs(coinPrice-coinChange);
                        Coin coin = new Coin();
                        coin.setCoinPrice(coinPrice);
                        coin.setCoinName(coinName);
                        coin.setCoinDaytoday((daytoday/(coinPrice-daytoday))*multiply);
                        coin.setCoinChange(change);
                        coin.setExchange("coinone");
                        coin.setMarket(coinName);
                        CoinRepository.getInstance().updateList(coin.getCoinName(),coin);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //거래소 배열 클리어
    public void ExchangeClear(){
        upbitMarket = new JSONArray();
        bithumbMarket = new JSONArray();
        coinoneMarket = new JSONArray();
        korbitMarket = new JSONArray();
        binanceMarket = new JSONArray();
    }
}
