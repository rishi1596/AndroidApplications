package rj.bkinfotech.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import rj.bkinfotech.Constants.Constants;
import rj.bkinfotech.ReusableCodeAdmin;
import rj.bkinfotech.TaskCompleted;

public class ResetUserDetailsAsync extends AsyncTask<String, Void, String> {

    private Context mContext;
    private TaskCompleted mCallBack;
    private ProgressDialog pg;

    public ResetUserDetailsAsync(Context context) {
        this.mContext = context;
        this.mCallBack = (TaskCompleted) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            pg = new ProgressDialog(mContext);
            pg.setMessage("Processing... Please Wait!");
            pg.setCancelable(false);
            pg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            String url = Constants.url + params[1];
            ReusableCodeAdmin.sendCredentials(params[0], url);
            return ReusableCodeAdmin.getCredentialsResponse();
        } catch (Exception e) {
            pg.dismiss();
            Log.d("Resuable Code", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            pg.dismiss();
            mCallBack.onTaskComplete(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}