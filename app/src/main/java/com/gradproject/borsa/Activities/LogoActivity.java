package com.gradproject.borsa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.gradproject.borsa.R;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                //start new activity here
                Intent in = new Intent(LogoActivity.this, GuidesActivity.class);
                startActivity(in);
            }
        }, 7000);


    }
}
