package com.gradproject.borsa.Fargments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gradproject.borsa.Activities.CompaniesDetailsActivity;
import com.gradproject.borsa.DataModel.Company;
import com.gradproject.borsa.DataModel.Stock;
import com.gradproject.borsa.R;
import com.gradproject.borsa.UIHelper.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import io.realm.Realm;


public class MarketOverViewFragment extends Fragment {
    private FragmentInteractionListener Listener;
    GridView company_list;
    JSONArray array = new JSONArray();
    TextView companyNameText, last_price, day_change, day_percent;
    ArrayList<Stock> stockArray;
    stockAdapter c;
    Stock stock;
    Realm realm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_market_over_view, container, false);
        company_list = (GridView) view.findViewById(R.id.company_list);
        stockArray = new ArrayList<>(realm.where(Stock.class).findAll());
        c = new stockAdapter(getActivity(), R.layout.companies_list_item, stockArray);
        company_list.setAdapter(c);
        new ExcuteNetworkOperation().execute();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Realm
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
    }


    public class stockAdapter extends ArrayAdapter {

        ArrayList<Stock> mCompanies;

        public stockAdapter(Context context, int resource, ArrayList<Stock> companies) {
            super(context, resource, companies);
            mCompanies = companies;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.companies_list_item, parent, false);
                companyNameText = (TextView) convertView.findViewById(R.id.company_text_view);
                last_price = (TextView) convertView.findViewById(R.id.last_price);
                day_change = (TextView) convertView.findViewById(R.id.day_change);
                day_percent = (TextView) convertView.findViewById(R.id.day_percent);

                int stockCount = realm.where(Stock.class).findAll().size();
                int templatesCount = realm.where(Company.class).findAll().size();

                Log.d("stock company count ", stockCount + " " + templatesCount);


                companyNameText.setText(mCompanies.get(position).getCompany().getName() + " (" + mCompanies.get(position).getCompany().getSymbol() + ")");
                double price_last = mCompanies.get(position).getCurrent_value();
                last_price.setText("" + roundTwoDecimals(price_last) + "");

                if (mCompanies.get(position).getCurrent_value() > mCompanies.get(position).getLast_value()) {

                    double change = (mCompanies.get(position).getCurrent_value() / mCompanies.get(position).getLast_value()) * 100;
                    day_percent.setText("+" + roundTwoDecimals(change) + "%");
                    day_change.setText("+" + roundTwoDecimals((mCompanies.get(position).getCurrent_value() - mCompanies.get(position).getLast_value())) + "");
                    day_percent.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

                } else {
                    double change = (mCompanies.get(position).getCurrent_value() / mCompanies.get(position).getLast_value()) * 100;
                    day_percent.setText("-" + roundTwoDecimals(change) + "%");
                    day_change.setText("-" + roundTwoDecimals((mCompanies.get(position).getLast_value() - mCompanies.get(position).getCurrent_value())) + "");
                    day_percent.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // ToDo
                        Intent intent = new Intent(getContext(), CompaniesDetailsActivity.class);
                        intent.putExtra("item", mCompanies.get(position).getId());
                        getContext().startActivity(intent);
                    }
                });

            }


            return convertView;
        }


    }

    public class ExcuteNetworkOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            Boolean isConnected = cm.getActiveNetworkInfo().isConnected();
            if (isConnected) {
                stockArray.clear();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {


            try {
                array = Utils.getAllStocks();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return array.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                for (int i = 0; i < array.length(); i++) {

                    Gson gson = new Gson();

                    JSONObject object = array.getJSONObject(i);
//                    int id = object.getInt("id");
//                    int type = object.getInt("type");
//                    double current_value = object.getDouble("current_value");
//                    double last_value = object.getDouble("last_value");

                    JSONObject companyObject = object.getJSONObject("company");
//                    int com_id = companyObject.getInt("id");
//                    String name = companyObject.getString("name");
//                    String phone = companyObject.getString("phone");
//                    String address = companyObject.getString("address");
//                    String symbol = companyObject.getString("symbol");

                    Company company = gson.fromJson(companyObject.toString(), Company.class);
//                    company.setId(com_id);
//                    company.setName(name);
//                    company.setPhone(phone);
//                    company.setSymbol(symbol);

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(company);
                    realm.commitTransaction();


                    Stock stock = gson.fromJson(object.toString(), Stock.class);
//                    stock.setId(id);
//                    stock.setType(type);
//                    stock.setCurrent_value(current_value);
//                    stock.setLast_value(last_value);
//                    stock.setCompany(company);

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(stock);
                    realm.commitTransaction();
                    stockArray.add(stock);
                }

                c.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(s);

        }
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String z) {
        if (Listener != null) {
            Listener.FragmentInteraction(z);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            Listener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            Listener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Listener = null;
    }

    public interface FragmentInteractionListener {
        // TODO: Update argument type and name
        void FragmentInteraction(String z);
    }
}

/*

 */

