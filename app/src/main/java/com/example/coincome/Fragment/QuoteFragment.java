package com.example.coincome.Fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coincome.Exchange.Exchange;
import com.example.coincome.R;
import com.example.coincome.RecyclerView.QuoteAdapter;
import com.example.coincome.Retrofit2.ApiInterface;
import com.example.coincome.Retrofit2.CallbackWithRetry;
import com.example.coincome.Retrofit2.HttpClient;
import com.example.coincome.WebSocket.WebSocketListener;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;


public class QuoteFragment extends Fragment {
    /**
     *  첫번째 탭 시세창
     *
     */
    ArrayList<String> list;
    private Context context;
    private OkHttpClient client;
    RecyclerView listView;

    Exchange exchange;
    Request request;
    WebSocketListener listener;
    ApiInterface api;

    QuoteAdapter quoteAdapter;
    String upbitSocketUrl = "wss://api.upbit.com/websocket/v1";
    String bithumbSocketUrl = "wss://pubwss.bithumb.com/pub/ws";
    String coinoneSocketUrl = "wss://api.upbit.com/websocket/v1";
    String korbitSocketUrl = "wss://api.upbit.com/websocket/v1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_quote, container, false);

        exchange = new Exchange();
        requestGet(upbitSocketUrl);
//        requestGet(bithumbSocketUrl);


        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();


        Log.d("Socket","Receiving : text");




        quoteAdapter = new QuoteAdapter(context);
        listView = rootView.findViewById(R.id.noti_list);
        listView.setAdapter(quoteAdapter);
        listView.setLayoutManager(new LinearLayoutManager(context));

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

        public void requestGet(String webSocketUrl) {
        String url = "all"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        Call<String> call = api.requestGet(url);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //     서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능

//                if (response.isSuccessful()) {
//                    // Do your success stuff...
//                } else {
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        Log.v("retrofit2", jObjError.getJSONObject("error").getString("message"));
//                    } catch (Exception e) {
//                        Log.v("retrofit2", e.getMessage());
//                    }
//                }


                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    exchange.Addlist(jsonArray,exchange.upbit_Market);

                    listener = new WebSocketListener(exchange,quoteAdapter,context);

                    request = new Request.Builder()
                            .url(webSocketUrl)
                            .build();
                    client.newWebSocket(request, listener);
                    Log.v("retrofit2", jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }

}
