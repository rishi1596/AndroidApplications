package tk.osmthemes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by jimeet29 on 30-07-2017.
 */

public class checkregistration extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*boolean firstRun = settings.getBoolean("firstRun",false);
        if(firstRun==false)//if running for first time
        {*/

            Intent i=new Intent(this,homeScreen.class);
            startActivity(i);
            finish();
        /*}
        else
        {
            Intent a=new Intent(this,MainActivity.class);  // done
            startActivity(a);
            finish();
        }*/
    }
}


