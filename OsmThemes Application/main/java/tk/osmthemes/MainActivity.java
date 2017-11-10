package tk.osmthemes;

/**
 * Created by jimeet29 on 10-07-2017.
 */
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private ProgressDialog mProgressDialog;

    TextView themename,madeby,downcount,likecount,dislikecount,profilename,profileemail;
    Button btndownload;
    ImageView ss1,ss2,ss3,likebtn,dislikebtn,profilephoto;

    String themenamest;
    String dwnlink;
    String idtmp;
    int downcounttmp = 0;
    int jsonloded = 0;
    public int dsize,tsize=0;
    public String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private int STORAGE_PERMISSION_CODE = 23;
    public ProgressDialog progressdialog;

    private ListView listView;
    private int downloaded;
    public static int adshow = 0;

    private ProgressDialog loading;
    private String myJSONString;
    private int jsonarraylength = 0;

    InterstitialAd mInterstitialAd;


    private JSONArray themes = null;
    private static final String JSON_ARRAY1 ="themes";



    private int TRACK = 0;

    private static final String JSON_URL = "http://themeosm.parodigi.com/all_themes.php";
    ListAdapter adapter;

    ArrayList<DataModel> dataModels;

    Spinner spinner;
    String Profile_name,Profile_email,Profile_photo;
    public static String themecategory,JsonDATA;
    public NavigationView navigationView;

    JSONObject type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        /*Bundle extras = getIntent().getExtras();

        String Profile_name = extras.getString("Profile_name");
        String Profile_email = extras.getString("Profile_email");
        String Profile_photo = extras.getString("Profile_photo");*/

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        Profile_name = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("Profile_name", "");
//        Profile_email = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("Profile_email", "");
//        Profile_photo = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("Profile_photo", "");
//        System.out.print("Profile_name"+Profile_name);
//        System.out.print(Profile_email);System.out.print(Profile_photo);



        themename = (TextView)findViewById(R.id.themename);
        madeby = (TextView)findViewById(R.id.madeby);
        downcount = (TextView)findViewById(R.id.downcount);
       // likecount = (TextView)findViewById(R.id.likecount);
       // dislikecount = (TextView)findViewById(R.id.dislikecount);

        View header = navigationView.getHeaderView(0);
//        profileemail = (TextView)header.findViewById(R.id.Profile_email);
//        profilename = (TextView)header.findViewById(R.id.Profile_name);
//        profilephoto = (ImageView)header.findViewById(R.id.Profile_photo);
//        if(Profile_email!=null && Profile_name!=null )
//        {
//        profileemail.setText(Profile_email);
//        profilename.setText(Profile_name);}

        /*Glide.with(getApplicationContext()).load(Profile_photo)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profilephoto);*/

//        Glide.with(getApplicationContext())
//                .load(Profile_photo)
//                .into(profilephoto);

        btndownload = (Button)findViewById(R.id.downlink);

        ss1 = (ImageView)findViewById(R.id.img1);
        ss2 = (ImageView)findViewById(R.id.img2);
        ss3 = (ImageView)findViewById(R.id.img3);
      //  likebtn = (ImageView)findViewById(R.id.likebtn);
      //  dislikebtn = (ImageView)findViewById(R.id.dislikebtn);

        //profilephoto.setImageDrawable(getResources().getDrawable(R.drawable.logo));

        dataModels= new ArrayList<>();

        type = new JSONObject();

        if (savedInstanceState == null) {
            themecategory ="all";
            allthemes.TRACK = 0;

            try
            {
                type.put("Category",themecategory);
                allthemes.JsonDATA = String.valueOf(type);
            }catch (Exception e)
            {
                System.out.print("JSON Exception in OnItem Selected"+e);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new allthemes()).commit();

        }







    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_signin, menu);
    //    MenuItem item = menu.findItem(R.id.categoryspinner);
//        spinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//               R.array.spinner_list_item_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//
//
//
//        spinner.setAdapter(adapter);
//
//         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//             @Override
//             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                 type = new JSONObject();
//                 String category = (String)parent.getItemAtPosition(position);
//                 System.out.print("Category value"+category);
//
//                 switch (category)
//                 {
//
//                     case "All Themes":
//                         themecategory ="all";
//                         dataModels.clear();
//                         TRACK = 0;
//                         break;
//                     case "GBWhatsApp":
//                         themecategory = "GBWhatsApp";
//                         dataModels.clear();
//                         TRACK = 0;
//                         break;
//                     case "YoWhatsApp":
//                         themecategory = "YoWhatsApp";
//                         dataModels.clear();
//                         TRACK = 0;
//                         break;
//                     case "GB Material Mod":
//                         themecategory = "GB Material Mod";
//                         dataModels.clear();
//                         TRACK = 0;
//                         break;
//                     case "Foud Mod":
//                         themecategory = "Foud Mod";
//                         dataModels.clear();
//                         TRACK = 0;
//                         break;
//                 }
//                 try
//                 {
//                     type.put("Category",themecategory);
//                     JsonDATA = String.valueOf(type);
//                    // getJSON(JSON_URL);
//                 }catch (Exception e)
//                 {
//                     System.out.print("JSON Exception in Spinner OnItem Selected"+e);
//                 }
//
//                // Toast.makeText(MainActivity.this,"Spnner value"+category,Toast.LENGTH_LONG).show();
//             }
//
//             @Override
//             public void onNothingSelected(AdapterView<?> parent) {
//
//             }
//         });
//
        return true;



    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.categoryspinner) {
            String text = spinner.getSelectedItem().toString();
            System.out.print("Soinner Value"+text);
            Toast.makeText(MainActivity.this,"Spnner value changed",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

//        if (id == R.id.nav_home) {
//            Intent home = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(home);
//            System.out.print("Navigation Value"+id);
//        }
//        else
//        {
            displaySelectedScreen(id);
//        }


        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_allthemes:
                themecategory ="all";
                allthemes.dataModels.clear();
                allthemes.TRACK = 0;
                fragment = new allthemes();

                break;
            case R.id.nav_gbwhatsapp:
                themecategory = "GBWhatsApp";
                allthemes.dataModels.clear();
                allthemes.TRACK = 0;
                fragment = new allthemes();
                break;

            case R.id.nav_yowhatsapp:
                themecategory = "YoWhatsApp";
                allthemes.dataModels.clear();
                allthemes.TRACK = 0;
                fragment = new allthemes();
                break;
            case R.id.nav_gbmaterialmod:
                themecategory = "GB Material Mod";
                allthemes.dataModels.clear();
                allthemes.TRACK = 0;
                fragment = new allthemes();
                break;
            case R.id.nav_foudmod:
                themecategory = "Foud Mod";
                allthemes.dataModels.clear();
                allthemes.TRACK = 0;
                fragment = new allthemes();
                break;
            case R.id.nav_gbinsta:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                fragment = new settings();
                break;
            case R.id.nav_feedback:
                fragment = new feedback();
                break;
            case R.id.nav_aboutus:
                fragment = new aboutus();
                break;
        }

        try
        {
            type.put("Category",themecategory);
            allthemes.JsonDATA = String.valueOf(type);
        }catch (Exception e)
        {
            System.out.print("JSON Exception in OnItem Selected"+e);
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }








//    void downloadfile(String dwlink){
//        new DownloadFileAsync().execute(dwlink);
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DIALOG_DOWNLOAD_PROGRESS:
//                mProgressDialog = new ProgressDialog(this);
//                mProgressDialog.setMessage("Downloading file..");
//                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                mProgressDialog.setCancelable(false);
//                mProgressDialog.show();
//                return mProgressDialog;
//            default:
//                return null;
//        }
//    }
//
//    class DownloadFileAsync extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showDialog(DIALOG_DOWNLOAD_PROGRESS);
//        }
//
//        @Override
//        protected String doInBackground(String... aurl) {
//            int count;
//
//            try {
//
//                URL url = new URL(aurl[0]);
//                URLConnection conexion = url.openConnection();
//                conexion.connect();
//
//                int lenghtOfFile = conexion.getContentLength();
//                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
//
//                InputStream input = new BufferedInputStream(url.openStream());
//                OutputStream output = new FileOutputStream("/sdcard/Osm Themes/" + aurl  );
//
//                byte data[] = new byte[1024];
//
//                long total = 0;
//
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    publishProgress(""+(int)((total*100)/lenghtOfFile));
//                    output.write(data, 0, count);
//                }
//
//                output.flush();
//                output.close();
//                input.close();
//            } catch (Exception e) {}
//            return null;
//
//        }
//        protected void onProgressUpdate(String... progress) {
//            Log.d("ANDRO_ASYNC",progress[0]);
//            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
//        }
//
//        @Override
//        protected void onPostExecute(String unused) {
//            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
//        }
//    }









}
