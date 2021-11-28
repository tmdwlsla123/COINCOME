package com.example.coincome.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coincome.Implements.CalculateTextWatcher;
import com.example.coincome.R;


public class CalcFragment extends Fragment {
    /**
     *  두번째 탭 계산기 창
     *
     */
    private Context context;
    EditText marketPrice,entryPrice,coinSize,addPrice,addSize;
    TextView averagePrice,pnl,valuationAmount,principal,quantity;
    private Activity a;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            a = (Activity)context;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = a;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calc, container, false);

        marketPrice = rootView.findViewById(R.id.market_price);
        entryPrice = rootView.findViewById(R.id.entry_price);
        coinSize = rootView.findViewById(R.id.coin_size);
        addPrice = rootView.findViewById(R.id.add_price);
        addSize = rootView.findViewById(R.id.add_size);

        averagePrice = rootView.findViewById(R.id.average_price);
        valuationAmount = rootView.findViewById(R.id.valuation_amount);
        pnl = rootView.findViewById(R.id.pnl);
        principal = rootView.findViewById(R.id.principal);
        quantity = rootView.findViewById(R.id.coin_quantity);

        CalculateTextWatcher watcher = new CalculateTextWatcher()
                .setMarketPrice(marketPrice)
                .setEntryPrice(entryPrice)
                .setCoinSize(coinSize)
                .setAddPrice(addPrice)
                .setAddSize(addSize)
                .setPNL(pnl)
                .setAveragePrice(averagePrice)
                .setValuationAmount(valuationAmount)
                .setQuantity(quantity)
                .setPricipal(principal);



        marketPrice.addTextChangedListener(watcher);
        entryPrice.addTextChangedListener(watcher);
        coinSize.addTextChangedListener(watcher);
        addPrice.addTextChangedListener(watcher);
        addSize.addTextChangedListener(watcher);



        return rootView;
    }


}