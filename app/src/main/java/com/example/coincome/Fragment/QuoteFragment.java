package com.example.coincome.Fragment;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.coincome.Exchange.Exchange;
import com.example.coincome.Implements.ScrollListener;

import com.example.coincome.Implements.setOnClickListener;
import com.example.coincome.R;
import com.example.coincome.RecyclerView.Coin;
import com.example.coincome.RecyclerView.QuoteAdapter;
import com.example.coincome.Retrofit2.ApiInterface;
import com.example.coincome.Retrofit2.HttpClient;

import com.example.coincome.ViewModel.CoinRepository;
import com.example.coincome.ViewModel.CoinViewModel;
import com.example.coincome.WebSocket.WebSocketListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;


public class QuoteFragment extends Fragment {
    /**
     *  첫번째 탭 시세창
     *
     */
    ArrayList<String> list;
    private Context context;
    private OkHttpClient client;
    RecyclerView listView;
    androidx.appcompat.widget.SearchView serach;

    Exchange exchange;
    Request request;
    WebSocketListener domesticListener,overseasListener;
    ApiInterface api;

    QuoteAdapter quoteAdapter;
    String upbitRESTApiUri = "https://api.upbit.com/v1/market/all";
    String upbitSocketUri = "wss://api.upbit.com/websocket/v1";

    String bithumbRESTApiUri = "https://api.bithumb.com/public/ticker/all/krw";
    String bithumbSocketUri = "wss://pubwss.bithumb.com/pub/ws";

    String coinoneRESTApiUri = "https://api.coinone.co.kr/ticker?currency=ALL";
    String coinoneSocketUri = "";

    String korbitRESTApiUri = "https://api.korbit.co.kr/v1/ticker/detailed/all";
    String korbitSocketUri = "wss://ws.korbit.co.kr/v1/user/push";

    String binanceSocketUri = "wss://stream.binance.com:9443/ws";
    String exchangeUSDKRW = "https://exchange.jaeheon.kr:23490/query/USDKRW";
    CoinViewModel viewModel;

    TextView usdkrw;
    Spinner domesticExchange;
    Spinner overseasExchange;
    LinearLayout nameSort,priceSort,daytodaySort,premiumSort;
    ImageView nameSortImage,priceSortImage,daytodaySortImage,premiumSortImage;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_quote, container, false);
        usdkrw = rootView.findViewById(R.id.usdkrw);
        serach = rootView.findViewById(R.id.serach);
        nameSort = rootView.findViewById(R.id.name_sort);
        priceSort = rootView.findViewById(R.id.price_sort);
        daytodaySort = rootView.findViewById(R.id.daytoday_sort);
        premiumSort = rootView.findViewById(R.id.premium_sort);
        domesticExchange = rootView.findViewById(R.id.domestic_exchange);
        overseasExchange = rootView.findViewById(R.id.overseas_exchange);
        ArrayAdapter exchangeAdapter = ArrayAdapter.createFromResource(context, R.array.domestic_exchange, android.R.layout.simple_spinner_dropdown_item);
        domesticExchange.setAdapter(exchangeAdapter);
        nameSortImage = rootView.findViewById(R.id.name_sort_image);
        nameSortImage.setImageLevel(10000);
        priceSortImage = rootView.findViewById(R.id.price_sort_image);
        priceSortImage.setImageLevel(10000);
        daytodaySortImage =rootView.findViewById(R.id.daytoday_sort_image);
        daytodaySortImage.setImageLevel(10000);
        premiumSortImage=rootView.findViewById(R.id.premium_sort_image);
        premiumSortImage.setImageLevel(10000);


        requestGetUSDKRW();
        domesticExchange.setSelection(0);
        domesticExchange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String flag = "";
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(!flag.equals(adapterView.getItemAtPosition(i))){
                        flag = adapterView.getItemAtPosition(i).toString();
                        CoinRepository.getInstance().getAllList().clear();
                        exchange.ExchangeClear();
                        Log.v("spinner",adapterView.getItemAtPosition(i).toString());

//                if(listener!=null &&  listener.isConnected == true) listener.webSocket.cancel();
                        if(domesticListener!=null)domesticListener.webSocket.close(1000,null);
                        if(overseasListener!=null)overseasListener.webSocket.close(1000,null);


                        if(adapterView.getItemAtPosition(i).equals("업비트"))  {
                            requestGet(upbitSocketUri,upbitRESTApiUri,adapterView.getItemAtPosition(i).toString());
                        }else if(adapterView.getItemAtPosition(i).equals("빗썸")){
                            requestGet(bithumbSocketUri,bithumbRESTApiUri,adapterView.getItemAtPosition(i).toString());
                        }else if(adapterView.getItemAtPosition(i).equals("코빗")){
                            requestGet(korbitSocketUri,korbitRESTApiUri,adapterView.getItemAtPosition(i).toString());
                        }else{
//                    requestGet(coinoneSocketUri);
                        }
                    }

                }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        exchange = new Exchange();





        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        //



        quoteAdapter = new QuoteAdapter(context);
        listView = rootView.findViewById(R.id.noti_list);
//        quoteAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);



        //뷰모델 객체 생성
        viewModel = new ViewModelProvider(this).get(CoinViewModel.class);
//        viewModel.getSearchliveData(viewModel.getListliveData());
        viewModel.getSearchliveData(viewModel.getListliveData()).observe(getViewLifecycleOwner(), data ->{


            quoteAdapter.updateQouteAdapter(data);
//            quoteAdapter.getStateRestorationPolicy();
//            Log.v("update", String.valueOf(data.size()));
//            Log.v("update", );
        });
        viewModel.searchQuery.postValue("");
        listView.setItemAnimator(null);
        listView.setAdapter(quoteAdapter);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setHasFixedSize(true);
        //setonscroll
        ScrollListener scrollListener = new ScrollListener(listView){};
        listView.addOnScrollListener(scrollListener);




        //뷰모델에 옵저버 등록


        serach.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                    viewModel.searchQuery.postValue(s);
                    quoteAdapter.notifyDataSetChanged();
//                    Log.v("onQueryTextChange",s);



                return false;
            }
        });
        setOnClickListener listener = new setOnClickListener(scrollListener);
        //setonclick
        listener.AddListener(nameSort,priceSort,daytodaySort,premiumSort);
        listener.AddClip(nameSortImage,priceSortImage,daytodaySortImage,premiumSortImage);


        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        client.dispatcher().executorService().shutdown();
//        client.connectionPool().evictAll();
        if(domesticListener!=null)domesticListener.webSocket.close(1000,null);
        if(overseasListener!=null)overseasListener.webSocket.close(1000,null);

//        if(listener !=null &&  listener.isConnected == true)listener.webSocket.cancel();


        CoinRepository.getInstance().getAllList().clear();
        exchange.ExchangeClear();
//        exchange = null;
//        listView = null;
//        quoteAdapter = null;
        Log.v("호출","onDestroy");
    }

        public void requestGet(String webSocketUrl,String restApiUri,String exchangeName) {
//        String url = "all"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        Call<String> call = api.requestGet(restApiUri);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //     서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능

                String result = response.body();
                String firstChar = String.valueOf(result.charAt(0));

                if (firstChar.equalsIgnoreCase("[")) {
                    //json array
                    try {
                        JSONArray jsonArray = new JSONArray(response.body());
                        Log.v("retrofit2", "REST API 국내 :"+jsonArray.toString());

                    exchange.AddUpbitList(jsonArray,exchange.upbitMarket);
//                    //국내거래소
//                    listener = new WebSocketListener(exchange,context,exchangeName);
                    domesticListener = WebSocketListener.getInstance(exchange,context, WebSocketListener.WebsocketType.domestic);
                    domesticListener.addExchangeName(exchangeName,exchange);
                    request = new Request.Builder()
                            .url(webSocketUrl)
                            .build();
                    client.newWebSocket(request, domesticListener);

                    exchange.AddBinanceList(jsonArray,exchange.binanceMarket,exchangeName);
                    //해외거래소 웹소켓
                    request = new Request.Builder()
                            .url(binanceSocketUri)
                            .build();
//                    listener1 = new WebSocketListener(exchange,context,"바이낸스");
                    overseasListener = WebSocketListener.getInstance(exchange,context, WebSocketListener.WebsocketType.overseas);
                    overseasListener.addExchangeName("바이낸스",exchange);
                    client.newWebSocket(request, overseasListener);




                        Log.v("retrofit2", "REST API :"+jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    //json object
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if(exchangeName.equals("빗썸")){
                            //국내거래소 빗썸
                            exchange.AddBithumbList(jsonObject,exchange.bithumbMarket);
//                            listener = new WebSocketListener(exchange,context,exchangeName);
                            domesticListener = WebSocketListener.getInstance(exchange,context, WebSocketListener.WebsocketType.domestic);
                            domesticListener.addExchangeName(exchangeName,exchange);
                            request = new Request.Builder()
                                    .url(webSocketUrl)
                                    .build();
                            client.newWebSocket(request, domesticListener);
                        }else if(exchangeName.equals("코빗")){
                            //국내거래소 코빗
                            exchange.AddKorbitList(jsonObject,exchange.korbitMarket);
//                            listener = new WebSocketListener(exchange,context,exchangeName);
                            domesticListener = WebSocketListener.getInstance(exchange,context, WebSocketListener.WebsocketType.domestic);
                            domesticListener.addExchangeName(exchangeName,exchange);
                            request = new Request.Builder()
                                    .url(webSocketUrl)
                                    .build();
                            client.newWebSocket(request, domesticListener);
                        }


                        exchange.AddBinanceList(jsonObject,exchange.binanceMarket,exchangeName);
                        //해외거래소 웹소켓
                        request = new Request.Builder()
                                .url(binanceSocketUri)
                                .build();
//                        listener1 = new WebSocketListener(exchange,context,"바이낸스");
                        overseasListener = WebSocketListener.getInstance(exchange,context, WebSocketListener.WebsocketType.overseas);
                        overseasListener.addExchangeName("바이낸스",exchange);
                        client.newWebSocket(request, overseasListener);
                        Log.v("retrofit2", "REST API :"+jsonObject.getString("data").length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }
    public void requestGetUSDKRW() {
        String url = "all"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        Call<String> call = api.requestGet(exchangeUSDKRW);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //     서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2",String.valueOf("error : "+response.body()));
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("USDKRW");
                    usdkrw.setText(jsonArray.getString(0)+"원");
                    CoinRepository.getInstance().setUsdkrw(jsonArray.getDouble(0));
                    Log.v("retrofit2", String.valueOf(jsonArray.get(0)));

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

}
