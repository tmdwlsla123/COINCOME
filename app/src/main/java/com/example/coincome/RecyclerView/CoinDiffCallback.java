package com.example.coincome.RecyclerView;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CoinDiffCallback extends DiffUtil.Callback {
    private final List<Coin> oldlist;
    private final List<Coin> newlist;

    public CoinDiffCallback(List<Coin> oldlist, List<Coin> newlist){
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

    return oldlist.get(oldItemPosition).getMarket()==newlist.get(newItemPosition).getMarket();


    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        return oldlist.get(oldItemPosition).getCoinPrice()!=newlist.get(newItemPosition).getCoinPrice();

    }
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        Log.v("diffcallback","oldItemPosition : "+oldItemPosition + ",newItemPosition : "+ newItemPosition);
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
