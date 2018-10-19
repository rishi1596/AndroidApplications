package rj.engineerbkinfotech.CustomDialogEngineer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import rj.engineerbkinfotech.Constants.Constants;
import rj.engineerbkinfotech.R;

public class CustomDialogBoxEngineer extends DialogFragment implements View.OnClickListener {
    private CheckBox cbDontShowItAgain;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_dialog_engineer, container, false);

        cbDontShowItAgain = v.findViewById(R.id.cb_id_dont_show_it_again);
        Button btnCancel = v.findViewById(R.id.btn_id_cancel);
        Button btnSettings = v.findViewById(R.id.btn_id_settings);

        btnCancel.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_id_cancel:
                storeCheckBoxStateInSP(cbDontShowItAgain.isChecked());
                dismiss();
                break;
            case R.id.btn_id_settings:
                storeCheckBoxStateInSP(cbDontShowItAgain.isChecked());
                startSettingsActivity();
                dismiss();
                break;
        }
    }

    private void startSettingsActivity() {
        Intent settingsIntent = new Intent();
        String manufacturer = Build.MANUFACTURER;
        try {
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                settingsIntent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                settingsIntent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                settingsIntent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                settingsIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                settingsIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            } else {
                settingsIntent.setAction(android.provider.Settings.ACTION_SETTINGS);
            }
        } catch (Exception e) {
            Log.e("Custom Dialog", e.toString());
        }
        try {
            startActivity(settingsIntent);
        } catch (Exception e) {
            Log.e("Custom Dialog1", e.toString());
        } finally {
            dismiss();
        }
    }

    private void storeCheckBoxStateInSP(boolean checked) {
        SharedPreferences sp = getContext().getSharedPreferences
                (Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constants.sharedPreferencesDontShowAutoStartPermissionDialog, checked);
        editor.apply();
    }
}
