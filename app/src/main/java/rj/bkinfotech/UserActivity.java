package rj.bkinfotech;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by jimeet29 on 05-12-2017.
 */

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_register_complaint, btn_in_process_complaints, btn_view_all_complaints, btn_view_about_us, btn_offers;
    FloatingActionButton fab_app_feedback;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        setCustomActionBar();
        initialize();
        setListerners();
    }

    private void initialize() {
        btn_register_complaint = (Button) findViewById(R.id.btn_id_register_complaint);
        btn_in_process_complaints = (Button) findViewById(R.id.btn_id_in_process_complaints);
        btn_view_all_complaints = (Button) findViewById(R.id.btn_id_view_all_complaints);
        btn_view_about_us = (Button) findViewById(R.id.btn_id_view_about_us);
        btn_offers = (Button) findViewById(R.id.btn_id_view_offers);
        fab_app_feedback = (FloatingActionButton) findViewById(R.id.fab_id_app_feedback);
    }

    private void setListerners() {
        btn_register_complaint.setOnClickListener(this);
        btn_in_process_complaints.setOnClickListener(this);
        btn_view_all_complaints.setOnClickListener(this);
        btn_view_about_us.setOnClickListener(this);
        btn_offers.setOnClickListener(this);
        fab_app_feedback.setOnClickListener(this);
    }


    private void setCustomActionBar() {

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setCustomView(R.layout.action_bar);
        TextView tv_custom_action_bar_title = (TextView) actionBar.getCustomView().findViewById(R.id.tv_id_custom_action_bar_title);
        tv_custom_action_bar_title.setText(R.string.app_name);
        ImageView iv_info = (ImageView) actionBar.getCustomView().findViewById(R.id.iv_id_info);
        iv_info.setVisibility(View.GONE);
        iv_info.setOnClickListener(this);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_id_register_complaint:
                Intent complaint_register = new Intent(getApplicationContext(), ResgisterComplaintActivity.class);
                startActivity(complaint_register);
                break;
            case R.id.btn_id_in_process_complaints:
                Intent in_process_complaints = new Intent(getApplicationContext(), InProcessComplaintActivity.class);
                startActivity(in_process_complaints);
                break;
            case R.id.btn_id_view_all_complaints:
                Intent complaints_status = new Intent(getApplicationContext(), AllComplaintsStatusActivity.class);
                complaints_status.putExtra("UserInterface", "1");
                startActivity(complaints_status);
                break;
            case R.id.btn_id_view_about_us:
                Intent about_us = new Intent(getApplicationContext(), AboutUs.class);
                startActivity(about_us);
                break;
            case R.id.btn_id_view_offers:
                Toast.makeText(getApplicationContext(),"Coming Soon!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_id_app_feedback:
                Intent implicit_email_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "appbkinfotech@gmail.com"));
                implicit_email_intent.putExtra(Intent.EXTRA_SUBJECT, "Application Feedback");
                startActivity(implicit_email_intent);
                break;
            case R.id.iv_id_info:
                Intent ticket_info_activity = new Intent(getApplicationContext(), TicketInfo.class);
                startActivity(ticket_info_activity);
                break;
        }
    }
}