package rj.bkinfotech;

/**
 * Created by jimeet29 on 22-12-2017.
 */

public class ComplaintModel {

    private String ticket_id = "";
   /* private String name= "";
    private String company_name= "";*/
    private String user_type = "";
    private String problem_type = "";
    private String description = "";
    private String engineer_appointed = "";
    //private String address= "";
    private String status = "";
    private String complaint_reg_time = "";
    private String complaint_reg_date = "";
    private String raisedagain = "";
    private String allotted_date = "";
    private String allotted_slot = "";
    private String engineer_appointed_time = "";
    private String engineer_appointed_date = "";
    private String requested_date = "";
    private String requested_slot = "";
    private String engineer_close_time = "";
    private String engineer_close_date = "";


    ComplaintModel(String ticket_id, String user_type, String problem_type, String description, String status, String engineer_appointed,
                          String complaint_reg_time,String complaint_reg_date,String raisedagain,String allotted_date,String allotted_slot,
                          String engineer_appointed_time,String engineer_appointed_date,String requested_date,String requested_slot,String engineer_close_time,String engineer_close_date)
    {
        this.ticket_id = ticket_id;
        /*this.name=name;
        this.company_name=company_name;*/
        this.user_type=user_type;
        this.problem_type=problem_type;
        this.description=description;

        this.status=status;
        this.engineer_appointed=engineer_appointed;
        this.complaint_reg_time=complaint_reg_time;
        this.complaint_reg_date=complaint_reg_date;
        this.raisedagain=raisedagain;
        this.allotted_date=allotted_date;
        this.allotted_slot=allotted_slot;
        this.engineer_appointed_time=engineer_appointed_time;
        this.engineer_appointed_date=engineer_appointed_date;
        this.requested_date=requested_date;
        this.requested_slot=requested_slot;
        this.engineer_close_time=engineer_close_time;
        this.engineer_close_date=engineer_close_date;

    }



    public String getTicket_id(){
        return  ticket_id;
    }
    /*
    public String getName(){
        return name;
    }
    public String getCompany_name(){
        return company_name;
    }*/
    public String getUser_type(){
        return user_type;
    }
    public String getProblem_type(){
        return problem_type;
    }
    public String getDescription(){
        return description;
    }
    public String getEngineer_appointed(){
        return  engineer_appointed;
    }
    /*public String getAddress(){
        return address;
    }*/
    public String getStatus(){
        return  status;
    }
    public String getComplaint_reg_time(){
        return  complaint_reg_time;
    }
    public String getComplaint_reg_date(){
        return  complaint_reg_date;
    }
    public String getRaisedagain(){
        return  raisedagain;
    }
    public String getAllotted_date(){
        return  allotted_date;
    }
    public String getAllotted_slot(){
        return  allotted_slot;
    }
    public String getEngineer_appointed_time(){
        return  engineer_appointed_time;
    }
    public String getEngineer_appointed_date(){
        return  engineer_appointed_date;
    }
    public String getRequested_date(){
        return  requested_date;
    }
    public String getRequested_slot(){
        return  requested_slot;
    }
    public String getEngineer_close_time(){
        return  engineer_close_time;
    }
    public String getEngineer_close_date(){
        return  engineer_close_date;
    }


}
