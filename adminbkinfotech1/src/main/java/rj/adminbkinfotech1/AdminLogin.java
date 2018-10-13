package rj.adminbkinfotech1;

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

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import rj.adminbkinfotech1.AsyncTasks.LogInOutAsync;
import rj.adminbkinfotech1.Constants.Constants;

public class AdminLogin extends AppCompatActivity implements TaskCompleted, View.OnClickListener {
    EditText et_username, et_password;
    TextView tv_error;
    Button btn_login;
    String in_username, in_password, UsernamePattern = "^[a-zA-Z0-9]+$", PasswordPattern = "^[a-zA-Z0-9@!$&]+$", firebase_token;
    JSONObject login_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login_activity);


        et_username = (EditText) findViewById(R.id.et_id_username);
        et_password = (EditText) findViewById(R.id.et_id_password);
        tv_error = (TextView) findViewById(R.id.tv_id_error);
        btn_login = (Button) findViewById(R.id.btn_id_submit);

        setCustomActionBar();

        btn_login.setOnClickListener(this);
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(R.string.app_name);
        ImageView iv_info = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);
        //iv_info.setOnClickListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void confirmationdialog() {

        final AlertDialog.Builder alertBox = new AlertDialog.Builder(AdminLogin.this);
        alertBox.setMessage("Are the credentails correct?");
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
                    login_details.put(Constants.strCodeKey, Constants.strLogin);
                    Log.d("Login_details Addmin", login_details.toString());

                } catch (Exception e) {
                    Log.d("Login_details Addmin", e.toString());
                }
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (login_details.length() > 0) {
                        new LogInOutAsync(AdminLogin.this).execute(login_details.toString());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_submit:
                in_username = et_username.getText().toString().trim();
                in_password = et_password.getText().toString().trim();

                if (!in_username.matches(UsernamePattern)) {
                    tv_error.setText(R.string.validation_username);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (!in_password.matches(PasswordPattern)) {
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

    @Override
    public void onTaskComplete(String result) {
        Log.d("AdminLogin", result);
        try {
            if (result.equals("1")) {
                SharedPreferences sp = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("fr", true);
                editor.putString("username", in_username);
                editor.apply();

                Intent adminActivity = new Intent(getApplicationContext(), AdminActivity.class);
                adminActivity.putExtra("AdminUsername", in_username);
                startActivity(adminActivity);
                finish();
            } else {
                tv_error.setText(R.string.incorrect);
                tv_error.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.d("Admin Login response", e.toString());
        }
    }

    @Override
    public void onTaskComplete(String[] result) {

    }


}

