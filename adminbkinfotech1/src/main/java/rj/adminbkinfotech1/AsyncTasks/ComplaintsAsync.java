package rj.adminbkinfotech1.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import rj.adminbkinfotech1.Constants.Constants;
import rj.adminbkinfotech1.ReusableCodeAdmin;
import rj.adminbkinfotech1.TaskCompleted;


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

        String url = Constants.url + Constants.getAllComplaintsEP;

        ReusableCodeAdmin.setApiRequest(params[0], url);
        return ReusableCodeAdmin.getApiResponse();
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
