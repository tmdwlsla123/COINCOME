package com.example.coincome.ViewModel;

import android.app.Application;
import android.util.Log;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.Room.DatabaseDao;
import com.example.coincome.Room.Favorite;
import com.example.coincome.Room.RoomDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CoinViewModel extends AndroidViewModel {
    private CoinRepository coin;
    private DatabaseDao databaseDao;
    private RoomDB roomDB;
    public LiveData<HashMap<String,Coin>> liveData;
    public MutableLiveData<String> searchQuery =  new MutableLiveData<>();
    public MutableLiveData<HashMap<String,Coin>> searchData = new MutableLiveData<>();


    public CoinViewModel(@NonNull Application application){
        super(application);
        coin = CoinRepository.getInstance();
        roomDB = RoomDB.getDatabase(application);
        databaseDao = roomDB.DatabaseDao();
    }

    public LiveData<HashMap<String,Coin>> getListliveData(){


        return coin.getListliveData();

    }
    public LiveData<HashMap<String,Coin>> getSearchliveData(LiveData<HashMap<String,Coin>> coinlist){

            liveData = Transformations.switchMap(searchQuery,string -> {



                if ((string == null || string.equals(""))&&coin.favFlag==0){
                    Log.v("coinrepoviewmodle","생성안됨");
                    if(string.equals("")) onlySearchData(string);
                    return coinlist;
                }else{
                    Log.v("coinrepoviewmodle",string);
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
