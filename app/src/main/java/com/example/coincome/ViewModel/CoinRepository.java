package com.example.coincome.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.coincome.RecyclerView.Coin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoinRepository {

    private static CoinRepository instance = new CoinRepository();
    private MutableLiveData<JSONArray> liveData = new MutableLiveData<>();

    private MutableLiveData<List<Coin>> listliveData = new MutableLiveData<>();
    private List<Coin> list = new ArrayList<>();

    public void add(Coin coin){
        list.add(coin);
    }
    public void remove(Coin coin){
        list.remove(coin);
    }
    public List<Coin> getAllList(){
        return list;
    }
    public void updateList(Coin coin){
        for(int i = 0; i<list.size(); i++){
            if(list.get(i).getMarket().equals(coin.getMarket())){
                list.get(i).setMarket(coin.getMarket());
                list.get(i).setCoinPrice(coin.getCoinPrice());
                listliveData.postValue(list);
//                Log.v("updateList", String.valueOf(list.get(i).getMarket().equals(coin.getMarket())));
            }
        }
    }


    public MutableLiveData<List<Coin>> getListliveData() {
        return listliveData;
    }

    public void setListliveData(MutableLiveData<List<Coin>> listliveData) {
        this.listliveData = listliveData;
    }


    private JSONArray coin = new JSONArray();
    public static CoinRepository getInstance() {
        if (instance == null) {
            instance = new CoinRepository();
        }
        return instance;
    }

    private CoinRepository() {

    }
    public MutableLiveData<JSONArray> getMutableLiveData(){
        return liveData;
    }
    public void setMutableLiveData(){
        liveData.postValue(coin);
    }

    public JSONArray getCoin() {
        return coin;
    }

    public void setCoin(JSONObject jsonObject) {
        this.coin.put(jsonObject);
    }
    public void setMerge(JSONObject jsonObject){

        for(int i=0; i<coin.length(); i++){
            try {
                if(coin.getJSONObject(i).getString("cd").equals(jsonObject.getString("cd"))){
                    coin.put(i,merge(jsonObject,coin.getJSONObject(i)));
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    setMutableLiveData();
    }
    private static JSONObject merge(JSONObject... jsonObjects) throws JSONException {

        JSONObject jsonObject = new JSONObject();

        for(JSONObject temp : jsonObjects){
            Iterator<String> keys = temp.keys();
            while(keys.hasNext()){
                String key = keys.next();
                jsonObject.put(key, temp.get(key));
            }

        }
        return jsonObject;
    }
}
