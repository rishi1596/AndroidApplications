package nadol.nadol;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nadol.nadol.R;

/**
 * Created by jimeet29 on 15-05-2017.
 */
public class cricketscreen extends AppCompatActivity {

    String[] activities = {
            "Live Score",
            "Teams",
            "Fixtures",
            "Results"
    } ;
    int[] activitiesimage = {
            R.drawable.livescore,
            R.drawable.teams,
            R.drawable.images,
            R.drawable.results
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cricketscreen);


        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<=3;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("activityname", activities[i]);
            hm.put("image",Integer.toString(activitiesimage[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "image","activityname" };

        // Ids of views in listview_layout
        int[] to = { R.id.cricketactivity,R.id.cricketactivityname};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.cricketscreenmain, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.cricketlist);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        Intent livescore = new Intent(getApplicationContext(), nadol.nadol.livescore.class);
                        startActivity(livescore);
                        break;

                    case 1:
                        Intent teams = new Intent(getApplicationContext(), nadol.nadol.teams.class);
                        startActivity(teams);

                        break;

                    case 2:
                        Intent fixtures = new Intent(getApplicationContext(), nadol.nadol.fixtures.class);
                        startActivity(fixtures);
                        ;
                        break;

                    case 3:
                        Intent results = new Intent(getApplicationContext(), nadol.nadol.results.class);
                        startActivity(results);
                        break;
                }
            }
        });


    }
}
