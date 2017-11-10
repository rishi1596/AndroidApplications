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
public class results extends AppCompatActivity {

    DBController controller = new DBController(this);
    HashMap<String,String> resultsfromserver;
    SwipeRefreshLayout srl;
    ListView resultslistt;
    Timer reload;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultsactivity);
        resultslistt = (ListView)findViewById(R.id.resultslist);
        empty = (TextView) findViewById(R.id.empty);
        reload = new Timer();

        srl = (SwipeRefreshLayout)findViewById(R.id.refreshlayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                syncSQLiteMySQLDB();
            }

        });

        ArrayList<HashMap<String, String>> news = controller.getresults();


        if(news.size() != 0)
        {
            empty.setText(" ");
            ListAdapter newsadapter = new SimpleAdapter(results.this, news, R.layout.resultsactivitymain, new String[]{"resultsid", "mainresult","mom"},
                    new int[]{R.id.resultid, R.id.mainresult, R.id.mom});
            /*for (int i = 0; i < news.size(); i++) {
                    System.out.println("NEWSSCREEN RRAYLIST" + (news.get(i)).toString());
            }*/



            try
            {
                assert resultslistt != null;
                resultslistt.setAdapter(newsadapter);



            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else{
            empty.setText("No Updates: Please Swipe downwards to refresh");
            //Toast.makeText(getApplicationContext(),"Empty :Please Swipe downwards to refresh", Toast.LENGTH_SHORT).show();
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


        client.post("http://karishmaexim.com/npl/getmainresult.php", params, new AsyncHttpResponseHandler() {

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
                controller.deleteresults();
                for(int i = 0; i<allnews.length(); i++)
                {
                    JSONObject obj = (JSONObject)allnews.get(i);
                    resultsfromserver = new HashMap<>();
                    resultsfromserver.put("resultid",obj.get("resultid").toString());
                    resultsfromserver.put("mainresult",obj.get("mainresult").toString());
                    resultsfromserver.put("mom",obj.get("mom").toString());

                    controller.insertresults(resultsfromserver);
                }
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), results.class);
        finish();
        startActivity(objIntent);

    }
}