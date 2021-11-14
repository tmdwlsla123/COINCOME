package com.example.coincome.ViewModel;

import android.util.Log;
import android.view.animation.Transformation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.coincome.RecyclerView.Coin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoinViewModel extends ViewModel {
    private CoinRepository coin;
    public LiveData<List<Coin>> liveData;
    public MutableLiveData<String> searchQuery =  new MutableLiveData<>();
    public MutableLiveData<List<Coin>> searchData = new MutableLiveData<>();

    public CoinViewModel(){
        coin = CoinRepository.getInstance();
    }

    public LiveData<List<Coin>> getListliveData(){


        return coin.getListliveData();

    }
    public LiveData<List<Coin>> getSearchliveData(LiveData<List<Coin>> coinlist){

            liveData = Transformations.switchMap(searchQuery,string -> {
                if (string == null || string.equals("")){
                    if(string.equals("")) onlySearchData(string);
                    return coinlist;
                }else{
                    onlySearchData(string);
                    return searchData;
                }


            });



    return liveData;
    }
    public void onlySearchData(String s){

        searchData = coin.getListOfSearchLiveData(s);

    };
}
