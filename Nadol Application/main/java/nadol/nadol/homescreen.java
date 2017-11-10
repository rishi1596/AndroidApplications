package nadol.nadol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by jimeet29 on 13-05-2017.
 */
public class homescreen extends AppCompatActivity {
    GridView grid;
    String[] web = {
            "News",
            "NPL",
            "NPL Sponsors",
            "Settings",
            "Feedback"
    } ;
    int[] imageId = {
            R.drawable.news,
            R.drawable.cricket,
            R.drawable.sponsors,
            R.drawable.settings,
            R.drawable.review
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);


        customgridhomescreen adapter = new customgridhomescreen(homescreen.this, web, imageId);
        grid=(GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(homescreen.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
                switch(position)
                {
                    case 0:
                        Intent news = new Intent(getApplicationContext(),newsscreen.class);//TODO
                        startActivity(news);
                        break;
                    case 1:
                        Intent cricket = new Intent(getApplicationContext(),cricketscreen.class);  //TODO
                        startActivity(cricket);
                        break;
                    case 2:
                        Intent sponsors = new Intent(getApplicationContext(),sponsors.class); //TODO
                        startActivity(sponsors);
                        break;
                    case 3:
                        Intent settings = new Intent(getApplicationContext(),settings.class); //TODO
                        startActivity(settings);
                        break;
                    case 4:
                        Intent feedback = new Intent(getApplicationContext(),feedback.class);//TODO
                        startActivity(feedback);
                        break;

                    default:
                        break;
                }
            }
        });

    }

}
