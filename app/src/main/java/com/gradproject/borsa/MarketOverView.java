package com.gradproject.borsa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MarketOverView extends AppCompatActivity {
GridView grid;
    String[] companyname={"company1","company2","company3","company4","company5","company6",
            "company7","company8","company9","company10","company11","company12"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_over_view);
        grid=(GridView)findViewById(R.id.grid);
        company c= new company(this,R.layout.custom_item, companyname);
        grid.setAdapter(c);
        Intent intent=new Intent(this,Menu.class);
        startActivity(intent);
    }

    public class company extends ArrayAdapter{

        public company(Context context, int resource, Object[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.custom_item, parent, false);
                TextView companyNameText = (TextView) convertView.findViewById(R.id.com_name);
                companyNameText.setText(companyname[position]);
            }
            return convertView;
        }
    }


}
