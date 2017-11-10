package nadol.nadol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
 * Created by jimeet29 on 17-05-2017.
 */
public class fixtures extends AppCompatActivity {

    DBController controller = new DBController(this);
    HashMap<String,String> fixturesfromserver;
    SwipeRefreshLayout srl;
    ListView fixturelistt;
    Timer reload;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixtureactivity);
        fixturelistt = (ListView)findViewById(R.id.fixturelist);
        empty = (TextView) findViewById(R.id.empty);
        reload = new Timer();

        srl = (SwipeRefreshLayout)findViewById(R.id.refreshlayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                syncSQLiteMySQLDB();
            }

        });

        ArrayList<HashMap<String, String>> fixtures = controller.getfixtures();

        if(fixtures.size() != 0)
        {empty.setText(" ");
            ListAdapter fixturesadapter = new SimpleAdapter(fixtures.this, fixtures, R.layout.fixtureactivitymain, new String[]{"fixtureid", "matchno","teamalogoid","teama","vs","teamblogoid","teamb"},
                    new int[]{R.id.fixtureid, R.id.matchno, R.id.teamalogo, R.id.teama, R.id.vss,R.id.teamblogo,R.id.teamb});
            /*for (int i = 0; i < news.size(); i++) {
                    System.out.println("NEWSSCREEN RRAYLIST" + (news.get(i)).toString());
            }*/



            try
            {
                assert fixturelistt != null;
                fixturelistt.setAdapter(fixturesadapter);


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else{
            empty.setText("No Updates: Please Swipe downwards to refresh");
            //Toast.makeText(getApplicationContext(), "Empty :Please Swipe downwards to refresh", Toast.LENGTH_SHORT).show();
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


        client.post("http://karishmaexim.com/npl/getfixtures.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String res = new String(responseBody);
                System.out.println("NEWSSSSSS"+res);
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
                controller.deletefixtures();
                for(int i = 0; i<allnews.length(); i++)
                {
                    JSONObject obj = (JSONObject)allnews.get(i);
                    fixturesfromserver = new HashMap<>();
                    fixturesfromserver.put("fixtureid",obj.get("fixtureid").toString());
                    fixturesfromserver.put("matchno",obj.get("matchno").toString());
                    fixturesfromserver.put("teama",obj.get("teama").toString());
                    fixturesfromserver.put("teamalogoid",obj.get("teamalogoid").toString());
                    fixturesfromserver.put("vs",obj.get("vs").toString());
                    fixturesfromserver.put("teamb",obj.get("teamb").toString());
                    fixturesfromserver.put("teamblogoid",obj.get("teamblogoid").toString());
                    controller.insertfixtures(fixturesfromserver);
                }
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), fixtures.class);
        finish();
        startActivity(objIntent);

    }
}
