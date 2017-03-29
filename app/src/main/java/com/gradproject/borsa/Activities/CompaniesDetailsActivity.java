package com.gradproject.borsa.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
    TextView last, percent;
    int item_id;
    Stock stock = new Stock();
    GraphView graph;
    Button min, hour, day, month, week;

    GoogleMap mMap;
    Button buyButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
       // month = (Button) findViewById(R.id.month);
        week = (Button) findViewById(R.id.week);
        buyButton = (Button) findViewById(R.id.btnbuy);

        Intent in = getIntent();
        item_id = in.getIntExtra("item", 0);

        stock = realm.where(Stock.class).equalTo("id", item_id).findFirst();
        initUI();

        graph = (GraphView) findViewById(R.id.graph);
        updateGraph(new ArrayList<>(realm.where(StockValue.class).equalTo("stock_id", item_id).findAll()));

        new ExcuteNetworkOperation().execute();
        new ExcuteNetworkGraph().execute();
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
        if (stock.getCurrent_value() > stock.getLast_value()) {
            double change = (stock.getCurrent_value() / stock.getLast_value()) * 100;
            percent.setText("+" + roundTwoDecimals(change) + "%");
            percent.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            double change = (stock.getCurrent_value() / stock.getLast_value()) * 100;
            percent.setText("-" + roundTwoDecimals(change) + "%");
            percent.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }


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
                updateGraph(stockValues);
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

    private void updateGraph(ArrayList<StockValue> sArray) {
        graph.removeAllSeries();
        final DataPoint[] dataPoints = new DataPoint[sArray.size()];
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        Double maxValue = 0.0;
        Double minValue = 0.0;

        for (int i = 0; i < sArray.size(); i++) {
            Double value = sArray.get(i).getValue();
            Date date = sArray.get(i).getDate_add();
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

            dataPoints[i] = new DataPoint(date, value);
        }

        final LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext(), new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)));

        series.setDrawBackground(true);


        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(minValue - (maxValue - minValue));
        graph.getViewport().setMaxY(maxValue);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext()));

        graph.getViewport().setXAxisBoundsManual(true);
        c.add(Calendar.DAY_OF_YEAR, -3);
        graph.getViewport().setMinX(c.getTime().getTime());
        graph.getViewport().setMaxX(new Date().getTime());

        graph.getGridLabelRenderer().setLabelsSpace(5);
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);

        graph.getGridLabelRenderer().setTextSize(15);

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hour.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                hour.setTextColor(getResources().getColor(android.R.color.white));
                week.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                week.setTextColor(getResources().getColor(android.R.color.white));
                day.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                day.setTextColor(getResources().getColor(android.R.color.white));

                min.setBackgroundColor(getResources().getColor(android.R.color.white));
                min.setTextColor(getResources().getColor(R.color.colorAccent));

                c.setTime(new Date());
                c.add(Calendar.MINUTE, -30);
                graph.getViewport().setMinX(c.getTime().getTime());
                graph.getViewport().setMaxX(new Date().getTime());
                ArrayList<StockValue> sArray = new ArrayList<>(realm.where(StockValue.class).equalTo("stock_id", item_id).between("date_add", c.getTime(), new Date()).findAll());
                Double maxValue = 0.0;
                Double minValue = 0.0;

                for (int i = 0; i < sArray.size(); i++) {
                    Double value = sArray.get(i).getValue();
                    Date date = sArray.get(i).getDate_add();
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

                    dataPoints[i] = new DataPoint(date, value);
                }
                graph.getViewport().setMinY(minValue - (maxValue - minValue));
                graph.getViewport().setMaxY(maxValue);
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext(), new SimpleDateFormat(":mm a, z", Locale.ENGLISH)));
                graph.getViewport().scrollToEnd();

            }
        });
        hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                min.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                min.setTextColor(getResources().getColor(android.R.color.white));
                week.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                week.setTextColor(getResources().getColor(android.R.color.white));
                day.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                day.setTextColor(getResources().getColor(android.R.color.white));

                hour.setBackgroundColor(getResources().getColor(android.R.color.white));
                hour.setTextColor(getResources().getColor(R.color.colorAccent));

                c.setTime(new Date());
                c.add(Calendar.HOUR_OF_DAY, -1);
                graph.getViewport().setMinX(c.getTime().getTime());
                graph.getViewport().setMaxX(new Date().getTime());
                ArrayList<StockValue> sArray = new ArrayList<>(realm.where(StockValue.class).equalTo("stock_id", item_id).between("date_add", c.getTime(), new Date()).findAll());
                Double maxValue = 0.0;
                Double minValue = 0.0;

                for (int i = 0; i < sArray.size(); i++) {
                    Double value = sArray.get(i).getValue();
                    Date date = sArray.get(i).getDate_add();
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

                    dataPoints[i] = new DataPoint(date, value);
                }
                graph.getViewport().setMinY(minValue - (maxValue - minValue));
                graph.getViewport().setMaxY(maxValue);
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext(), new SimpleDateFormat("h:mm a", Locale.ENGLISH)));
                graph.getViewport().scrollToEnd();
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                hour.setTextColor(getResources().getColor(android.R.color.white));
                week.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                week.setTextColor(getResources().getColor(android.R.color.white));
                min.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                min.setTextColor(getResources().getColor(android.R.color.white));

                day.setBackgroundColor(getResources().getColor(android.R.color.white));
                day.setTextColor(getResources().getColor(R.color.colorAccent));

                c.setTime(new Date());
                c.add(Calendar.DAY_OF_WEEK, -1);
                graph.getViewport().setMinX(c.getTime().getTime());
                graph.getViewport().setMaxX(new Date().getTime());
                ArrayList<StockValue> sArray = new ArrayList<>(realm.where(StockValue.class).equalTo("stock_id", item_id).between("date_add", c.getTime(), new Date()).findAll());
                Double maxValue = 0.0;
                Double minValue = 0.0;

                for (int i = 0; i < sArray.size(); i++) {
                    Double value = sArray.get(i).getValue();
                    Date date = sArray.get(i).getDate_add();
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

                    dataPoints[i] = new DataPoint(date, value);
                }
                graph.getViewport().setMinY(minValue - (maxValue - minValue));
                graph.getViewport().setMaxY(maxValue);
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext(), new SimpleDateFormat("EEE", Locale.ENGLISH)));
                graph.getViewport().scrollToEnd();
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hour.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                hour.setTextColor(getResources().getColor(android.R.color.white));
                min.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                min.setTextColor(getResources().getColor(android.R.color.white));
                day.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                day.setTextColor(getResources().getColor(android.R.color.white));

                week.setBackgroundColor(getResources().getColor(android.R.color.white));
                week.setTextColor(getResources().getColor(R.color.colorAccent));


                c.setTime(new Date());
                c.add(Calendar.WEEK_OF_MONTH, -1);
                graph.getViewport().setMinX(c.getTime().getTime());
                graph.getViewport().setMaxX(new Date().getTime());
                ArrayList<StockValue> sArray = new ArrayList<>(realm.where(StockValue.class).equalTo("stock_id", item_id).between("date_add", c.getTime(), new Date()).findAll());
                Double maxValue = 0.0;
                Double minValue = 0.0;

                for (int i = 0; i < sArray.size(); i++) {
                    Double value = sArray.get(i).getValue();
                    Date date = sArray.get(i).getDate_add();
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

                    dataPoints[i] = new DataPoint(date, value);
                }
                graph.getViewport().setMinY(minValue - (maxValue - minValue));
                graph.getViewport().setMaxY(maxValue);
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext(), new SimpleDateFormat("EEE, MMM", Locale.ENGLISH)));
                graph.getViewport().scrollToEnd();
            }
        });
//        month.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                c.setTime(new Date());
//                c.add(Calendar.MONTH, -1);
//                graph.getViewport().setMinX(c.getTime().getTime());
//                graph.getViewport().setMaxX(new Date().getTime());
//                ArrayList<StockValue> sArray = new ArrayList<>(realm.where(StockValue.class).equalTo("stock_id", item_id).between("date_add", c.getTime(), new Date()).findAll());
//                Double maxValue = 0.0;
//                Double minValue = 0.0;
//
//                for (int i = 0; i < sArray.size(); i++) {
//                    Double value = sArray.get(i).getValue();
//                    Date date = sArray.get(i).getDate_add();
//                    if (i == 0) {
//                        minValue = value;
//                    } else {
//                        if (value < minValue) {
//                            minValue = value;
//                        }
//                    }
//                    if (value > maxValue) {
//                        maxValue = value;
//                    }
//
//                    dataPoints[i] = new DataPoint(date, value);
//                }
//                graph.getViewport().setMinY(minValue - (maxValue - minValue));
//                graph.getViewport().setMaxY(maxValue);
//                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext(), new SimpleDateFormat("MMM", Locale.ENGLISH)));
//                graph.getViewport().scrollToEnd();
//            }
//        });
        graph.addSeries(series);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
    }


}

