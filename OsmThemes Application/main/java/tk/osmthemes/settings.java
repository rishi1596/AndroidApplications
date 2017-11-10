package tk.osmthemes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import tk.osmthemes.R;

/**
 * Created by Tushar on 04-07-2017.
 */

public class settings extends Fragment {
    CheckBox push_notification;
    SharedPreferences newspushnotification;
    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settingsfrag,container,false);
        newspushnotification =  getContext().getSharedPreferences("newspush", 0);
        boolean state = newspushnotification.getBoolean("newspush",true);


        push_notification = (CheckBox)view.findViewById(R.id.notify);
        push_notification.setChecked(state);
        push_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b)
                {
                    editor =  newspushnotification.edit();
                    editor.putBoolean("newspush",false);
                    editor.commit();
                    //Toast.makeText(getActivity().getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editor = newspushnotification.edit();
                    editor.putBoolean("newspush",true);
                    editor.commit();
                    //Toast.makeText(getActivity().getApplicationContext(),"ON",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Settings");
    }
}