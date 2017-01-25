package com.gradproject.borsa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BrokerSignUP extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_broker_sign_u, container, false);
        final EditText fname_tv = (EditText) v.findViewById(R.id.name2);
        final EditText lname_tv = (EditText) v.findViewById(R.id.fname2);
        final EditText email_tv = (EditText) v.findViewById(R.id.mail2);
        final EditText pass_tv = (EditText) v.findViewById(R.id.pass2);
        final EditText mobile_tv = (EditText) v.findViewById(R.id.phone2);

        final Button submit = (Button) v.findViewById(R.id.done);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject params = new JSONObject();
                try {
                    params.put("first_name", fname_tv.getText().toString());
                    params.put("last_name", lname_tv.getText().toString());
                    params.put("email", email_tv.getText().toString());
                    params.put("password", pass_tv.getText().toString());

                    Utils.signUpBroker(params);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
        });

        return v;
    }
}
