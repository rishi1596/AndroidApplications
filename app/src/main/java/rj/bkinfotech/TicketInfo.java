package rj.bkinfotech;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jimeet29 on 17-06-2018.
 */

public class TicketInfo extends AppCompatActivity {
    int deviceWidth,deviceHeight;
    TextView tv_info_close;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.info_ticket_status);

        initialize();

        determineDeviceDimensions();

        setWindowDimensions();

        setListeners();

    }



    private void initialize() {
        tv_info_close = (TextView)findViewById(R.id.tv_id_info_close);
    }

    private void determineDeviceDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

    }

    private void setWindowDimensions() {
        //Window Dimension are modified here
        // Method 1
        this.getWindow().setLayout(deviceWidth-150, ViewGroup.LayoutParams.WRAP_CONTENT);

        //Method 2
        /*WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = 1000;
        params.width = 1000;
        this.getWindow().setAttributes(params);*/
    }


    private void setListeners() {
        tv_info_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v.getId() == R.id.tv_id_info_close)
                {
                    finish();
                    return true;
                }

                return false;
            }
        });
    }
}
