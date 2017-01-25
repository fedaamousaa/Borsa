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
                JSONObject params=new JSONObject();
                try {
                    params.put("first_name",fname.getText().toString());
                    params.put("last_name",lname.getText().toString());
                    params.put("email",e_mail.getText().toString());
                    params.put("password",password.getText().toString());
                    Utils.signUpCustomer(params);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

}
