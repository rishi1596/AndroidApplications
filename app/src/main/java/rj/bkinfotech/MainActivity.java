package rj.bkinfotech;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity  {

    EditText et_mobile_no,et_email;
    String in_mno,token,in_email;
    //String url = "http://192.168.1.35:81/bkinfotech/newUserDetails.php";
    String url = "http://bkinfotech.in/app/newUserDetails.php";
    Button btn_register;
    TextView tv_already,tv_error;
    String MobilePattern = "^[789][0-9]{9}$";
    //String EmailPattern = "^[a-zA-Z0-9.@_\\-]+$";
    ProgressDialog pg;
    //static Activity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCustomActionBar();

        //mainActivity =this;
        pg = new ProgressDialog(MainActivity.this);

        et_mobile_no = (EditText) findViewById(R.id.et_mobileno);

        btn_register = (Button) findViewById(R.id.btn_register);
        tv_already = (TextView) findViewById(R.id.tv_already);
        tv_error = (TextView) findViewById(R.id.tv_error);

        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                in_mno = et_mobile_no.getText().toString().trim();


                if (!in_mno.matches(MobilePattern)) {
                    //Toast.makeText(getApplicationContext(), "Please Enter a Valid Mobile No.", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_mobile_no);
                    tv_error.setVisibility(View.VISIBLE);
                }  else {
                    confirmationdialog(in_mno);
                }
            }
        });

        tv_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetActivity = new Intent(getApplicationContext(),ResetActivity.class);
                startActivity(resetActivity);
            }
        });


    }
    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(R.string.app_name);
        ImageView iv_info = (ImageView)actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);
        //iv_info.setOnClickListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void confirmationdialog(String mobno)
    {

        final AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
        //ToDO string error it shows no at runtime
        //alertbox.setMessage(R.string.no_registration_confirmation + mobno);
        alertbox.setMessage("Are You Sure about Mobile No? As this number will be the point of contact "+mobno);

        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Toast.makeText(getApplicationContext(),"You clicked Yes button",Toast.LENGTH_LONG).show();
                JSONObject devicedetails = new JSONObject();

                try
                {

                    token = FirebaseInstanceId.getInstance().getToken();
                    //Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();


                    devicedetails.put(Constants.strClientIdKey,Constants.clientId);
                    devicedetails.put("number", in_mno);
                    devicedetails.put("devicetoken", token);

                    Log.d("Details", devicedetails.toString());

                    ConnectivityManager conm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo  = conm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                        if (devicedetails.length() > 0)
                            new UserRegAsync().execute(String.valueOf(devicedetails));
                    }
                    else {
                        //Toast.makeText(getApplicationContext(), "There is no active network connection!", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.no_internet_con);
                        tv_error.setVisibility(View.VISIBLE);
                    }

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
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

    private class UserRegAsync extends AsyncTask<String,Void,Integer>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                pg.setMessage("Registration in Process Please Wait!");
                pg.setCancelable(false);
                pg.show();

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }



        @Override
        protected Integer doInBackground(String... params){

        int jsonResponse=0;
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
        urlConnection.setConnectTimeout(3000);
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

        //System.out.println("Response"+jsonResponse);
       // response_server = jsonResponse;

        return jsonResponse;

    }catch (Exception e)
        {   pg.dismiss();
            //Toast.makeText(getApplicationContext(), "Unsuccessful. Please Try Again!", Toast.LENGTH_SHORT).show();
            //Log.d("Resuable Code",e.toString());
        }finally {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                //pg.dismiss();
                //Toast.makeText(getApplicationContext(), "Unsuccessful. Please Try Again!", Toast.LENGTH_SHORT).show();
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
                //Log.d("response",s.toString());
                pg.dismiss();
                int res = s;
                if(res == 1) {
                    SharedPreferences firstRun = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                    SharedPreferences.Editor editor = firstRun.edit();
                    editor.putBoolean("fr", true);
                    editor.putString("mobileno", in_mno);

                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                    Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(userActivity);
                    finish();
                } else{
                    Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                    tv_error.setText("Number Already Registered!");
                    tv_error.setVisibility(View.VISIBLE);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

