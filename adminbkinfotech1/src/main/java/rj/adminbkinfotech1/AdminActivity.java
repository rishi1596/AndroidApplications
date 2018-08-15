package rj.adminbkinfotech1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import rj.adminbkinfotech1.Constants.Constants;

/**
 * Created by jimeet29 on 21-12-2017.
 */

public class AdminActivity extends AppCompatActivity implements View.OnClickListener,TaskCompleted {
    Button btn_new_complaint,btn_appointed,btn_all_complaint;
    JSONObject logOut_details;

    SharedPreferences sp;
    //int firstrun;
    ConnectivityManager cm;
    NetworkInfo networkInfo;
    String titleText;
    FloatingActionButton fab_app_feedback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getExtraDataFromPreviousActivity();

        setContentView(R.layout.admin_activity);

        initialize();

        setListerners();

        setCustomActionBar();

        try {
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    JSONObject send_details = new JSONObject();
                    send_details.put(Constants.strClientIdKey, Constants.clientId);
                    new getEngineers().execute(send_details.toString());
            }else{
                Toast.makeText(getApplicationContext(),"No Network! Please Try Again.",Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void initialize() {
        cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = cm.getActiveNetworkInfo();

        btn_new_complaint = (Button) findViewById(R.id.btn_id_new_complaint);
        btn_appointed = (Button) findViewById(R.id.btn_id_appointed);
        btn_all_complaint = (Button) findViewById(R.id.btn_id_all_complaint);
        fab_app_feedback = (FloatingActionButton)findViewById(R.id.fab_id_app_feedback);

    }

    private void setListerners() {
        btn_new_complaint.setOnClickListener(this);
        btn_appointed.setOnClickListener(this);
        btn_all_complaint.setOnClickListener(this);
        fab_app_feedback.setOnClickListener(this);
    }

    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(titleText);
        tv_custom_action_bar_title.setGravity(Gravity.START);
        ImageView iv_info = (ImageView)actionBar.getCustomView().findViewById(R.id.iv_id_info);
        //Changed info to logout just for homescreen
        iv_info.setImageResource(R.drawable.logoutvariant);
        iv_info.setOnClickListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    private void getExtraDataFromPreviousActivity() {
        sp = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
        String uname = sp.getString("username", null);
        uname = uname.substring(0, 1).toUpperCase() + uname.substring(1);
        titleText = "Welcome " + uname;
        setTitle("Welcome " + uname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_id_new_complaint:
                Intent new_complaint = new Intent(getApplicationContext(),NewComplaint.class);
                startActivity(new_complaint);
                break;
            case R.id.btn_id_appointed:
                Intent appointed_complaint = new Intent(getApplicationContext(),AppointedComplaint.class);
                startActivity(appointed_complaint);
                break;
            case R.id.btn_id_all_complaint:
                Intent all_complaint = new Intent(getApplicationContext(),AllComplaint.class);
                all_complaint.putExtra("UserInterface","1");
                startActivity(all_complaint);
                break;
            //Logout just for homescreen
            case R.id.iv_id_info:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
                alertBuilder.setMessage(R.string.log_out_text);
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut_details = new JSONObject();
                        SharedPreferences sf = getSharedPreferences("settings",0);

                        try {
                            logOut_details.put(Constants.strClientIdKey, Constants.clientId);
                            logOut_details.put("username",sf.getString("username",null));
                            String code = "2";
                            logOut_details.put("code",code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                            if (logOut_details.length() > 0) {
                                new LogInOutAsync(AdminActivity.this).execute(logOut_details.toString());
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"No Network! Please Try Again.",Toast.LENGTH_SHORT).show();
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
            case R.id.fab_id_app_feedback:
                Intent implicit_email_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+"appbkinfotech@gmail.com"));
                implicit_email_intent.putExtra(Intent.EXTRA_SUBJECT, "Application Feedback (Admin)");
                startActivity(implicit_email_intent);
                break;
            default:
                break;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.id_log_out:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AdminActivity.this);
                alertBuilder.setMessage(R.string.log_out_text);
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut_details = new JSONObject();
                        SharedPreferences sf = getSharedPreferences("settings",0);

                        try {
                            logOut_details.put("username",sf.getString("username",null));
                            String code = "2";
                            logOut_details.put("code",code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                            if (logOut_details.length() > 0) {
                                new LogInOutAsync(AdminActivity.this).execute(logOut_details.toString());
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"No Network! Please Try Again.",Toast.LENGTH_SHORT).show();
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

               return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public void onTaskComplete(String result) {
        Log.d("AdminLogin",result);
        try {
            //JSONObject response_server = new JSONObject(s);
            if(result.equals("1"))
            {

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("fr",false);
                editor.apply();

                Intent adminActivity = new Intent(getApplicationContext(),AdminLogin.class);
                startActivity(adminActivity);
                AdminActivity.this.finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Please Try Again!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Log.d("Admin Logout response",e.toString());
        }
    }

    private class getEngineers extends AsyncTask<String,Void,String>
    {
        ReusableCodeAdmin rca;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                rca = new ReusableCodeAdmin();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }



        @Override
        protected String doInBackground(String... params) {
            try {
                //String get_engineer_code = params[0];
                String url  = "http://bkinfotech.in/app/getEngineersName.php";

                rca.sendCredentials(params[0],url);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String res_get_engineers_list = rca.getCredentialsResponse();
                Log.d("response Engineer Name",res_get_engineers_list);

                //if(!res_get_engineers_list.equals("0")) {
                    SharedPreferences sp = getSharedPreferences("settings", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("engineers", res_get_engineers_list);
                    editor.apply();
               // }
              /* Log.d("engineerrr",res_get_engineers_list);
                JSONArray engineers_array = new JSONArray(res_get_engineers_list);
                Log.d("engineerrr", String.valueOf(engineers_array));
                String engineer_name[] = new String[engineers_array.length()];
                for(int i = 0 ;i<engineers_array.length();i++)
                {
                    engineer_name[i] =String.valueOf(engineers_array.get(i));
                }for(int i = 0 ;i<engineers_array.length();i++)
                {
                   Log.d("engineerrr",engineer_name[i]);
                }*/

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
