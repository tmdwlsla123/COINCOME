package com.example.coincome.RecyclerView;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoinDiffCallback extends DiffUtil.Callback {
    private final List<Pair<String,Coin>> oldlist;
    private final List<Pair<String,Coin>> newlist;

    public CoinDiffCallback(List<Pair<String,Coin>> oldlist, List<Pair<String,Coin>> newlist){
        this.oldlist = oldlist;
        this.newlist = newlist;
    }
    @Override
    public int getOldListSize() {
     return  oldlist.size();
    }

    @Override
    public int getNewListSize() {
     return  newlist.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

    return oldlist.get(oldItemPosition).second.getMarket()==newlist.get(newItemPosition).second.getMarket();


    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        return (oldlist.get(oldItemPosition).second.getCoinPrice()!=newlist.get(newItemPosition).second.getCoinPrice())||(oldlist.get(oldItemPosition).second.getCoinOverseasPrice()!=newlist.get(newItemPosition).second.getCoinOverseasPrice());

    }
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
//        Log.v("diffcallback","oldItemPosition : "+oldItemPosition + ",newItemPosition : "+ newItemPosition);
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
