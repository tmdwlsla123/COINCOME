package com.example.coincome.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.coincome.Bybit.Bybit;
import com.example.coincome.Paging.NoticeModel;
import com.example.coincome.R;
import com.example.coincome.Retrofit2.ApiInterface;
import com.example.coincome.Retrofit2.HttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import org.json.JSONException;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.AroonOscillatorIndicator;
import org.ta4j.core.indicators.RSIIndicator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;


public class OrderFragment extends Fragment {


ApiInterface api;
    String apiKey = "dwSIyiQoTERQR5oMHn";
    String secret = "er314Soekay3ackbKnEOgLfatYNBCobtMaS5";
    Double openPositionPrice;
    boolean flag=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order, container, false);
        Button buyLong = rootView.findViewById(R.id.order_long);
        Button sellShort = rootView.findViewById(R.id.order_short);
        Button auto = rootView.findViewById(R.id.auto);
        buyLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest("Buy","0.1","false");
            }
        });
        sellShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest("Sell","0.1","true");
            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag==true){
                    flag=false;
                }else{
                    flag=true;
                    getOrderList();
                }

            }
        });
        return rootView;
    }




        public void sendRequest(String side, String qty,String reduce_only) {
        String url = "https://api-testnet.bybit.com/private/linear/order/create?"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
            TreeMap map = new TreeMap<String, String>(
                    new Comparator<String>() {
                        public int compare(String obj1, String obj2) {
                            //sort in alphabet order
                            return obj1.compareTo(obj2);
                        }
                    });
        HashMap<String,String> params = new HashMap<>();

            map.put("api_key","dwSIyiQoTERQR5oMHn");
            map.put("side",side);
            map.put("symbol","BTCUSDT");
            map.put("order_type","Market");
            map.put("qty",qty);
            map.put("time_in_force","GoodTillCancel");
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
            map.put("close_on_trigger","false");
            map.put("reduce_only",reduce_only);



            String queryString = null;
            try {
                queryString = genQueryString(map, secret);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            Call<String> call = api.requestPostOrder(url+queryString,params);
        Log.v("timestamp", String.valueOf(System.currentTimeMillis()));

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능

                Log.v("retrofit2",String.valueOf(response.body()));

            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }
    private String genQueryString(TreeMap<String, String> params, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Set<String> keySet = params.keySet();
        Iterator<String> iter = keySet.iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append(key + "=" + params.get(key));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return sb+"&sign="+bytesToHex(sha256_HMAC.doFinal(sb.toString().getBytes()));
    }
    private String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private void getOrderList(){

        long timestamp = (System.currentTimeMillis() - (1*1000*60*60*80)) / 1000;
        Log.v("timestamp", String.valueOf(timestamp));
        Date date = new Date(System.currentTimeMillis() - (1*1000*60*60*80));
        Date date1 = new Date(System.currentTimeMillis());
        Log.v("timestamp",date.toString());
        Log.v("timestamp",date1.toString());
        String url = "https://api-testnet.bybit.com/public/linear/index-price-kline?symbol=BTCUSDT&interval=240&limit=20&from="+timestamp; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );




        Call<Bybit> call = api.requestBybit(url);
        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<Bybit>() {
            // 통신성공 후
            @Override
            public void onResponse(Call<Bybit> call, retrofit2.Response<Bybit> response) {
                   //     서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능


                if(response.isSuccessful()) {
                    Bybit bybits = response.body();
                    double average = 0.0;

                        for(int i=0; i<bybits.getData().size(); i++){
                            average+=bybits.getData().get(i).getClose();
                        }
                    average= (average / bybits.getData().size());
                    double nowPrice = bybits.getData().get(bybits.getData().size()-1).getClose();
                    Log.v("retrofit2", "now price : "+nowPrice);
                    Log.v("retrofit2", "average : "+average);
                    Log.v("retrofit2","daytoday : "+(nowPrice-average)/average*100);

                    if(openPositionPrice==null){
//                     평균값보다 현재가격이 2퍼센트~2.5퍼센트 높을때 매수
                        if((nowPrice-average)/average*100>2&&2.5>(nowPrice-average)/average*100){
                            openPositionPrice = nowPrice;
                            sendRequest("Buy","0.1","false");
                        }
                    }else{
 //                       수익률이 4퍼센트 넘어가면 매도 -2퍼센트넘어가면 매도
                        if((nowPrice-openPositionPrice)/openPositionPrice*100>4||-2>(nowPrice-openPositionPrice)/openPositionPrice*100) {
                            openPositionPrice = null;
                            sendRequest("Sell", "0.1", "true");
                        }
                    }
                    try {
                        Thread.sleep(1000); //1초 대기
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(flag==true){
                        getOrderList();
                    }else{

                    }

                }
            }

            @Override
            public void onFailure(Call<Bybit> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }

            // 통신실패

        });

    }
    private void postOrderCancel(){

        String url = "https://api-testnet.bybit.com/private/linear/position/trading-stop?"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );


        TreeMap map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        //sort in alphabet order
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("api_key",apiKey);
        map.put("symbol","BTCUSDT");
        map.put("side","Sell");
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        String queryString = null;
        try {
            queryString = genQueryString(map, secret);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        HashMap<String,String> params = new HashMap<>();
        Call<String> call = api.requestPostOrder(url+queryString,params);
        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //     서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(response.body());
                String prettyJsonString = gson.toJson(je);
                Log.v("retrofit2",prettyJsonString);

            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });

    }

}