package nadol.nadol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by jimeet29 on 13-05-2017.
 */
public class settings extends AppCompatActivity {
Switch newsswitch , cricketswitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final SharedPreferences newspushnotification = getSharedPreferences("newspush",1);
        final SharedPreferences cricketpushnotification = getSharedPreferences("cricketpush",1);

        newsswitch = (Switch)findViewById(R.id.newsswitch);
        cricketswitch = (Switch)findViewById(R.id.cricketswitch);
        newsswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    SharedPreferences.Editor editor=newspushnotification.edit();
                    editor.putBoolean("newspush",false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences.Editor editor=newspushnotification.edit();
                    editor.putBoolean("newspush",true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"On",Toast.LENGTH_SHORT).show();
                }

            }
        });
        cricketswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    SharedPreferences.Editor editor=cricketpushnotification.edit();
                    editor.putBoolean("cricketpush",false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences.Editor editor=cricketpushnotification.edit();
                    editor.putBoolean("cricketpush",true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"On",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
