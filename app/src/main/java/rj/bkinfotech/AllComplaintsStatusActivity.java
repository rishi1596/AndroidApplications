package rj.bkinfotech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rj.bkinfotech.AsyncTasks.ComplaintsAsync;
import rj.bkinfotech.Constants.Constants;

/**
 * Created by jimeet29 on 07-01-2018.
 */

public class AllComplaintsStatusActivity extends AppCompatActivity implements TaskCompleted, View.OnClickListener {


    ListAdapter adapter;
    ListView listView;
    JSONObject engineer_complaint, send_details;
    JSONArray all_complaints;
    static JSONArray STATIC_ALL_COMPLAINTS_ACTIVITY;
    static String STATIC_ALL_COMPLAINTS_STRING;
    ArrayList<ComplaintModel> complaintModel;

    TextView tv_error;
    LinearLayout oc;
    TextView tv_open, tv_close;
    int open_close = 0; //0 open 1 close
    ColorStateList cl_open, cl_closed;
    static Activity allcomplaints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setCustomActionBar();

        allcomplaints = this;
        String ui = getIntent().getStringExtra("UserInterface");
        setContentView(R.layout.list_in_process_complaint_activity);
        complaintModel = new ArrayList<>();
        listView = findViewById(R.id.lv_id_complaints_list);

        if (ui.equals("1")) {
            oc = findViewById(R.id.ll_id_openclose);
            oc.setVisibility(View.VISIBLE);
            tv_open = findViewById(R.id.tv_id_open);
            tv_close = findViewById(R.id.tv_id_closed);
            tv_open.setOnClickListener(this);
            tv_close.setOnClickListener(this);
            cl_open = tv_open.getTextColors();
            cl_closed = tv_close.getTextColors();
        }


        tv_error = findViewById(R.id.empty);
        if (savedInstanceState == null) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                send_details = new JSONObject();
                try {
                    SharedPreferences sf = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                    String rg_no = sf.getString(Constants.sharedPreferencesMobileNo, null);
                    String code = "3";
                    send_details.put(Constants.strClientIdKey, Constants.clientId);
                    send_details.put(Constants.strRegisteredNoKey, rg_no);
                    send_details.put(Constants.strCodeKey, code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new ComplaintsAsync(this).execute(send_details.toString());
            } else {
                tv_error.setText(R.string.no_network);
                tv_error.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_SHORT).show();


            }
        }
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        ImageView iv_info = actionBar.getCustomView().findViewById(R.id.iv_id_info);
        tv_custom_action_bar_title.setText(R.string.all_tickets);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        iv_info.setOnClickListener(this);
    }

 /*   public void updateComplaintStatus_all(String data) {
        new ComplaintsAsync(this).execute(data);
    }*/

    @Override
    public void onTaskComplete(String result) {
        open_or_close(result);
    }

    @Override
    public void onTaskComplete(String[] result) {

    }

    private void open_or_close(String complaints) {

        try {
            Log.d("NewComplaint", complaints);
            if (complaints.equals("1") || complaints.equals("0")) {

                AlertDialog alertDialog;
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Ticket successfully raised again");

                alertDialog = alert.create();
                alertDialog.setCancelable(false);
                alertDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), AllComplaintsStatusActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.IE_KEY_USER_INTERFACE, Constants.USER_INTERFACE_VALUE_ONE);
                        startActivity(intent);
                        allcomplaints.finish();
                    }
                }, 3000); //End
            } else {


                all_complaints = new JSONArray(complaints);
                if (all_complaints.length() == 0) {
                    tv_error.setText(R.string.no_complaints);
                    tv_error.setVisibility(View.VISIBLE);
                }
                STATIC_ALL_COMPLAINTS_STRING = complaints;
                STATIC_ALL_COMPLAINTS_ACTIVITY = new JSONArray(complaints);

                // Log.d("NewComplaint", String.valueOf(STATIC_ALL_COMPLAINTS));
                for (int i = 0; i < all_complaints.length(); i++) {
                    engineer_complaint = (JSONObject) all_complaints.get(i);
                    String ticket_id = engineer_complaint.getString(Constants.KEY_TICKET_ID);
                    //String name = engineer_complaint.getString("name");
                    //String company_name = engineer_complaint.getString("companyname");
                    String user_type = engineer_complaint.getString(Constants.KEY_USER_TYPE);
                    String problemtype = engineer_complaint.getString(Constants.KEY_PROBLEM_TYPE);
                    String description = engineer_complaint.getString(Constants.KEY_DESCRIPTION);
                    String complaint_reg_time = engineer_complaint.getString(Constants.KEY_COMPLAINT_REG_TIME);
                    String complaint_reg_date = engineer_complaint.getString(Constants.KEY_COMPLAINT_REG_DATE);
                    String raisedagain = engineer_complaint.getString(Constants.KEY_RAISED_AGAIN);
                    String allotted_date = engineer_complaint.getString(Constants.KEY_ALLOTTED_DATE);
                    String allotted_slot = engineer_complaint.getString(Constants.KEY_ALLOTTED_SLOT);
                    String engineer_appointed = engineer_complaint.getString(Constants.KEY_ENGINEER_APPOINTED);
                    String engineer_appointed_time = engineer_complaint.getString(Constants.KEY_ENGINEER_APPOINTED_TIME);
                    String engineer_appointed_date = engineer_complaint.getString(Constants.KEY_ENGINEER_APPOINTED_DATE);
                    String ticketstatus = engineer_complaint.getString(Constants.KEY_TICKET_STATUS);
                    String requested_date = engineer_complaint.getString(Constants.KEY_REQUESTED_DATE);
                    String requested_slot = engineer_complaint.getString(Constants.KEY_REQUESTED_SLOT);
                    String engineer_close_time = engineer_complaint.getString(Constants.KEY_ENGINEER_CLOSE_TIME);
                    String engineer_close_date = engineer_complaint.getString(Constants.KEY_ENGINEER_CLOSE_DATE);
                    //String address = engineer_complaint.getString("address");

                    if (open_close == 0) { //open
                        if (!ticketstatus.equals(Constants.TICKET_CLOSED)) {
                            complaintModel.add(new ComplaintModel(ticket_id, user_type, problemtype, description,
                                    ticketstatus, engineer_appointed, complaint_reg_time, complaint_reg_date, raisedagain,
                                    allotted_date, allotted_slot, engineer_appointed_time, engineer_appointed_date,
                                    requested_date, requested_slot, engineer_close_time, engineer_close_date));

                        }
                    } else { //closed
                        if (ticketstatus.equals(Constants.TICKET_CLOSED)) {
                            complaintModel.add(new ComplaintModel(ticket_id, user_type, problemtype,
                                    description, ticketstatus, engineer_appointed, complaint_reg_time, complaint_reg_date,
                                    raisedagain, allotted_date, allotted_slot, engineer_appointed_time, engineer_appointed_date,
                                    requested_date, requested_slot, engineer_close_time, engineer_close_date));
                        }
                    }
                    if (complaintModel.size() != 0) {
                        tv_error.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        adapter = new CustomAdaptorNewComplaint(complaintModel, this, 1);
                        listView.setAdapter(adapter);
                    } else {
                        listView.setVisibility(View.GONE);
                        tv_error.setText(R.string.no_complaints);
                        tv_error.setVisibility(View.VISIBLE);
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.tv_id_open:
                    if (open_close != 0) {
                        //Toast.makeText(getApplicationContext(),"Open",Toast.LENGTH_SHORT).show();
                        tv_open.setTextColor(cl_open);
                        tv_close.setTextColor(cl_closed);
                        complaintModel.clear();
                        open_close = 0;
                        open_or_close(STATIC_ALL_COMPLAINTS_STRING);
                    }
                    break;
                case R.id.tv_id_closed:

                    if (open_close != 1) {
                        //Toast.makeText(getApplicationContext(),"Closed",Toast.LENGTH_SHORT).show();
                        tv_open.setTextColor(cl_closed);
                        tv_close.setTextColor(cl_open);
                        complaintModel.clear();
                        open_close = 1;
                        open_or_close(STATIC_ALL_COMPLAINTS_STRING);
                    }
                    break;

                case R.id.iv_id_info:
                    Intent ticket_info_activity = new Intent(getApplicationContext(), TicketInfo.class);
                    startActivity(ticket_info_activity);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

