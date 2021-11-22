package com.example.coincome.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coincome.MainActivity;
import com.example.coincome.R;
import com.example.coincome.RecyclerView.Notice;

import java.util.ArrayList;


public class NoticeDetailFragment extends Fragment {


    private Context context;
    TextView noticeTitle;
    LinearLayout backLayout;
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("NoticeDetailFragment","파괴");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice_detail, container, false);
        noticeTitle = rootView.findViewById(R.id.notice_title);
        backLayout = rootView.findViewById(R.id.back_press);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        ArrayList<Notice> noticeList = (ArrayList<Notice>)getArguments().getSerializable("data");
        int position = getArguments().getInt("position");
        noticeTitle.setText(noticeList.get(position).getText());
        Log.v("NoticeDetailFragment","생성");
        return rootView;
    }


}