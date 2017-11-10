package nadol.nadol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jimeet29 on 23-04-2017.
 */
public class newsscreen extends AppCompatActivity {

    DBController controller = new DBController(this);
    HashMap<String,String> newsfromserver;
    SwipeRefreshLayout srl;
    ListView newslistt;
    Timer reload;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsactivity);
        newslistt = (ListView)findViewById(R.id.newslist);
        empty = (TextView) findViewById(R.id.empty);

        reload = new Timer();

        srl = (SwipeRefreshLayout)findViewById(R.id.refreshlayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                syncSQLiteMySQLDB();
            }

        });

        ArrayList<HashMap<String, String>> news = controller.getnews();

        if(news.size() != 0)
        {
            empty.setText(" ");
            ListAdapter newsadapter = new MyAdapter(newsscreen.this, news, R.layout.newsactivitymain, new String[]{"newsid", "newstitle","newscontent","newsimage","newstime"},
                    new int[]{R.id.newsid, R.id.newstitle, R.id.newscontent, R.id.newsimage, R.id.newstime});
            /*for (int i = 0; i < news.size(); i++) {
                    System.out.println("NEWSSCREEN RRAYLIST" + (news.get(i)).toString());
            }*/



            try
            {
                assert newslistt != null;
                newslistt.setAdapter(newsadapter);

                newslistt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String newsid = String.valueOf(position+1);

                        TextView textView = (TextView) view.findViewById(R.id.newsid);
                        String newsid = textView.getText().toString();

                        //String newsid=(String)parent.getItemAtPosition(position);
                        System.out.println("Newsid "+newsid);
                        Intent specificnews = new Intent(getApplicationContext(),specificnews.class);
                        specificnews.putExtra("newsid",newsid);
                        startActivity(specificnews);

                    }
                });

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    else{
            empty.setText("No Updates: Please Swipe downwards to refresh");
            //Toast.makeText(getApplicationContext(),"Empty :Please Swipe downwards to refresh",Toast.LENGTH_SHORT).show();
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


        client.post("http://karishmaexim.com/npl/getallnews.php", params, new AsyncHttpResponseHandler() {

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
                controller.deletenews();
                for(int i = 0; i<allnews.length(); i++)
                {
                    JSONObject obj = (JSONObject)allnews.get(i);
                    newsfromserver = new HashMap<>();
                    newsfromserver.put("newsid",obj.get("newsid").toString());
                    newsfromserver.put("newstitle",obj.get("newstitle").toString());
                    newsfromserver.put("newscontent",obj.get("newscontent").toString());
                    newsfromserver.put("newsimage",obj.get("newsimage").toString());
                    newsfromserver.put("newstime",obj.get("newstime").toString());
                    controller.insertnews(newsfromserver);
                }
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), newsscreen.class);
        finish();
        startActivity(objIntent);

    }


}
