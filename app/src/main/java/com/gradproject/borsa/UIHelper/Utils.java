package com.gradproject.borsa.UIHelper;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    private static String Domain = "http://borsa.herokuapp.com";
//   private static String Domain = "http://www.zeowls.com:41384";
//    private static String Domain = "http://192.168.0.101:8080";

    private static String getRequest(String url) throws IOException {
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



    public static JSONObject signUpCustomer(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/signupCustomer", params);
        json = new JSONObject(response);
        Log.d("response", response);
        return json;
    }
    public static JSONObject loginCustomer(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/loginCustomer", params);
        json = new JSONObject(response);
        Log.d("response", response);
        return json;
    }
    public static JSONArray getAllCompanies() throws IOException, JSONException {
        JSONArray json;
        String response = getRequest(Domain + "/getAllCompanies");
        json = new JSONArray(response);
        Log.d("response", response);
        return json;
}
    public static JSONArray getAllStocks() throws IOException, JSONException {
        JSONArray json;
        String response = getRequest(Domain + "/getAllStocks");
        json = new JSONArray(response);
        Log.d("response", response);
        return json;
    }

    public static JSONObject getStock(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/getStock",params);
        json = new JSONObject(response);
        Log.d("response", response);
        return json;
    }
    public static JSONObject getCustomer(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/getCustomer",params);
        json = new JSONObject(response);
        Log.d("response", response);
        return json;
    }
    public static JSONObject buyStockRequest(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/buyStockRequest",params);
        json = new JSONObject(response);
        Log.d("response", response);
        return json;
    }
    public static JSONArray getUserStocks(JSONObject params) throws IOException, JSONException {
        JSONArray json;
        String response = postRequest(Domain + "/getUserStocks",params);
        json = new JSONArray(response);
        Log.d("response", response);
        return json;
    }
    public static JSONObject sellStockRequest(JSONObject params) throws IOException, JSONException {
        JSONObject json;
        String response = postRequest(Domain + "/sellStockRequest",params);
        json = new JSONObject(response);
        Log.d("response", response);
        return json;
    }

    public static JSONArray getStockValues(JSONObject params) throws IOException, JSONException {
        JSONArray json;
        String response = postRequest(Domain + "/getStockValues",params);
        json = new JSONArray(response);
        Log.d("response", response);
        return json;
    }

    public static String formatCurrency (Double value){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\'');
        symbols.setDecimalSeparator(',');

        DecimalFormat decimalFormat = new DecimalFormat("#,###.00 EGP", symbols);
        String formated = decimalFormat.format(value);
        return formated;
    }


    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo!= null&& networkInfo.isConnected();
    }



}
