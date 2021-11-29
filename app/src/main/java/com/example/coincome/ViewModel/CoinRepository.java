package com.example.coincome.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.coincome.Implements.SortArrayList;
import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.Room.Favorite;

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

public class CoinRepository {

    private static CoinRepository instance = new CoinRepository();


    private MutableLiveData<List<Coin>> listliveData = new MutableLiveData<>();
    private MutableLiveData<List<Coin>> searchliveData = new MutableLiveData<>();
    public int favFlag;



    private List<Coin> list = new ArrayList<>();
    private String s;
    private int flag;
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index = 0;
    public Double getUsdkrw() {
        return usdkrw;
    }

    public void setUsdkrw(double usdkrw) {
        this.usdkrw = usdkrw;
    }

    private Double usdkrw;
    public void add(Coin coin){
        list.add(coin);
    }
    public void remove(Coin coin){
        list.remove(coin);
    }
    public List<Coin> getAllList(){
        return list;
    }
    public void updateList(Coin coin) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMarket().equals(coin.getMarket())) {
                list.get(i).setCoinPrice(coin.getCoinPrice());
                list.get(i).setCoinDaytoday(coin.getCoinDaytoday());
                list.get(i).setCoinChange(coin.getCoinChange());
                list.get(i).setTradeVolume(coin.getTradeVolume());
                break;
            }
        }


//        if(s!=null){
//            Set set = s.keySet();
//            Iterator iterator = set.iterator();
//            while(iterator.hasNext()){
//                key = (String)iterator.next();
//                Log.v("coinrepo",key);
//            }
//        }
        //정렬
        try {
            if(flag==0){
                //이름
                Collections.sort(list, new SortArrayList().new NameSortArrayList(index){});
            }else if(flag==1){
                //가격
                Collections.sort(list, new SortArrayList().new PriceSortArrayList(index){});
            }else if(flag==2){
                //전일대비
                Collections.sort(list, new SortArrayList().new DaytodaySortArrayList(index){});
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //검색 데이터 + 관심상태
        if((!s.equals("") && s !=null) && favFlag==1){
            List<Coin> coinlist = new ArrayList<>();
            for(int i = 0; i< list.size(); i++) {
                //검색+즐찾
                if ((list.get(i).getCoinName().contains(s) || list.get(i).getSymbol().contains(s.toUpperCase())) && list.get(i).isChecked()) {
                    coinlist.add(list.get(i));
                }

            }
            searchliveData.postValue(coinlist);
            //검색만
        }else if((!s.equals("") && s !=null) && favFlag==0){
            List<Coin> coinlist = new ArrayList<>();
            for(int i = 0; i< list.size(); i++) {
                //검색
                if (list.get(i).getCoinName().contains(s) || list.get(i).getSymbol().contains(s.toUpperCase())) {
                    coinlist.add(list.get(i));
                }

            }
            searchliveData.postValue(coinlist);
        }else if((s.equals("") || s ==null) && favFlag==1){
            List<Coin> coinlist = new ArrayList<>();
            for(int i = 0; i< list.size(); i++) {
                //검색
                if (list.get(i).isChecked()) {
                    coinlist.add(list.get(i));
                }

            }
            searchliveData.postValue(coinlist);
        }else{
            listliveData.postValue(list);
        }





    }
    //해외거래소 시세
    public void updateOverseasList(Coin coin){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMarket().equals(coin.getMarket())) {
                list.get(i).setCoinOverseasPrice(coin.getCoinOverseasPrice());
                if(list.get(i).getCoinPrice()!=null&&getUsdkrw()!=null&&coin.getCoinOverseasPrice()!=null){
                    double kimpPrice =  (list.get(i).getCoinPrice()-CoinRepository.getInstance().getUsdkrw()*coin.getCoinOverseasPrice());
                    double kimpPercent = kimpPrice/(list.get(i).getCoinPrice()-kimpPrice)*100;
                list.get(i).setCoinPremium(kimpPercent);
                }
                break;
            }
        }

        if(flag==3){
            //김프
            try {
                Collections.sort(list, new SortArrayList().new PremiumSortArrayList(index){});

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        listliveData.postValue(list);
    }


    public MutableLiveData<List<Coin>> getListliveData() {
        return listliveData;
    }

    public void setListliveData(MutableLiveData<List<Coin>> listliveData) {
        this.listliveData = listliveData;
    }
    public MutableLiveData<List<Coin>> getListOfSearchLiveData(String s){
        this.s = s;
        return searchliveData;
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
