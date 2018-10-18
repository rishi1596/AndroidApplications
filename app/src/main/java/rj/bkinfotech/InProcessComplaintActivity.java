package rj.bkinfotech;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rj.bkinfotech.AsyncTasks.ComplaintsAsync;
import rj.bkinfotech.Constants.Constants;

/**
 * Created by jimeet29 on 11-01-2018.
 */

public class InProcessComplaintActivity extends AppCompatActivity implements TaskCompleted, View.OnClickListener {

    ListAdapter adapter;
    ListView listView;
    JSONObject engineer_complaint, send_details;
    JSONArray all_complaints;
    static JSONArray STATIC_ALL_COMPLAINTS;
    ArrayList<ComplaintModel> complaintModel;

    TextView tv_error;
    static int RESULT;
    int calling_class_code = 1; // for in process complaints
    public static Activity inprocess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_in_process_complaint_activity);

        setCustomActionBar();

        inprocess = this;

        tv_error = (TextView) findViewById(R.id.empty);

        complaintModel = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_id_complaints_list);
        if (savedInstanceState == null) {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                send_details = new JSONObject();
                try {
                    SharedPreferences sf = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                    String rg_no = sf.getString("mobileno", null);
                    String code = "1";
                    send_details.put(Constants.strClientIdKey, Constants.clientId);
                    send_details.put("registered_no", rg_no);
                    send_details.put("code", code);
                    System.out.println(send_details.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new ComplaintsAsync(this).execute(send_details.toString());
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
        ImageView iv_info = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_id_info);
        tv_custom_action_bar_title.setText(R.string.in_process_complaints);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        iv_info.setOnClickListener(this);
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
        if (result.equals("1") || result.equals("0")) {

            RESULT = Integer.parseInt(result);

        } else {

            Log.d("NewComplaint", result);
            try {
                all_complaints = new JSONArray(result);
                if (all_complaints.length() == 0) {
                    tv_error.setText(R.string.no_complaints);
                    tv_error.setVisibility(View.VISIBLE);
                }
                STATIC_ALL_COMPLAINTS = new JSONArray(result);
                Log.d("NewComplaint", String.valueOf(STATIC_ALL_COMPLAINTS));
                for (int i = 0; i < all_complaints.length(); i++) {
                    engineer_complaint = (JSONObject) all_complaints.get(i);
                    String ticket_id = engineer_complaint.getString("ticket_id");
                    //String name = engineer_complaint.getString("name");
                    //String company_name = engineer_complaint.getString("companyname");
                    String user_type = engineer_complaint.getString("usertype");
                    String problemtype = engineer_complaint.getString("problemtype");
                    String description = engineer_complaint.getString("description");
                    String complaint_reg_time = engineer_complaint.getString("complaint_reg_time");
                    String complaint_reg_date = engineer_complaint.getString("complaint_reg_date");
                    String raisedagain = engineer_complaint.getString("raisedagain");
                    String allotted_date = engineer_complaint.getString("allotted_date");
                    String allotted_slot = engineer_complaint.getString("allotted_slot");
                    String engineer_appointed = engineer_complaint.getString("engineerappointed");
                    String engineer_appointed_time = engineer_complaint.getString("engineer_appointed_time");
                    String engineer_appointed_date = engineer_complaint.getString("engineer_appointed_date");
                    String ticketstatus = engineer_complaint.getString("ticketstatus");
                    String requested_date = engineer_complaint.getString("requested_date");
                    String requested_slot = engineer_complaint.getString("requested_slot");
                    String engineer_close_time = engineer_complaint.getString("engineer_close_time");
                    String engineer_close_date = engineer_complaint.getString("engineer_close_date");
                    //String engineer_status = engineer_complaint.getString("engineersidestatus");
                    //String address = engineer_complaint.getString("address");


                    complaintModel.add(new ComplaintModel(ticket_id, user_type, problemtype, description, ticketstatus, engineer_appointed, complaint_reg_time, complaint_reg_date, raisedagain,
                            allotted_date, allotted_slot, engineer_appointed_time, engineer_appointed_date, requested_date, requested_slot, engineer_close_time, engineer_close_date));
                    adapter = new CustomAdaptorNewComplaint(complaintModel, getApplicationContext(), 0);

                    listView.setAdapter(adapter);

                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {
                            engineer_complaint = (JSONObject) all_complaints.get(position); // or change it to id
                            Intent specific_complaint = new Intent(getApplicationContext(), ResgisterComplaintActivity.class);


                            if (engineer_complaint.getString("ticketstatus").equalsIgnoreCase("pending") || engineer_complaint.getString("ticketstatus").equalsIgnoreCase("Ticket Processed Partially")) {
                                Toast.makeText(getApplicationContext(), R.string.date_not_alloted, Toast.LENGTH_SHORT).show();
                            } else {
                               /* if (null)
                                {
                                    TODo time difference
                                }
                                else {*/
                                specific_complaint.putExtra("classcode", calling_class_code);
                                specific_complaint.putExtra("jsonobject", engineer_complaint.toString());
                                startActivity(specific_complaint);
                                // }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

