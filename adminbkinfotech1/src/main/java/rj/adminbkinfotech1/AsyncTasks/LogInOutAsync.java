package rj.adminbkinfotech1.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import rj.adminbkinfotech1.Constants.Constants;
import rj.adminbkinfotech1.ReusableCodeAdmin;
import rj.adminbkinfotech1.TaskCompleted;

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
            String url = Constants.url + Constants.adminLoginEP;
            ReusableCodeAdmin.setApiRequest(params[0], url);
            return ReusableCodeAdmin.getApiResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            pg.dismiss();
            Log.d("Response JSON Login Out", s);
            mCallback.onTaskComplete(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
