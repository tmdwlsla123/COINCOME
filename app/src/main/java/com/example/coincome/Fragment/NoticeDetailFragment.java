package com.example.coincome.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coincome.MainActivity;
import com.example.coincome.Paging.NoticeModel;
import com.example.coincome.R;
import com.example.coincome.RecyclerView.Notice;
import com.google.gson.Gson;

import java.util.ArrayList;


public class NoticeDetailFragment extends Fragment {


    private Context context;
    TextView noticeTitle,noticeText,noticeDatetime,noticeExchange;
    LinearLayout backLayout;
    private Activity a;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            a = (Activity)context;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("NoticeDetailFragment","파괴");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = a;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice_detail, container, false);
        noticeTitle = rootView.findViewById(R.id.notice_title);
        noticeText = rootView.findViewById(R.id.notice_text);
        noticeDatetime = rootView.findViewById(R.id.notice_datetime);
        noticeExchange = rootView.findViewById(R.id.notice_exchange);
        backLayout = rootView.findViewById(R.id.back_press);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        String title = getArguments().getString("title");
        String text = getArguments().getString("text");
        String exchange = getArguments().getString("exchange");
        String datetime = getArguments().getString("datetime");
//        int position = getArguments().getInt("position");
        noticeTitle.setText(title);
        noticeText.setText(Html.fromHtml(text));
        noticeDatetime.setText(datetime);
        noticeExchange.setText(exchange);
        Log.v("NoticeDetailFragment","생성");
        return rootView;
    }


}