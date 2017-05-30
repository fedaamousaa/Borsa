package com.gradproject.borsa.UIHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gradproject.borsa.Activities.SearchActivity;

/**
 * Created by تكنولوجى on 22/05/2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        finish();
    }
}
