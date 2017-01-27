package com.gradproject.borsa.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gradproject.borsa.CustomViewPager;
import com.gradproject.borsa.R;

public class LoginDialogFragment extends DialogFragment {

    public LoginDialogFragment() {
        // Required empty public constructor
    }

    ViewPager mViewPager;
    TabLayout mTabLayout;
    SamplePagerAdapter mPageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_dialog, container, false);
        mViewPager = (CustomViewPager) v.findViewById(R.id.login_pager);
        mPageAdapter = new SamplePagerAdapter(getChildFragmentManager());
        mTabLayout = (TabLayout) v.findViewById(R.id.tabbar_login_symbol);
        // handle selection
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getDialog().setTitle(tab.getText());
                // read Company name from SQLite database & set actionbar Title
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

        mViewPager.setAdapter(mPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        TabLayout.Tab initialTab = mTabLayout.getTabAt(0);
        if (initialTab != null) {
            initialTab.select();
        }
        // Inflate the layout for this fragment
        return v;
    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        String[] titles = {"User", "Company", "Broker"};

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new CustomerLoginFragment();
            } else if (position == 1) {
                return new CompanyLoginFragment();
            }else {
                return new BrokerLoginFragment();
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
