package nadol.nadol;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by jimeet29 on 09-05-2017.
 */
public class checkregistration extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SharedPreferences newspushnotification = getSharedPreferences("newspush",1);
        SharedPreferences.Editor editor=newspushnotification.edit();
        editor.putBoolean("newspush",true);
        editor.apply();

        SharedPreferences cricketpushnotification = getSharedPreferences("cricketpush",1);
        editor=cricketpushnotification.edit();
        editor.putBoolean("cricketpush",true);
        editor.apply();

        SharedPreferences settings = getSharedPreferences("prefs",0);
        boolean firstRun = settings.getBoolean("firstRun",false);
        if(firstRun==false)//if running for first time
        {
            Intent i=new Intent(this,numberscreen.class);
            startActivity(i);
            finish();
        }
        else
        {
            Intent a=new Intent(this,homescreen.class);  // done
            startActivity(a);
            finish();
        }
    }
}


