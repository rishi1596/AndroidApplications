package rj.bkinfotech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jimeet29 on 17-06-2018.
 */

public class AboutUs extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_privacy_policy;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        tv_privacy_policy = findViewById(R.id.tv_id_privacy_policy);
        tv_privacy_policy.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_id_privacy_policy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://bkinfotech.in/privacy_policy_app.html"));
                startActivity(browserIntent);
                break;
        }
    }
}
