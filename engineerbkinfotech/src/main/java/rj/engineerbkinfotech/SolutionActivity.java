package rj.engineerbkinfotech;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rj.engineerbkinfotech.Constants.Constants;

import static java.security.AccessController.getContext;

/**
 * Created by jimeet29 on 02-02-2018.
 */

public class SolutionActivity extends AppCompatActivity implements TaskCompleted,View.OnClickListener {
    Button btn_submit;
    EditText et_solution;
    TextView tv_problem,tv_error,tv_ticket_solution_close;
    String in_solution,problem_desc,partial_close,ticket_id;
    JSONObject data;
    int deviceWidth,deviceHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        problem_desc = getIntent().getStringExtra("Problem");
        //problem_desc = "ajsgdagsdkgakjs ajsdkjhasdjahsjd  ashdlkasd hasdlkhasdhlkashdo ashdlnasdhkasdohalkhsdasdhans lhalsdnhanduasdhkasdk";
        partial_close = getIntent().getStringExtra("Partial/Close");   //3:partial 2:close
        ticket_id = getIntent().getStringExtra("ticket_id");

        setContentView(R.layout.solution_activity);

        initialize();

        determineDeviceDimensions();

        setWindowDimensions();

        setListerners();

      /*  iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent intent= new Intent(getApplicationContext(),EngineerActivity.class);


                startActivity(intent);*//*
                SolutionActivity.this.finish();
            }
        });*/

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_solution = et_solution.getText().toString().trim();
                if(in_solution.length()>0)
                {
                    update_server(ticket_id,partial_close,in_solution);
                }
                else{
                    tv_error.setText(R.string.solutionError);
                    tv_error.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private void initialize() {
        tv_problem = (TextView)findViewById(R.id.tv_id_problem_desc);
        tv_error = (TextView)findViewById(R.id.tv_id_error);
        tv_ticket_solution_close = (TextView)findViewById(R.id.tv_id_ticket_solution_close);
        et_solution = (EditText)findViewById(R.id.et_id_solution);
        btn_submit = (Button)findViewById(R.id.btn_id_submit);

        tv_problem.setText(problem_desc);
        tv_problem.setVisibility(View.VISIBLE);
    }

    private void determineDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

    }

    private void setWindowDimensions() {
        //Window Dimension are modified here
        // Method 1
        this.getWindow().setLayout(deviceWidth-150, ViewGroup.LayoutParams.WRAP_CONTENT);

        //Method 2
        /*WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = 1000;
        params.width = 1000;
        this.getWindow().setAttributes(params);*/
    }

    private void setListerners() {
        btn_submit.setOnClickListener(this);
        tv_ticket_solution_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.tv_id_ticket_solution_close)
                {
                    SolutionActivity.this.finish();
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_id_submit:
                in_solution = et_solution.getText().toString().trim();
                if(in_solution.length()>0)
                {
                    update_server(ticket_id,partial_close,in_solution);
                }
                else{
                    tv_error.setText(R.string.solutionError);
                    tv_error.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    private void update_server(String ticket_id, String partial_close, String in_solution) {
        try {
            ConnectivityManager cm =(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                data = new JSONObject();
                data.put(Constants.strClientIdKey,Constants.clientId);
                data.put("ticket_id",ticket_id);
                data.put("code",partial_close);
                data.put("solution",in_solution);
                data.put("engineer_close_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                data.put("engineer_close_date", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                new ComplaintsAsync(this).execute(data.toString());

            } else{
                tv_error.setText(R.string.no_network);
                tv_error.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            Log.d("SolutionActivity",e.toString());
        }
    }

    @Override
    public void onTaskComplete(String result) {
        if(result.equals("1"))
        {

            Intent intent= new Intent(getApplicationContext(),EngineerActivity.class);
            //Intent intent1 = getIntent();
            //setResult(RESULT_OK, intent1);
            startActivity(intent);
            EngineerActivity.fa.finish();





        }
        else {
            tv_error.setText(R.string.unsuccessful);
            tv_error.setVisibility(View.VISIBLE);
        }
    }



}
