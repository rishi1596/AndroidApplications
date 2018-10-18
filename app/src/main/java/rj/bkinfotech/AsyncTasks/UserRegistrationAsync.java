package rj.bkinfotech.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import rj.bkinfotech.Constants.Constants;
import rj.bkinfotech.ReusableCodeAdmin;
import rj.bkinfotech.TaskCompleted;

public class UserRegistrationAsync extends AsyncTask<String, Void, String> {
    private Context mContext;
    private TaskCompleted mTaskCallBack;
    private ProgressDialog pg;
    public UserRegistrationAsync(Context context){
        this.mContext = context;
        this.mTaskCallBack = (TaskCompleted)mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try{
            pg = new ProgressDialog(mContext);
            pg.setMessage("Registration in Process Please Wait!");
            pg.setCancelable(false);
            pg.show();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    protected String doInBackground(String... params) {
        String url = Constants.url + params[1];
        ReusableCodeAdmin.sendCredentials(params[0],url);

        return ReusableCodeAdmin.getCredentialsResponse();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        try{
            Log.d("response",response);
            pg.dismiss();
            mTaskCallBack.onTaskComplete(response);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
