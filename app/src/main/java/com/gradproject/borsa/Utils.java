package com.gradproject.borsa;


import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    private static String Domain = "http://borsa.herokuapp.com";

    private String getRequest(String url) throws IOException {
        String data;
        BufferedReader reader;
        URL url1 = new URL(url);
        Log.d("url", url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "627562626c6520617069206b6579");
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.connect();
        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        return data;
    }

    public static String postRequest(String url, JSONObject params) throws IOException {
        URL url1 = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestProperty("Authorization", "627562626c6520617069206b6579");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream stream = connection.getOutputStream();
        DataOutputStream outputStream = new DataOutputStream(stream);
        Log.d("WARN", params.toString());
        outputStream.writeBytes(params.toString());
        outputStream.flush();
        outputStream.close();
        stream.close();
        String data;
        BufferedReader reader;
        InputStream inputStream = connection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        Log.d("data", data);
        return data;
    }

    public static JSONObject signUpBroker(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/signupBroker", params);
        json = new JSONObject(response);
        Log.d("response", response);
        return json;
    }

    public static JSONObject signUpCustomer(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/signupCostomer", params);
        json = new JSONObject(response);

        return json;
    }

    public static JSONObject signUpCompany(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/signupCompany", params);
        json = new JSONObject(response);
        return json;
    }
}
