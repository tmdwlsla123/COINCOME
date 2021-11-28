package com.example.coincome.Implements;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CalculateTextWatcher implements TextWatcher {
    private EditText marketPrice,entryPrice,coinSize,addPrice,addSize;
    private TextView pnl,averagePrice,valuationAmount,principal,quantity;
    public CalculateTextWatcher() {
    }

    public CalculateTextWatcher setEntryPrice(EditText entryPrice) {
        this.entryPrice = entryPrice;
        return this;
    }

    public CalculateTextWatcher setMarketPrice(EditText marketPrice){
        this.marketPrice = marketPrice;
        return this;
    }
    public CalculateTextWatcher setCoinSize(EditText coinSize){
        this.coinSize = coinSize;
        return this;
    }
    public CalculateTextWatcher setAddPrice(EditText addPrice){
        this.addPrice = addPrice;
        return this;
    }
    public CalculateTextWatcher setAddSize(EditText addSize){
        this.addSize = addSize;
        return this;
    }
    public CalculateTextWatcher setPNL(TextView pnl){
        this.pnl = pnl;
        return this;
    }
    public CalculateTextWatcher setAveragePrice(TextView averagePrice){
        this.averagePrice = averagePrice;
        return this;
    }
    public CalculateTextWatcher setValuationAmount(TextView valuationAmount){
        this.valuationAmount = valuationAmount;
        return this;
    }
    public CalculateTextWatcher setPricipal(TextView principal){
        this.principal = principal;
        return this;
    }    public CalculateTextWatcher setQuantity(TextView quantity){
        this.quantity = quantity;
        return this;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        calculateResult();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    private void calculateResult(){
        Double marketPrice,entryPrice,coinSize,addPrice,addSize,commission;
        if(!this.marketPrice.getText().toString().equals("") && this.marketPrice.getText().toString() != null)
            try {
                marketPrice = Double.parseDouble(this.marketPrice.getText().toString());
            }catch (NumberFormatException e){
                marketPrice = 0.0;
            }
        else
            marketPrice = 0.0;

        if(!this.entryPrice.getText().toString().equals("") && this.entryPrice.getText().toString() != null)
            try {
                entryPrice = Double.parseDouble(this.entryPrice.getText().toString());
            }catch (NumberFormatException e){
                entryPrice = 0.0;
            }
        else
            entryPrice = 0.0;

        if(!this.coinSize.getText().toString().equals("") && this.coinSize.getText().toString() != null)
            try {
                coinSize = Double.parseDouble(this.coinSize.getText().toString());
            }catch (NumberFormatException e){
                coinSize = 0.0;
            }
        else
            coinSize = 0.0;

        if(!this.addPrice.getText().toString().equals("") && this.addPrice.getText().toString() != null)
            try {
                addPrice = Double.parseDouble(this.addPrice.getText().toString());
            }catch (NumberFormatException e){
                addPrice = 0.0;
            }
        else
            addPrice = 0.0;

        if(!this.addSize.getText().toString().equals("") && this.addSize.getText().toString() != null)
            try {
                addSize = Double.parseDouble(this.addSize.getText().toString());
            }catch (NumberFormatException e){
                addSize = 0.0;
            }
        else
            addSize = 0.0;






//[(보유 평균단가 x 보유수량) + (신규 매수단가 x 신규 매수수량) / (보유수량+신규매수수량)]
        Double price = (entryPrice*coinSize + (addPrice*addSize))/(coinSize+addSize);
        Double pnl = ((marketPrice - price)/ price) *100;
        Double valuationAmount = marketPrice*(coinSize+addSize);
        Double quantity = coinSize+addSize;
        //원금
        Double principal = entryPrice*coinSize + addPrice*addSize;
        String sign;
        if(pnl>0){
            sign = "+";
        }else{
            sign = "";
        }


        averagePrice.setText(MakePriceFormat(price)+"원");
        this.quantity.setText(MakePriceFormat(quantity));
        this.principal.setText(MakePriceFormat(principal)+"원");
        this.valuationAmount.setText(MakePriceFormat(valuationAmount)+"원");
        this.pnl.setText(sign+MakePNLFormat(pnl)+"%");
    }
    private String MakePriceFormat(double price){
        String tmp;
        if(Double.isNaN(price) || Double.isInfinite(price)) price = 0.0;
        BigDecimal bigDecimal = new BigDecimal(price);
        DecimalFormat df = new DecimalFormat("###,###");

        if(0==price){
            tmp = String.format("%.0f",bigDecimal);
        }
        else if(price>100){
            tmp = df.format(bigDecimal);
        }else if(1>price){
            tmp = String.format("%.4f",bigDecimal);
        }else if(10>price){
            tmp = String.format("%.2f",bigDecimal);
        }else{
            tmp = String.format("%.1f",bigDecimal);
        }
        return tmp;
    }
    private String MakePNLFormat(double price){
        String tmp;
        if(Double.isNaN(price) || Double.isInfinite(price)) price = 0.0;
        BigDecimal bigDecimal = new BigDecimal(price);
        DecimalFormat df = new DecimalFormat("###,###");
        tmp = String.format("%.2f",bigDecimal);
        return tmp;
    }
}
