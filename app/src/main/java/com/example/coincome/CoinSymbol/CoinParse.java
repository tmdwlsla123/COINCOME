package com.example.coincome.CoinSymbol;

import java.util.ArrayList;
import java.util.List;

public class CoinParse {
    private final List<CoinAPIInterface> list;
    private final String exchangeName;
    private StringBuilder subscribeSymbols;
    public CoinParse(List<CoinAPIInterface> list, String exchangeName) {
        this.list = list;
        this.exchangeName = exchangeName;
        CoinParsing();
    }
    private void CoinParsing(){
        switch (exchangeName){
            case "업비트":
                subscribeSymbols = new StringBuilder();
                ArrayList arrayList = new ArrayList();
                for (CoinAPIInterface item: list) {
                    if(item instanceof UpbitAPI){
                        if(null != item.getCoinSymbol()){
                            UpbitAPI upbitAPI = (UpbitAPI) item;
                            arrayList.add(upbitAPI.getMarket());
                        }
                    }
                }
                subscribeSymbols.append(arrayList);
                break;
        }

    }
    public StringBuilder getSubscribeSymbols() {
        return subscribeSymbols;
    }

}
