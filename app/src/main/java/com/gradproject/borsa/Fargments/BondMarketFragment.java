package com.gradproject.borsa.Fargments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class BondMarketFragment extends Fragment {

    GridView Bond;
    JSONArray array = new JSONArray();
    TextView companyNameText, last_price, day_change, day_percent, type;
    RealmResults<Stock> bondArray;
    bondAdapter adapter;
    Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bond_market_fragment, container, false);
        Bond = (GridView) view.findViewById(R.id.bond);
        bondArray = realm.where(Stock.class).equalTo("type", 1).findAll();
        adapter = new bondAdapter(getActivity(), R.layout.companies_list_item, bondArray);
        Bond.setAdapter(adapter);
        Log.e("Bond Array", bondArray.toString());

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Realm
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
    }

    public class bondAdapter extends ArrayAdapter {

        RealmResults<Stock> mCompanies;

        bondAdapter(Context context, int resource, RealmResults<Stock> companies) {
            super(context, resource, companies);
            mCompanies = companies;
        }

        @Override
        public int getCount() {
            return mCompanies.size();
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.companies_list_item, parent, false);
            }
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
//                    day_percent.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
//                    company_list.setBackground(getResources().getDrawable(R.drawable.gradient_green_color));
                day_percent.setTextColor(getResources().getColor(android.R.color.black));
                convertView.setBackground(getResources().getDrawable(R.drawable.gradient_green_color));
            } else {
                double change = (mCompanies.get(position).getCurrent_value() / mCompanies.get(position).getLast_value()) * 100;
                day_percent.setText("-" + roundTwoDecimals(change) + "%");
                day_change.setText("-" + roundTwoDecimals((mCompanies.get(position).getLast_value() - mCompanies.get(position).getCurrent_value())) + "");
//                    day_percent.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                day_percent.setTextColor(getResources().getColor(android.R.color.black));
                convertView.setBackground(getResources().getDrawable(R.drawable.gradient_red_color));
            }

            if (mCompanies.get(position).getType() == 1) {
                type.setText("bond");
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ToDo
                    Intent intent = new Intent(getContext(), CompaniesDetailsActivity.class);
                    int id = mCompanies.get(position).getId();
                    intent.putExtra("item", id);
                    getContext().startActivity(intent);
                }
            });

            return convertView;
        }


    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
}
