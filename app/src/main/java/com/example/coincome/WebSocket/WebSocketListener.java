package com.example.coincome.WebSocket;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;

import androidx.annotation.Nullable;

import com.example.coincome.Exchange.Exchange;
import com.example.coincome.RecyclerView.QuoteAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class WebSocketListener extends okhttp3.WebSocketListener {
    Exchange exchange;
    StringBuilder str;
    QuoteAdapter quoteAdapter;
    Context context;
    public WebSocketListener(Exchange exchange, QuoteAdapter quoteAdapter,Context context) {
    this.exchange = exchange;
    this.quoteAdapter = quoteAdapter;
    this.context = context;
        str = new StringBuilder();
        marketList();
    }
    private void marketList(){
        str.append(exchange.upbit_Market);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.v("socket", "str"+str);
        webSocket.send("[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":"+str+"},{\"format\":\"SIMPLE\"}]");

        webSocket.close(1000, null); //없을 경우 끊임없이 서버와 통신함
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
            Log.v("socket",str);
            JSONObject jsonObject =   new JSONObject(str);
            Log.v("socket", "market : " + jsonObject.getString("cd"));
            Log.v("socket", "현재가 : " + jsonObject.getString("tp"));
            Log.v("socket", "전일대비 : " + jsonObject.getString("c"));


            ((Activity) context).runOnUiThread(new Runnable() {
                @Override public void run() {
                    quoteAdapter.Add(jsonObject);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


//        Log.d("Socket","Receiving : bytes" + bytes);
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
    final Handler handler = new Handler()

    {

        public void handleMessage(Message msg)

        {

            // 원래 하고싶었던 일들 (UI변경작업 등...)

        }

    };
}
