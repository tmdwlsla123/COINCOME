package com.example.coincome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class ChartActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();
        String exchange = intent.getStringExtra("exchange");
        String symbol = intent.getStringExtra("symbol");
        String theme = intent.getStringExtra("theme");

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.loadUrl("file:///android_asset/chart.html?exchange="+exchange+"&symbol="+symbol+"&theme="+theme);
    }
}