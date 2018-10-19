package rj.bkinfotech;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import rj.bkinfotech.AsyncTasks.ComplaintsAsync;
import rj.bkinfotech.Constants.Constants;


/**
 * Created by jimeet29 on 23-12-2017.
 */

/**
 * activity = 0 then in_process_activity_context n if 1 then allcomplaints context
 */
public class CustomAdaptorNewComplaint extends ArrayAdapter<ComplaintModel> implements TaskCompleted {


    private ArrayList<ComplaintModel> complaints;
    private Context mContext;
    String code;
    JSONObject data;
    private int activity; //0 for inprocesscomplaintsactivity and 1 for allcomplaintsactivity
    JSONArray array_in_custom_adaptor;
    Intent intent;


    private String update_ticket_id;


    private static class ViewHolder {
        TextView ticket_id;
        TextView usertype;
        TextView problemtype;
        TextView description;
        TextView engineer_appointed;
        TextView complaint_reg_time;
        TextView complaint_reg_date;
        TextView allotted_date;
        TextView allotted_slot;
        TextView engineer_appointed_time;
        TextView engineer_appointed_date;
        TextView requested_date;
        TextView requested_slot;
        TextView engineer_close_time;
        TextView engineer_close_date;
        ImageView iv_raised_again;
        Button btn_close;

        LinearLayout ll_engineer_name, ll_engineer_appointed_date_time, ll_appointment_details, ll_requested_details, ll_engineer_closing_details;
        View view_ticket_status;

    }

    CustomAdaptorNewComplaint(ArrayList<ComplaintModel> data, Context context, int activity) {
        super(context, R.layout.in_process_complaint_activity, data);
        this.complaints = data;
        this.mContext = context;
        this.activity = activity;
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ComplaintModel compplaintModel = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.in_process_complaint_activity, parent, false);
            viewHolder.ticket_id = (TextView) convertView.findViewById(R.id.tv_id_ticketid);
            viewHolder.usertype = (TextView) convertView.findViewById(R.id.tv_id_usertype);
            viewHolder.problemtype = (TextView) convertView.findViewById(R.id.tv_id_problemtype);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_id_description);
            viewHolder.engineer_appointed = (TextView) convertView.findViewById(R.id.tv_id_engineer_appointed);
            viewHolder.complaint_reg_time = (TextView) convertView.findViewById(R.id.tv_id_reg_time);
            viewHolder.complaint_reg_date = (TextView) convertView.findViewById(R.id.tv_id_reg_date);
            viewHolder.iv_raised_again = (ImageView) convertView.findViewById(R.id.iv_id_raisedagain);
            viewHolder.allotted_date = (TextView) convertView.findViewById(R.id.tv_id_allotted_date);
            viewHolder.allotted_slot = (TextView) convertView.findViewById(R.id.tv_id_allotted_slot);
            viewHolder.engineer_appointed_time = (TextView) convertView.findViewById(R.id.tv_id_engineer_appointed_time);
            viewHolder.engineer_appointed_date = (TextView) convertView.findViewById(R.id.tv_id_engineer_appointed_date);
            viewHolder.requested_date = (TextView) convertView.findViewById(R.id.tv_id_requested_date);
            viewHolder.requested_slot = (TextView) convertView.findViewById(R.id.tv_id_requested_slot);
            viewHolder.engineer_close_time = (TextView) convertView.findViewById(R.id.tv_id_engineer_close_time);
            viewHolder.engineer_close_date = (TextView) convertView.findViewById(R.id.tv_id_engineer_close_date);
            viewHolder.ll_engineer_name = (LinearLayout) convertView.findViewById(R.id.ll_id_engineers_name);
            viewHolder.ll_engineer_appointed_date_time = (LinearLayout) convertView.findViewById(R.id.ll_id_engineer_appointed_details);
            viewHolder.ll_appointment_details = (LinearLayout) convertView.findViewById(R.id.ll_id_appointment);
            viewHolder.ll_requested_details = (LinearLayout) convertView.findViewById(R.id.ll_id_requested_appointment);
            viewHolder.ll_engineer_closing_details = (LinearLayout) convertView.findViewById(R.id.ll_id_engineer_closing_details);
            viewHolder.view_ticket_status = convertView.findViewById(R.id.view_id_ticket_status_color);
            viewHolder.btn_close = (Button) convertView.findViewById(R.id.btn_id_close);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        assert compplaintModel != null;

        viewHolder.ll_engineer_name.setVisibility(View.GONE);
        viewHolder.ll_engineer_appointed_date_time.setVisibility(View.GONE);
        viewHolder.ll_appointment_details.setVisibility(View.GONE);
        viewHolder.ll_requested_details.setVisibility(View.GONE);
        viewHolder.ll_engineer_closing_details.setVisibility(View.GONE);
        viewHolder.iv_raised_again.setVisibility(View.GONE);
        viewHolder.btn_close.setVisibility(View.GONE);
        viewHolder.ticket_id.setText(compplaintModel.getTicket_id());

        viewHolder.usertype.setText(compplaintModel.getUser_type());
        viewHolder.problemtype.setText(compplaintModel.getProblem_type());
        viewHolder.description.setText(compplaintModel.getDescription());

        viewHolder.complaint_reg_time.setText(compplaintModel.getComplaint_reg_time());
        viewHolder.complaint_reg_date.setText(compplaintModel.getComplaint_reg_date());

        if (compplaintModel.getRaisedagain().equals("1")) {
            viewHolder.iv_raised_again.setVisibility(View.VISIBLE);
        }


        switch (compplaintModel.getStatus()) {
            case Constants.TICKET_PENDING:
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_pending);
                break;
            case Constants.TICKET_IN_PROCESS:
                viewHolder.engineer_appointed.setText(compplaintModel.getEngineer_appointed());
                viewHolder.ll_engineer_name.setVisibility(View.VISIBLE);

                viewHolder.allotted_date.setText(compplaintModel.getAllotted_date());
                viewHolder.allotted_slot.setText(compplaintModel.getAllotted_slot());
                viewHolder.ll_appointment_details.setVisibility(View.VISIBLE);

                if (compplaintModel.getRequested_date().length() > 1) {
                    viewHolder.requested_date.setText(compplaintModel.getRequested_date());
                    viewHolder.requested_slot.setText(compplaintModel.getRequested_slot());
                    viewHolder.ll_requested_details.setVisibility(View.VISIBLE);
                }
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_in_process);
                break;
            case Constants.TICKET_PROCESSED:
                viewHolder.btn_close.setVisibility(View.VISIBLE);
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_processed);
                break;
            case Constants.TICKET_PROCESSED_PARTIALLY:
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_processed_partially);
                break;
            case Constants.TICKET_CLOSED:  //for raise again button
                if (activity != 0) {
                    viewHolder.btn_close.setText(R.string.raise_again);
                    viewHolder.btn_close.setVisibility(View.VISIBLE);
                }
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_closed);
                break;
            default:
                break;
        }

        viewHolder.btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("POSITION", String.valueOf(position));
                update_ticket_id = viewHolder.ticket_id.getText().toString();
                if (activity == 0) {
                    try {
                        code = "2";
                        intent = new Intent(mContext, TicketFeedback.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.strCodeKey, code);
                        intent.putExtra(Constants.KEY_TICKET_ID, update_ticket_id);
                        Log.d("TicketAdap", String.valueOf(update_ticket_id));
                        mContext.startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    code = "4";
                    update_server(update_ticket_id, code);
                }

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private void update_server(String local_ticket_id, String sv_code) {
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                code = sv_code;
                data = new JSONObject();
                data.put(Constants.strClientIdKey, Constants.clientId);
                data.put("code", code);

                data.put("ticket_id", local_ticket_id);
                data.put("complaint_raised_again_time", new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
                data.put("complaint_raised_again_date", new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()));
                new ComplaintsAsync(mContext).execute(data.toString());

            } else {
                Toast.makeText(getContext(), "No Network", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("ONClick Method", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String result) {

    }

    @Override
    public void onTaskComplete(String[] result) {

    }
}
