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

public class CompanySignUp extends Fragment {
EditText name,phone, email, address, password, longitude, latitude, com_number, tax_number;
    Button submit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_company_sign_up, container, false);
        name=(EditText)view.findViewById(R.id.name);
        phone=(EditText)view.findViewById(R.id.phone2);
        email=(EditText)view.findViewById(R.id.mail);
        address=(EditText)view.findViewById(R.id.address);
        password=(EditText)view.findViewById(R.id.pass2);
        longitude=(EditText)view.findViewById(R.id.longitude);
        latitude=(EditText)view.findViewById(R.id.latitude);
        com_number=(EditText)view.findViewById(R.id.com_number);
        tax_number=(EditText)view.findViewById(R.id.tax_number);
        submit=(Button)view.findViewById(R.id.done);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject param=new JSONObject();
                try {
                    param.put("name",name.getText().toString());
                    param.put("phone",phone.getText().toString());
                    param.put("email",email.getText().toString());
                    param.put("address",address.getText().toString());
                    param.put("longitude",longitude.getText().toString());
                    param.put("latitude",latitude.getText().toString());
                    param.put("com_number",com_number.getText().toString());
                    param.put("tax_number",tax_number.getText().toString());
                    Utils.signUpCompany(param);

                } catch (JSONException |IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}
