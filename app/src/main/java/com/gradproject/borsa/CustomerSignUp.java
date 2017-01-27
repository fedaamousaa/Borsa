package com.gradproject.borsa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CustomerSignUp extends Fragment {
    EditText fname,lname,e_mail,password;
    Button submit;
    public CustomerSignUp() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_customer_sign_up, container, false);
        fname=(EditText)view.findViewById(R.id.fstname);
        lname=(EditText)view.findViewById(R.id.last);
        e_mail=(EditText)view.findViewById(R.id.mail);
        password=(EditText)view.findViewById(R.id.password);
        submit=(Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExcuteNetworkOperation().execute();
                Toast.makeText(getContext(),"done",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
    public class ExcuteNetworkOperation extends AsyncTask<Void,Void,String> {
        final JSONObject param = new JSONObject();

        @Override
        protected void onPreExecute() {
            try {

                param.put("first_name", fname.getText().toString());
                param.put("last_name", lname.getText().toString());
                param.put("email", e_mail.getText().toString());
                param.put("password", password.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(Void... params) {
            JSONObject s =new JSONObject();

            try {
                s=  Utils.signUpCustomer(param);
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








