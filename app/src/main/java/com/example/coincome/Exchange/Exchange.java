package com.example.coincome.Exchange;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Exchange {
    public JSONArray upbit_Market;
    public ArrayList bithumb_Market;
    public ArrayList coinone_Market;
    public ArrayList korbit_Market;
    int abc;
    public Exchange(){
        upbit_Market = new JSONArray();
        bithumb_Market = new ArrayList();
        coinone_Market = new ArrayList();
        korbit_Market = new ArrayList();
    }



        public void Addlist(JSONArray jsonArray,JSONArray exchange){

            try {
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject =  jsonArray.getJSONObject(i);
                    if(jsonObject.getString("market").contains("KRW")){
                        jsonObject.put("cd",jsonObject.getString("market"));
                        jsonObject.put("cn",jsonObject.getString("korean_name"));
                        jsonObject.remove("korean_name");
                        jsonObject.remove("market");
                        exchange.put(jsonObject);
                    }
                }
                Log.v("retrofit2","거래소 : "+exchange);
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }
}
