package com.gradproject.borsa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                //start new activity here
                Intent in = new Intent(Logo.this, guides.class);
                startActivity(in);
            }
        }, 7000);


    }
}
