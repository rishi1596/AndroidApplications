package rj.engineerbkinfotech;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import rj.engineerbkinfotech.AsyncTasks.LogInOutAsync;
import rj.engineerbkinfotech.Constants.Constants;

public class EngineerLogin extends AppCompatActivity implements TaskCompleted, View.OnClickListener {
    EditText et_username, et_password;
    TextView tv_error;
    Button btn_login;
    String in_username, in_password, UsernamePattern = "^[a-zA-Z0-9]+$", PasswordPattern = "^[a-zA-Z0-9@!$&]+$", firebase_token;
    JSONObject login_details;
    LocationManager locationManager;
    GPS gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engineer_login_activity);

        initialize();

        setCustomActionBar();

        btn_login.setOnClickListener(this);
    }

    private void initialize() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        et_username = findViewById(R.id.et_id_username);
        et_password = findViewById(R.id.et_id_password);
        tv_error = findViewById(R.id.tv_id_error);
        btn_login = findViewById(R.id.btn_id_submit);
        gps = new GPS(EngineerLogin.this);

        //ToDo Next Update
     /*   if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(EngineerLogin.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.READ_EXTERNAL_STORAGE},1331);
        }
        // check if GPS enabled
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            gps.showSettingsAlert();
           *//*double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
       *//*}*/
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(R.string.app_name);
        ImageView iv_info = actionBar.getCustomView().findViewById(R.id.iv_id_info);
        ImageView iv_log_out = actionBar.getCustomView().findViewById(R.id.iv_id_log_out);
        iv_info.setVisibility(View.GONE);
        iv_log_out.setVisibility(View.GONE);
        /*iv_info.setOnClickListener(this);
        iv_log_out.setOnClickListener(this);
        */
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void confirmationdialog() {

        final AlertDialog.Builder alertBox = new AlertDialog.Builder(EngineerLogin.this);
        alertBox.setMessage(R.string.credentials_dailog);
        alertBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                login_details = new JSONObject();
                try {

                    firebase_token = FirebaseInstanceId.getInstance().getToken();
                    login_details.put(Constants.strClientIdKey, Constants.clientId);
                    login_details.put(Constants.strUserNameKey, in_username);
                    login_details.put(Constants.strPasswordKey, in_password);
                    login_details.put(Constants.strTokenKey, firebase_token);
                    String code = "1";
                    login_details.put(Constants.strCodeKey, code);
                    Log.d("Login_details Engineer", login_details.toString());

                } catch (Exception e) {
                    Log.d("Login_details Engineer", e.toString());
                }
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (login_details.length() > 0) {
                        new LogInOutAsync(EngineerLogin.this).execute(login_details.toString());
                    } else {
                        tv_error.setText(R.string.no_internet);
                        tv_error.setVisibility(View.VISIBLE);
                    }

                }

            }
        });
        alertBox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertBox.create();
        alertDialog.show();
    }

    @Override
    public void onTaskComplete(String result) {
        //Log.d("engineerLogin", result);
        try {
            //JSONObject response_server = new JSONObject(s);
            if (result.equals("1")) {
                SharedPreferences sp = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(Constants.sharedPreferencesFirstRun, true);
                editor.putString(Constants.strUserNameKey, in_username);
                editor.apply();

                Intent engineerActivity = new Intent(getApplicationContext(), EngineerActivity.class);
                startActivity(engineerActivity);
                EngineerLogin.this.finish();
            } else {
                tv_error.setText(R.string.incorrect);
                tv_error.setVisibility(View.VISIBLE);
                // Toast.makeText(getApplicationContext(), "Credentials are incorrect", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("Engineer Login response", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_submit:
                in_username = et_username.getText().toString().trim();
                in_password = et_password.getText().toString().trim();

                if (!in_username.matches(UsernamePattern)) {
                    //Toast.makeText(getApplicationContext(), "Please Enter a Valid Username", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_username);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (!in_password.matches(PasswordPattern)) {
                    //Toast.makeText(getApplicationContext(), "Please Enter a Valid Password", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_password);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    confirmationdialog();
                }
                break;
            default:
                break;
        }
    }
}


