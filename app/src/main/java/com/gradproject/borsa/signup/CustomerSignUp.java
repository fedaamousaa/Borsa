package com.gradproject.borsa.signup;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gradproject.borsa.R;
import com.gradproject.borsa.UIHelper.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CustomerSignUp extends Fragment {
    EditText fname,lname,e_mail,password,confirm;
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
        confirm=(EditText)view.findViewById(R.id.password_confirm);
        submit=(Button)view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString()== confirm.getText().toString()){
                    new ExcuteNetworkOperation().execute();
                    Toast.makeText(getContext(),"done",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "password error", Toast.LENGTH_SHORT).show();
                }

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
            try {
                JSONObject response=new JSONObject(s);
                int id=response.getInt("response");
                switch (id){
                    case -2:
                        Toast.makeText(getContext(), "email already exist", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor pref_edit=pref.edit();
                        pref_edit.putInt("Id",id);
                        pref_edit.apply();


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);

        }
    }
}








