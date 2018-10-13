package rj.adminbkinfotech1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jimeet29 on 23-12-2017.
 */

public class CustomAdaptorNewComplaint extends ArrayAdapter<ComplaintModel> implements View.OnClickListener {


    private ArrayList<ComplaintModel> complaints;
    private Context mContext;

    private static class ViewHolder {
        TextView ticket_id;
        TextView name;
        TextView company_name;
        TextView number;
        TextView description;
        TextView previous_engineer_appointed;
        TextView previous_engineer_solution;
        TextView engineer_appointed;
        TextView allotted_date;
        TextView allotted_slot;
        TextView requested_date;
        TextView requested_slot;
        TextView solution;
        TextView registration_time;
        TextView registration_date;
        LinearLayout ll_engineer_name, ll_appointment_details, ll_requested_details, ll_solution, ll_previous_engineer, ll_previous_solution, ll_company_name;

        ImageView iv_raised_again, iv_ticket_status;
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ComplaintModel compplaintModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.new_complaint_activity, parent, false);
            viewHolder.ticket_id = (TextView) convertView.findViewById(R.id.tv_id_ticketid);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_id_name);
            viewHolder.company_name = (TextView) convertView.findViewById(R.id.tv_id_company_name);
            viewHolder.number = (TextView) convertView.findViewById(R.id.tv_id_number);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_id_description);
            viewHolder.previous_engineer_appointed = (TextView) convertView.findViewById(R.id.tv_id_previous_engineer_appointed);
            viewHolder.previous_engineer_solution = (TextView) convertView.findViewById(R.id.tv_id_previous_solution);
            viewHolder.engineer_appointed = (TextView) convertView.findViewById(R.id.tv_id_engineer_appointed);
            viewHolder.allotted_date = (TextView) convertView.findViewById(R.id.tv_id_allotted_date);
            viewHolder.allotted_slot = (TextView) convertView.findViewById(R.id.tv_id_allotted_slot);
            viewHolder.requested_date = (TextView) convertView.findViewById(R.id.tv_id_requested_date);
            viewHolder.requested_slot = (TextView) convertView.findViewById(R.id.tv_id_requested_slot);
            viewHolder.solution = (TextView) convertView.findViewById(R.id.tv_id_engineer_solution);


            viewHolder.registration_time = (TextView) convertView.findViewById(R.id.tv_id_registration_time);
            viewHolder.registration_date = (TextView) convertView.findViewById(R.id.tv_id_registration_date);

            viewHolder.iv_raised_again = (ImageView) convertView.findViewById(R.id.iv_id_raisedagain);

            viewHolder.ll_engineer_name = (LinearLayout) convertView.findViewById(R.id.ll_id_engineer_name);
            viewHolder.ll_appointment_details = (LinearLayout) convertView.findViewById(R.id.ll_id_appointment);
            viewHolder.ll_requested_details = (LinearLayout) convertView.findViewById(R.id.ll_id_requested_appointment);
            viewHolder.ll_solution = (LinearLayout) convertView.findViewById(R.id.ll_id_engineer_solution);
            viewHolder.ll_previous_engineer = (LinearLayout) convertView.findViewById(R.id.ll_id_previous_engineer);
            viewHolder.ll_previous_solution = (LinearLayout) convertView.findViewById(R.id.ll_id_previous_solution);
            viewHolder.ll_company_name = (LinearLayout) convertView.findViewById(R.id.ll_id_company_name);

            viewHolder.view_ticket_status = convertView.findViewById(R.id.view_id_ticket_status_color);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        assert compplaintModel != null;
        viewHolder.ll_engineer_name.setVisibility(View.GONE);
        viewHolder.ll_appointment_details.setVisibility(View.GONE);
        viewHolder.ll_requested_details.setVisibility(View.GONE);
        viewHolder.ll_solution.setVisibility(View.GONE);
        viewHolder.ll_previous_engineer.setVisibility(View.GONE);
        viewHolder.ll_previous_solution.setVisibility(View.GONE);
        viewHolder.ll_company_name.setVisibility(View.GONE);
        viewHolder.iv_raised_again.setVisibility(View.GONE);

        if (compplaintModel.getRaisedagain().equals("1")) {
            viewHolder.iv_raised_again.setVisibility(View.VISIBLE);
            if (compplaintModel.getEngineer_appointed().equalsIgnoreCase("null")) {
                viewHolder.previous_engineer_appointed.setText(compplaintModel.getPrevious_engineer());
                viewHolder.ll_previous_engineer.setVisibility(View.VISIBLE);
                viewHolder.previous_engineer_solution.setText(compplaintModel.getPrevious_solution());
                viewHolder.ll_previous_solution.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.ticket_id.setText(compplaintModel.getTicket_id());
        viewHolder.name.setText(compplaintModel.getName());
        viewHolder.number.setText(compplaintModel.getNumber());
        viewHolder.description.setText(compplaintModel.getDescription());


        if (!compplaintModel.getCompanyname().equalsIgnoreCase("null") && !compplaintModel.getCompanyname().isEmpty()) {
            viewHolder.company_name.setText(compplaintModel.getCompanyname());
            viewHolder.ll_company_name.setVisibility(View.VISIBLE);
        }

        viewHolder.registration_time.setText(compplaintModel.getComplaint_reg_time());
        viewHolder.registration_date.setText(compplaintModel.getComplaint_reg_date());


        switch (compplaintModel.getTicketstatus()) {
            case "Pending":
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_pending);
                break;
            case "Ticket In Process":
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
            case "Ticket Processed":
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_processed);
                break;
            case "Ticket Processed Partially":
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_processed_partially);
                break;
            case "Closed":  //for raise again button
                viewHolder.solution.setText(compplaintModel.getSolution());
                viewHolder.ll_solution.setVisibility(View.VISIBLE);
                viewHolder.view_ticket_status.setBackgroundResource(R.color.ticket_closed);
                break;
            default:
                break;
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
