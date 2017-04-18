package com.gradproject.borsa.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.gradproject.borsa.DataModel.Stock;
import com.gradproject.borsa.DataModel.StockValue;
import com.gradproject.borsa.Fargments.BuyDialogFragment;
import com.gradproject.borsa.R;
import com.gradproject.borsa.UIHelper.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class CompaniesDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView last, percent ,name,phone,email,name_ar ,curr;
    int item_id;
    Stock stock = new Stock();
    Button min, hour, day,week;
    GoogleMap mMap;
    Button buyButton;
    private Realm realm;


    ArrayList<String> Date = new ArrayList<>();
    ArrayList<Entry> Value=new ArrayList<>();

    LineChart chart;
    DrawerLayout drawerLayout;

    IAxisValueFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_companies_details);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        last = (TextView) findViewById(R.id.lastprice);
        percent = (TextView) findViewById(R.id.percent);
        min = (Button) findViewById(R.id.min);
        hour = (Button) findViewById(R.id.hours);
        day = (Button) findViewById(R.id.day);

        week = (Button) findViewById(R.id.week);
        buyButton = (Button) findViewById(R.id.btnbuy);

        name=(TextView)findViewById(R.id.comany_name);
        name_ar=(TextView)findViewById(R.id.ar_name);
        email=(TextView)findViewById(R.id.email_company);
        phone=(TextView)findViewById(R.id.phone_company);

        curr=(TextView)findViewById(R.id.cur_value);

        Intent in = getIntent();
        item_id = in.getIntExtra("item", 0);

        stock = realm.where(Stock.class).equalTo("id", item_id).findFirst();
        initUI();

       chart = (LineChart) findViewById(R.id.chart);
       graph(new ArrayList<>(realm.where(StockValue.class).equalTo("stock_id", item_id).findAll()));
        chart.animateX(2500, Easing.EasingOption.EaseInOutQuart);


        new ExcuteNetworkOperation().execute();
        new ExcuteNetworkGraph().execute();




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
    public void onMapReady(final GoogleMap map) {
        mMap = map;
        //ToDo
        try {
            LatLng location = new LatLng(stock.getCompany().getLatitude(), stock.getCompany().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            mMap.addMarker(new MarkerOptions()
                    .title(stock.getCompany().getSymbol())
                    .snippet(stock.getCompany().getName() + "|" + stock.getCompany().getName_ar() + "\n" + stock.getCompany().getEmail())
                    .position(location));
        }catch (Exception e){
        }


    }

    private void initUI() {
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyDialogFragment buyDialogFragment = new BuyDialogFragment();
                buyDialogFragment.setCurr_value(stock.getCurrent_value());
                buyDialogFragment.setInit_no(String.valueOf(stock.getInit_no()));
                buyDialogFragment.setCurr_no(String.valueOf(stock.getCurr_no()));
                buyDialogFragment.setStock(stock);
                buyDialogFragment.show(getFragmentManager(), null);
            }
        });

        last.setText(String.valueOf(Utils.formatCurrency(roundTwoDecimals(stock.getCurrent_value()))));
        curr.setText(String.valueOf(Utils.formatCurrency(roundTwoDecimals(stock.getCurrent_value()))));
        if (stock.getCurrent_value() > stock.getLast_value()) {
            double change = (stock.getCurrent_value() / stock.getLast_value()) * 100;
            percent.setText("+" + roundTwoDecimals(change) + "%");
            percent.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            double change = (stock.getCurrent_value() / stock.getLast_value()) * 100;
            percent.setText("-" + roundTwoDecimals(change) + "%");
            percent.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        name.setText(""+stock.getCompany().getName()+"("+ stock.getCompany().getSymbol()+")");
        name_ar.setText(stock.getCompany().getName_ar());
        phone.setText(stock.getCompany().getPhone());
        //symbol.setText();
        email.setText(stock.getCompany().getEmail());


    }

    public class ExcuteNetworkOperation extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject stock_item = new JSONObject();
            JSONObject object = new JSONObject();
            try {
                object.put("stock_id", item_id);
                stock_item = Utils.getStock(object);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return stock_item;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                Gson gson=new Gson();
                JSONObject s = response.getJSONObject("response");
                stock = gson.fromJson(s.toString(),Stock.class);


                initUI();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(response);

        }
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public class ExcuteNetworkGraph extends AsyncTask<Void, Void, ArrayList<StockValue>> {
        @Override
        protected ArrayList<StockValue> doInBackground(Void... params) {

            JSONArray graph_values = new JSONArray();
            JSONObject object = new JSONObject();
            ArrayList<StockValue> stockValues = new ArrayList<>();
            try {
                object.put("stock_id", item_id);
                graph_values = Utils.getStockValues(object);


                for (int i = 0; i < graph_values.length(); i++) {
                    Gson gson = new Gson();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    JSONObject valueObj = graph_values.getJSONObject(i);
                    Date date = new Date(valueObj.getString("date_add"));
                    Double value = valueObj.getDouble("value");
                    int id = valueObj.getInt("id");

                    StockValue stockValue = new StockValue(id, item_id, date, value);
//                   StockValue stockValue = gson.fromJson(valueObj.toString(),StockValue.class);



                    stockValues.add(stockValue);
                }
                graph(stockValues);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return stockValues;
        }

        @Override
        protected void onPostExecute(ArrayList<StockValue> jsonArray) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(jsonArray);
            realm.commitTransaction();
            super.onPostExecute(jsonArray);
        }
    }
    private void graph(ArrayList<StockValue> sArray) {


        Double maxValue = 0.0;
        Double minValue = 0.0;

        for (int i = 0; i < sArray.size(); i++) {
            Double value = sArray.get(i).getValue();
            Date date = sArray.get(i).getDate_add();
            final Calendar c = Calendar.getInstance();
            c.setTime(new Date());

            if (i == 0) {
                minValue = value;
            } else {
                if (value < minValue) {
                    minValue = value;
                }
            }
            if (value > maxValue) {
                maxValue = value;
            }
            Date.add(date.toString());
            Value.add(new Entry(Float.parseFloat(String.valueOf(i)), Float.parseFloat(String.valueOf(value))));

            formatter = new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return Date.get(Integer.parseInt(String.valueOf(value)) );
                }

            };

            XAxis xAxis = chart.getXAxis();
//            xAxis.setTextSize(11f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.getAxisLineColor();
            xAxis.setValueFormatter(formatter);
            c.add(Calendar.DAY_OF_YEAR, -3);
//            xAxis.setAxisMaxValue(c.getTime().getTime());
//            xAxis.setAxisMinValue(new Date().getTime());
            xAxis.setTextColor(ColorTemplate.getHoloBlue());
//            xAxis.setDrawGridLines(false);
//            xAxis.setDrawAxisLine(true);
            xAxis.setGranularity(1f);


            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setTextColor(ColorTemplate.getHoloBlue());
            leftAxis.setAxisMaxValue(Float.valueOf(maxValue.toString()));
            Double min=minValue - (maxValue - minValue);
            leftAxis.setAxisMinValue(Float.valueOf(min.toString()));
            leftAxis.setDrawGridLines(false);
            leftAxis.setGranularityEnabled(true);

            setData();

            chart.invalidate();
            chart.notifyDataSetChanged();
        }
    }


    private void setData() {

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(Value, "Value");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);




        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets

        LineData data = new LineData(dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
        chart.getAxisRight().setEnabled(false);
//        chart.moveViewToX(data.getEntryCount());

    }



}

