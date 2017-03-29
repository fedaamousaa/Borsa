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

import com.gradproject.borsa.Fargments.MarketOverViewFragment;
import com.gradproject.borsa.R;
import com.gradproject.borsa.login.LoginDialogFragment;

public class MainActivity extends AppCompatActivity implements MarketOverViewFragment.FragmentInteractionListener {
    Toolbar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
//        actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_apps_black_24dp);
        }
        getFragmentManager().beginTransaction().replace(R.id.frame, new MarketOverViewFragment()).commit();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_drawer,menu);
        MenuItem item = menu.getItem(0);
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

//            case R.id.settings:
//                Intent n = new Intent(MainActivity.this, SettingsActivity.class);
//                startActivity(n);
//                break;
            case R.id.news:
                Intent intent=new Intent(MainActivity.this,NewsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void FragmentInteraction(String z) {

    }
}
