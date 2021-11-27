package com.example.coincome.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coincome.MainActivity;
import com.example.coincome.R;
import com.example.coincome.RecyclerView.Notice;
import com.example.coincome.RecyclerView.NoticeAdapter;
import com.example.coincome.Retrofit2.ApiInterface;
import com.example.coincome.Retrofit2.HttpClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;


public class NoticeFragment extends Fragment {
    /**
     *  세번째 탭 공지창
     *
     */
    private Context context;
    RecyclerView noticeRecyclerView;
    NoticeAdapter noticeAdapter;
    ArrayList<Notice> noticeList;
    ApiInterface api;
    String noticeUrl = "http://118.67.142.47/notice.py";
    String id;
    String TAG = "FirebaseMessagingService";
    int flag = 0;
    private Activity a;
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
        noticeAdapter = new NoticeAdapter(context);
        noticeRecyclerView.setAdapter(noticeAdapter);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        requestGet(noticeUrl);

        return rootView;
    }
    //    get 방식
    public void requestGet(String noticeUrl) {
        String url = noticeUrl; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        Call<String> call = api.requestGet(url);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                   //     서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2",response.body().toString());
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    Log.v("retrofit2", String.valueOf(jsonArray));
                    for(int i = 0; i < jsonArray.length(); i++){
                        Notice notice = new Notice();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String text = jsonObject.getString("text");
                        String exchange = jsonObject.getString("exchange");
                        String datetime = jsonObject.getString("datetime");
                        SimpleDateFormat dateParser = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                        try {
                            Date date = dateParser.parse(datetime);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
                            datetime = dateFormat.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        notice.setId(id);
                        notice.setTitle(title);
                        notice.setText(text);
                        notice.setExchange(exchange);
                        notice.setDatetime(datetime);
                        noticeList.add(notice);

                        Log.v("retrofit2", jsonObject.getString("title"));

                    }

                    if(id!=null){
                        noticeAdapter.submitList(noticeList,id);
                        Log.v(TAG,"NoticeFragment 어댑터에 아이디 전송");
                    }else{
                        noticeAdapter.submitList(noticeList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("NoticeFragment","파괴");
    }
}
