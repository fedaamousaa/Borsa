package com.gradproject.borsa.signup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gradproject.borsa.R;
import com.gradproject.borsa.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BrokerSignUP extends Fragment {

    EditText fname_tv,lname_tv,email_tv,pass_tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_broker_sign_u, container, false);
         fname_tv = (EditText) v.findViewById(R.id.name2);
         lname_tv = (EditText) v.findViewById(R.id.fname2);
         email_tv = (EditText) v.findViewById(R.id.mail2);
         pass_tv = (EditText) v.findViewById(R.id.pass2);


        final Button submit = (Button) v.findViewById(R.id.done);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExcuteNetworkOperation().execute();
                Toast.makeText(getContext(),"done",Toast.LENGTH_LONG).show();

            }
        });


        return v;
    }
    public class ExcuteNetworkOperation extends AsyncTask<Void,Void,String> {
        final  JSONObject param = new JSONObject();
        @Override
        protected void onPreExecute() {
            try {

                param.put("first_name", fname_tv.getText().toString());
                param.put("last_name", lname_tv.getText().toString());
                param.put("email", email_tv.getText().toString());
                param.put("password", pass_tv.getText().toString());



            } catch (JSONException  e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject s = new JSONObject();

            try {
                s=   Utils.signUpBroker(param);
            } catch (IOException |JSONException e) {
                e.printStackTrace();
            }

            return s.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
