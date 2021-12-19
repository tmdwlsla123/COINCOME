package com.example.coincome.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coincome.MainActivity;
import com.example.coincome.Paging.NoticeViewModel;
import com.example.coincome.R;
import com.example.coincome.RecyclerView.Notice;
import com.example.coincome.RecyclerView.NoticeAdapter;
import com.example.coincome.RecyclerView.NoticeLoadStateAdapter;
import com.example.coincome.Retrofit2.ApiInterface;

import java.util.ArrayList;


public class NoticeFragment extends Fragment {
    /**
     *  세번째 탭 공지창
     *
     */
    private Context context;
    RecyclerView noticeRecyclerView;
    NoticeAdapter noticeAdapter;
    NoticeLoadStateAdapter loadStateAdapter;
    ArrayList<Notice> noticeList;
    ApiInterface api;
    String noticeUrl = "http://118.67.142.47/notice.py";
    String id;
    String TAG = "FirebaseMessagingService";
    int flag = 0;
    private Activity a;
    NoticeViewModel viewModel;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            a = (Activity)context;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle extra = getArguments();
        if(extra != null)
        {
                if(extra.getString("id")!=null){
                    id = extra.getString("id");
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.bottomNavigationView.getMenu().findItem(R.id.c).setChecked(true);
                    extra.remove("id");
                }else{
                    id ="";
                }

            Log.v(TAG,"NoticeFragment"+id);
        }else{
            id = "";
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("NoticeFragment","생성");
        // Inflate the layout for this fragment
        context = a;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notice, container, false);
        noticeList = new ArrayList<>();
        noticeRecyclerView = rootView.findViewById(R.id.noti_list);
        noticeAdapter = new NoticeAdapter(context,id);
        loadStateAdapter = new NoticeLoadStateAdapter(v->noticeAdapter.retry());
        noticeRecyclerView.setAdapter(noticeAdapter.withLoadStateFooter(loadStateAdapter));
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        viewModel = new ViewModelProvider(requireActivity()).get(NoticeViewModel.class);
        viewModel.getNotices().subscribe(noticePagingData ->{
                Log.d("noticeViewModel" , noticePagingData.toString());
                noticeAdapter.submitData(getLifecycle(),noticePagingData);
                Log.d("data", String.valueOf(noticeAdapter.getItemCount()));
    });


        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("NoticeFragment","파괴");
    }
}
