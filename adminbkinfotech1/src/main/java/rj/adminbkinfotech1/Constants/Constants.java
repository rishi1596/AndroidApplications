package rj.adminbkinfotech1.Constants;

public class Constants {
    //public static final int clientId = 1;
    //DEMO
    public static final int clientId = 22;

    //Url
    public static final String url = "http://bkinfotech.in/app/";

    //Api endpoint
    public static final String registerNewComplaintEP = "registerComplaintAdmin.php";
    public static final String getAllComplaintsEP = "getAllComplaints.php";
    public static final String getEngineerNamesEP = "getEngineersName.php";
    public static final String adminLoginEP = "adminLogin.php";
    public static final String getAddressEP = "getAddress.php";

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

    //Success Response
    public static final String successResponse = "1";
    //Error Codes
    public static final String ERROR_CODE_100 = "100";
    public static final String ERROR_CODE_101 = "101";
    public static final String ERROR_CODE_102 = "102";
    public static final String ERROR_CODE_103 = "103";
    public static final String ERROR_CODE_104 = "104";
    public static final String ERROR_CODE_105 = "105";
    public static final String ERROR_CODE_106 = "106";

    public static final String ERROR_CODE_100_AND_DEFAULT_MSG = "You are not a registered user. Please contact the authority.";
    public static final String ERROR_CODE_101_MSG = "Failed to insert customer details";
    public static final String ERROR_CODE_102_MSG = "Failed to insert complaint details";
    public static final String ERROR_CODE_103_MSG = "Failed to insert ticketcode in feedback table";
    public static final String ERROR_CODE_104_MSG = "Username doesn't exist or not a authorized user.";
    public static final String ERROR_CODE_105_MSG = "Address of the given number not found";
    public static final String ERROR_CODE_106_MSG = "Number Already Registered! or User Already Exist!";


    //SharedPreferences
    public static final String sharedPreferencesFileNameSettings = "settings";
    public static final int sharedPreferencesAccessMode = 0;
    public static final String sharedPreferencesEngineerNames = "engineers";
    public static final String sharedPreferencesUserNames = "username";
    public static final String sharedPreferencesDontShowAutoStartPermissionDialog = "dontshowautostartpermissiondialog";

    public static final String appointment_dates_slots[] = {"Select the Time", "Morning", "Afternoon", "Evening"};

    //getAllComplaintsEP request Codes
    public static final String newComplaints = "1";
    public static final String appointEngineerForAComplaint = "2";
    public static final String inProcessComplaints = "3";
    public static final String allComplaints = "4";

    //login logout request Codes
    public static final String strLogin = "1";
    public static final String strLogout = "2";

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
    public static final String CHANNEL_ID = "1509";
    public static final int NOTIFICATION_ID = 0;

    //Ticket Status
    public static final String TICKET_PENDING = "pending";
    public static final String TICKET_PROCESSED_PARTIALLY = "Ticket Processed Partially";
    public static final String TICKET_IN_PROCESS = "Ticket In Process";
    public static final String TICKET_PROCESSED = "Ticket Processed";
    public static final String TICKET_CLOSED = "Closed";

}
