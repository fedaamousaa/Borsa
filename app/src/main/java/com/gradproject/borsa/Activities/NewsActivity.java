package com.gradproject.borsa.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.gradproject.borsa.R;
import com.gradproject.borsa.login.LoginDialogFragment;

public class NewsActivity extends AppCompatActivity {
Toolbar actionBar;
    int Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        WebView webView = (WebView) findViewById(R.id.activity_news);
        webView.loadUrl("http://www.mubasher.info/countries/EG");

        actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_apps_black_24dp);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_drawer,menu);
        MenuItem item = menu.getItem(0);
        MenuItem news = menu.findItem(R.id.news);
        news.setVisible(false);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int user_id = pref.getInt("Id", -1);
        if (user_id == -1) {
            item.setTitle("Login");
        }else {
            item.setTitle("Profile");
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id) {
            case R.id.login:
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                int user_id = pref.getInt("Id", -1);
                if (user_id == -1) {
                    LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
                    loginDialogFragment.show(getSupportFragmentManager(),
                            loginDialogFragment.getClass().getSimpleName());
                }else {
                    Intent in = new Intent(this, ProfileActivity.class);
                    startActivity(in);
                }
                invalidateOptionsMenu();
                break;
            case R.id.news:
                Intent intent=new Intent(this,NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                Intent in=new Intent(this,SearchActivity.class);
                startActivity(in);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
