package com.example.coincome.Retrofit2;

import com.example.coincome.CoinSymbol.CoinAPIInterface;
import com.example.coincome.CoinSymbol.UpbitAPI;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class InterfaceAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() != CoinAPIInterface.class) {
            return null; // 이 팩토리가 처리하는 타입이 아닌 경우 null을 반환
        }

        TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        return (TypeAdapter<T>) new TypeAdapter<CoinAPIInterface>() {
            @Override
            public void write(JsonWriter out, CoinAPIInterface value) throws IOException {
                // 필요한 경우 여기에 직렬화 로직 구현
            }

            @Override
            public CoinAPIInterface read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                // 특정 키의 존재 여부에 따라 다른 구현체로 매핑
                if (jsonObject.has("korean_name")) {
                    return gson.fromJson(jsonObject, UpbitAPI.class);
                }
//                else if (jsonObject.has("keyB")) {
//                    return gson.fromJson(jsonObject, ImplementationB.class);
//                }
                throw new IOException("Unknown key in the JSON object");
            }
        }.nullSafe();
    }
}
