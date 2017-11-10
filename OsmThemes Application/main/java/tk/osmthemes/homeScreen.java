package tk.osmthemes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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

import tk.osmthemes.R;

/**
 * Created by jimeet29 on 20-09-2017.
 */

public class homeScreen extends AppCompatActivity {
    String instance;
    JSONObject details;
    SharedPreferences settings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        details = new JSONObject();
        settings = getSharedPreferences("prefs",0);
        final boolean firstrun = settings.getBoolean("firstRun",false);
        //System.out.print("Heyyyyyy111");
        if(!firstrun) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    instance = FirebaseInstanceId.getInstance().getToken();
                    Toast.makeText(getApplicationContext(),instance,Toast.LENGTH_SHORT).show();
                    System.out.println("Token"+instance);
                    //System.out.println("Token"+hoja);
                    TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String mPhoneLocation = String.valueOf(tMgr.getCellLocation());
                    System.out.println("Location"+mPhoneLocation);
                    try {
                        details.put("token",instance);
                        details.put("phonelocation", mPhoneLocation);
                        System.out.println("Detailssss"+details);
                    } catch (Exception e) {
                        System.out.println("In homescreen.java Inside Catch");
                        e.printStackTrace();
                    }



                }
            }, 2000);
            //System.out.print("Heyyyyyy");
           /* MyFirebaseInstanceIDService mf = new MyFirebaseInstanceIDService();
            mf.setToken();
            instance = mf.gettoken();*/



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                     if(networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected())
                     {
                            if (details.length() > 0) {

                                new SendDeviceDetails().execute(String.valueOf(details));
                            }
                    }

            }}, 4000);
        }
        else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainscreen = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(mainscreen);
                            finish();
                        }
                    },3000);


        }
    }


    private class SendDeviceDetails extends AsyncTask<String,String,String>
    {

        ProgressDialog process = new ProgressDialog(homeScreen.this);
        @Override
        protected void onPreExecute() {
            process.setMessage("Loading Please Wait!");
            process.show();
            process.setCancelable(false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse  = null;
            String JsonDATA = params[0];
            System.out.println("BACKGROUD TASK");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
           // Toast.makeText(homeScreen.this, "tk", Toast.LENGTH_SHORT).show();
            try
            {
                System.out.println("Device Details"+JsonDATA);
                URL url = new URL("http://themeosm.parodigi.com/user_details1.php");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setConnectTimeout(3000);
                urlConnection.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8"));
                writer.write(JsonDATA);
                writer.close();


                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    builder.append(inputLine + "\n");
                if (builder.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = builder.toString().trim();
                //response data
                System.out.println("Response"+JsonResponse);
               // Toast.makeText(homeScreen.this, "Response"+JsonResponse, Toast.LENGTH_SHORT).show();
                //process.hide();
                //send to post execute
                return JsonResponse;
            }catch (Exception e)
            {
                System.out.print("Exception in asynctask"+e);
                e.printStackTrace();
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            process.dismiss();
            super.onPostExecute(s);
            try
            {
                if (s.equals("0"))
                {
                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                    SharedPreferences settings = getSharedPreferences("prefs",0);
                    SharedPreferences.Editor editor=settings.edit();
                    editor.putBoolean("firstRun",true);
                    editor.apply();

                    Intent mainscreen = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(mainscreen);
                    finish();



                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }


        }
    }
}
