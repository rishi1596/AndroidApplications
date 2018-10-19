package rj.bkinfotech;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rj.bkinfotech.AsyncTasks.ComplaintsAsync;
import rj.bkinfotech.Constants.Constants;

/**
 * Created by jimeet29 on 20-05-2018.
 */

public class TicketFeedback extends AppCompatActivity implements TaskCompleted, View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    ImageView iv_close;
    RatingBar rb_service, rb_engineer;
    EditText et_remark;
    Button btn_submit;
    String remarkPattern = "^[a-zA-Z@0-9,.':;()\\- \\s+]+$", in_remark;
    float in_rate_service, in_rate_engineer;
    TextView tv_error, tv_info_close;
    JSONObject data;
    String ticket_id, code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticket_id = getIntent().getStringExtra("ticket_id");
        /* ticket_id = "BK_01";
        code = "1";*/
        code = getIntent().getStringExtra("code");
        setContentView(R.layout.ticket_feedback);

        Log.d("Feedback", ticket_id);

        //iv_close = (ImageView) findViewById(R.id.iv_id_cancel);
        rb_service = (RatingBar) findViewById(R.id.rb_id_rate_service);
        rb_engineer = (RatingBar) findViewById(R.id.rb_id_rate_engineer);
        et_remark = (EditText) findViewById(R.id.et_id_remark);
        btn_submit = (Button) findViewById(R.id.btn_id_submit);
        tv_error = (TextView) findViewById(R.id.tv_error);
        tv_info_close = (TextView) findViewById(R.id.tv_id_ticket_feedback_close);
        rb_service.setRating(5);
        rb_engineer.setRating(5);
        setListeners();

    }

    private void update_server() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                data = new JSONObject();
                data.put(Constants.strClientIdKey, Constants.clientId);
                data.put("code", code);
                data.put("ticket_id", ticket_id);
                data.put("rate_service", in_rate_service);
                data.put("rate_engineer", in_rate_engineer);
                data.put("remark", in_remark);
                data.put("ticket_close_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                data.put("ticket_close_date", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                Log.d("TecketFeedback", data.toString());
                new ComplaintsAsync(this).execute(data.toString());

            } else {
                tv_error.setText(R.string.no_network);
                tv_error.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("SolutionActivity", e.toString());
        }
    }

    @Override
    public void onTaskComplete(String result) {
        if (result.equals("1")) {
            AlertDialog alertDialog;
            AlertDialog.Builder alert = new AlertDialog.Builder(TicketFeedback.this);
            alert.setMessage("Thank You! For your valuable feedback.Your ticket is closed.");

            alertDialog = alert.create();
            alertDialog.setCancelable(false);
            alertDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent user_activity = new Intent(getApplicationContext(), InProcessComplaintActivity.class);
                    startActivity(user_activity);
                    InProcessComplaintActivity.inprocess.finish();
                }
            }, 3000);
        } else {
            tv_error.setText(R.string.unsuccessful);
            tv_error.setVisibility(View.VISIBLE);
            TicketFeedback.this.finish();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        rb_service.setOnRatingBarChangeListener(this);
        rb_engineer.setOnRatingBarChangeListener(this);
        btn_submit.setOnClickListener(this);
        tv_info_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.tv_id_ticket_feedback_close) {
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_submit:
                in_rate_service = rb_service.getRating();
                in_rate_engineer = rb_engineer.getRating();
                in_remark = et_remark.getText().toString();

                if (in_remark.length() != 0 && !in_remark.matches(remarkPattern)) {
                    tv_error.setText(R.string.error_remark);
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    update_server();
                }
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()) {
            case R.id.rb_id_rate_service:
                rb_service.setRating(rating);
                break;

            case R.id.rb_id_rate_engineer:
                rb_engineer.setRating(rating);
                break;

            default:
                break;
        }
    }

    @Override
    public void onTaskComplete(String[] result) {

    }
}
