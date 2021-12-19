package com.example.coincome.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coincome.Fragment.NoticeDetailFragment;
import com.example.coincome.MainActivity;
import com.example.coincome.Paging.NoticeModel;
import com.example.coincome.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NoticeAdapter extends PagingDataAdapter<NoticeModel.Data,NoticeAdapter.ViewHolder> {
    private Context context;
    private String id;

    static DiffUtil.ItemCallback<NoticeModel.Data> diff = new
            DiffUtil.ItemCallback<NoticeModel.Data>() {
                @Override
                public boolean areItemsTheSame(@NonNull NoticeModel.Data oldItem, @NonNull NoticeModel.Data newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull NoticeModel.Data oldItem, @NonNull NoticeModel.Data newItem) {
                    return oldItem.equals(newItem);
                }
            };
    public NoticeAdapter(Context context,String id) {
        super(diff);
        this.context = context;
        this.id = id;

    }
    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_list, parent, false);
        NoticeAdapter.ViewHolder holder = new NoticeAdapter.ViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder holder, int position) {
//        final Notice notice = noticeList.get(position);
//        Log.d("onBindViewHolder", getItem(position).toString());
        if(id!=null){
            if(id.equals(getItem(position).getId())){
                id="";
                holder.view.performClick();
                Log.v("FirebaseMessagingService","NoticeAdapter"+id);
            }
        }
        holder.title.setText(getItem(position).getTitle());
        holder.datetime.setText(getItem(position).getDatetime());
        holder.exchange.setText(getItem(position).getExchange());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView datetime;
        TextView exchange;
        Fragment noticeDetail;
        View view;
        public ViewHolder(View view, Context context) {
            super(view);
            title = view.findViewById(R.id.title);
            datetime = view.findViewById(R.id.datetime);
            exchange = view.findViewById(R.id.exchange);
            this.view = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noticeDetail = new NoticeDetailFragment();
                    ((MainActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment,noticeDetail,"noticeDetail")
                            .addToBackStack(null)
                            .commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("title",  getItem(getPosition()).getTitle());
                    bundle.putString("text",  getItem(getPosition()).getText());
                    bundle.putString("exchange",  getItem(getPosition()).getExchange());
                    bundle.putString("datetime",  getItem(getPosition()).getDatetime());
                    noticeDetail.setArguments(bundle);

                }
            });

        }

    }
}
