package rj.bkinfotech.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import rj.bkinfotech.Constants.Constants;
import rj.bkinfotech.ReusableCodeAdmin;
import rj.bkinfotech.TaskCompleted;

public class RegisterComplaintHit extends AsyncTask<String, Void, String> {
    private Context mContext;
    private TaskCompleted mTaskCallBack;
    private ProgressDialog pg;

    public RegisterComplaintHit(Context context) {
        this.mContext = context;
        this.mTaskCallBack = (TaskCompleted) mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pg = new ProgressDialog(mContext);
        pg.setMessage("Processing please wait!");
        pg.setCancelable(false);
        pg.show();

    }


    @Override
    protected String doInBackground(String... params) {

        String url = Constants.url + params[1];
        ReusableCodeAdmin.sendCredentials(params[0], url);

        return ReusableCodeAdmin.getCredentialsResponse();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        try {
            Log.d("response", response);
            pg.dismiss();
            mTaskCallBack.onTaskComplete(response);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

