package rj.engineerbkinfotech;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;

import rj.engineerbkinfotech.AsyncTasks.ComplaintsAsync;
import rj.engineerbkinfotech.AsyncTasks.LogInOutAsync;
import rj.engineerbkinfotech.Constants.Constants;
import rj.engineerbkinfotech.CustomDialogEngineer.CustomDialogBoxEngineer;

/**
 * Created by jimeet29 on 26-12-2017.
 */

public class EngineerActivity extends AppCompatActivity implements TaskCompleted, View.OnClickListener {
    // 1 get complaints ,2 for close , 3 for partial close
    ProgressDialog pg;
    ListAdapter adapter;
    ListView listView;
    JSONObject engineer_complaint, send_details, logOut_details;
    JSONArray all_complaints;
    static JSONArray STATIC_ALL_COMPLAINTS;
    ArrayList<ComplaintModel> complaintModel;
    TextView tv_error, tv_privacy_policy;
    static int RESULT;
    SharedPreferences sp;
    public static final int CALL_BACK_CODE = 1;
    Intent locationService;
    private GPS GPSObj;
    Context ctx;
    LocationManager locationManager;
    String titleText;

    public static Activity fa;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = getApplicationContext();
        fa = this;
        sp = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
        String engineerUName = sp.getString("username", null);
        engineerUName = engineerUName.substring(0, 1).toUpperCase() + engineerUName.substring(1);
        titleText = "Welcome " + engineerUName;
        setTitle("Welcome " + engineerUName);

        setContentView(R.layout.list_new_complaint_activity);

        initialize();

        setCustomActionBar();

        //Todo for next Update
      /*  if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            GPSObj.showSettingsAlert();
        }else {

            locationService = new Intent(getCtx(), GPSObj.getClass());
            if (!isMyServiceRunning(GPSObj.getClass())) {
                startService(locationService);
                //Toast.makeText(getApplicationContext(), "STTART", Toast.LENGTH_LONG).show();
            }
        }*/


        if (savedInstanceState == null) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                send_details = new JSONObject();
                try {
                    //SharedPreferences sf = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                    String uname = sp.getString(Constants.strUserNameKey, null);
                    String code = "1";
                    send_details.put(Constants.strClientIdKey, Constants.clientId);
                    send_details.put(Constants.strUserNameKey, uname);
                    send_details.put(Constants.strCodeKey, code);
                    Log.d("Engineer Detail", send_details.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new ComplaintsAsync(EngineerActivity.this).execute(send_details.toString(), Constants.getEngineerComplaintsEP);
            } else {
                //Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_SHORT).show();
                tv_error.setText(R.string.no_network);
                tv_error.setVisibility(View.VISIBLE);
            }
        }


    }

    private void initialize() {
        //todo next update
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //GPSObj = new GPS(EngineerActivity.this);
        pg = new ProgressDialog(EngineerActivity.this);
        tv_error = findViewById(R.id.empty);
        tv_privacy_policy = findViewById(R.id.tv_id_privacy_policy);
        complaintModel = new ArrayList<>();
        listView = findViewById(R.id.lv_id_complaints_list);
        tv_privacy_policy.setOnClickListener(this);
        ReusableCodeAdmin.createNotificationChannel(EngineerActivity.this);
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(titleText);
        tv_custom_action_bar_title.setTextSize(23.0f);
        tv_custom_action_bar_title.setGravity(Gravity.START);
        ImageView iv_info = actionBar.getCustomView().findViewById(R.id.iv_id_info);
        ImageView iv_log_out = actionBar.getCustomView().findViewById(R.id.iv_id_log_out);
        iv_info.setOnClickListener(this);
        iv_log_out.setOnClickListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void checkIfMiUiDevice() {
        if (ReusableCodeAdmin.isOtherDevice()) {
            SharedPreferences sp = getSharedPreferences
                    (Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
            if (!sp.getBoolean(Constants.sharedPreferencesDontShowAutoStartPermissionDialog, false)) {
                FragmentManager ft = getSupportFragmentManager();
                DialogFragment dialogFragment = new CustomDialogBoxEngineer();
                dialogFragment.show(ft, "autoStartDialog");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_id_info:
                Intent ticket_info_activity = new Intent(getApplicationContext(), TicketInfo.class);
                startActivity(ticket_info_activity);
                break;
            case R.id.iv_id_log_out:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EngineerActivity.this);
                alertBuilder.setMessage(R.string.log_out_text);
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut_details = new JSONObject();
                        SharedPreferences sf = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);

                        try {
                            logOut_details.put(Constants.strUserNameKey, sf.getString(Constants.strUserNameKey, null));
                            String code = "2";
                            logOut_details.put(Constants.strClientIdKey, Constants.clientId);
                            logOut_details.put(Constants.strCodeKey, code);
                            Log.d("Engineer Details", logOut_details.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                            //if(isInternetActive()) {
                            // Log.d("Inside if","true");
                            if (logOut_details.length() > 0) {
                                new LogInOutAsync(EngineerActivity.this).execute(logOut_details.toString());
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                            }
                            // }
                        }
                    }
                });
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                AlertDialog logOutAlert = alertBuilder.create();
                logOutAlert.show();
                break;

            case R.id.tv_id_privacy_policy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://bkinfotech.in/privacy_policy_engg.html"));
                startActivity(browserIntent);
                break;
        }
    }

    public boolean isInternetActive() {
        Log.d("Inside isInternetActive", "true");
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            //You can replace it with your name
            Log.d("Inside isInternetActive", String.valueOf(ipAddr));
            return !ipAddr.equals("");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onTaskComplete(String result) {
        if (result.equals("1") || result.equals("0")) {
            RESULT = Integer.parseInt(result);

            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Constants.sharedPreferencesFirstRun, false);
            editor.apply();

            Intent engineerActivity = new Intent(getApplicationContext(), EngineerLogin.class);
            startActivity(engineerActivity);
            EngineerActivity.this.finish();
        } else {

            Log.d("NewComplaint", result);
            try {
                all_complaints = new JSONArray(result);
                if (all_complaints.length() == 0) {
                    tv_error.setText(R.string.no_complaints);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    STATIC_ALL_COMPLAINTS = new JSONArray(result);
                    Log.d("NewComplaint", String.valueOf(STATIC_ALL_COMPLAINTS));
                    for (int i = 0; i < all_complaints.length(); i++) {
                        engineer_complaint = (JSONObject) all_complaints.get(i);
                        String ticket_id = engineer_complaint.getString(Constants.KEY_TICKET_ID);
                        String name = engineer_complaint.getString(Constants.KEY_NAME);
                        String company_name = engineer_complaint.getString(Constants.KEY_COMPANY_NAME);
                        String user_type = engineer_complaint.getString(Constants.KEY_USER_TYPE);
                        String problemtype = engineer_complaint.getString(Constants.KEY_PROBLEM_TYPE);
                        String description = engineer_complaint.getString(Constants.KEY_DESCRIPTION);
                        String address = engineer_complaint.getString(Constants.KEY_ADDRESS);

                        //String engineerstatus = engineer_complaint.getString("engineersidestatus");
                        String complaint_reg_time = engineer_complaint.getString(Constants.KEY_COMPLAINT_REG_TIME);
                        String complaint_reg_date = engineer_complaint.getString(Constants.KEY_COMPLAINT_REG_DATE);
                        String allotted_date = engineer_complaint.getString(Constants.KEY_ALLOTTED_DATE);
                        String allotted_slot = engineer_complaint.getString(Constants.KEY_ALLOTTED_SLOT);
                        String ticketstatus = engineer_complaint.getString(Constants.KEY_TICKET_STATUS);
                        String raisedagain = engineer_complaint.getString(Constants.KEY_RAISED_AGAIN);
                        complaintModel.add(new ComplaintModel(ticket_id, name, company_name, user_type, problemtype,
                                description, address, complaint_reg_time, complaint_reg_date, allotted_date,
                                allotted_slot, ticketstatus, raisedagain));
                        adapter = new CustomAdaptorNewComplaint(complaintModel, getApplicationContext());

                        listView.setAdapter(adapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        checkIfMiUiDevice();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            Log.d("CallBack", "inside call back");
            if (resultCode == RESULT_OK) {

                //EngineerActivity.this.finish();
                Log.d("CallBack", "done");
            } else {
                //TODO uncomment in next update
                //GPSObj.showSettingsAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

