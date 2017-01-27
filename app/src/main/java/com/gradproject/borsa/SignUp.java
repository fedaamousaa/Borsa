package com.gradproject.borsa;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    ViewPager mViewPager;
    TabLayout mTabLayout;
    SamplePagerAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
        mTabLayout = (TabLayout) findViewById(R.id.tabbar_stock_symbol);

        // handle selection
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // read Company name from SQLite database & set actionbar Title
                getSupportActionBar().setTitle(tab.getText().toString());
                tab.select();
                mViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPageAdapter = new SamplePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        TabLayout.Tab initialTab = mTabLayout.getTabAt(0);
        if (initialTab != null) {
            getSupportActionBar().setTitle(initialTab.getText().toString());
            initialTab.select();
        }

    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        String[] titles = {CustomerSignUp.class.getSimpleName(),
                CompanySignUp.class.getSimpleName(),
                BrokerSignUP.class.getSimpleName()};

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new CustomerSignUp();
            } else if (position == 1) {
                return new CompanySignUp();
            }else {
                return new BrokerSignUP();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
