package rj.bkinfotech.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import rj.bkinfotech.Constants.Constants;
import rj.bkinfotech.ReusableCodeAdmin;
import rj.bkinfotech.TaskCompleted;

/**
 * Created by jimeet29 on 22-12-2017.
 */

public class ComplaintsAsync extends AsyncTask<String, Void, String> {

    private Context mContext;
    private ProgressDialog pg;
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
        String url = Constants.url + Constants.userSpecificComplaintsEP;
        ReusableCodeAdmin.sendCredentials(params[0], url);
        return ReusableCodeAdmin.getCredentialsResponse();
    }


    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            pg.dismiss();
            Log.d("Response JSON", response);
            mCallback.onTaskComplete(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
