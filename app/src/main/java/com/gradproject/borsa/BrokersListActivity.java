package com.gradproject.borsa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gradproject.borsa.DataModel.Broker;

import java.util.ArrayList;

public class BrokersListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Broker> mBrokerArrayList;
    ArrayAdapter<Broker> adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brokers_list);
        mListView = (ListView) findViewById(R.id.broker_list_view);
        mBrokerArrayList = new ArrayList<>();
        adaptor = new MyAdapter(this, R.layout.broker_list_item, mBrokerArrayList);
        addBroker();
        mListView.setAdapter(adaptor);
    }

    public void addBroker() {
        Broker shery = new Broker();
        shery.setName("Shrouk");
        shery.setPhone("01111111199");
        Broker mahmoud = new Broker();
        mahmoud.setName("mahmoud");
        mahmoud.setPhone("0155555599");
        Broker mohamed = new Broker();
        mohamed.setName("mohamed");
        mohamed.setPhone("01777777779");
        mBrokerArrayList.add(shery);
        mBrokerArrayList.add(mahmoud);
        mBrokerArrayList.add(mohamed);
        adaptor.notifyDataSetChanged();
    }

    private class MyAdapter extends ArrayAdapter<Broker> {

        public MyAdapter(Context context, int resource, ArrayList<Broker> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.broker_list_item, parent, false);
            }
            TextView nameTextView = (TextView) convertView.findViewById(R.id.broker_name);
            TextView phoneTextView = (TextView) convertView.findViewById(R.id.broker_phone);

            nameTextView.setText(mBrokerArrayList.get(position).getName());
            phoneTextView.setText(mBrokerArrayList.get(position).getPhone());

            return super.getView(position, convertView, parent);
        }
    }
}





