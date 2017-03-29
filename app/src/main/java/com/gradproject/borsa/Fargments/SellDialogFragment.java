package com.gradproject.borsa.Fargments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gradproject.borsa.DataModel.Stock;
import com.gradproject.borsa.R;
import com.gradproject.borsa.UIHelper.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

public class SellDialogFragment extends DialogFragment {
    int mProgress;
    TextView stock_num;
    TextView total;
    String init_no;
    String curr_no;
    double curr_value;
    double total_value;
    int user_id;
    Stock stock;



    Activity mActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public void setCurr_value(double curr_value) {
        this.curr_value = roundTwoDecimals(curr_value);
    }

    public void setCurr_no(String curr_no) {
        this.curr_no = curr_no;
    }

    public void setInit_no(String init_no) {
        this.init_no = init_no;
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle("sell " + stock.getCompany().getName())
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new ExcuteNetwork().execute();
                                Toast.makeText(getActivity(), "you sell with :" + roundTwoDecimals(total_value), Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        LayoutInflater i = getActivity().getLayoutInflater();
        View v = i.inflate(R.layout.activity_sell_dialog_fragment,null);

        SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        TextView price = (TextView) v.findViewById(R.id.price);
        total = (TextView) v.findViewById(R.id.total);
        price.setText(String.valueOf(curr_value));
        total.setText(String.valueOf(0));
        if (null == curr_no || curr_no.equals("null") || curr_no.isEmpty()) {
            seekBar.setMax(Integer.valueOf(init_no));
        } else {
            seekBar.setMax(Integer.valueOf(curr_no));
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mProgress=progress;
                stock_num.setText(String.valueOf(mProgress));
                total.setText(String.valueOf(roundTwoDecimals(curr_value * mProgress)));
                total_value = curr_value * progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        stock_num = (TextView) v.findViewById(R.id.stock_number_v);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        user_id = pref.getInt("Id", -1);
        b.setView(v);
        return b.create();
    }
    public class ExcuteNetwork extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {

            JSONObject buy_Request = new JSONObject();
            JSONObject object = new JSONObject();
            try {

                int id=stock.getId();
                object.put("stock_id", id);
                object.put("customer_id",user_id);
                object.put("no_stocks",mProgress);
                buy_Request= Utils.sellStockRequest(object);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return buy_Request;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject ) {

            try {
                int response = jsonObject.getInt("response");

                switch (response) {
                    case -2:
                        Toast.makeText(mActivity.getBaseContext(), "not enough stocks", Toast.LENGTH_SHORT).show();
                        break;
                    case 404:
                        Toast.makeText(mActivity.getBaseContext(), " unknown error", Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(mActivity.getBaseContext(), "stock not found", Toast.LENGTH_SHORT).show();
                    default:
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mActivity.getBaseContext());
                        SharedPreferences.Editor pref_edit = pref.edit();
                        pref_edit.putInt("Id", response);
                        pref_edit.apply();
                        Log.d("response",String.valueOf(response));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(jsonObject);
        }
    }
}
