package com.gradproject.borsa.login;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gradproject.borsa.R;
import com.gradproject.borsa.UIHelper.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CustomerLoginFragment extends Fragment {

    EditText e_mail, password;
    Button login;

    public CustomerLoginFragment() {
        // Required empty public constructor
    }


    OnUserLogedIn mCallback;

    // Container Activity must implement this interface
    public interface OnUserLogedIn {
        public void onAction(boolean bool);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCallback = (OnUserLogedIn) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_login, container, false);
        e_mail = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.pass);
        login = (Button) view.findViewById(R.id.log);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExcuteNetworkOperation().execute();
            }
        });
        return view;
    }

    public class ExcuteNetworkOperation extends AsyncTask<Void, Void, String> {
        final JSONObject param = new JSONObject();

        @Override
        protected void onPreExecute() {
            try {

                param.put("email", e_mail.getText().toString());
                param.put("password", password.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            JSONObject s = new JSONObject();

            try {

                s = Utils.loginCustomer(param);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return s.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                Gson gson=new Gson();
                JSONObject response = new JSONObject(s);
                //gson.fromJson(response.toString(),);
                Log.d("response", String.valueOf(response));
                int resp = response.getInt("response");

                switch (resp) {
                    case -2:
                        Toast.makeText(getContext(), "incorrect email", Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(getContext(), "incorrect password", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor pref_edit = pref.edit();
                        pref_edit.putInt("id_customer", resp);
                        pref_edit.apply();
                        mCallback.onAction(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);

        }
    }
}
