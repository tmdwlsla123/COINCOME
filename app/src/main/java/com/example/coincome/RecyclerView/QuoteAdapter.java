package com.example.coincome.RecyclerView;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coincome.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {
    Context context;

    JSONArray jsonArray;
    List<Coin> coinlist;
    public void submitList(final List<Coin> newList) {
        if (newList == coinlist) {
            // nothing to do
            return;
        }
    }
    public void updateQouteAdapter(List<Coin> coinlist) {
        final CoinDiffCallback diffCallback = new CoinDiffCallback(this.coinlist, coinlist);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.coinlist.clear();
        this.coinlist.addAll(coinlist);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public QuoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list, parent, false);
        QuoteAdapter.ViewHolder holder = new QuoteAdapter.ViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Coin coin = coinlist.get(position);
//        if(Float.parseFloat(coin.getCoinPrice())>=100){
//            holder.coin_price.setText(Integer.parseInt(coin.getCoinPrice()));
//        }else{
            holder.coin_price.setText(coin.getCoinPrice());
//        }
        holder.coin_name.setText(coin.getCoinName());

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);


    }

//    @Override
//    public void onBindViewHolder(QuoteAdapter.ViewHolder holder, int position) {
//
//        final Coin coin = coinlist.get(position);
//
//            holder.coin_name.setText(coin.getCoinName());
//            holder.coin_price.setText(coin.getCoinPrice());
//
//
//    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return coinlist.size();
    }

    public QuoteAdapter(Context context) {
        this.context = context;

        jsonArray = new JSONArray();
       coinlist = new ArrayList<>();
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView coin_name;
        TextView coin_price;

        public ViewHolder(View view, Context context) {
            super(view);

            coin_name = view.findViewById(R.id.coin_name);
            coin_price = view.findViewById(R.id.coin_price);
        }

    }

    public void Add(List<Coin> coin) {
//            this.jsonArray = jsonArray;
        this.coinlist = coin;
//            notifyDataSetChanged();
    }

}

