package rj.adminbkinfotech1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import rj.adminbkinfotech1.AsyncTasks.DateAsyncTask;
import rj.adminbkinfotech1.AsyncTasks.RegisterComplaintAsync;
import rj.adminbkinfotech1.AsyncTasks.getEngineerNamesOrAddressAsync;
import rj.adminbkinfotech1.Constants.Constants;

/**
 * Created by jimeet29 on 09-12-2017.
 */

public class ResgisterComplaintActivity extends AppCompatActivity implements View.OnClickListener, TaskCompleted, TextView.OnEditorActionListener {

    String TAG = "ResgisterComplaintActivity";
    EditText et_name, et_company_name, et_contact_no, et_alt_contact_no, et_description, et_address;

    TextView tv_error;
    Spinner spinner_user_type, spinner_problem_type;
    String in_name, in_company, in_alt_contact_no, in_description, in_user_type, in_problem_type,
            in_contact_no, in_address, in_allotment_date, in_allotment_date_slot, in_engineer_appointed_name;
    Button register_complaint;
    String NamePattern = "^[A-Z a-z ]+$", CompanyPattern = "^[A-Z a-z@!&$._\\- ]+$", MobilePattern = "^[789][0-9]{9}$", AddressPattern = "^[A-Za-z0-9@,._\\- ]+$", DescriptionPattern = "^[A-Za-z0-9@:();,._'\\- \\s+]+$";
    HashMap<String, String> complaint_details;

    LinearLayout ll_apt_dates, ll_apt_slots;
    TextView tv_ast_name, tv_ast_user_type, tv_ast_problem_type, tv_ast_desc;
    Spinner spinner_engineer, spinner_allot_date, spinner_allot_slot;

    AlertDialog alertDialog;
    SharedPreferences settings;
    JSONObject json_received_ticket_details;

    ArrayAdapter<String> adapter_spinner_appointment_date, adapter_spinner_appointment_date_slot, adapter_spinner_engineers;
    String confirmation_dailog_message = "";
    private SharedPreferences sp;
    private String strUsernameSP;
    private boolean isGetAddressApiResponse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_register_complaint_activity);

        setCustomActionBar();

        initialize();

        initializeDropDownList();

        new DateAsyncTask(ResgisterComplaintActivity.this).execute();
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        ImageView iv_info = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);

        tv_custom_action_bar_title.setText(R.string.register_complaint);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    private void initialize() {
        et_name = (EditText) findViewById(R.id.et_id_name);
        et_company_name = (EditText) findViewById(R.id.et_id_company_name);
        et_contact_no = (EditText) findViewById(R.id.et_id_reg_contact_no);
        et_alt_contact_no = (EditText) findViewById(R.id.et_id_alt_contact_no);
        et_address = (EditText) findViewById(R.id.et_id_specific_address);
        et_description = (EditText) findViewById(R.id.et_id_description);
        spinner_user_type = (Spinner) findViewById(R.id.spinner_select_type);
        spinner_problem_type = (Spinner) findViewById(R.id.spinner_problem_type);
        tv_error = (TextView) findViewById(R.id.tv_error);
        tv_ast_name = (TextView) findViewById(R.id.tv_id_name_ast);
        tv_ast_user_type = (TextView) findViewById(R.id.tv_id_user_type_ast);
        tv_ast_problem_type = (TextView) findViewById(R.id.tv_id_problem_type_ast);
        tv_ast_desc = (TextView) findViewById(R.id.tv_id_desc_ask);

        spinner_engineer = (Spinner) findViewById(R.id.spinner_engineers);
        spinner_allot_date = (Spinner) findViewById(R.id.spinner_appointment_dates);
        spinner_allot_slot = (Spinner) findViewById(R.id.spinner_appointment_date_slots);
        ll_apt_dates = (LinearLayout) findViewById(R.id.ll_id_apt_dates);
        ll_apt_slots = (LinearLayout) findViewById(R.id.ll_id_apt_slots);

        register_complaint = (Button) findViewById(R.id.btn_id_launch_complaint);
        register_complaint.setOnClickListener(this);
        et_contact_no.setOnEditorActionListener(this);

        sp = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
        strUsernameSP = sp.getString("username", null);
    }


    private void initializeDropDownList() {
        ArrayAdapter userTypeAdapter = ArrayAdapter.createFromResource(this, R.array.select_type, R.layout.custom_spinner);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_user_type.setAdapter(userTypeAdapter);
        spinner_user_type.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter problemTypeAdapter = ArrayAdapter.createFromResource(this, R.array.problem_type, R.layout.custom_spinner);
        problemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_problem_type.setAdapter(problemTypeAdapter);
        spinner_problem_type.getBackground().setColorFilter(getResources().getColor(R.color.editTextBgColor), PorterDuff.Mode.SRC_ATOP);

        //Set Engineer Names spinner
        JSONArray engineerNamesArray = ReusableCodeAdmin.getEngineerNamesFromSharedPreferences(this);
        String engineer_name[];
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmation_dialog() {
        final AlertDialog.Builder alertBox = new AlertDialog.Builder(ResgisterComplaintActivity.this);
        confirmation_dailog_message = String.valueOf(R.string.raise_ticket_dailog);
        alertBox.setMessage(Integer.parseInt(confirmation_dailog_message));
        alertBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(),"You clicked Yes button",Toast.LENGTH_LONG).show();
                JSONObject complaint_details = new JSONObject();
                try {
                    complaint_details.put(Constants.strClientIdKey, Constants.clientId);

                    complaint_details.put(Constants.strUserNameKey, strUsernameSP);
                    complaint_details.put("name", in_name);
                    complaint_details.put("company_name", in_company);
                    complaint_details.put("user_type", in_user_type);
                    complaint_details.put("problem_type", in_problem_type);
                    complaint_details.put(Constants.strRegisteredNoKey, in_contact_no);
                    complaint_details.put("alt_register_no", in_alt_contact_no);
                    //complaint_details.put("address",in_address);
                    complaint_details.put("problem_desc", in_description);
                    complaint_details.put("allotment_date", in_allotment_date);
                    complaint_details.put("allotment_date_slot", in_allotment_date_slot);
                    complaint_details.put("engineer_appointed", in_engineer_appointed_name);
                    complaint_details.put("complaint_reg_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                    complaint_details.put("complaint_reg_date", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                    System.out.println(complaint_details.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (complaint_details.length() > 0) {
                        tv_error.setVisibility(View.GONE);
                        new RegisterComplaintAsync(ResgisterComplaintActivity.this).execute(complaint_details.toString());
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
    public void onTaskComplete(String result) {
        try {
            if (result != null && !result.isEmpty()) {
                //NOTE: The response here will be of getAddress ep
                Log.d("Response", result);
                JSONObject complaint_details_response = new JSONObject(result);
                if (isGetAddressApiResponse) {
                    if (complaint_details_response.has(Constants.keyErrorCode)) {
                        Log.d(TAG, complaint_details_response.optString(Constants.keyErrorCode));
                    } else if (complaint_details_response.has(Constants.keyResponse) &&
                            complaint_details_response.optString(Constants.keyResponse)
                                    .equalsIgnoreCase(Constants.successResponse)) {
                        try {
                            et_address.setText(complaint_details_response.optString(Constants.KEY_ADDRESS));
                        } catch (Exception e) {
                            Log.d(TAG, "Falied to set address to ET");
                        }
                    }
                    isGetAddressApiResponse = false;
                } else {
                    //NOTE: The response here will be of resgisterComplaint EP
                    AlertDialog.Builder alert = new AlertDialog.Builder(ResgisterComplaintActivity.this);
                    if (complaint_details_response.has(Constants.keyErrorCode)) {
                        switch (complaint_details_response.optString(Constants.keyErrorCode)) {
                            case Constants.ERROR_CODE_100:
                                alert.setMessage(Constants.ERROR_CODE_100_AND_DEFAULT_MSG);
                                break;
                            case Constants.ERROR_CODE_101:
                                alert.setMessage(Constants.ERROR_CODE_101_MSG);
                                break;
                            case Constants.ERROR_CODE_102:
                                alert.setMessage(Constants.ERROR_CODE_102_MSG);
                                break;
                            case Constants.ERROR_CODE_103:
                                alert.setMessage(Constants.ERROR_CODE_103_MSG);
                                break;
                            case Constants.ERROR_CODE_104:
                                alert.setMessage(Constants.ERROR_CODE_104_MSG);
                                break;
                            default:
                                alert.setMessage(Constants.ERROR_CODE_100_AND_DEFAULT_MSG);
                                break;
                        }

                    } else if (complaint_details_response.has(Constants.keyResponse) &&
                            complaint_details_response.optString(Constants.keyResponse)
                                    .equalsIgnoreCase(Constants.successResponse)) {
                        alert.setMessage("Ticket Number for following complaint is " + complaint_details_response.get("ticket_id").toString());
                    }
                    alertDialog = alert.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                            Intent user_activity = new Intent(getApplicationContext(), AdminActivity.class);
                            ResgisterComplaintActivity.this.finish();
                            startActivity(user_activity);

                        }
                    }, 3000);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String[] appointedDates) {
        try {
            adapter_spinner_appointment_date = new ArrayAdapter<>(this, R.layout.custom_spinner, appointedDates);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_launch_complaint:

                in_contact_no = et_contact_no.getText().toString().trim();
                in_name = et_name.getText().toString().trim();
                in_company = et_company_name.getText().toString().trim();
                in_alt_contact_no = et_alt_contact_no.getText().toString().trim();
                in_address = et_address.getText().toString().trim();
                in_description = et_description.getText().toString().trim();

                in_user_type = String.valueOf(spinner_user_type.getSelectedItemPosition());
                in_problem_type = String.valueOf(spinner_problem_type.getSelectedItemPosition());
                in_allotment_date = String.valueOf(spinner_allot_date.getSelectedItemPosition());
                in_allotment_date_slot = String.valueOf(spinner_allot_slot.getSelectedItemPosition());
                in_engineer_appointed_name = String.valueOf(spinner_engineer.getSelectedItemPosition());
                
                if (in_name.isEmpty() || !in_name.matches(NamePattern)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_name), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_name);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (!in_company.isEmpty() && !in_company.matches(CompanyPattern)) {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_company_name), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_company_name);
                    tv_error.setVisibility(View.VISIBLE);

                } else if (in_user_type.equals("0")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_user_type), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_user_type);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_problem_type.equals("0")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_problem_type), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_problem_type);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_contact_no.isEmpty() || !in_contact_no.matches(MobilePattern)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_mno), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_mno);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (!in_alt_contact_no.isEmpty() && !in_alt_contact_no.matches(MobilePattern)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_alt_mno), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_alt_mno);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_address.isEmpty() || !in_address.matches(AddressPattern)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_address), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_address);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_description.isEmpty() || !in_description.matches(DescriptionPattern)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation_desc), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.validation_desc);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_engineer_appointed_name.equals("0")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.spinner_engineer_error), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.spinner_engineer_error);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_allotment_date.equals("0")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.spinner_allotment_error), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.spinner_allotment_error);
                    tv_error.setVisibility(View.VISIBLE);
                } else if (in_allotment_date_slot.equals("0")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.spinner_slot_error), Toast.LENGTH_SHORT).show();
                    tv_error.setText(R.string.spinner_slot_error);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    in_user_type = spinner_user_type.getSelectedItem().toString();
                    in_problem_type = spinner_problem_type.getSelectedItem().toString();
                    in_allotment_date = spinner_allot_date.getSelectedItem().toString();
                    in_allotment_date_slot = spinner_allot_slot.getSelectedItem().toString();
                    in_engineer_appointed_name = spinner_engineer.getSelectedItem().toString();
                    confirmation_dialog();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getImeActionId() == 1) {
            in_contact_no = et_contact_no.getText().toString().trim();
            if (in_contact_no.length() != 0 && in_contact_no.matches(MobilePattern)) {
                try {
                    JSONObject oRequestAddress = new JSONObject();
                    oRequestAddress.put(Constants.strClientIdKey, Constants.clientId);
                    oRequestAddress.put(Constants.strUserNameKey, strUsernameSP);
                    oRequestAddress.put(Constants.strRegisteredNoKey, in_contact_no);
                    isGetAddressApiResponse = true;
                    new getEngineerNamesOrAddressAsync(ResgisterComplaintActivity.this)
                            .execute(oRequestAddress.toString(), Constants.getAddressEP);
                } catch (Exception e) {
                    Log.d(TAG, "onEditorAction");
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
