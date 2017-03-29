package com.gradproject.borsa.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gradproject.borsa.DataModel.Customer;
import com.gradproject.borsa.DataModel.Stock;
import com.gradproject.borsa.DataModel.UserStocks;
import com.gradproject.borsa.Fargments.SellDialogFragment;
import com.gradproject.borsa.R;
import com.gradproject.borsa.UIHelper.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity {
    int user_id;
    TextView name, mail, phone, Stock_name, value, type ,curr_value;
    ActionBar actionBar;
    ListView cusromer_stocks;
    ArrayList<UserStocks> stockArray;
    Button sell;
    arrayAdapter adapter;
    Stock stock = new Stock();
    Realm realm;
    Customer customer = new Customer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        name = (TextView) findViewById(R.id.name);
        mail = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        cusromer_stocks = (ListView) findViewById(R.id.user_stock);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = pref.getInt("Id", -1);
        if (user_id != -1) {
            new ExcuteNetwork().execute();
        } else

            Realm.init(this);
        realm = Realm.getDefaultInstance();

        customer = realm.where(Customer.class).findFirst();
        stockArray = new ArrayList<>();
        adapter = new arrayAdapter(getBaseContext(), R.layout.user_stocks, stockArray);
        cusromer_stocks.setAdapter(adapter);
        new ExcuteNetworkOperation().execute();
    }

    public class arrayAdapter extends ArrayAdapter {
        ArrayList<UserStocks> mStock;

        public arrayAdapter(Context context, int resource, ArrayList<UserStocks> stocks) {
            super(context, resource, stocks);
            mStock = stocks;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.user_stocks, parent, false);
                Stock_name = (TextView) convertView.findViewById(R.id.Stock_name);
                value = (TextView) convertView.findViewById(R.id.value);
                type = (TextView) convertView.findViewById(R.id.type);
                sell = (Button) convertView.findViewById(R.id.sell);
                curr_value=(TextView)convertView.findViewById(R.id.stock_value);

                Stock_name.setText(mStock.get(position).getStock().getCompany().getName() + " (" + mStock.get(position).getStock().getCompany().getSymbol() + ")");
                Double last_value=mStock.get(position).getStock().getCurrent_value();
                curr_value.setText( "last value "+""+roundTwoDecimals(last_value)+"");
                if (mStock.get(position).getStock().getCurrent_value() > mStock.get(position).getStock().getLast_value()) {
                    value.setText("+" + roundTwoDecimals((mStock.get(position).getStock().getCurrent_value() - mStock.get(position).getStock().getLast_value()))+"");
                    value.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

                } else {
                    value.setText("-" + roundTwoDecimals((mStock.get(position).getStock().getLast_value() - mStock.get(position).getStock().getCurrent_value()))+"");
                    value.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));

                }
                switch (mStock.get(position).getStock().getType()){
                    case 0:
                        type.setText("Stock");
                        break;
                    case 1:
                        type.setText("Bound");
                        break;
                }


                sell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SellDialogFragment sellDialogFragment = new SellDialogFragment();
                        Stock stock = mStock.get(position).getStock();
                        sellDialogFragment.setCurr_value(stock.getCurrent_value());
                        sellDialogFragment.setInit_no(null);
                        sellDialogFragment.setCurr_no(String.valueOf(mStock.get(position).getQuantity()));
                        sellDialogFragment.setStock(stock);
                        sellDialogFragment.show(getFragmentManager(), null);
                    }
                });

            }
            return convertView;
        }
    }

    public class ExcuteNetwork extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {

            JSONObject user_info = new JSONObject();
            JSONObject object = new JSONObject();
            try {
                object.put("user_id", user_id);
                user_info = Utils.getCustomer(object);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return user_info;
        }

        @Override
        protected void onPostExecute(JSONObject res) {
            try {
                Gson gson = new Gson();
                JSONObject user_info = res.getJSONObject("response");
//                String fir_name = user_info.getString("first_name");
//                String last_name=user_info.getString("last_name");
//                String e_mail = user_info.getString("email");
//                String user_phone = user_info.getString("phone");
                customer = gson.fromJson(user_info.toString(), Customer.class);
//                customer.setName(fir_name+last_name);
//                customer.setE_mail(e_mail);
//                customer.setPhone(user_phone);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(customer);
                realm.commitTransaction();

                name.setText(customer.getFirst_name() + " " + customer.getLast_name());
                mail.setText(customer.getE_mail());
                phone.setText(customer.getPhone());
                Toast.makeText(getBaseContext(), "welcome " + customer.getFirst_name() + " " + customer.getLast_name(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_drawer, menu);
        MenuItem item = menu.findItem(R.id.login);
        MenuItem log_out = menu.findItem(R.id.sign_out);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int user_id = pref.getInt("Id", -1);
        if (user_id == -1) {
            log_out.setVisible(false);
            item.setVisible(true);

        } else {
            log_out.setVisible(true);
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

//            case R.id.settings:
//                Intent n = new Intent(ProfileActivity.this, SettingsActivity.class);
//                startActivity(n);
//                break;
            case R.id.news:
                Intent intent = new Intent(ProfileActivity.this, NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_out:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor pref_edit = preferences.edit();
                pref_edit.putInt("Id", -1);
                pref_edit.clear();
                pref_edit.apply();
                finish();
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ExcuteNetworkOperation extends AsyncTask<Void, Void, String> {
        JSONArray user_stock = new JSONArray();

        @Override
        protected String doInBackground(Void... params) {

            JSONObject object = new JSONObject();
            try {
                object.put("customer_id", user_id);
                user_stock = Utils.getUserStocks(object);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return user_stock.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            for (int i = 0; i <= user_stock.length(); i++) {
                try {
                    stockArray.add(gson.fromJson(user_stock.getJSONObject(i).toString(), UserStocks.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
            super.onPostExecute(s);
        }
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

}
