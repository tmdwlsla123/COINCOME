package com.example.coincome.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.coincome.CoinSymbol.CoinAPIInterface;
import com.example.coincome.CoinSymbol.UpbitAPI;
import com.example.coincome.Implements.SortArrayList;
import com.example.coincome.RecyclerView.Coin;
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

public class CoinRepository {

    private static CoinRepository instance = new CoinRepository();


    private MutableLiveData<List<Coin>> listliveData = new MutableLiveData<>();
    private MutableLiveData<List<Coin>> searchliveData = new MutableLiveData<>();
    public int favFlag;

    private List<Coin> valueList = new ArrayList<>();


    private ConcurrentHashMap<String,Coin> list = new ConcurrentHashMap<>();
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
    public void add(String key, Coin coin){
        list.put(key,coin);
    }
    public void put(List<CoinAPIInterface> coin, Context context) {
        for (CoinAPIInterface item : coin) {
            if (item instanceof UpbitAPI) {
                if (null != item.getCoinSymbol()) {
                    UpbitAPI upbitAPI = (UpbitAPI) item;
                    Coin coin1 = new Coin();
                    String[] array = upbitAPI.getMarket().split("-");
                    coin1.setMarket(array[1]+"-"+array[0]);
                    coin1.setSymbol(array[1]);
                    coin1.setCoinName(upbitAPI.getKorean_name());
                    coin1.setExchange("upbit");
//                    coin1.setChecked(RoomDB.getDatabase(context).DatabaseDao().favoriteExist(array[1],"upbit"));
                    coin1.setChecked(false);
                    list.put(coin1.getMarket(), coin1);
                }
            }
        }
    }
    public void remove(Coin coin){
        list.remove(coin);
    }
    public List<Coin> getAllList(){
        return valueList;
    }
    public void updateList(String key,Coin coin) {

//        coin.setTradeVolume(jsonObject.getDouble("atp24h"));
//        coin.setCoinChange(jsonObject.getString("c"));
//        coin.setCoinPrice(jsonObject.getDouble("tp"));
//
//        coin.setCoinDaytoday(jsonObject.getDouble("scp")/(coin.getCoinPrice()-jsonObject.getDouble("scp")));
//        String[] array = jsonObject.getString("cd").split("-");
//        coin.setMarket(array[1]+"-"+array[0]);
//        coinRepo.updateList(coin.getMarket(),coin);
// 리팩토링 해야함
        list.get(key).setTradeVolume(coin.getTradeVolume());
        list.get(key).setCoinChange(coin.getCoinChange());
        list.get(key).setCoinPrice(coin.getCoinPrice());
        list.get(key).setCoinDaytoday(coin.getCoinDaytoday());
        list.get(key).setMarket(coin.getMarket());
        valueList = new ArrayList<>(list.values());
        //정렬
        try {
            if(flag==0){
                //이름
                Collections.sort(valueList, new SortArrayList().new NameSortArrayList(index){});
            }else if(flag==1){
                //가격
                Collections.sort(valueList, new SortArrayList().new PriceSortArrayList(index){});
            }else if(flag==2){
                //전일대비
                Collections.sort(valueList, new SortArrayList().new DaytodaySortArrayList(index){});
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //검색 데이터 + 관심상태
        if((!s.equals("") && s !=null) && favFlag==1){
            List<Coin> coinlist = new ArrayList<>();
            for(int i = 0; i< valueList.size(); i++) {
                //검색+즐찾
                if ((valueList.get(i).getCoinName().contains(s) || valueList.get(i).getSymbol().contains(s.toUpperCase())) && valueList.get(i).isChecked()) {
                    coinlist.add(valueList.get(i));
                }

            }
            searchliveData.postValue(coinlist);
            //검색만
        }else if((!s.equals("") && s !=null) && favFlag==0){
            List<Coin> coinlist = new ArrayList<>();
            for(int i = 0; i< valueList.size(); i++) {
                //검색
                if (valueList.get(i).getCoinName().contains(s) || valueList.get(i).getSymbol().contains(s.toUpperCase())) {
                    coinlist.add(valueList.get(i));
                }

            }
            searchliveData.postValue(coinlist);
        }else if((s.equals("") || s ==null) && favFlag==1){
            List<Coin> coinlist = new ArrayList<>();
            for(int i = 0; i< valueList.size(); i++) {
                //검색
                if (valueList.get(i).isChecked()) {
                    coinlist.add(valueList.get(i));
                }

            }
            searchliveData.postValue(coinlist);
        }else{
            listliveData.postValue(valueList);
        }





    }
    //해외거래소 시세
    public void updateOverseasList(String key,Coin coin){
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getMarket().equals(coin.getMarket())) {
//                list.get(i).setCoinOverseasPrice(coin.getCoinOverseasPrice());
//                if(list.get(i).getCoinPrice()!=null&&getUsdkrw()!=null&&coin.getCoinOverseasPrice()!=null){
//                    double kimpPrice =  (list.get(i).getCoinPrice()-CoinRepository.getInstance().getUsdkrw()*coin.getCoinOverseasPrice());
//                    double kimpPercent = kimpPrice/(list.get(i).getCoinPrice()-kimpPrice)*100;
//                list.get(i).setCoinPremium(kimpPercent);
//                }
//                break;
//            }
//        }
        list.put(key,coin);
        List<Coin> valueList = new ArrayList<>(list.values());
        if(flag==3){
            //김프
            try {
                Collections.sort(valueList, new SortArrayList().new PremiumSortArrayList(index){});

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        listliveData.postValue(valueList);
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
