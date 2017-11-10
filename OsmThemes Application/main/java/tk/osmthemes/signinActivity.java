/*
package tk.osmthemes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.Timer;
import java.util.TimerTask;

public class signinActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    SignInButton signinButton;
    GoogleApiClient mgoogleapiclient;
    GoogleSignInOptions gso;
    private int RC_SIGN_IN = 100;
    String name,email,token ,profile;
    Timer reload;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinactivity);
        signinButton = (SignInButton)findViewById(R.id.btn_sign_in);
        reload = new Timer();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mgoogleapiclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this */
/* FragmentActivity *//*
, this */
/* OnConnectionFailedListener *//*
)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signinButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            case R.id.btn_sign_in:
                signin();
                break;
        }
    }

    private void signin()
    {

        Intent signinIntent = Auth.GoogleSignInApi.getSignInIntent(mgoogleapiclient);
        startActivityForResult(signinIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlesigninResult(result);
        }
    }

    private void handlesigninResult(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {

            GoogleSignInAccount acct = result.getSignInAccount();
            JSONObject devicedetails = new JSONObject();
            try {

                name = acct.getDisplayName();
               //Toast.makeText(getApplicationContext(), "!" + name, Toast.LENGTH_SHORT).show();
                email = acct.getEmail();
                try {
                    profile = acct.getPhotoUrl().toString();
                   // Toast.makeText(getApplicationContext(), "qwe " + profile, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    profile = "http://themeosm.parodigi.com/ic_launcher.png";
                    e.printStackTrace();


                }

//                profile = acct.getPhotoUrl().toString();
//                Toast.makeText(getApplicationContext(), "qwe " + profile, Toast.LENGTH_SHORT).show();
                token = FirebaseInstanceId.getInstance().getToken();

                devicedetails.put("Profile_name",name);
                devicedetails.put("Profile_email",email);
                devicedetails.put("Profile_token",token);

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



                }catch(Exception e)
                {
                    System.out.print("Exception In handlesigninResult "+e);
                }



        }
        else
        {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Toast.makeText(this, "Connection Failure", Toast.LENGTH_LONG).show();
    }


    private class SendDeviceDetails extends AsyncTask<String,String,String>
    {

        ProgressDialog process = new ProgressDialog(signinActivity.this);
        @Override
        protected void onPreExecute() {
            process.setMessage("Loading Please Wait!");
            process.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String JsonResponse  = null;
            String JsonDATA = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try
            {
                URL url = new URL("http://themeosm.parodigi.com/user_details.php");
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
                //process.hide();
                //send to post execute
                return JsonResponse;
            }catch (IOException e)
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
            if (process.isShowing()) {
                process.dismiss();
            }

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
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("Profile_name", name).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("Profile_email", email).apply();
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("Profile_photo", profile).apply();

                    //mainactivity.putExtra("Profile_name",name);
                    //mainactivity.putExtra("Profile_email",email);
                    //mainactivity.putExtra("Profile_photo",profile);
                    reload.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent mainactivity = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(mainactivity);
                            finish();
                        }
                    },4000);


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

*/
