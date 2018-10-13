package rj.adminbkinfotech1.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import rj.adminbkinfotech1.AdminActivity;
import rj.adminbkinfotech1.Constants.Constants;
import rj.adminbkinfotech1.ReusableCodeAdmin;
import rj.adminbkinfotech1.TaskCompleted;

public class getEngineerNamesOrAddressAsync extends AsyncTask<String, Void, String> {
    private Context mContext;
    private TaskCompleted mCallBack;
    private String TAG = "getEngineerNamesOrAddressAsync";

    public getEngineerNamesOrAddressAsync(Context context) {
        this.mContext = context;
        this.mCallBack = (TaskCompleted) mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            String url = Constants.url + params[1];

            ReusableCodeAdmin.setApiRequest(params[0], url);
            if (params[1].equalsIgnoreCase(Constants.getEngineerNamesEP)) {
                SharedPreferences sp = mContext.getSharedPreferences(Constants.sharedPreferencesFileNameSettings, 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.sharedPreferencesEngineerNames, ReusableCodeAdmin.getApiResponse());
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReusableCodeAdmin.getApiResponse();
    }

    @Override
    protected void onPostExecute(String adminActivityResponse) {
        super.onPostExecute(adminActivityResponse);
        try {
            if (!(mContext instanceof AdminActivity)) {
                mCallBack.onTaskComplete(adminActivityResponse);
            }
        } catch (Exception e) {
            Log.d(TAG, "onPostExecute");
        }
    }
}

