package rj.bkinfotech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import rj.bkinfotech.AsyncTasks.DateAsyncTaskApp;
import rj.bkinfotech.AsyncTasks.RegisterComplaintHit;
import rj.bkinfotech.Constants.Constants;

/**
 * Created by jimeet29 on 09-12-2017.
 */

public class ResgisterComplaintActivity extends AppCompatActivity implements TaskCompleted, View.OnClickListener {
    EditText et_name, et_company_name, et_contact_no, et_alt_contact_no, et_description;


    TextView tv_error;
    Spinner spinner_user_type, spinner_problem_type;
    String in_name, in_company, in_alt_contact_no, in_description, in_user_type, in_problem_type, in_contact_no;
    Button register_complaint;
    String NamePattern = "^[A-Z a-z ]+$", CompanyPattern = "^[A-Z a-z@!&$._\\- ]+$", MobilePattern = "^[789][0-9]{9}$", AddressPattern = "^[A-Za-z0-9@,._\\- ]+$", DescriptionPattern = "^[A-Za-z0-9@:();,._'\\- \\s+]+$";
    ProgressDialog pg;
    HashMap<String, String> complaint_details;

    LinearLayout ll_allotted_date, ll_apt_dates, ll_apt_slots;
    TextView tv_allotted_date, tv_allotted_slot, tv_ast_name, tv_ast_user_type, tv_ast_problem_type, tv_ast_desc;
    Spinner spinner_allot_date, spinner_allot_slot;


    String prefs_name, prefs_company, prefs_alt_mob_no, prefs_address, prefs_in_contact_no;
    AlertDialog alertDialog;
    SharedPreferences settings;
    int get_calling_class_code = 0;  // 0 to insert  1 to update
    String received_ticket_details;
    JSONObject json_received_ticket_details;
    String in_allotment_date, in_allotment_date_slot;

    ArrayAdapter<String> adapter_spinner_appointment_date, adapter_spinner_appointment_date_slot;
    String appointment_dates[];
    String confirmation_dailog_message = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        get_calling_class_code = getIntent().getIntExtra("classcode", 0);
        if (get_calling_class_code == 1) {
            setTitle("Change Appointment");
            received_ticket_details = getIntent().getStringExtra("jsonobject");
            confirmation_dailog_message = getResources().getString(R.string.change_appointment_dailog);
        }
        confirmation_dailog_message = getResources().getString(R.string.raise_ticket_dailog);

        setContentView(R.layout.register_complaint_activity);

        setCustomActionBar();

        initialize();

        setValuesFromSharedPreferences();

        setListeners();

    }

    private void setListeners() {
        register_complaint.setOnClickListener(this);
    }

    private void setValuesFromSharedPreferences() {
        ArrayAdapter userTypeAdapter = ArrayAdapter.createFromResource(this, R.array.select_type, R.layout.custom_spinner);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_user_type.setAdapter(userTypeAdapter);
        spinner_user_type.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter problemTypeAdapter = ArrayAdapter.createFromResource(this, R.array.problem_type, R.layout.custom_spinner);
        problemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_problem_type.setAdapter(problemTypeAdapter);
        spinner_problem_type.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);
        settings = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
        prefs_in_contact_no = settings.getString("mobileno", null);
        if (prefs_in_contact_no != null) {
            in_contact_no = prefs_in_contact_no;
            et_contact_no.setText(prefs_in_contact_no);
        }
        et_contact_no.setEnabled(false);


        prefs_name = settings.getString("name", null);
        if (prefs_name != null) {
            in_name = prefs_name;
            et_name.setText(in_name);
        }
        prefs_company = settings.getString("company_name", null);
        if (prefs_company != null) {
            in_company = prefs_company;
            et_company_name.setText(in_company);
        }
        prefs_alt_mob_no = settings.getString("alternate_no", null);
        if (prefs_alt_mob_no != null) {
            in_alt_contact_no = prefs_alt_mob_no;
            et_alt_contact_no.setText(in_alt_contact_no);
        }
        if (get_calling_class_code == 1) {
            tv_ast_name.setVisibility(View.GONE);
            tv_ast_user_type.setVisibility(View.GONE);
            tv_ast_problem_type.setVisibility(View.GONE);
            tv_ast_desc.setVisibility(View.GONE);

            et_name.setEnabled(false);
            et_name.setTextColor(Color.parseColor("#272d35"));
            et_company_name.setEnabled(false);
            et_company_name.setTextColor(Color.parseColor("#272d35"));
            et_alt_contact_no.setEnabled(false);
            et_alt_contact_no.setTextColor(Color.parseColor("#272d35"));
            ll_allotted_date.setVisibility(View.VISIBLE);
            ll_apt_dates.setVisibility(View.VISIBLE);
            ll_apt_slots.setVisibility(View.VISIBLE);
            try {

                appointment_dates = new String[6];
                appointment_dates[0] = "Select Allotment Date";
                new DateAsyncTaskApp(ResgisterComplaintActivity.this).execute();
                json_received_ticket_details = new JSONObject(received_ticket_details);

                spinner_user_type.setSelection(((ArrayAdapter) spinner_user_type.getAdapter()).getPosition(json_received_ticket_details.getString("usertype")));
                spinner_user_type.setEnabled(false);

                spinner_problem_type.setSelection(((ArrayAdapter) spinner_problem_type.getAdapter()).getPosition(json_received_ticket_details.getString("problemtype")));
                //spinner_problem_type.setSelected(Boolean.parseBoolean(json_received_ticket_details.getString("problemtype")));
                spinner_problem_type.setEnabled(false);
                et_description.setText(json_received_ticket_details.getString("description"));
                et_description.setEnabled(false);
                et_description.setTextColor(Color.parseColor("#272d35"));
                tv_allotted_date.setText(json_received_ticket_details.getString("allotted_date"));
                tv_allotted_slot.setText(json_received_ticket_details.getString("allotted_slot"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initialize() {
        et_name = (EditText) findViewById(R.id.et_id_name);
        et_company_name = (EditText) findViewById(R.id.et_id_company_name);
        et_contact_no = (EditText) findViewById(R.id.et_id_reg_contact_no);
        et_alt_contact_no = (EditText) findViewById(R.id.et_id_alt_contact_no);
        //et_address = (EditText) findViewById(R.id.et_id_address);
        et_description = (EditText) findViewById(R.id.et_id_description);
        spinner_user_type = (Spinner) findViewById(R.id.spinner_select_type);
        spinner_problem_type = (Spinner) findViewById(R.id.spinner_problem_type);
        tv_error = (TextView) findViewById(R.id.tv_error);
        tv_ast_name = (TextView) findViewById(R.id.tv_id_name_ast);
        tv_ast_user_type = (TextView) findViewById(R.id.tv_id_user_type_ast);
        tv_ast_problem_type = (TextView) findViewById(R.id.tv_id_problem_type_ast);
        tv_ast_desc = (TextView) findViewById(R.id.tv_id_desc_ask);

        ll_allotted_date = (LinearLayout) findViewById(R.id.ll_id_already_allotted_time);
        tv_allotted_date = (TextView) findViewById(R.id.tv_id_allotted_date);
        tv_allotted_slot = (TextView) findViewById(R.id.tv_id_allotted_slot);
        spinner_allot_date = (Spinner) findViewById(R.id.spinner_appointment_dates);
        spinner_allot_slot = (Spinner) findViewById(R.id.spinner_appointment_date_slots);
        ll_apt_dates = (LinearLayout) findViewById(R.id.ll_id_apt_dates);
        ll_apt_slots = (LinearLayout) findViewById(R.id.ll_id_apt_slots);

        register_complaint = (Button) findViewById(R.id.btn_id_launch_complaint);
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        ImageView iv_info = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);

        if (get_calling_class_code == 1) {
            tv_custom_action_bar_title.setText(R.string.request_appointment);
        } else {
            tv_custom_action_bar_title.setText(R.string.register_complaint);
        }

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void confirmation_dialog() {
        final AlertDialog.Builder alertBox = new AlertDialog.Builder(ResgisterComplaintActivity.this);
        alertBox.setMessage(confirmation_dailog_message);
        alertBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"You clicked Yes button",Toast.LENGTH_LONG).show();
                JSONObject complaint_details = new JSONObject();
                if (get_calling_class_code == 0) {
                    try {
                        complaint_details.put(Constants.strClientIdKey, Constants.clientId);
                        complaint_details.put("insert_update_code", get_calling_class_code); //insert call
                        complaint_details.put("name", in_name);
                        complaint_details.put("company_name", in_company);
                        complaint_details.put("user_type", in_user_type);
                        complaint_details.put("problem_type", in_problem_type);
                        complaint_details.put("register_no", in_contact_no);
                        complaint_details.put("alt_register_no", in_alt_contact_no);
                        //complaint_details.put("address",in_address);
                        complaint_details.put("problem_desc", in_description);
                        complaint_details.put("complaint_reg_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                        complaint_details.put("complaint_reg_date", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                        System.out.println(complaint_details.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        complaint_details.put(Constants.strClientIdKey, Constants.clientId);
                        complaint_details.put("insert_update_code", get_calling_class_code); //update call
                        complaint_details.put("ticket_id", json_received_ticket_details.getString("ticket_id"));
                        complaint_details.put("requested_date", in_allotment_date);
                        complaint_details.put("requested_slot", in_allotment_date_slot);

                        System.out.println(complaint_details.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (complaint_details.length() > 0) {
                        tv_error.setVisibility(View.GONE);
                        new RegisterComplaintHit(ResgisterComplaintActivity.this)
                                .execute(complaint_details.toString(), Constants.registerNewComplaintsEP);
                    }

                } else {
                    //Toast.makeText(getApplicationContext(), "No Network!", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.no_network);
                    tv_error.setVisibility(View.VISIBLE);
                }

            }
        });
        alertBox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog = alertBox.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_launch_complaint:

                if (get_calling_class_code == 0) {

                    in_name = et_name.getText().toString().trim();
                    in_company = et_company_name.getText().toString().trim();
                    in_alt_contact_no = et_alt_contact_no.getText().toString().trim();
                    //in_address = et_address.getText().toString().trim();
                    in_description = et_description.getText().toString().trim();
                    in_user_type = String.valueOf(spinner_user_type.getSelectedItemPosition());
                    in_problem_type = String.valueOf(spinner_problem_type.getSelectedItemPosition());
                    if (in_name.isEmpty() || !in_name.matches(NamePattern)) {
                        Toast.makeText(getApplicationContext(), "Please Enter a Valid Name", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.validation_name);
                        tv_error.setVisibility(View.VISIBLE);
                    } else if (!in_company.isEmpty() && !in_company.matches(CompanyPattern)) {

                        Toast.makeText(getApplicationContext(), "Please Enter a Valid Company Name", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.validation_company_name);
                        tv_error.setVisibility(View.VISIBLE);

                    } else if (in_user_type.equalsIgnoreCase("0")) {
                        Toast.makeText(getApplicationContext(), "Please Select a User type", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.validation_user_type);
                        tv_error.setVisibility(View.VISIBLE);
                    } else if (in_problem_type.equalsIgnoreCase("0")) {
                        Toast.makeText(getApplicationContext(), "Please Select a Problem type", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.validation_problem_type);
                        tv_error.setVisibility(View.VISIBLE);
                    } else if (in_alt_contact_no.length() != 0 && !in_alt_contact_no.matches(MobilePattern)) {

                        Toast.makeText(getApplicationContext(), "Please Enter a Valid Mobile No.", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.validation_alt_mno);
                        tv_error.setVisibility(View.VISIBLE);

                    }/*else if(!in_address.matches(AddressPattern))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter a Valid Address", Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_address);
                    tv_error.setVisibility(View.VISIBLE);
                }*/ else if (!in_description.matches(DescriptionPattern)) {
                        Toast.makeText(getApplicationContext(), "Please Enter a Valid Description", Toast.LENGTH_SHORT).show();
                        tv_error.setText(R.string.validation_desc);
                        tv_error.setVisibility(View.VISIBLE);
                    } else {
                        in_user_type = spinner_user_type.getSelectedItem().toString();
                        in_problem_type = spinner_problem_type.getSelectedItem().toString();
                        confirmation_dialog();
                    }
                } else {
                    in_allotment_date = String.valueOf(spinner_allot_date.getSelectedItemPosition());
                    in_allotment_date_slot = String.valueOf(spinner_allot_slot.getSelectedItemPosition());
                    if (in_allotment_date.equals("0")) {
                        tv_error.setText(R.string.spinner_allotment_error);
                        tv_error.setVisibility(View.VISIBLE);

                    } else if (in_allotment_date_slot.equals("0")) {
                        tv_error.setText(R.string.spinner_slot_error);
                        tv_error.setVisibility(View.VISIBLE);
                    } else {
                        in_user_type = spinner_user_type.getSelectedItem().toString();
                        in_problem_type = spinner_problem_type.getSelectedItem().toString();
                        in_allotment_date = spinner_allot_date.getSelectedItem().toString();
                        in_allotment_date_slot = spinner_allot_slot.getSelectedItem().toString();
                        confirmation_dialog();
                    }
                }
                break;
            default:
                break;
        }
    }

   /* private class RegisterComplaintHit extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg = new ProgressDialog(ResgisterComplaintActivity.this);
            pg.setMessage("Processing please wait!");
            pg.setCancelable(false);
            pg.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String jsonData = params[0], jsonResponse = null;
            URL url;
            HttpURLConnection conn = null;

            try {
                url = new URL("http://bkinfotech.in/app/registerComplaint.php");
                //url = new URL("http://192.168.1.34:81/bkinfotech/registerComplaint.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(4000);
                conn.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(jsonData);
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String input_line;

                while ((input_line = reader.readLine()) != null) {
                    builder.append(input_line).append("\n");
                }
                //System.out.print("RESPONSE IN COMPLKAINT REGISTER ACITIVITY"+builder);

                jsonResponse = builder.toString().trim();
                Log.d("Register Complaint", jsonResponse);
                return jsonResponse;
            } catch (Exception e) {
                pg.dismiss();
                Log.d("REGISTER E ACITIVITY", String.valueOf(e));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (get_calling_class_code == 0) {
                try {
                    Log.d("Response", s);
                    pg.dismiss();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("name", in_name);
                    editor.putString("company_name", in_company);
                    //editor.putString("address",in_address);
                    editor.putString("alternate_no", in_alt_contact_no);
                    editor.apply();

                    JSONObject complaint_details_response = new JSONObject(s);

                    AlertDialog.Builder alert = new AlertDialog.Builder(ResgisterComplaintActivity.this);
                    alert.setMessage("Ticket Number for following complaint is " + complaint_details_response.get("ticket_id").toString());

                    alertDialog = alert.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            Intent user_activity = new Intent(getApplicationContext(), InProcessComplaintActivity.class);
                            ResgisterComplaintActivity.this.finish();
                            startActivity(user_activity);

                        }
                    }, 3000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Log.d("Response", s);
                    pg.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(ResgisterComplaintActivity.this);
                    if (s.equals("1")) {
                        alert.setMessage("New Date Requested Successfully");
                    } else {
                        alert.setMessage("New Date Already Requested Once");
                    }
                    alertDialog = alert.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            Intent user_activity = new Intent(getApplicationContext(), InProcessComplaintActivity.class);
                            ResgisterComplaintActivity.this.finish();
                            startActivity(user_activity);

                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }*/

    @Override
    public void onTaskComplete(String result) {
        try {
            if (get_calling_class_code == 0) {
                try {
                    Log.d("Response", result);
                    pg.dismiss();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("name", in_name);
                    editor.putString("company_name", in_company);
                    //editor.putString("address",in_address);
                    editor.putString("alternate_no", in_alt_contact_no);
                    editor.apply();

                    JSONObject complaint_details_response = new JSONObject(result);

                    AlertDialog.Builder alert = new AlertDialog.Builder(ResgisterComplaintActivity.this);
                    alert.setMessage("Ticket Number for following complaint is " + complaint_details_response.get("ticket_id").toString());

                    alertDialog = alert.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            Intent user_activity = new Intent(getApplicationContext(), InProcessComplaintActivity.class);
                            ResgisterComplaintActivity.this.finish();
                            startActivity(user_activity);

                        }
                    }, 3000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Log.d("Response", result);
                    pg.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(ResgisterComplaintActivity.this);
                    if (result.equals("1")) {
                        alert.setMessage("New Date Requested Successfully");
                    } else {
                        alert.setMessage("New Date Already Requested Once");
                    }
                    alertDialog = alert.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            Intent user_activity = new Intent(getApplicationContext(), InProcessComplaintActivity.class);
                            ResgisterComplaintActivity.this.finish();
                            startActivity(user_activity);

                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("onTaskComplete", e.toString());
        }
    }

    @Override
    public void onTaskComplete(String[] appointedDates) {

        try {
            adapter_spinner_appointment_date = new ArrayAdapter<>(this, R.layout.custom_spinner, appointment_dates);
            adapter_spinner_appointment_date.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_allot_date.setAdapter(adapter_spinner_appointment_date);
            spinner_allot_date.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);

            adapter_spinner_appointment_date_slot = new ArrayAdapter<>(this, R.layout.custom_spinner, Constants.appointment_dates_slots);
            adapter_spinner_appointment_date_slot.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_allot_slot.setAdapter(adapter_spinner_appointment_date_slot);
            spinner_allot_slot.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);
        } catch (Exception e) {
            Log.d("OnTaskRegisterCom", e.toString());
        }

    }

}
