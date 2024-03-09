package com.example.coincome.Retrofit2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpClient {

    private static Retrofit retrofit;


    // Http 통신을 위한 Retrofit 객체반환
    public static Retrofit getRetrofit() {
        if( retrofit == null )
        {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new InterfaceAdapterFactory())
                    .create();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
//            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
//                    .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl("https://api.upbit.com/");//http://ccit2020.cafe24.com:8082/http://10.0.2.2
//            요청의 순서가 매우 중요함
//            Retrofit 설정에서 GsonConverterFactory가 ScalarsConverterFactory보다 먼저 나열되거나, 반환 타입에 따라 Retrofit이 자동으로 JSON 응답을 처리하기 위해 GsonConverterFactory를 사용하려고 시도할 수 있습니다. Retrofit은 등록된 컨버터 팩토리 목록을 순회하며, 요청에 맞는 적절한 컨버터 팩토리를 찾습니다. 만약 JSON 응답을 받고 이를 String 타입으로 처리하고자 하는 경우에도, Retrofit 설정에 GsonConverterFactory가 포함되어 있으면, Retrofit이 JSON 응답을 자동으로 Gson 컨버터를 사용하여 파싱하려고 시도할 수 있습니다.
            builder.addConverterFactory(ScalarsConverterFactory.create());  // String 등 처리시
            builder.addConverterFactory( GsonConverterFactory.create(gson) );  // 받아오는 Json 구조의 데이터를 객체 형태로 변환
            builder.addConverterFactory( GsonConverterFactory.create() );  // 받아오는 Json 구조의 데이터를 객체 형태로 변환
            builder.addCallAdapterFactory(RxJava3CallAdapterFactory.create());
            builder.client(okHttpClient);


            retrofit = builder.build();
        }

        return retrofit;
    }
}

