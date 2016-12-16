package com.gradproject.borsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
Button button ,c,z,logo,guide,settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.go);
        c=(Button)findViewById(R.id.goo);
        z=(Button)findViewById(R.id.gooo);

        logo=(Button)findViewById(R.id.logoPage);
        guide=(Button)findViewById(R.id.logoGuide);
        settings=(Button)findViewById(R.id.logoSetting);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,Menu.class);
                startActivity(in);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,MarketOverView.class);
                startActivity(in);
            }
        });
        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,LogIn.class);
                startActivity(in);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,Logo.class);
               startActivity(in);
         }
        });
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,guides.class);
                startActivity(in);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent in=new Intent(MainActivity.this,Settings.class);
                startActivity(in);
            }
        });

    }
}
