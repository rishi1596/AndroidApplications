package rj.engineerbkinfotech.Constants;

public class Constants {
    //public static final int clientId = 1;
    //DEMO
    public static final int clientId = 22;

    //Url
    public static final String url = "http://bkinfotech.in/app/";

    //Api endpoint
    public static final String getEngineerComplaintsEP = "getSpecificEngineerComplaint.php";
    public static final String getEngineerLoginEP = "engineerLogin.php";

    //For API Hit 'keys'
    public static final String strClientIdKey = "clientId";
    public static final String strUserNameKey = "username";
    public static final String strPasswordKey = "password";
    public static final String strTokenKey = "token";
    public static final String strRegisteredNoKey = "register_no";
    public static final String strCodeKey = "code";

    //Key Names of json response from api
    public static final String keyErrorCode = "error_code";
    public static final String keyResponse = "response";


    //SharedPreferences
    public static final String sharedPreferencesFileNameSettings = "settings";
    public static final int sharedPreferencesAccessMode = 0;
    public static final String sharedPreferencesMobileNo = "mobileno";
    public static final String sharedPreferencesFirstRun = "fr";
    public static final String sharedPreferencesDontShowAutoStartPermissionDialog = "dontshowautostartpermissiondialog";

    //Each Ticket object KEYS
    public static final String KEY_TICKET_STATUS = "ticketstatus";
    public static final String KEY_TICKET_ID = "ticket_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPANY_NAME = "companyname";
    public static final String KEY_USER_TYPE = "usertype";
    public static final String KEY_PROBLEM_TYPE = "problemtype";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_COMPLAINT_REG_TIME = "complaint_reg_time";
    public static final String KEY_COMPLAINT_REG_DATE = "complaint_reg_date";
    public static final String KEY_RAISED_AGAIN = "raisedagain";
    public static final String KEY_ALLOTTED_DATE = "allotted_date";
    public static final String KEY_ALLOTTED_SLOT = "allotted_slot";
    public static final String KEY_ENGINEER_APPOINTED = "engineerappointed";
    public static final String KEY_ENGINEER_APPOINTED_TIME = "engineer_appointed_time";
    public static final String KEY_ENGINEER_APPOINTED_DATE = "engineer_appointed_date";
    public static final String KEY_REQUESTED_DATE = "requested_date";
    public static final String KEY_REQUESTED_SLOT = "requested_slot";
    public static final String KEY_ENGINEER_CLOSE_TIME = "engineer_close_time";
    public static final String KEY_ENGINEER_CLOSE_DATE = "engineer_close_date";
    public static final String KEY_ENGINEER_SIDE_STATUS = "engineersidestatus";
    public static final String KEY_ADDRESS = "address";
    //Manufacturers
    public static final String devicesArr[] = {"oppo", "vivo", "xiaomi", "Letv", "Honor"};

    //Channel Id for notification
    public static final String CHANNEL_ID = "1511";
    public static final int NOTIFICATION_ID = 0;

    //Ticket Status
    public static final String TICKET_PENDING = "pending";
    public static final String TICKET_PROCESSED_PARTIALLY = "Ticket Processed Partially";
    public static final String TICKET_IN_PROCESS = "Ticket In Process";
    public static final String TICKET_PROCESSED = "Ticket Processed";
    public static final String TICKET_CLOSED = "Closed";

    //Intent Extras Key
    public static final String IE_KEY_TICKET_ID = "ticket_id";
    public static final String IE_KEY_PROBLEM = "Problem";
    public static final String IE_KEY_PARTIAL_CLOSE = "Partial/Close";

    //Partial And Close code for solutionactivity
    public static final String CLOSE_TICKET = "2";
    public static final String PARTIALLY_CLOSE_TICKET = "3";
}
