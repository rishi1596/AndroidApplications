package rj.engineerbkinfotech;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by jimeet29 on 26-01-2018.
 */

public class LogInOutAsync extends AsyncTask<String,Void,String> {
    //ArrayList<String> complaintmodel;
    ReusableCodeAdmin rca = new ReusableCodeAdmin();
    //String url="http://bkinfotech.in/app/getAllComplaints.php";
    //String url="http://192.168.1.35:81/bkinfotech/getAllComplaints.php";
    String url="http://bkinfotech.in/app/engineerLogin.php";
    String responseObject;
    private Context mContext;
    ProgressDialog pg;
    private TaskCompleted mCallback;

    public LogInOutAsync(Context context)
    {
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

        rca.sendCredentials(params[0],url);
        responseObject = rca.getCredentialsResponse();
        return responseObject;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pg.dismiss();
        Log.d("Response JSON Login Out",s);
        mCallback.onTaskComplete(s);
    }
}
