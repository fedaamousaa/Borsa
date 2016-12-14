package com.gradproject.borsa;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Menu extends AppCompatActivity {
   final String[] menu={"log in","companies","brokers","news","settings","search","information"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListView l=(ListView)findViewById(R.id.list);
        arr a=new arr(this,R.layout.list_item,menu);
        l.setAdapter(a);
    }
    public class arr extends ArrayAdapter {
         arr(Context context, int resource, Object[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null) {

                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.list_item, parent, false);
                if (menu[position]=="log in") {
                    TextView menu_item = (TextView) convertView.findViewById(R.id.menutxt);
                    ImageView menu_image=(ImageView)convertView.findViewById(R.id.menuimage);
                    menu_image.setImageResource(R.drawable.log);
                    menu_item.setText(menu[position]);
                }
                else if (menu[position]=="companies") {
                    TextView menu_item = (TextView) convertView.findViewById(R.id.menutxt);
                    ImageView menu_image=(ImageView)convertView.findViewById(R.id.menuimage);
                    menu_image.setImageResource(R.drawable.company);
                    menu_item.setText(menu[position]);
                }
                else if (menu[position]=="brokers") {
                    TextView menu_item = (TextView) convertView.findViewById(R.id.menutxt);
                    ImageView menu_image=(ImageView)convertView.findViewById(R.id.menuimage);
                    menu_image.setImageResource(R.drawable.broker);
                    menu_item.setText(menu[position]);
                }
                else if (menu[position]=="news") {
                    TextView menu_item = (TextView) convertView.findViewById(R.id.menutxt);
                    ImageView menu_image=(ImageView)convertView.findViewById(R.id.menuimage);
                    menu_image.setImageResource(R.drawable.news);
                    menu_item.setText(menu[position]);
                }
                else if (menu[position]=="settings") {
                    TextView menu_item = (TextView) convertView.findViewById(R.id.menutxt);
                    ImageView menu_image=(ImageView)convertView.findViewById(R.id.menuimage);
                    menu_image.setImageResource(R.drawable.settings);
                    menu_item.setText(menu[position]);
                }
                else if (menu[position]=="search") {
                    TextView menu_item = (TextView) convertView.findViewById(R.id.menutxt);
                    ImageView menu_image=(ImageView)convertView.findViewById(R.id.menuimage);
                    menu_image.setImageResource(R.drawable.search);
                    menu_item.setText(menu[position]);
                }
                else if (menu[position]=="information") {
                    TextView menu_item = (TextView) convertView.findViewById(R.id.menutxt);
                    ImageView menu_image=(ImageView)convertView.findViewById(R.id.menuimage);
                    menu_image.setImageResource(R.drawable.about);
                    menu_item.setText(menu[position]);
                }
            }
                return convertView;

        }
    }
}
