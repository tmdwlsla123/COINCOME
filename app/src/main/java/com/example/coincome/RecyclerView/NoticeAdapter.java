package com.example.coincome.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coincome.Fragment.NoticeDetailFragment;
import com.example.coincome.MainActivity;
import com.example.coincome.R;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Notice> noticeList = new ArrayList<>();

    public NoticeAdapter(Context context) {
        this.context = context;

    }
    public void submitList(ArrayList<Notice> noticeList){
        this.noticeList = noticeList;
        notifyDataSetChanged();
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
        final Notice notice = noticeList.get(position);
        holder.title.setText(notice.getTitle());
        holder.datetime.setText(notice.getDatetime());
        holder.exchange.setText(notice.getExchange());
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView datetime;
        TextView exchange;
        Fragment noticeDetail;
        public ViewHolder(View view, Context context) {
            super(view);
            title = view.findViewById(R.id.title);
            datetime = view.findViewById(R.id.datetime);
            exchange = view.findViewById(R.id.exchange);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noticeDetail = new NoticeDetailFragment();
                    ((MainActivity)context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment,noticeDetail)
                            .addToBackStack(null)
                            .commit();
                    Bundle bundle = new Bundle();
//                    bundle.putString("title", noticeList.get(getLayoutPosition()).getTitle());
                    bundle.putSerializable("data",noticeList);
                    bundle.putInt("position",getLayoutPosition());
                    noticeDetail.setArguments(bundle);

                }
            });

        }

    }
}
