package com.gradproject.borsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class guides extends AppCompatActivity {
TextView skip;
    Button buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides);

        skip=(TextView)findViewById(R.id.skip);
        buttons=(Button)findViewById(R.id.buttons);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(guides.this, MainActivity.class);
                startActivity(in);
            }
        });


        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(guides.this, MainActivity.class);
                startActivity(in);
            }
        });
    }
}
