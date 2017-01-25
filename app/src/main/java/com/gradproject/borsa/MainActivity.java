package com.gradproject.borsa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements  MarketOverView.FragmentInteractionListener,Companies.OnFragmentInteractionListener{
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav=(NavigationView)findViewById(R.id.navi);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Drawer();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id) {
                    case R.id.login:
                        Intent in = new Intent(MainActivity.this, LogIn.class);
                        startActivity(in);
                        break;
                    case R.id.companies:
                        Intent intent = new Intent(MainActivity.this, Companies.class);
                        startActivity(intent);
                        break;
                    case R.id.brokers:
                        Intent intn = new Intent(MainActivity.this, BrokerDetails.class);
                        startActivity(intn);
                        break;
                    case R.id.settings:
                        Intent n = new Intent(MainActivity.this, Settings.class);
                        startActivity(n);
                        break;
                    case R.id.search:
                        Intent i = new Intent(MainActivity.this, Search.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
        getFragmentManager().beginTransaction().add(R.id.frame, new MarketOverView()).commit();
    }
    public void Drawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void FragmentInteraction(String z) {

    }

    @Override
    public void onFragmentInteraction(String z) {

    }
}
