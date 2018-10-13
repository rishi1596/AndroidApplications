package rj.adminbkinfotech1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

public class NewComplaint extends AppCompatActivity implements TaskCompleted, View.OnClickListener {
    // 1 for new complaints ,2 for specific , 3 for appointed
    ListAdapter adapter;
    ListView listView;
    JSONObject individual_complaint, send_details;
    JSONArray all_complaints;
    ArrayList<ComplaintModel> complaintModel;
    TextView tv_error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_new_complaint_activity);
        tv_error = (TextView) findViewById(R.id.empty);
        complaintModel = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_id_complaints_list);

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
                    send_details.put(Constants.strCodeKey, Constants.newComplaints);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new ComplaintsAsync(NewComplaint.this).execute(send_details.toString());
            } else {
                tv_error.setText(R.string.no_network);
                tv_error.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_id_info:
                Intent ticket_info_activity = new Intent(getApplicationContext(), TicketInfo.class);
                startActivity(ticket_info_activity);
                break;
        }
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            Log.d("NewComplaint", result);
            all_complaints = new JSONArray(result);

            if (all_complaints.length() == 0) {
                tv_error.setText("No Complaints");
                tv_error.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < all_complaints.length(); i++) {
                individual_complaint = (JSONObject) all_complaints.get(i);
                String ticket_id = individual_complaint.getString("ticket_id");
                String name = individual_complaint.getString("name");
                String companyname = individual_complaint.getString("companyname");
                //String usertype=individual_complaint.getString("usertype");
                //String problemtype=individual_complaint.getString("problemtype");
                String number = individual_complaint.getString("registeredno");
                String description = individual_complaint.getString("description");
                String complaint_reg_time = individual_complaint.getString("complaint_reg_time");
                String complaint_reg_date = individual_complaint.getString("complaint_reg_date");
                String raisedagain = individual_complaint.getString("raisedagain");
                String allotted_date = individual_complaint.getString("allotted_date");
                String allotted_slot = individual_complaint.getString("allotted_slot");
                String engineerappointed = individual_complaint.getString("engineerappointed");
                //String engineer_appointed_time=individual_complaint.getString("engineer_appointed_time");
                //String engineer_appointed_date=individual_complaint.getString("engineer_appointed_date");
                //String appointedbyadmin=individual_complaint.getString("appointedbyadmin");
                String ticketstatus = individual_complaint.getString("ticketstatus");
                String requested_date = individual_complaint.getString("requested_date");
                String requested_slot = individual_complaint.getString("requested_slot");
                //String engineer_close_time=individual_complaint.getString("engineer_close_time");
                //String engineer_close_date=individual_complaint.getString("engineer_close_date");
                //String ticket_close_time=individual_complaint.getString("ticket_close_time");
                //String ticket_close_date=individual_complaint.getString("ticket_close_date");
                String solution = individual_complaint.getString("solution");
                String previous_engineer = individual_complaint.getString("previous_engineer");
                String previous_solution = individual_complaint.getString("previous_solution");

                complaintModel.add(new ComplaintModel(ticket_id, name, companyname, number, description, complaint_reg_time, complaint_reg_date,
                        raisedagain, allotted_date, allotted_slot, engineerappointed, ticketstatus,
                        requested_date, requested_slot, solution, previous_engineer, previous_solution));
                adapter = new CustomAdaptorNewComplaint(complaintModel, getApplicationContext());
                listView.setAdapter(adapter);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        individual_complaint = (JSONObject) all_complaints.get(position); // or change it to id
                        Log.d("New Complaint Position", String.valueOf(position));
                        Log.d("New Complaint id", String.valueOf(id));
                        Intent specific_complaint = new Intent(getApplicationContext(), NewSpecificComplaint.class);
                        specific_complaint.putExtra("jsonobject", individual_complaint.toString());
                        startActivity(specific_complaint);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String[] result) {

    }
}
