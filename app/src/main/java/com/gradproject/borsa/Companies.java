package com.gradproject.borsa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Companies extends Fragment {
    private OnFragmentInteractionListener mListener;

    // Defined Array values to show in ListView
    String[] values = new String[] { "Porto",
            "KFS inc",
            "Microsoft"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_companies, container, false);
        ListView lv = (ListView) view.findViewById(R.id.companies_list_view);
        lv.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.companies_list_item,
                R.id.company_text_view, values));
        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String z) {
        if (mListener != null) {
            mListener.onFragmentInteraction(z);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String z);
    }
}