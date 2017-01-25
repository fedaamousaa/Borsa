package com.gradproject.borsa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;



public class MarketOverView extends Fragment implements Companies.OnFragmentInteractionListener{
    private FragmentInteractionListener Listener;
    GridView grid;
    Button button, button1;
    String[] companyname = {"company1", "company2", "company3", "company4", "company5", "company6",
            "company7", "company8", "company9", "company10", "company11", "company12"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_market_over_view,container,false);
        grid = (GridView)view.findViewById(R.id.grid);
        company c = new company(getActivity(),R.layout.custom_item,companyname);
        grid.setAdapter(c);
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onFragmentInteraction(String z) {

    }
    public class company extends ArrayAdapter  {
        public company(Context context, int resource, Object[] objects) {
            super(context, resource, objects);
        }
        @NonNull
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.custom_item, parent, false);
                TextView companyNameText = (TextView) convertView.findViewById(R.id.com_name);
                companyNameText.setText(companyname[position]);
            }
            button = (Button) convertView.findViewById(R.id.buy);
            button1 = (Button) convertView.findViewById(R.id.sell);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "buy button", Toast.LENGTH_LONG).show();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "sell button", Toast.LENGTH_LONG).show();
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().replace(R.id.marketframe,new Companies()).addToBackStack(null).commit();
                }
            });
            return convertView;
        }
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



