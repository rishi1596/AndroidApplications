package rj.adminbkinfotech1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
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

import rj.adminbkinfotech1.AsyncTasks.ComplaintsAsync;
import rj.adminbkinfotech1.Constants.Constants;

/**
 * Created by jimeet29 on 21-12-2017.
 */

public class AllComplaint extends AppCompatActivity implements TaskCompleted, View.OnClickListener {


    ListAdapter adapter;
    ListView listView;
    JSONObject engineer_complaint, send_details;
    JSONArray all_complaints;
    static JSONArray STATIC_ALL_COMPLAINTS_ACTIVITY;
    static String STATIC_ALL_COMPLAINTS_STRING;
    ArrayList<ComplaintModel> complaintModel;
    ReusableCodeAdmin rca;
    TextView tv_error;
    LinearLayout oc;
    TextView tv_open, tv_close;
    int open_close = 0; //0 open 1 close
    ColorStateList cl_open, cl_closed;
    static Activity allcomplaints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allcomplaints = this;
        String ui = getIntent().getStringExtra("UserInterface");
        setContentView(R.layout.list_new_complaint_activity);

        complaintModel = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_id_complaints_list);

        if (ui.equals("1")) {
            oc = (LinearLayout) findViewById(R.id.ll_id_openclose);
            oc.setVisibility(View.VISIBLE);
            tv_open = (TextView) findViewById(R.id.tv_id_open);
            tv_close = (TextView) findViewById(R.id.tv_id_closed);
            tv_open.setOnClickListener(this);
            tv_close.setOnClickListener(this);
            cl_open = tv_open.getTextColors();
            cl_closed = tv_close.getTextColors();
        }
        rca = new ReusableCodeAdmin();
        tv_error = (TextView) findViewById(R.id.empty);

        setCustomActionBar();

        if (savedInstanceState == null) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                send_details = new JSONObject();
                try {
                    SharedPreferences sf = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                    String username = sf.getString(Constants.sharedPreferencesUserNames, null);
                    send_details.put(Constants.strClientIdKey, Constants.clientId);
                    send_details.put(Constants.strUserNameKey, username);
                    send_details.put(Constants.strCodeKey, Constants.allComplaints);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //pb.setIndeterminate(true);


                new ComplaintsAsync(AllComplaint.this).execute(send_details.toString());
                //pb.setVisibility(View.GONE);
                // pg.dismiss();
                //Log.d("asdada",res.toString());
               /*NewComplaintActivityBinding binding = DataBindingUtil.setContentView(this,R.layout.new_complaint_activity);
               ComplaintModel cm = new ComplaintModel();
               binding.setComplaintmodel(cm);*/
            } else {
                tv_error.setText(R.string.no_network);
                tv_error.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_SHORT).show();
                //pb.setVisibility(View.GONE);

            }
        }
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(R.string.app_name);
        ImageView iv_info = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_id_info);
        //iv_info.setVisibility(View.GONE);
        iv_info.setOnClickListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

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
                String ticket_id = engineer_complaint.getString("ticket_id");
                String name = engineer_complaint.getString("name");
                String companyname = engineer_complaint.getString("companyname");
                //String usertype=individual_complaint.getString("usertype");
                //String problemtype=individual_complaint.getString("problemtype");
                String number = engineer_complaint.getString("registeredno");
                String description = engineer_complaint.getString("description");
                String complaint_reg_time = engineer_complaint.getString("complaint_reg_time");
                String complaint_reg_date = engineer_complaint.getString("complaint_reg_date");
                String raisedagain = engineer_complaint.getString("raisedagain");
                String allotted_date = engineer_complaint.getString("allotted_date");
                String allotted_slot = engineer_complaint.getString("allotted_slot");
                String engineerappointed = engineer_complaint.getString("engineerappointed");
                //String engineer_appointed_time=individual_complaint.getString("engineer_appointed_time");
                //String engineer_appointed_date=individual_complaint.getString("engineer_appointed_date");
                //String appointedbyadmin=individual_complaint.getString("appointedbyadmin");
                String ticketstatus = engineer_complaint.getString("ticketstatus");
                String requested_date = engineer_complaint.getString("requested_date");
                String requested_slot = engineer_complaint.getString("requested_slot");
                //String engineer_close_time=individual_complaint.getString("engineer_close_time");
                //String engineer_close_date=individual_complaint.getString("engineer_close_date");
                //String ticket_close_time=individual_complaint.getString("ticket_close_time");
                //String ticket_close_date=individual_complaint.getString("ticket_close_date");
                String solution = engineer_complaint.getString("solution");
                String previous_engineer = engineer_complaint.getString("previous_engineer");
                String previous_solution = engineer_complaint.getString("previous_solution");

                if (open_close == 0) { //open
                    if (!ticketstatus.equals("Closed")) {

                        complaintModel.add(new ComplaintModel(ticket_id, name, companyname, number, description, complaint_reg_time, complaint_reg_date,
                                raisedagain, allotted_date, allotted_slot, engineerappointed, ticketstatus,
                                requested_date, requested_slot, solution, previous_engineer, previous_solution));

                    }
                } else { //closed
                    if (ticketstatus.equals("Closed")) {
                        complaintModel.add(new ComplaintModel(ticket_id, name, companyname, number, description, complaint_reg_time, complaint_reg_date,
                                raisedagain, allotted_date, allotted_slot, engineerappointed, ticketstatus,
                                requested_date, requested_slot, solution, previous_engineer, previous_solution));

                    }
                }
                if (complaintModel.size() != 0) {
                    tv_error.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    adapter = new CustomAdaptorNewComplaint(complaintModel, getApplicationContext());
                    listView.setAdapter(adapter);
                } else {
                    listView.setVisibility(View.GONE);
                    tv_error.setText(R.string.no_complaints);
                    tv_error.setVisibility(View.VISIBLE);
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
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
