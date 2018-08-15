package rj.bkinfotech;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by jimeet29 on 22-12-2017.
 */

public class ComplaintsAsync extends AsyncTask<String, Void, String> {

    //ArrayList<String> complaintmodel;
    ReusableCodeAdmin rca = new ReusableCodeAdmin();
    String url = "http://bkinfotech.in/app/getSpecificUserComplaint.php";
    //String url="http://192.168.1.35:81/bkinfotech/getSpecificUserComplaint.php";
    String responseObject;
    private Context mContext;
    ProgressDialog pg;
    private TaskCompleted mCallback;

    public ComplaintsAsync(Context context) {
        this.mContext = context;
        this.mCallback = (TaskCompleted) context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pg = new ProgressDialog(mContext);
        pg.setMessage("Processing... Please Wait!");
        pg.setCancelable(false);
        pg.show();
    }


    @Override
    protected String doInBackground(String... params) {
        rca.sendCredentials(params[0], url);
        responseObject = rca.getCredentialsResponse();
        return responseObject;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            pg.dismiss();
            Log.d("Response JSON", s);
            mCallback.onTaskComplete(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
