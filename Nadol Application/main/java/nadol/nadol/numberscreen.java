package nadol.nadol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

import java.net.URL;



/**
 * Created by jimeet29 on 08-04-2017.
 */

public class numberscreen extends Activity
{
    EditText mobileno;
    String tmobileno,token;

    String MobilePattern = "^[789][0-9]{9}$";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numberscreen);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mobileno = (EditText)findViewById(R.id.mobileno);

        mobileno.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {


            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {

              if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    tmobileno = mobileno.getText().toString().trim();
                        if(!tmobileno.matches(MobilePattern))
                        {
                            Toast.makeText(getApplicationContext(),"Please Enter a Valid Mobile No.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            confirmationdialog(tmobileno);
                        }
                }
                return false;
            }
        });

    }

    private void confirmationdialog(String mobno)
    {

        final AlertDialog.Builder alertbox = new AlertDialog.Builder(numberscreen.this);
        alertbox.setMessage("Are You Sure about Mobile No "+mobno);

        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getApplicationContext(),"You clicked Yes button",Toast.LENGTH_LONG).show();
                JSONObject devicedetails = new JSONObject();

                try
                {

                    token = FirebaseInstanceId.getInstance().getToken();
//                    System.out.println("Token length"+(token.length()));
                    System.out.println("TokenKEY" +token);
                    String msg = getString(R.string.msg_token_fmt, token);
                    System.out.println("TOKEN" + msg);
                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                    devicedetails.put("number", tmobileno);
                    devicedetails.put("devicetoken", token);
                    System.out.print("Device" + devicedetails);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                ConnectivityManager conm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo  = conm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (devicedetails.length() > 0) {

                        new SendDeviceDetails().execute(String.valueOf(devicedetails));
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "There is no active network connection!", Toast.LENGTH_SHORT).show();
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

    private class SendDeviceDetails extends AsyncTask<String, String, String>{

        ProgressDialog process = new ProgressDialog(nadol.nadol.numberscreen.this);
        @Override
        protected void onPreExecute() {
            process.setMessage("Loading Please Wait!");
            process.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            String JsonResponse = null;
            String JsonDATA = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://karishmaexim.com/npl/devicedetails.php");
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setConnectTimeout(3000);
                urlConnection.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();

                InputStream inputstream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if(inputstream == null)
                {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputstream));
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
                //process.hide();
                //send to post execute
                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
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
                    editor.commit();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("mobileno", tmobileno).commit();
                    Intent newsscreen = new Intent(getApplicationContext(), homescreen.class);
                    startActivity(newsscreen);
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
