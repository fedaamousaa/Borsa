package com.gradproject.borsa.Fargments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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


public class MarketOverViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private FragmentInteractionListener Listener;
    GridView company_list;
    JSONArray array = new JSONArray();
    ArrayList<Stock> stockArray;
    stockAdapter adapter;
    Realm realm;
    SwipeRefreshLayout swipeRefreshLayout;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_market_over_view, container, false);
        company_list = (GridView) view.findViewById(R.id.company_list);

        stockArray = new ArrayList<>(realm.where(Stock.class).equalTo("type",0).findAll());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new stockAdapter(getActivity(), R.layout.companies_list_item, stockArray);
        company_list.setAdapter(adapter);

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

    @Override
    public void onRefresh() {
        new ExcuteNetworkOperation().execute();
    }


    public class stockAdapter extends ArrayAdapter<Stock> implements View.OnClickListener{

        ArrayList<Stock> mCompanies;
        TextView companyNameText, last_price, day_change, day_percent,type;

        stockAdapter(Context context, int resource, ArrayList<Stock> companies) {
            super(context, resource, companies);
            mCompanies = companies;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.companies_list_item, parent, false);
            }
            companyNameText = (TextView) convertView.findViewById(R.id.company_text_view);
            last_price = (TextView) convertView.findViewById(R.id.last_price);
            day_change = (TextView) convertView.findViewById(R.id.day_change);
            day_percent = (TextView) convertView.findViewById(R.id.day_percent);
            type=(TextView)convertView.findViewById(R.id.market_type);
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
            convertView.setTag(position);
            if (mCompanies.get(position).getType()==0)
            {
                type.setText("stock");
            }
            convertView.setOnClickListener(this);
            return convertView;
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), CompaniesDetailsActivity.class);
            int id = mCompanies.get((Integer) v.getTag()).getId();
            intent.putExtra("item", id);
            getContext().startActivity(intent);
        }
    }

    class ExcuteNetworkOperation extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
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

            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
//                stockArray.clear();
//                adapter.notifyDataSetChanged();
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
//                        stockArray.add(stock);
                    }
//                    Log.e("Stock Array", stockArray.toString());
//                    swipeRefreshLayout.setRefreshing(false);
//                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(getActivity(), "No Internet Connection" , Toast.LENGTH_SHORT).show();
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

