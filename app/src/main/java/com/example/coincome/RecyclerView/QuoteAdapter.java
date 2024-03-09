package com.example.coincome.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coincome.ChartActivity;
import com.example.coincome.Implements.ThemeUtil;
import com.example.coincome.R;
import com.example.coincome.Room.Favorite;
import com.example.coincome.Room.RoomDB;
import com.example.coincome.Room.Setting;
import com.example.coincome.ViewModel.CoinRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder>{
    Context context;

    final AsyncListDiffer<Coin> differ  = new AsyncListDiffer<Coin>(this,DIFF_CALLBACK);
    private HashMap<String,Coin> coinHashMap = new HashMap<>();
    private List<Pair<String,Coin>> coinList;


    public static final DiffUtil.ItemCallback<Coin> DIFF_CALLBACK
            = new DiffUtil.ItemCallback<Coin>() {

        @Override
        public boolean areItemsTheSame(
                @NonNull Coin oldUser, @NonNull Coin newUser) {
            // User properties may have changed if reloaded from the DB, but ID is fixed
            Log.v("diffcallback","areItemsTheSame oldItem : "+oldUser + ",newItem : "+ newUser);
            return oldUser.getMarket() == newUser.getMarket();

        }
        @Override
        public boolean areContentsTheSame(
                @NonNull Coin oldUser, @NonNull Coin newUser) {
            // NOTE: if you use equals, your object must properly override Object#equals()
            // Incorrectly returning false here will result in too many animations.
            Log.v("diffcallback","areContentsTheSame oldItem : "+oldUser + ",newItem : "+ newUser);
            return oldUser.getCoinPrice()!=newUser.getCoinPrice()||oldUser.getCoinOverseasPrice()!=newUser.getCoinOverseasPrice();
        }

        @Nullable
        @Override
        public Object getChangePayload(@NonNull Coin oldItem, @NonNull Coin newItem) {
            Log.v("diffcallback","oldItem : "+oldItem + ",newItem : "+ oldItem);
            return super.getChangePayload(oldItem, newItem);
        }
    };

    public void updateQouteAdapter(HashMap<String,Coin> coinHashMap) {
        Log.v("null 참조",coinHashMap.toString());
        Log.v("null 참조",this.coinHashMap.toString());
        this.coinHashMap.putAll(coinHashMap);
        coinList = new ArrayList<>(coinHashMap.size());
        coinHashMap.forEach((s,s2) -> coinList.add(new Pair<>(s,s2)));

        final CoinDiffCallback diffCallback = new CoinDiffCallback(this.coinList, coinList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);





        diffResult.dispatchUpdatesTo(QuoteAdapter.this);
//        differ.submitList(coinList);

//


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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {

            super.onBindViewHolder(holder, position, payloads);

                    final Coin coin = coinList.get(position).second;
//            final Coin coin = differ.getCurrentList().get(position);
            try {
                if(coin.getCoinPrice()!=null){
                    holder.coin_price.setText(MakePriceFormat(coin.getCoinPrice()));
                }else{
                    holder.coin_price.setText("");
                }
                if(coin.getCoinOverseasPrice()!=null){
                    holder.coin_overseasprice.setText(MakePriceFormat(coin.getCoinOverseasPrice()*CoinRepository.getInstance().getUsdkrw()));
                }else{
                    holder.coin_overseasprice.setText("");
                }

                if(coin.isChecked()){
                    holder.coin_favorite.setImageResource(R.drawable.star_selected);
                }else{
                    holder.coin_favorite.setImageResource(R.drawable.star);
                }
                if(coin.getExchange().equals("upbit")||coin.getExchange().equals("bithumb")){
                    holder.chart_icon.setImageResource(R.drawable.chart);
                }else{
                    holder.chart_icon.setImageResource(0);
                }
                holder.coin_favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {

                                boolean isExist = RoomDB.getDatabase(context).DatabaseDao().favoriteExist(coin.getSymbol(),coin.getExchange());
                                if(isExist){
                                    RoomDB.getDatabase(context).DatabaseDao().deleteFavorite(coin.getSymbol(),coin.getExchange());
                                    coin.setChecked(false);
                                }else{
                                    Favorite favorite = new Favorite();
                                    favorite.setSymbol(coin.getSymbol());
                                    favorite.setExchange(coin.getExchange());


                                        RoomDB.getDatabase(context).DatabaseDao().insesrtFavorite(favorite);


                                    coin.setChecked(true);
                                }

                            }
                        });
                    }
                });
                holder.coin_name.setText(coin.getCoinName());
//            Log.v("adapter",coin.getCoinName());
                String plus;

                //전일대비 계산식 (현재가 - 전일종가)/전일 종가 x 100%
                if(coin.getCoinChange().equals("RISE")){
                    holder.coin_daytoday.setTextColor(Color.parseColor("#ef5350"));
                    holder.coin_price.setTextColor(Color.parseColor("#ef5350"));
                    plus = "+";
                }else if(coin.getCoinChange().equals("FALL")){
                    holder.coin_daytoday.setTextColor(Color.parseColor("#285ff5"));
                    holder.coin_price.setTextColor(Color.parseColor("#285ff5"));
                    plus = "";
                }else{
                    //같음
                    plus = "";
                    holder.coin_daytoday.setTextColor(ContextCompat.getColor(context,R.color.black));
                    holder.coin_price.setTextColor(ContextCompat.getColor(context,R.color.black));
                }
//        coin.getCoinOverseasPrice()
                String daytoday = String.format("%.2f",coin.getCoinDaytoday()*100);
                holder.coin_symbol.setText(coin.getMarket());
                holder.coin_daytoday.setText(plus+daytoday+"%");
                if(coin.getCoinPremium()!=null){
                    String plus1;
                    if(coin.getCoinPremium()>0){
                        plus1 = "+";
                        holder.coin_premium.setTextColor(Color.parseColor("#26A69A"));
                    }else{
                        plus1 = "";
                        holder.coin_premium.setTextColor(Color.parseColor("#EF5350"));
                    }
                    String premium = String.format("%.2f",coin.getCoinPremium());
                    holder.coin_premium.setText(plus1+premium+"%");
                }else{
                    holder.coin_premium.setText("");
                }
            }catch (Exception e){

            }


    }




    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
//        return differ.getCurrentList().size();
        return coinList.size();
    }

    public QuoteAdapter(Context context) {
        this.context = context;


        coinList = new ArrayList<>();
//        notifyDataSetChanged();

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout chart;
        TextView coin_name;
        TextView coin_price;
        TextView coin_daytoday;
        TextView coin_overseasprice;
        TextView coin_premium;
        TextView coin_symbol;
        ImageView coin_favorite;
        ImageView chart_icon;
        public ViewHolder(View view, Context context) {
            super(view);
            coin_daytoday = view.findViewById(R.id.coin_daytoday);
            coin_name = view.findViewById(R.id.coin_name);
            coin_price = view.findViewById(R.id.coin_price);
            coin_overseasprice = view.findViewById(R.id.coin_overseasprice);
            coin_premium = view.findViewById(R.id.coin_premium);
            coin_symbol = view.findViewById(R.id.coin_symbol);
            coin_favorite = view.findViewById(R.id.coin_favorite);
            chart = view.findViewById(R.id.chart);
            chart_icon = view.findViewById(R.id.chart_icon);
            chart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(coinList.get(getPosition()).second.getExchange().equals("upbit")||coinList.get(getPosition()).second.getExchange().equals("bithumb")){
                        Intent intent = new Intent(context, ChartActivity.class);
                        intent.putExtra("exchange",coinList.get(getPosition()).second.getExchange());
                        intent.putExtra("symbol",coinList.get(getPosition()).second.getSymbol()+"KRW");
                        intent.putExtra("theme", ThemeUtil.NOW_MODE);
                        context.startActivity(intent);
                    }
                }
            });
        }

    }

    public void Add(HashMap<String,Coin> coin) {
//            this.jsonArray = jsonArray;
        this.coinHashMap = coin;
//            notifyDataSetChanged();
    }
    private String MakePriceFormat(double price){
        String tmp;
//        Log.v("double", String.valueOf(price));
        BigDecimal bigDecimal = new BigDecimal(price);
        DecimalFormat df = new DecimalFormat("###,###");
        if(price>100){
          tmp = df.format(bigDecimal);
        }else if(1>price){
            tmp = String.format("%.4f",bigDecimal);
        }else if(10>price){
            tmp = String.format("%.2f",bigDecimal);
        }else{
            tmp = String.format("%.1f",bigDecimal);
        }
        return tmp;
    }
}

