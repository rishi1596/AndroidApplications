package rj.engineerbkinfotech;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import rj.engineerbkinfotech.Constants.Constants;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static rj.engineerbkinfotech.EngineerActivity.STATIC_ALL_COMPLAINTS;


/**
 * Created by jimeet29 on 23-12-2017.
 */

public class CustomAdaptorNewComplaint extends ArrayAdapter<ComplaintModel> implements View.OnClickListener {


    private ArrayList<ComplaintModel> complaints;
    private Context mContext;
    String code;
    private JSONObject specific_complaint_in_custom_adaptor, data;
    private JSONArray array_in_custom_adaptor;
    public static final int CALL_BACK_CODE = 1;


    private static class ViewHolder {
        TextView ticket_id;
        TextView name;
        TextView company_name;
        TextView usertype;
        TextView problemtype;
        TextView description;
        TextView address;
        TextView complaint_reg_time;
        TextView complaint_reg_date;
        TextView allotted_date;
        TextView allotted_slot;

        ImageView iv_raised_again;
        Button btn_close;
        Button btn_partial;

        LinearLayout ll_company_name;
        View view_ticket_status;
    }

    CustomAdaptorNewComplaint(ArrayList<ComplaintModel> data, Context context) {
        super(context, R.layout.new_complaint_activity, data);
        this.complaints = data;
        this.mContext = context;

    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ComplaintModel compplaintModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.new_complaint_activity, parent, false);
            viewHolder.ticket_id = convertView.findViewById(R.id.tv_id_ticketid);
            viewHolder.name = convertView.findViewById(R.id.tv_id_name);
            viewHolder.company_name = convertView.findViewById(R.id.tv_id_company_name);
            viewHolder.usertype = convertView.findViewById(R.id.tv_id_usertype);
            viewHolder.problemtype = convertView.findViewById(R.id.tv_id_problemtype);
            viewHolder.description = convertView.findViewById(R.id.tv_id_description);
            viewHolder.address = convertView.findViewById(R.id.tv_id_address);

            viewHolder.complaint_reg_time = convertView.findViewById(R.id.tv_id_reg_time);
            viewHolder.complaint_reg_date = convertView.findViewById(R.id.tv_id_reg_date);
            viewHolder.iv_raised_again = convertView.findViewById(R.id.iv_id_raisedagain);
            viewHolder.allotted_date = convertView.findViewById(R.id.tv_id_allotted_date);
            viewHolder.allotted_slot = convertView.findViewById(R.id.tv_id_allotted_slot);

            viewHolder.btn_close = convertView.findViewById(R.id.btn_id_close);
            viewHolder.btn_partial = convertView.findViewById(R.id.btn_id_partially_done);

            viewHolder.ll_company_name = convertView.findViewById(R.id.ll_id_company_name);

            viewHolder.view_ticket_status = convertView.findViewById(R.id.view_id_ticket_status_color);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        assert compplaintModel != null;

        viewHolder.iv_raised_again.setVisibility(View.GONE);
        viewHolder.ll_company_name.setVisibility(View.GONE);


        viewHolder.ticket_id.setText(compplaintModel.getTicket_id());
        viewHolder.name.setText(compplaintModel.getName());

        if (compplaintModel.getCompany_name() != null && !compplaintModel.getCompany_name().isEmpty()) {
            viewHolder.company_name.setText(compplaintModel.getCompany_name());
            viewHolder.ll_company_name.setVisibility(View.VISIBLE);
        }

        viewHolder.usertype.setText(compplaintModel.getUser_type());
        viewHolder.problemtype.setText(compplaintModel.getProblem_type());
        viewHolder.description.setText(compplaintModel.getDescription());
        viewHolder.address.setText(compplaintModel.getAddress());

        viewHolder.complaint_reg_time.setText(compplaintModel.getComplaint_reg_time());
        viewHolder.complaint_reg_date.setText(compplaintModel.getComplaint_reg_date());
        viewHolder.allotted_date.setText(compplaintModel.getAllotted_date());
        viewHolder.allotted_slot.setText(compplaintModel.getAllotted_slot());

        if (compplaintModel.getRaisedagain().equals("1")) {
            viewHolder.iv_raised_again.setVisibility(View.VISIBLE);
        }

        switch (compplaintModel.getTicketstatus()) {
            case Constants.TICKET_PENDING:
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_pending);
                break;
            case Constants.TICKET_IN_PROCESS:
                viewHolder.btn_close.setVisibility(View.VISIBLE);
                viewHolder.btn_partial.setVisibility(View.VISIBLE);
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_in_process);
                break;
            case Constants.TICKET_PROCESSED:
                viewHolder.btn_close.setVisibility(View.GONE);
                viewHolder.btn_partial.setVisibility(View.GONE);
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_processed);
                break;
            case Constants.TICKET_PROCESSED_PARTIALLY:
                viewHolder.btn_close.setVisibility(View.VISIBLE);
                viewHolder.btn_partial.setVisibility(View.GONE);
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_processed_partially);
                break;
            case Constants.TICKET_CLOSED:
                viewHolder.btn_close.setVisibility(View.GONE);
                viewHolder.btn_partial.setVisibility(View.GONE);
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_closed);
                break;
            default:
                break;
        }

        viewHolder.btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(mContext, SolutionActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    array_in_custom_adaptor = STATIC_ALL_COMPLAINTS;
                    specific_complaint_in_custom_adaptor = (JSONObject) array_in_custom_adaptor.get(position);
                    i.putExtra(Constants.IE_KEY_PROBLEM, specific_complaint_in_custom_adaptor.get(Constants.KEY_DESCRIPTION).toString());
                    i.putExtra(Constants.IE_KEY_TICKET_ID, specific_complaint_in_custom_adaptor.get(Constants.KEY_TICKET_ID).toString());
                    i.putExtra(Constants.IE_KEY_PARTIAL_CLOSE, Constants.CLOSE_TICKET);   //3:partial  2:close
                    mContext.startActivity(i);

                } catch (Exception e) {
                    Log.d("Exception in Dialog", e.toString());
                }

                Log.d("POSITION", String.valueOf(position));

            }
        });
        viewHolder.btn_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //SolutionActivity sa = new SolutionActivity(mContext);
                    Intent i = new Intent(mContext, SolutionActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    array_in_custom_adaptor = STATIC_ALL_COMPLAINTS;
                    specific_complaint_in_custom_adaptor = (JSONObject) array_in_custom_adaptor.get(position);
                    i.putExtra(Constants.IE_KEY_PROBLEM, specific_complaint_in_custom_adaptor.get(Constants.KEY_DESCRIPTION).toString());
                    i.putExtra(Constants.IE_KEY_TICKET_ID, specific_complaint_in_custom_adaptor.get(Constants.KEY_TICKET_ID).toString());
                    i.putExtra(Constants.IE_KEY_PARTIAL_CLOSE, Constants.PARTIALLY_CLOSE_TICKET);   //3:partial  2:close

                    mContext.startActivity(i);

                } catch (Exception e) {
                    Log.d("Exception in Dialog", e.toString());
                }

                Log.d("POSITION", String.valueOf(position));
                // viewHolder.ticket_id.setOnClickListener(this);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()){
            case R.id.btn_id_partially_done:
                //todo it later because not getting the position
                break;
            case R.id.btn_id_close:
                break;
        }*/

    }


}
