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
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {
    Context context;
    JSONArray jsonArray;
    @NonNull
    @Override
    public QuoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list, parent, false);
        QuoteAdapter.ViewHolder holder = new QuoteAdapter.ViewHolder(view,context);
        return holder;
    }

    @Override
    public void onBindViewHolder(QuoteAdapter.ViewHolder holder, int position) {

        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.coin_name.setText(jsonObject.getString("cd"));
                holder.coin_price.setText(jsonObject.getString("tp"));




            Log.v("onBindViewHolder", "onBindViewHolder : "+jsonObject.getString("cd"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
    public QuoteAdapter(Context context){
        this.context = context;
        jsonArray = new JSONArray();
        notifyDataSetChanged();

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView coin_name;
        TextView coin_price;
        public ViewHolder(View view,Context context) {
            super(view);

            coin_name = view.findViewById(R.id.coin_name);
            coin_price = view.findViewById(R.id.coin_price);
        }

    }
    public void Add(JSONObject jsonObject){
        jsonArray.put(jsonObject);
        notifyDataSetChanged();
    }

}
