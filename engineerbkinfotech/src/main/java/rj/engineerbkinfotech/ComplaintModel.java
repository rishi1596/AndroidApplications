package rj.engineerbkinfotech;

/**
 * Created by jimeet29 on 22-12-2017.
 */

public class ComplaintModel {

    private String ticket_id = "";
    private String name = "";
    private String company_name = "";
    private String user_type = "";
    private String problem_type = "";
    private String description = "";
    private String address = "";
    //private String engineerstatus = "";
    private String complaint_reg_time = "";
    private String complaint_reg_date = "";
    private String allotted_date = "";
    private String allotted_slot = "";
    private String ticketstatus = "";
    private String raisedagain = "";
    //private final String status;

    public ComplaintModel(String ticket_id, String name, String company_name, String user_type, String problem_type,
                          String description, String address, String complaint_reg_time, String complaint_reg_date,
                          String allotted_date, String allotted_slot, String ticketstatus, String raisedagain) {
        this.ticket_id = ticket_id;
        this.name = name;
        this.company_name = company_name;
        this.user_type = user_type;
        this.problem_type = problem_type;
        this.description = description;
        this.address = address;
        //this.engineerstatus = engineerstatus;
        this.complaint_reg_time = complaint_reg_time;
        this.complaint_reg_date = complaint_reg_date;
        this.allotted_date = allotted_date;
        this.allotted_slot = allotted_slot;
        this.ticketstatus = ticketstatus;
        this.raisedagain = raisedagain;
    }


    public String getTicket_id() {
        return ticket_id;
    }

    public String getName() {
        return name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getProblem_type() {
        return problem_type;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    /* public String getEngineerstatus(){
         return engineerstatus;
     }*/
    public String getComplaint_reg_time() {
        return complaint_reg_time;
    }

    public String getComplaint_reg_date() {
        return complaint_reg_date;
    }

    public String getAllotted_date() {
        return allotted_date;
    }

    public String getAllotted_slot() {
        return allotted_slot;
    }

    public String getTicketstatus() {
        return ticketstatus;
    }

    public String getRaisedagain() {
        return raisedagain;
    }

}
