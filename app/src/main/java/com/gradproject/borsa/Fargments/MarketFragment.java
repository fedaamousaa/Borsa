package com.gradproject.borsa.Fargments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.gradproject.borsa.Activities.CompaniesDetailsActivity;
import com.gradproject.borsa.DataModel.Stock;
import com.gradproject.borsa.R;

import org.json.JSONArray;

import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MarketFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    RealmResults<Stock> egxArray;

    JSONArray array = new JSONArray();

    Realm realm;

    egxAdapter adapter;

    TextView companyNameText, last_price, day_change, day_percent, type;

    GridView EGX;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(new CustomAdarter(getSupportFragmentManager(), getContext()));
        EGX = (GridView) view.findViewById(R.id.EGX_list);

        egxArray = realm.where(Stock.class).equalTo("type", 2).findAll();
        adapter = new egxAdapter(getActivity(), R.layout.companies_list_item, egxArray);
        EGX.setAdapter(adapter);
        Log.e("EGX Array", egxArray.toString());

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Realm
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
    }

    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    private class CustomAdarter extends FragmentPagerAdapter {
        private String fragments[] = {"Stock Market", "Bond Market"};

        public CustomAdarter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MarketOverViewFragment();
                case 1:
                    return new BondMarketFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }

    public class egxAdapter extends ArrayAdapter {

        RealmResults<Stock> mCompanies;

        egxAdapter(Context context, int resource, RealmResults<Stock> companies) {
            super(context, resource, companies);
            mCompanies = companies;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.companies_list_item, parent, false);
                companyNameText = (TextView) convertView.findViewById(R.id.company_text_view);
                last_price = (TextView) convertView.findViewById(R.id.last_price);
                day_change = (TextView) convertView.findViewById(R.id.day_change);
                day_percent = (TextView) convertView.findViewById(R.id.day_percent);
                type = (TextView) convertView.findViewById(R.id.market_type);


                companyNameText.setText(mCompanies.get(position).getCompany().getName() + " (" + mCompanies.get(position).getCompany().getSymbol() + ")");
                double price_last = mCompanies.get(position).getCurrent_value();
                last_price.setText("" + roundTwoDecimals(price_last) + "");

                if (mCompanies.get(position).getCurrent_value() > mCompanies.get(position).getLast_value()) {

                    double change = (mCompanies.get(position).getCurrent_value() / mCompanies.get(position).getLast_value()) * 100;
                    day_percent.setText("+" + roundTwoDecimals(change) + "%");
                    day_change.setText("+" + roundTwoDecimals((mCompanies.get(position).getCurrent_value() - mCompanies.get(position).getLast_value())) + "");
                    day_percent.setTextColor(getResources().getColor(android.R.color.black));
                    convertView.setBackground(getResources().getDrawable(R.drawable.gradient_green_color));
                } else {
                    double change = (mCompanies.get(position).getCurrent_value() / mCompanies.get(position).getLast_value()) * 100;
                    day_percent.setText("-" + roundTwoDecimals(change) + "%");
                    day_change.setText("-" + roundTwoDecimals((mCompanies.get(position).getLast_value() - mCompanies.get(position).getCurrent_value())) + "");
                    day_percent.setTextColor(getResources().getColor(android.R.color.black));
                    convertView.setBackground(getResources().getDrawable(R.drawable.gradient_red_color));
                }

                if (mCompanies.get(position).getType() == 2) {
                    type.setText("egx");
                }


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // ToDo
                        Intent intent = new Intent(getContext(), CompaniesDetailsActivity.class);
                        int id = mCompanies.get(position).getId();
                        int type = mCompanies.get(position).getType();
                        intent.putExtra("item", id);
                        intent.putExtra("type", type);
                        getContext().startActivity(intent);
                    }
                });

            }


            return convertView;
        }

        double roundTwoDecimals(double d) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            return Double.valueOf(twoDForm.format(d));
        }

    }

}
