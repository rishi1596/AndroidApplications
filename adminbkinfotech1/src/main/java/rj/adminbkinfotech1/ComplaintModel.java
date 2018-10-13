package rj.adminbkinfotech1;

/**
 * Created by jimeet29 on 22-12-2017.
 */

public class ComplaintModel {

    private String ticket_id = "";
    private String name = "";
    private String companyname = "";
    private String number = "";
    private String description = "";
    private String complaint_reg_time = "";
    private String complaint_reg_date = "";
    private String raisedagain = "";
    private String allotted_date = "";
    private String allotted_slot = "";
    private String engineer_appointed = "";
    private String ticketstatus = "";
    private String requested_date = "";
    private String requested_slot = "";
    private String solution = "";
    private String previous_engineer = "";
    private String previous_solution = "";


    public ComplaintModel(String ticket_id, String name, String companyname, String number, String description, String complaint_reg_time, String complaint_reg_date,
                          String raisedagain, String allotted_date, String allotted_slot, String engineerappointed, String ticketstatus,
                          String requested_date, String requested_slot, String solution) {
        this.ticket_id = ticket_id;
        this.name = name;
        this.companyname = companyname;
        this.number = number;
        this.description = description;
        this.complaint_reg_time = complaint_reg_time;
        this.complaint_reg_date = complaint_reg_date;
        this.raisedagain = raisedagain;
        this.allotted_date = allotted_date;
        this.allotted_slot = allotted_slot;
        this.engineer_appointed = engineerappointed;
        this.ticketstatus = ticketstatus;
        this.requested_date = requested_date;
        this.requested_slot = requested_slot;
        this.solution = solution;
    }

    public ComplaintModel(String ticket_id, String name, String companyname, String number, String description, String complaint_reg_time, String complaint_reg_date,
                          String raisedagain, String allotted_date, String allotted_slot, String engineerappointed, String ticketstatus,
                          String requested_date, String requested_slot, String solution, String previous_engineer, String previous_solution) {
        this(ticket_id, name, companyname, number, description, complaint_reg_time, complaint_reg_date, raisedagain, allotted_date, allotted_slot,
                engineerappointed, ticketstatus, requested_date, requested_slot, solution);

        this.previous_engineer = previous_engineer;
        this.previous_solution = previous_solution;

    }


    public String getTicket_id() {
        return ticket_id;
    }

    public String getName() {
        return name;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public String getComplaint_reg_time() {
        return complaint_reg_time;
    }

    public String getComplaint_reg_date() {
        return complaint_reg_date;
    }

    public String getRaisedagain() {
        return raisedagain;
    }

    public String getAllotted_date() {
        return allotted_date;
    }

    public String getAllotted_slot() {
        return allotted_slot;
    }

    public String getEngineer_appointed() {
        return engineer_appointed;
    }

    public String getTicketstatus() {
        return ticketstatus;
    }

    public String getRequested_date() {
        return requested_date;
    }

    public String getRequested_slot() {
        return requested_slot;
    }

    public String getSolution() {
        return solution;
    }

    public String getPrevious_engineer() {
        return previous_engineer;
    }

    public String getPrevious_solution() {
        return previous_solution;
    }
}
