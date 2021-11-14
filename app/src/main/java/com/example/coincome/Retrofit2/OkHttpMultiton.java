package com.example.coincome.Retrofit2;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkHttpMultiton {
    private static OkHttpMultiton instance = new OkHttpMultiton();
    private OkHttpClient client;
    private OkHttpMultiton(){
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
    }
    public OkHttpMultiton getInstance(){
        if(instance==null){
            instance = new OkHttpMultiton();
        }
        return instance;
    }
    public void newWebsocket(){
//        client.newWebSocket();
    }
}
