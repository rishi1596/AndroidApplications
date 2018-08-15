package rj.bkinfotech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import rj.bkinfotech.Constants.Constants;

/**
 * Created by jimeet29 on 30-11-2017.
 */

public class ResetActivity extends AppCompatActivity {
    EditText et_mobile_no,et_email,et_otp;
    public String in_mno,token,in_email,in_otp,url=null;
    Button btn_reset;
    TextView tv_error;
    int res_ref,count=1;
    String MobilePattern = "^[789][0-9]{9}$";
    //String EmailPattern = "^[a-z A-Z0-9.@_\\-]+$";
    String OTPPattern = "^[0-9]{5}$";
    SharedPreferences reset_prefs;
    JSONObject devicedetails;
    ConnectivityManager conm;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_activity);

        setCustomActionBar();

        et_mobile_no = (EditText) findViewById(R.id.et_mobileno);
        et_otp = (EditText) findViewById(R.id.et_otp);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        tv_error = (TextView) findViewById(R.id.tv_error);

        devicedetails = new JSONObject();

            btn_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    in_mno = et_mobile_no.getText().toString().trim();
                    //Toast.makeText(getApplicationContext(), in_mno, Toast.LENGTH_SHORT).show();

                    if (!in_mno.matches(MobilePattern)) {
                        //Toast.makeText(getApplicationContext(), "Please Enter a Valid Mobile No.", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.validation_mobile_no);
                        tv_error.setVisibility(View.VISIBLE);
                    } else {
                        confirmationdialog(in_mno);
                    }
                }
            });
    }
    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(R.string.reset);
        ImageView iv_info = (ImageView)actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);
        //iv_info.setOnClickListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }
    private void confirmationdialog(String mobno)
    {

        final AlertDialog.Builder alertbox = new AlertDialog.Builder(ResetActivity.this);
        if(count==1) {
//todo runtime error
//            alertbox.setMessage(R.string.reset_activity_mobile_no_confirmation + mobno);
            alertbox.setMessage("Are the Details Correct? OTP will be send on given mobile number "+ mobno);
        }else{
            alertbox.setMessage(R.string.opt_confirmation);
        }
        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Toast.makeText(getApplicationContext(),"You clicked Yes button",Toast.LENGTH_LONG).show();


                try
                {
                    conm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    networkInfo  = conm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {

                        if(count==1) {
                            //url="http://192.168.1.35:81/bkinfotech/resetUserDetails.php";
                            url="http://bkinfotech.in/app/resetUserDetails.php";

                            devicedetails.put(Constants.strClientIdKey,Constants.clientId);
                            devicedetails.put("number", in_mno);

                        }
                        else {
                            token = FirebaseInstanceId.getInstance().getToken();
                            devicedetails.put(Constants.strClientIdKey,Constants.clientId);
                            devicedetails.put("number", in_mno);
                            devicedetails.put("devicetoken", token);
                            devicedetails.put("otp", in_otp);
                            //url = "http://192.168.1.35:81/bkinfotech/otpValidateUser.php";
                            url="http://bkinfotech.in/app/otpValidateUser.php";
                        }

                        if (devicedetails.length() > 0) {
                            new ResetAsync().execute(String.valueOf(devicedetails));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "There is no active network connection!", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.no_network);
                        tv_error.setVisibility(View.VISIBLE);
                    }


                    System.out.print("Device Details In RESET ACTIVITY" + devicedetails);
                    Log.d("Details", devicedetails.toString());

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
               /*ConnectivityManager conm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo  = conm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (devicedetails.length() > 0) {

                        final ProgressDialog pg = new ProgressDialog(ResetActivity.this);
                        pg.setMessage("Processing... Please Wait!");
                        pg.setCancelable(false);
                        pg.show();
                        final ReusableCode rc = new ReusableCode();
                        rc.server_call(url,devicedetails.toString());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                        pg.dismiss();
                        res_ref = rc.get_server_response();
                                Log.d("Reset Activity", String.valueOf(res_ref));
                        if(res_ref == 1)
                        {
                            Log.d("Reset count before", String.valueOf(count));
                            count += 1;
                            Log.d("Reset After", String.valueOf(count));
                            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                            if(count==2) {

                                otpValidateUI();

                            }else{
                                SharedPreferences firstRun = getSharedPreferences("settings", 0);
                                SharedPreferences.Editor editor = firstRun.edit();
                                editor.putBoolean("fr", true);
                                editor.putString("mobileno", in_mno);
                                editor.apply();

                                Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);

                                startActivity(userActivity);
                                ResetActivity.this.finish();
                                //((Activity)getApplicationContext()).finish();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                            tv_error.setText("Please Try Again!");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                            }
                        },5000);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "There is no active network connection!", Toast.LENGTH_SHORT).show();
                    tv_error.setText("There is no active network connection!");
                    tv_error.setVisibility(View.VISIBLE);
                }*/

            }
        });

        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertbox.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"PAUSED",Toast.LENGTH_SHORT).show();
        if(count==2) {
            reset_prefs = getSharedPreferences(Constants.sharedPreferencesFileNameReset, Constants.sharedPreferencesAccessMode);
            SharedPreferences.Editor ed = reset_prefs.edit();
            ed.putInt("Count", count);
            ed.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),"Resumed",Toast.LENGTH_SHORT).show();
        reset_prefs = getSharedPreferences(Constants.sharedPreferencesFileNameReset,Constants.sharedPreferencesAccessMode);
        int localCount = reset_prefs.getInt("Count",0);
        if(localCount==2)
        {
            SharedPreferences.Editor ed = reset_prefs.edit();
            ed.putInt("Count",0);
            ed.apply();
            otpValidateUI();
        }
    }

    private void otpValidateUI()
    {
        et_otp.setVisibility(View.VISIBLE);
        tv_error.setVisibility(View.GONE);
        btn_reset.setText(R.string.validate_otp);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_otp = et_otp.getText().toString().trim();

                if (!in_otp.matches(OTPPattern)) {
                    //Toast.makeText(getApplicationContext(), "Please Enter a Valid OTP", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_otp);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    confirmationdialog(in_mno);
                }
            }
        });
    }

    private class ResetAsync extends AsyncTask<String,Void,Integer> {
        ProgressDialog pg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                pg = new ProgressDialog(ResetActivity.this);
                pg.setMessage("Processing... Please Wait!");
                pg.setCancelable(false);
                pg.show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }



        @Override
        protected Integer doInBackground(String... params) {
            int jsonResponse;
            String jsonData = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            URL link;
            try{
                link = new URL(url);
                urlConnection =(HttpURLConnection)link.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json;charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8"));
                writer.write(jsonData);
                writer.close();

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while((inputLine=reader.readLine())!= null)
                {
                    response.append(inputLine + "\n");
                }
                jsonResponse = Integer.parseInt(response.toString().trim());

                System.out.println("Response"+jsonResponse);
                //response_server = jsonResponse;

                return jsonResponse;

            }catch (Exception e)
            {
                pg.dismiss();
                Log.d("Resuable Code",e.toString());
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        pg.dismiss();
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            try{
                pg.dismiss();
                if(s == 1)
                {
                    Log.d("Reset count before", String.valueOf(count));
                    count += 1;
                    Log.d("Reset After", String.valueOf(count));
                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                    if(count==2) {

                        otpValidateUI();

                    }else{
                        SharedPreferences firstRun = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                        SharedPreferences.Editor editor = firstRun.edit();
                        editor.putBoolean("fr", true);
                        editor.putString("mobileno", in_mno);
                        editor.apply();

                        Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(userActivity);
                        //MainActivity.mainActivity.finish();
                        ResetActivity.this.finish();
                        //((Activity)getApplicationContext()).finish();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                    tv_error.setText("Please Try Again!");
                    tv_error.setVisibility(View.VISIBLE);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}



