package nadol.nadol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jimeet29 on 15-05-2017.
 */
public class livescore extends AppCompatActivity {

    DBController controller = new DBController(this);
    HashMap<String,String> scorefromserver;
    SwipeRefreshLayout srl;
    ListView scorelist;
    Timer reload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreactivity);
        reload = new Timer();
        srl = (SwipeRefreshLayout)findViewById(R.id.refreshlayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                syncSQLiteMySQLDB();

            }

        });

        ArrayList<HashMap<String, String>> score = controller.getscore();
        if(score.size() != 0)
        {
            ListAdapter scoreadapter = new SimpleAdapter(livescore.this, score, R.layout.scoreactivitymain, new String[]{"scoreid", "matchno","teama","teamalogoid","teamb","teamblogoid","over","score","wicket","target","result"},
                    new int[]{R.id.scoreid, R.id.matchno,R.id.teama,R.id.teamalogo,R.id.teamb,R.id.teamblogo,R.id.overno,R.id.runs,R.id.wicketno,R.id.target,R.id.result});
            scorelist = (ListView)findViewById(R.id.scorelist);
            try
            {
                assert scorelist != null;
                scorelist.setAdapter(scoreadapter);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Empty :Please Swipe downwards to refresh", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.refresh) {
            syncSQLiteMySQLDB();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void syncSQLiteMySQLDB() {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();


        client.post("http://karishmaexim.com/npl/getscore.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String res = new String(responseBody);
                System.out.println("Score"+res);
                updateSQLite(res);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        reload.schedule(new TimerTask() {
            public void run() {

                reloadActivity();
            }
        }, 3000);

    }

    public void updateSQLite(String res) {




        try
        {

            JSONArray allnews = new JSONArray(res);
            if(allnews.length()!=0)
            {
                controller.deletescore();
                for(int i = 0; i<allnews.length(); i++)
                {
                    JSONObject obj = (JSONObject)allnews.get(i);
                    scorefromserver = new HashMap<>();
                    scorefromserver.put("id",obj.get("id").toString());
                    scorefromserver.put("matchno",obj.get("matchno").toString());
                    scorefromserver.put("teama",obj.get("teama").toString());
                    scorefromserver.put("teamalogoid",obj.get("teamalogoid").toString());
                    scorefromserver.put("teamb",obj.get("teamb").toString());
                    scorefromserver.put("teamblogoid",obj.get("teamblogoid").toString());
                    scorefromserver.put("over",obj.get("over").toString());
                    scorefromserver.put("score",obj.get("score").toString());
                    scorefromserver.put("wicket",obj.get("wicket").toString());
                    scorefromserver.put("target",obj.get("target").toString());
                    scorefromserver.put("result",obj.get("result").toString());

                    controller.insertscore(scorefromserver);
                }
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), livescore.class);
        finish();
        startActivity(objIntent);

    }
}
