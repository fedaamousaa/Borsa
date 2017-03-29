package com.gradproject.borsa.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.gradproject.borsa.R;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        WebView webView = (WebView) findViewById(R.id.activity_news);
        webView.loadUrl("http://www.mubasher.info/countries/EG");
    }
}
