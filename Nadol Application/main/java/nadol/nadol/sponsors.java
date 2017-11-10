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
public class sponsors extends AppCompatActivity {

    DBController controller = new DBController(this);
    HashMap<String,String> sponsorfromserver;
    SwipeRefreshLayout srl;
    ListView sponsorlist;
    Timer reload;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sponsoractivity);
        sponsorlist = (ListView)findViewById(R.id.sponsorlist);
        empty = (TextView) findViewById(R.id.empty);
        reload = new Timer();
        srl = (SwipeRefreshLayout)findViewById(R.id.refreshlayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                syncSQLiteMySQLDB();

            }

        });

        ArrayList<HashMap<String, String>> sponsor = controller.getsponsor();
        if(sponsor.size() != 0)
        {
            empty.setText(" ");
            ListAdapter sponsoradapter = new SimpleAdapter(sponsors.this, sponsor, R.layout.sponsoractivitymain, new String[]{"sponsorid", "sponsorthing","sponsorname"},
                    new int[]{R.id.sponsorid, R.id.sponsorthing,R.id.sponsorname});
            sponsorlist = (ListView)findViewById(R.id.sponsorlist);
            try
            {
                assert sponsorlist != null;
                sponsorlist.setAdapter(sponsoradapter);

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


        client.post("http://karishmaexim.com/npl/getsponsor.php", params, new AsyncHttpResponseHandler() {

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

            JSONArray allsponsor = new JSONArray(res);
            if(allsponsor.length()!=0)
            {
                controller.deletesponsor();
                for(int i = 0; i<allsponsor.length(); i++)
                {
                    JSONObject obj = (JSONObject)allsponsor.get(i);
                    sponsorfromserver = new HashMap<>();
                    sponsorfromserver.put("sponsorid",obj.get("id").toString());
                    sponsorfromserver.put("sponsorthing",obj.get("thing").toString());
                    sponsorfromserver.put("sponsorname",obj.get("name").toString());


                    controller.insertsponsor(sponsorfromserver);
                }
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), sponsors.class);
        finish();
        startActivity(objIntent);

    }
}
