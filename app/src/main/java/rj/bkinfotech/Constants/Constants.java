package rj.bkinfotech.Constants;

public class Constants {
    public static final int clientId = 1;
    //Url
    public static final String url = "http://bkinfotech.in/app/";

    //Api endpoint
    public static final String registerNewUserEP = "newUserDetails.php";
    public static final String userSpecificComplaintsEP = "getSpecificUserComplaint.php";

    //For API Hit KEY for Json
    public static final String strClientIdKey = "clientId";

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
    public static final String ERROR_CODE_107 = "107";

    public static final String ERROR_CODE_100_AND_DEFAULT_MSG = "You are not a registered user. Please contact the authority.";
    public static final String ERROR_CODE_101_MSG = "Failed to insert customer details";
    public static final String ERROR_CODE_102_MSG = "Failed to insert complaint details";
    public static final String ERROR_CODE_103_MSG = "Failed to insert ticketcode in feedback table";
    public static final String ERROR_CODE_104_MSG = "Username doesn't exist or not a authorized user.";
    public static final String ERROR_CODE_105_MSG = "Address of the given number not found";
    public static final String ERROR_CODE_106_MSG = "Number Already Registered! or User Already Exist!";
    public static final String ERROR_CODE_107_MSG = "Failed to update customer details!";

    //SharedPreferences
    public static final String sharedPreferencesFileNameSettings = "settings";
    public static final String sharedPreferencesFileNameReset = "reset";
    public static final int sharedPreferencesAccessMode = 0;
    public static final String sharedPreferencesMobileNo = "mobileno";
    public static final String sharedPreferencesFirstRun = "fr";
    public static final String sharedPreferencesDontShowAutoStartPermissionDialog = "dontshowautostartpermissiondialog";

    //Dialog Box button texts
    public static final String strYes = "Yes";
    public static final String strNo = "No";

    //Channel Id for notification
    public static final String CHANNEL_ID = "1510";
    public static final int NOTIFICATION_ID = 0;

    //Manufacturers
    public static final String devicesArr[] = {"oppo", "vivo", "xiaomi", "Letv", "Honor"};
}

