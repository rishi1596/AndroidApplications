package rj.bkinfotech;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import rj.bkinfotech.AsyncTasks.UserRegistrationAsync;
import rj.bkinfotech.Constants.Constants;

public class MainActivity extends AppCompatActivity implements TaskCompleted {

    private EditText et_mobile_no;
    private String in_mno, token;
    private Button btn_register;
    private TextView tv_already, tv_error;
    private String MobilePattern = "^[789][0-9]{9}$";
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCustomActionBar();

        et_mobile_no = (EditText) findViewById(R.id.et_mobileno);
        btn_register = (Button) findViewById(R.id.btn_register);
        tv_already = (TextView) findViewById(R.id.tv_already);
        tv_error = (TextView) findViewById(R.id.tv_error);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_mno = et_mobile_no.getText().toString().trim();
                if (in_mno.isEmpty() || !in_mno.matches(MobilePattern)) {
                    tv_error.setText(R.string.validation_mobile_no);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    confirmationdialog(in_mno);
                }
            }
        });

        tv_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetActivity = new Intent(getApplicationContext(), ResetActivity.class);
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
        ImageView iv_info = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void confirmationdialog(final String mobno) {

        final AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
        alertbox.setMessage(getResources().getString(R.string.no_registration_confirmation) + " " + mobno);
        alertbox.setPositiveButton(Constants.strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"You clicked Yes button",Toast.LENGTH_LONG).show();
                JSONObject devicedetails = new JSONObject();

                try {

                    token = FirebaseInstanceId.getInstance().getToken();
                    //Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();


                    devicedetails.put(Constants.strClientIdKey, Constants.clientId);
                    devicedetails.put("number", mobno);
                    devicedetails.put("devicetoken", token);

                    Log.d("Details", devicedetails.toString());

                    ConnectivityManager conm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = conm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                        if (devicedetails.length() > 0)
                            new UserRegistrationAsync(MainActivity.this)
                                    .execute(String.valueOf(devicedetails), Constants.registerNewUserEP);
                    } else {
                        tv_error.setText(R.string.no_internet_con);
                        tv_error.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        alertbox.setNegativeButton(Constants.strNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertbox.create();
        alertDialog.show();
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            JSONObject responseObj = new JSONObject(result);
            if (responseObj.has(Constants.keyErrorCode)) {
                String error_code = responseObj.optString(Constants.keyErrorCode);
                switch (error_code) {
                    case Constants.ERROR_CODE_106:
                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.tv_no_already_reg_msg);
                        tv_error.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.tv_something_failed_pta);
                        tv_error.setVisibility(View.VISIBLE);
                        break;
                }
                Log.e(TAG, error_code);
            } else if (responseObj.has(Constants.keyResponse) &&
                    responseObj.optString(Constants.keyResponse)
                            .equalsIgnoreCase(Constants.successResponse)) {
                SharedPreferences firstRun = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                SharedPreferences.Editor editor = firstRun.edit();
                editor.putBoolean(Constants.sharedPreferencesFirstRun, true);
                editor.putString(Constants.sharedPreferencesMobileNo, in_mno);

                editor.apply();

                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(userActivity);
                finish();
            } else {
                Log.e(TAG, result);
            }
        } catch (Exception e) {
            Log.w(TAG + "onTaskComplete", e.toString());
        }
    }

    @Override
    public void onTaskComplete(String[] result) {

    }
}

