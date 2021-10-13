package com.example.coincome.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.coincome.RecyclerView.Coin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoinViewModel extends ViewModel {
    private CoinRepository coin;
    public CoinViewModel(){
        coin = CoinRepository.getInstance();
    }
    public LiveData<JSONArray> getCurrentCoin(){

        return coin.getMutableLiveData();
    }
    public LiveData<List<Coin>> getListliveData(){
        return coin.getListliveData();
    }
}
