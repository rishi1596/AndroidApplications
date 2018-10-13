package rj.adminbkinfotech1.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import rj.adminbkinfotech1.Constants.Constants;
import rj.adminbkinfotech1.ReusableCodeAdmin;
import rj.adminbkinfotech1.TaskCompleted;

public class RegisterComplaintAsync extends AsyncTask<String, Void, String> {

    private Context mContext;
    private TaskCompleted mCallBack;
    private ProgressDialog pg;

    public RegisterComplaintAsync(Context context) {
        this.mContext = context;
        this.mCallBack = (TaskCompleted) mContext;
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
        try {
            String url = Constants.url + Constants.registerNewComplaintEP;
            ReusableCodeAdmin.setApiRequest(params[0], url);
            return ReusableCodeAdmin.getApiResponse();
        } catch (Exception e) {
            pg.dismiss();
            Log.d("REGISTER E ACITIVITY", String.valueOf(e));
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            pg.dismiss();
            Log.d("Response JSON Register", s);
            mCallBack.onTaskComplete(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
