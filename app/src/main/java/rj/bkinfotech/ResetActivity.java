package rj.bkinfotech;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import rj.bkinfotech.AsyncTasks.ResetUserDetailsAsync;
import rj.bkinfotech.Constants.Constants;

/**
 * Created by jimeet29 on 30-11-2017.
 */

public class ResetActivity extends AppCompatActivity implements TaskCompleted {
    EditText et_mobile_no, et_email, et_otp;
    public String in_mno, token, in_email, in_otp, url = null;
    Button btn_reset;
    TextView tv_error;
    int count = 1;
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

        initialize();

        setListerners();
    }

    private void initialize() {
        et_mobile_no = findViewById(R.id.et_mobileno);
        et_otp = findViewById(R.id.et_otp);
        btn_reset = findViewById(R.id.btn_reset);
        tv_error = findViewById(R.id.tv_error);

        devicedetails = new JSONObject();
    }

    private void setListerners() {
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_mno = et_mobile_no.getText().toString().trim();
                //Toast.makeText(getApplicationContext(), in_mno, Toast.LENGTH_SHORT).show();
                if (in_mno.isEmpty() || !in_mno.matches(MobilePattern)) {
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
        TextView tv_custom_action_bar_title = actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(R.string.reset);
        ImageView iv_info = actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    private void confirmationdialog(String mobno) {

        final AlertDialog.Builder alertbox = new AlertDialog.Builder(ResetActivity.this);
        if (count == 1) {
            alertbox.setMessage(getResources().getString(R.string.reset_activity_mobile_no_confirmation) + " " + mobno);
        } else {
            alertbox.setMessage(R.string.opt_confirmation);
        }
        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    conm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    networkInfo = conm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {

                        if (count == 1) {
                            url = Constants.resetUserDetailsEP;
                            devicedetails.put(Constants.strClientIdKey, Constants.clientId);
                            devicedetails.put("number", in_mno);

                        } else {
                            token = FirebaseInstanceId.getInstance().getToken();
                            devicedetails.put(Constants.strClientIdKey, Constants.clientId);
                            devicedetails.put("number", in_mno);
                            devicedetails.put("devicetoken", token);
                            devicedetails.put("otp", in_otp);
                            url = Constants.otpValidateUserEP;
                        }

                        if (devicedetails.length() > 0) {
                            new ResetUserDetailsAsync(ResetActivity.this).execute(String.valueOf(devicedetails), url);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "There is no active network connection!", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.no_network);
                        tv_error.setVisibility(View.VISIBLE);
                    }
                    System.out.print("Device Details In RESET ACTIVITY" + devicedetails);
                    Log.d("Details", devicedetails.toString());

                } catch (Exception e) {
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

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"PAUSED",Toast.LENGTH_SHORT).show();
        if (count == 2) {
            reset_prefs = getSharedPreferences(Constants.sharedPreferencesFileNameReset, Constants.sharedPreferencesAccessMode);
            SharedPreferences.Editor ed = reset_prefs.edit();
            ed.putInt(Constants.sharedPreferencesResetActivityCount, count);
            ed.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),"Resumed",Toast.LENGTH_SHORT).show();
        reset_prefs = getSharedPreferences(Constants.sharedPreferencesFileNameReset, Constants.sharedPreferencesAccessMode);
        int localCount = reset_prefs.getInt(Constants.sharedPreferencesResetActivityCount, 0);
        if (localCount == 2) {
            SharedPreferences.Editor ed = reset_prefs.edit();
            ed.putInt(Constants.sharedPreferencesResetActivityCount, 0);
            ed.apply();
            otpValidateUI();
        }
    }

    private void otpValidateUI() {
        et_otp.setVisibility(View.VISIBLE);
        tv_error.setVisibility(View.GONE);
        btn_reset.setText(R.string.validate_otp);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_otp = et_otp.getText().toString().trim();

                if (in_otp.isEmpty() || !in_otp.matches(OTPPattern)) {
                    //Toast.makeText(getApplicationContext(), "Please Enter a Valid OTP", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_otp);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    confirmationdialog(in_mno);
                }
            }
        });
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            int res = Integer.parseInt(result);
            if (res == 1) {
                Log.d("Reset count before", String.valueOf(count));
                count += 1;
                Log.d("Reset After", String.valueOf(count));
                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                if (count == 2) {
                    otpValidateUI();
                } else {
                    SharedPreferences firstRun = getSharedPreferences(Constants.sharedPreferencesFileNameSettings,
                            Constants.sharedPreferencesAccessMode);
                    SharedPreferences.Editor editor = firstRun.edit();
                    editor.putBoolean(Constants.sharedPreferencesFirstRun, true);
                    editor.putString(Constants.sharedPreferencesMobileNo, in_mno);
                    editor.apply();

                    Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(userActivity);
                    //MainActivity.mainActivity.finish();
                    ResetActivity.this.finish();
                    //((Activity)getApplicationContext()).finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                tv_error.setText("Please Try Again!");
                tv_error.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.w("ResetActiity Async", e.toString());
        }
    }

    @Override
    public void onTaskComplete(String[] result) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



