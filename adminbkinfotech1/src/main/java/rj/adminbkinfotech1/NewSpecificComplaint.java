package rj.adminbkinfotech1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rj.adminbkinfotech1.AsyncTasks.ComplaintsAsync;
import rj.adminbkinfotech1.AsyncTasks.DateAsyncTask;
import rj.adminbkinfotech1.Constants.Constants;

import static rj.adminbkinfotech1.AppointedComplaint.appointedComplaintActivityContext;


/**
 * Created by jimeet29 on 24-12-2017.
 */

public class NewSpecificComplaint extends AppCompatActivity implements TaskCompleted, View.OnClickListener {
    String string_object;
    JSONObject specific_complaint_obj, send_details;
    Button btn_appoint_engineer;
    TextView tv_ticket_id, tv_name, tv_company_name, tv_contact_no, tv_alt_contact_no, tv_description, tv_user_type, tv_problem_type, tv_error, tv_previous_engineer_appointed, tv_complaint_reg_time, tv_complaint_reg_date;
    String in_ticket, in_name, in_company, in_registered_no, in_alt_contact_no, in_address, in_description, in_user_type, in_problem_type, in_previous_engineer_appointed, in_engineer_appointed, in_complaint_reg_time, in_complaint_reg_date, in_allotment_date, in_allotment_date_slot;
    ProgressDialog pgnew_specific;
    EditText et_address;
    String AddressPattern = "^[A-Za-z0-9@(),':;._\\- \\s+]+$";
    Spinner spinner_engineer, spinner_appointment_date, spinner_appointment_date_slot;
    ArrayAdapter<String> adapter_spinner_appointment_date, adapter_spinner_appointment_date_slot, adapter_spinner_engineers;
    String appointment_dates[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string_object = getIntent().getStringExtra("jsonobject");

        setContentView(R.layout.new_specific_complaint_activity);
        Log.d("NewSpecific", string_object);
        pgnew_specific = new ProgressDialog(NewSpecificComplaint.this);
        tv_ticket_id = (TextView) findViewById(R.id.tv_id_specfic_ticket);
        tv_name = (TextView) findViewById(R.id.tv_id_specfic_name);
        tv_company_name = (TextView) findViewById(R.id.tv_id_specfic_company_name);
        tv_user_type = (TextView) findViewById(R.id.tv_id_specfic_user_type);
        tv_problem_type = (TextView) findViewById(R.id.tv_id_specfic_problem_type);
        tv_contact_no = (TextView) findViewById(R.id.tv_id_specfic_registered_no);
        tv_alt_contact_no = (TextView) findViewById(R.id.tv_id_specfic_alternate_no);
        tv_description = (TextView) findViewById(R.id.tv_id_specfic_description);
        tv_error = (TextView) findViewById(R.id.tv_id_specfic_error);
        tv_previous_engineer_appointed = (TextView) findViewById(R.id.tv_id_previous_engineer_appointed);
        tv_complaint_reg_time = (TextView) findViewById(R.id.tv_id_registration_time);
        tv_complaint_reg_date = (TextView) findViewById(R.id.tv_id_registration_date);
        et_address = (EditText) findViewById(R.id.et_id_specific_address);

        spinner_engineer = (Spinner) findViewById(R.id.spinner_engineers);
        spinner_appointment_date = (Spinner) findViewById(R.id.spinner_appointment_dates);
        spinner_appointment_date_slot = (Spinner) findViewById(R.id.spinner_appointment_date_slots);

        btn_appoint_engineer = (Button) findViewById(R.id.btn_appoint_engineer);

        setCustomActionBar();

        try {
            JSONArray engineerNamesArray = ReusableCodeAdmin.getEngineerNamesFromSharedPreferences(getApplicationContext());
            String engineer_name[];
            if (engineerNamesArray != null) {
                engineer_name = new String[engineerNamesArray.length() + 1];
                for (int i = 1; i < engineerNamesArray.length() + 1; i++) {
                    engineer_name[0] = "Select Engineer";
                    engineer_name[i] = String.valueOf(engineerNamesArray.get(i - 1));
                }
            } else {
                engineer_name = new String[1];
                engineer_name[0] = "Select Engineer";
            }

            adapter_spinner_engineers = new ArrayAdapter<>(this, R.layout.custom_spinner, engineer_name);
            adapter_spinner_engineers.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_engineer.setAdapter(adapter_spinner_engineers);
            spinner_engineer.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);

            appointment_dates = new String[6];
            appointment_dates[0] = "Select Allotment Date";
            new DateAsyncTask(NewSpecificComplaint.this).execute();
            specific_complaint_obj = new JSONObject(string_object);
            in_ticket = specific_complaint_obj.getString("ticket_id");
            in_name = specific_complaint_obj.getString("name");
            in_company = specific_complaint_obj.getString("companyname");
            in_user_type = specific_complaint_obj.getString("usertype");
            in_problem_type = specific_complaint_obj.getString("problemtype");
            in_registered_no = specific_complaint_obj.getString("registeredno");
            in_alt_contact_no = specific_complaint_obj.getString("alternateno");
            in_address = specific_complaint_obj.getString("address");
            in_description = specific_complaint_obj.getString("description");

            //for edit appointedcomplaints
            in_previous_engineer_appointed = specific_complaint_obj.getString("engineerappointed");
            in_complaint_reg_time = specific_complaint_obj.getString("complaint_reg_time");
            in_complaint_reg_date = specific_complaint_obj.getString("complaint_reg_date");


            tv_ticket_id.setText(in_ticket);
            tv_name.setText(in_name);
            if (!in_company.isEmpty() && !in_company.equalsIgnoreCase("null")) {
                tv_company_name.setText(in_company);
                tv_company_name.setVisibility(View.VISIBLE);
            }
            tv_user_type.setText(in_user_type);
            tv_problem_type.setText(in_problem_type);
            tv_contact_no.setText(in_registered_no);
            if (!in_alt_contact_no.isEmpty() && !in_alt_contact_no.equalsIgnoreCase("null")) {
                tv_alt_contact_no.setText(in_alt_contact_no);
                tv_alt_contact_no.setVisibility(View.VISIBLE);
            }
            if (!in_address.isEmpty() && !in_address.equalsIgnoreCase("null")) {
                et_address.setText(in_address);
            }
            tv_description.setText(in_description);

            if (!in_previous_engineer_appointed.isEmpty() && !in_previous_engineer_appointed.equalsIgnoreCase("null")) {
                tv_previous_engineer_appointed.setText(in_previous_engineer_appointed);
                tv_previous_engineer_appointed.setVisibility(View.VISIBLE);
            }

            tv_complaint_reg_time.setText(in_complaint_reg_time);
            tv_complaint_reg_date.setText(in_complaint_reg_date);
            btn_appoint_engineer.setOnClickListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onTaskComplete(String result) {
        if (result.equals("1")) {
            Intent new_complaint = new Intent(getApplicationContext(), AppointedComplaint.class);
            startActivity(new_complaint);
            appointedComplaintActivityContext.finish();

        } else {
            tv_error.setText(R.string.error_msg_specific_complaint);
            tv_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_appoint_engineer:
                in_address = et_address.getText().toString();
                in_engineer_appointed = String.valueOf(spinner_engineer.getSelectedItemPosition());
                in_allotment_date = String.valueOf(spinner_appointment_date.getSelectedItemPosition());
                in_allotment_date_slot = String.valueOf(spinner_appointment_date_slot.getSelectedItemPosition());
                if (in_address.isEmpty() || !in_address.matches(AddressPattern)) {
                    //Toast.makeText(getApplicationContext(), "Please Enter a Valid Address", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_address);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_engineer_appointed.equals("0")) {
                    tv_error.setText(R.string.spinner_engineer_error);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_allotment_date.equals("0")) {
                    tv_error.setText(R.string.spinner_allotment_error);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_allotment_date_slot.equals("0")) {
                    tv_error.setText(R.string.spinner_slot_error);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    in_engineer_appointed = spinner_engineer.getSelectedItem().toString();
                    in_allotment_date = spinner_appointment_date.getSelectedItem().toString();
                    in_allotment_date_slot = spinner_appointment_date_slot.getSelectedItem().toString();
                    try {
                        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                            send_details = new JSONObject();
                            send_details.put(Constants.strClientIdKey, Constants.clientId);
                            send_details.put("code", Constants.appointEngineerForAComplaint); //code 1 2 n 3    2 for update
                            SharedPreferences sp = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                            send_details.put("username", sp.getString("username", null));
                            send_details.put("ticket_id", in_ticket);
                            send_details.put("registeredno", in_registered_no);
                            send_details.put("address", in_address);  // ADDED NEW  php file getallcomplaints.php code 2  update customer details table(update address field) and update complaint table (update address field)
                            send_details.put("engineer_appointed", in_engineer_appointed);
                            send_details.put("allotment_date", in_allotment_date);
                            send_details.put("allotment_date_slot", in_allotment_date_slot);
                            send_details.put("engineer_appointed_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                            send_details.put("engineer_appointed_date", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                            pgnew_specific.setMessage("Appointing a Engineer! Please Wait..!");
                            pgnew_specific.show();
                            new ComplaintsAsync(NewSpecificComplaint.this).execute(send_details.toString());
                            pgnew_specific.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "No Network Specific", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.d("New Specific Complaint", e.toString());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onTaskComplete(String[] appointedDates) {

        try {
            adapter_spinner_appointment_date = new ArrayAdapter<>(this, R.layout.custom_spinner, appointedDates);
            adapter_spinner_appointment_date.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_appointment_date.setAdapter(adapter_spinner_appointment_date);
            spinner_appointment_date.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);

            adapter_spinner_appointment_date_slot = new ArrayAdapter<>(this, R.layout.custom_spinner, Constants.appointment_dates_slots);
            adapter_spinner_appointment_date_slot.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_appointment_date_slot.setAdapter(adapter_spinner_appointment_date_slot);
            spinner_appointment_date_slot.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);
        } catch (Exception e) {
            Log.d("OnTaskRegisterCom", e.toString());
        }

    }
}
