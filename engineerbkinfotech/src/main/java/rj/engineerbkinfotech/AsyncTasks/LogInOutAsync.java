package rj.engineerbkinfotech.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import rj.engineerbkinfotech.Constants.Constants;
import rj.engineerbkinfotech.ReusableCodeAdmin;
import rj.engineerbkinfotech.TaskCompleted;

/**
 * Created by jimeet29 on 26-01-2018.
 */

public class LogInOutAsync extends AsyncTask<String, Void, String> {

    private Context mContext;
    private ProgressDialog pg;
    private TaskCompleted mCallback;

    public LogInOutAsync(Context context) {
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
        String url = Constants.url + Constants.getEngineerLoginEP;
        ReusableCodeAdmin.sendCredentials(params[0], url);
        return ReusableCodeAdmin.getCredentialsResponse();

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            pg.dismiss();
            Log.d("Response JSON Login Out", s);
            mCallback.onTaskComplete(s);
        } catch (Exception e) {
            Log.w("LogInOutAsync",e.toString());
        }
    }
}
