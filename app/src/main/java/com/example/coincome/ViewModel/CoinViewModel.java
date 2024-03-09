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
import java.util.concurrent.ConcurrentHashMap;

public class CoinViewModel extends AndroidViewModel {
    private CoinRepository coin;
    private DatabaseDao databaseDao;
    private RoomDB roomDB;
    public LiveData<List<Coin>> liveData;
    public MutableLiveData<String> searchQuery =  new MutableLiveData<>();
    public MutableLiveData<List<Coin>> searchData = new MutableLiveData<>();


    public CoinViewModel(@NonNull Application application){
        super(application);
        coin = CoinRepository.getInstance();
        roomDB = RoomDB.getDatabase(application);
        databaseDao = roomDB.DatabaseDao();
    }

    public LiveData<List<Coin>> getListliveData(){


        return coin.getListliveData();

    }
    public LiveData<List<Coin>> getSearchliveData(LiveData<List<Coin>> coinlist){

            liveData = Transformations.switchMap(searchQuery,string -> {



                if ((string == null || string.equals(""))&&coin.favFlag==0){

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
