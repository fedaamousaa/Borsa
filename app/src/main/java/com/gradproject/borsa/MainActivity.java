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

import com.gradproject.borsa.login.LoginDialogFragment;

public class MainActivity extends AppCompatActivity implements  MarketOverView.FragmentInteractionListener,Companies.OnFragmentInteractionListener{
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView nav;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav=(NavigationView)findViewById(R.id.navi);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_apps_black_24dp);
        }

        Drawer();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id) {
                    case R.id.login:
//                        Intent in = new Intent(MainActivity.this, LogIn.class);
//                        startActivity(in);
                        LoginDialogFragment loginDialogFragment = new LoginDialogFragment();
                        loginDialogFragment.show(getSupportFragmentManager(),
                                loginDialogFragment.getClass().getSimpleName());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.companies:
                        getFragmentManager().beginTransaction().add(R.id.frame, new Companies()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.settings:
                        Intent n = new Intent(MainActivity.this, Settings.class);
                        startActivity(n);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.search:
                        Intent i = new Intent(MainActivity.this, Search.class);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return false;
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.frame, new MarketOverView()).commit();
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
