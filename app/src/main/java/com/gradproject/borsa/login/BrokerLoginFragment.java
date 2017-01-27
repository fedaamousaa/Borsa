package com.gradproject.borsa.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gradproject.borsa.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrokerLoginFragment extends Fragment {


    public BrokerLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_broker_login, container, false);
    }

}
