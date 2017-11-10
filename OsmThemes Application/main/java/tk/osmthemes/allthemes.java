package tk.osmthemes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.process;
import static android.app.ProgressDialog.show;

/**
 * Created by Tushar on 04-07-2017.
 */

public class allthemes extends Fragment implements
        ListAdapter.customButtonListener {
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private ProgressDialog mProgressDialog;

    TextView themename, madeby, downcount, likecount, dislikecount, profilename, profileemail;
    Button btndownload;
    ImageView ss1, ss2, ss3, likebtn, dislikebtn, profilephoto;

    String themenamest;
    String dwnlink;
    String idtmp;
    int downcounttmp = 0;
    int jsonloded = 0;
    public int dsize, tsize = 0;
    public String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private int STORAGE_PERMISSION_CODE = 23;
    public ProgressDialog progressdialog;

    private ListView listView;
    private int downloaded;


  //  private ProgressDialog loading;
  ProgressDialog loading;
    private String myJSONString;
    private int jsonarraylength = 0;

    InterstitialAd mInterstitialAd;


    private JSONArray themes = null;
    private static final String JSON_ARRAY1 = "themes";


    public static int TRACK = 0;

    private static String JSON_URL = "http://themeosm.parodigi.com/all_themes.php";
    ListAdapter adapter;

    public static ArrayList<DataModel> dataModels;

    Spinner spinner;
    String Profile_name, Profile_email, Profile_photo;
    public static String themecategory, JsonDATA;

    JSONObject type;
    TextView tvrecent, tvpopular;
    String recent_popular_switch = "recent";
    ColorStateList defaulttxtcolor, newtxtcolor;

    public NotificationCompat.Builder b;
    public int m;
    public NotificationManager nm;
    String filevalue;
    Button retry;

    ConnectivityManager cm;
    NetworkInfo activeNetwork;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.allthemesfrag, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        JSON_URL = "http://themeosm.parodigi.com/all_themes.php";


        mInterstitialAd = new InterstitialAd(getContext());
        tvrecent = (TextView) view.findViewById(R.id.txtrecent);
        tvpopular = (TextView) view.findViewById(R.id.txtpopular);
        retry = (Button) view.findViewById(R.id.retry);

        dataModels = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listView);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork == null) {
            retry.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();

        }
        else {
            if (jsonloded == 0 ) {

                getJSON(JSON_URL);
            }
            System.out.print("Json Loaded before ASYnC Task" + jsonloded);

        }



        Random r = new Random();
        int i1 = r.nextInt(5 - 1) + 1;
        switch (i1) {
            case 1:
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
                break;
            case 2:
                mInterstitialAd.setAdUnitId(getString(R.string.fullscreen_ad_2));
                break;
            case 3:
                mInterstitialAd.setAdUnitId(getString(R.string.fullscreen_ad_3));
                break;
            case 4:
                mInterstitialAd.setAdUnitId(getString(R.string.fullscreen_ad_videp_1));
                break;
            default:
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
                break;
        }


        if (isReadStorageAllowed()) {
            //If permission is already having then showing the toast
            //Toast.makeText(this,"You already have the permission",Toast.LENGTH_LONG).show();
            //Existing the method with return

        } else {

            //If the app has not the permission then asking for the permission

            requestStoragePermission();
        }

        defaulttxtcolor = tvpopular.getTextColors();
        newtxtcolor = tvrecent.getTextColors();


        tvrecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    JSON_URL = "http://themeosm.parodigi.com/all_themes.php";

                    if (recent_popular_switch == "popular") {
                        recent_popular_switch = "recent";
                        tvrecent.setTextColor(newtxtcolor);
                        tvpopular.setTextColor(defaulttxtcolor);
                        TRACK = 0;
                        dataModels.clear();
                        if (activeNetwork == null) {
                            retry.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                        else {
                        getJSON(JSON_URL);
                    }
                }


            }
        });

        tvpopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    JSON_URL = "http://themeosm.parodigi.com/popularphp.php";
                    if (recent_popular_switch == "recent") {
                        recent_popular_switch = "popular";
                        tvrecent.setTextColor(defaulttxtcolor);
                        tvpopular.setTextColor(newtxtcolor);
                        TRACK = 0;
                        dataModels.clear();
                        if (activeNetwork == null) {
                            retry.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();

                        }
                        else {
                        getJSON(JSON_URL);
                    }
                }


            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(allthemes.this).attach(allthemes.this).commit();

                downcounttmp = 0;
                jsonloded = 0;
                dsize = 0;
                tsize = 0;
                STORAGE_PERMISSION_CODE = 23;
                jsonarraylength = 0;
                TRACK = 0;


            }
        });




        return view;
    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        Toast.makeText(getContext(), "Downloading...",
                Toast.LENGTH_SHORT).show();
        String dollarsign = "#";
        String[] parts = value.split(dollarsign);
        themenamest = parts[1];
        dwnlink = parts[0];
        idtmp = parts[2];

        try {
            downcounttmp = Integer.parseInt(parts[3]);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }


        // progressdialog = new ProgressDialog(this,R.style.myAlertDialogStyle);


        Downloadfile(dwnlink);

    }

    @Override
    public void onImageClickListner(int position, String value) {
        //  Toast.makeText(MainActivity.this, "Image click " + value,
        //  Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), FullscreenActivity.class);
        intent.putExtra("img link", value);
        startActivity(intent);


    }

    @Override
    public void onlikeImageClickListner(int position, String value) {


        String dollarsign = "#";
        String[] parts = value.split(dollarsign);


        String update_url = "http://themeosm.parodigi.com/updatelikecount.php?id=" + parts[1] + "&likecount=" + parts[0];

        StringRequest stringRequest = new StringRequest(update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Theme liked", Toast.LENGTH_SHORT).show();


                //  likebtn.setImageResource(R.drawable.ic_thumb_up_red_24dp);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    @Override
    public void ondislikeImageClickListner(int position, String value) {


        String dollarsign = "#";
        String[] parts = value.split(dollarsign);

        String update_url = "http://themeosm.parodigi.com/updatedislikecount.php?id=" + parts[1] + "&dislikecount=" + parts[0];

        StringRequest stringRequest = new StringRequest(update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Theme disliked", Toast.LENGTH_SHORT).show();
                // dislikebtn.setImageResource(R.drawable.ic_thumb_down_red_24dp);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }


    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        loading = new ProgressDialog(getActivity());
                        loading.setMessage("Loading Please Wait!");
                        loading.setCancelable(false);
                        loading.show();

                        //loading = ProgressDialog.show(getContext(), null, "Please Wait", true);
                    }
                });
//                loading = new ProgressDialog(getActivity());
//                loading.setMessage("Loading..");
//                loading.setIndeterminate(false);
//                loading.setCancelable(true);
//                loading.show();


            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    con.setRequestProperty("Accept", "application/json");
                    con.setConnectTimeout(3000);
                    con.connect();
                    //if(!themecategory.equals("all"))
                    //{
                    Writer writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                    writer.write(JsonDATA);
                    writer.close();
                    //}
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    e.printStackTrace();
                    getJSON(JSON_URL);
                    if (loading.isShowing()) {
                        loading.dismiss();
                    }


                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                myJSONString = s;


                  // Toast.makeText(getContext(),"doinbg" + s,Toast.LENGTH_LONG).show();


    extractJSON();

    fildatamodel();
    try {
        adapter = new ListAdapter(dataModels, getActivity());
        adapter.setCustomButtonListner(allthemes.this);
        listView.setAdapter(adapter);

    } catch (NullPointerException e) {
        getJSON(JSON_URL);
    }


                //  listView.setAdapter(adapter);

                //showData();
                // t1.setText(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

    private void extractJSON() {
        try {

            JSONObject jsonObject = new JSONObject(myJSONString);
            themes = jsonObject.getJSONArray(JSON_ARRAY1);
            //  Toast.makeText(this, " " + themes, Toast.LENGTH_LONG).show();
            jsonarraylength = themes.length();

        } catch (Exception e) {
            getJSON(JSON_URL);
            if (loading.isShowing()) {
                loading.dismiss();
            }
            e.printStackTrace();

        }
    }

    private void fildatamodel() {

        while (TRACK < jsonarraylength) {

            try {

                JSONObject jsonObject = themes.getJSONObject(TRACK);

                String s1 = jsonObject.getString(Config.KEY_id);
                String s2 = jsonObject.getString(Config.KEY_themename);
                //    Toast.makeText(this,"Yasd" + s2,Toast.LENGTH_SHORT).show();
                String s3 = jsonObject.getString(Config.KEY_madeby);
                String s4 = jsonObject.getString(Config.KEY_ss_1);
                String s5 = jsonObject.getString(Config.KEY_ss_2);
                String s6 = jsonObject.getString(Config.KEY_ss_3);
                String s7 = jsonObject.getString(Config.KEY_down_no);
                String s8 = jsonObject.getString(Config.KEY_like_no);
                String s9 = jsonObject.getString(Config.KEY_dislike_no);
                String s10 = jsonObject.getString(Config.KEY_down);

                dataModels.add(new DataModel(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10));
                TRACK++;

            } catch (JSONException e) {
                getJSON(JSON_URL);
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                e.printStackTrace();
            }


        }
        jsonloded = 1;
        System.out.print("Json Loaded after ASYnC Task" + jsonloded);

    }


    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "To download Theme we need Storage permission", Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                //Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    void Downloadfile(final String filevalue1) {
        filevalue = filevalue1;


        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        if (activeNetwork != null) {


                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice("BE57D129BCBFA39D7B6EC4ADDBE2BB8D")
                        .build();

                // Load ads into Interstitial Ads

                mInterstitialAd.loadAd(adRequest);

                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });





            b = new NotificationCompat.Builder(getActivity());
            m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

            b.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_file_download_white_24dp)
                    .setContentTitle("Downloading...")
                    .setProgress(100, 0, true)
                    .setOngoing(true)
                    .setContentText(themenamest)
                    .setVibrate(new long[]{0l});
            nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(m, b.build());

            b.setAutoCancel(true).setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_done_white_24dp)
                    .setContentTitle("Theme Downloaded")
                    .setContentText(themenamest)
                    .setProgress(0, 0, false)
                    .setOngoing(false)
                    .setVibrate(new long[]{0l});



//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.invokeAny(Arrays.asList(new MyTask()), 10, TimeUnit.MINUTES); // Timeout of 10 minutes.
//            executor.shutdown();
//            this.runOnUiThread(new Runnable() {
//                public void run() {
//
//                    Toast toast = Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT);
//                    toast.show();
//
//
//                }
//            });


            new Thread(new Runnable() {
                @Override
                public void run() {

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        try {
                            URL url = new URL(filevalue);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setDoOutput(true);
                            urlConnection.connect();
                            String rootpath = Environment.getExternalStorageDirectory().toString();
                            new File(rootpath + "/Osm Themes").mkdir();
                            //File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
                            String filename = themenamest;
                            Log.i("Local filename:", "" + filename);
                            File file = new File(rootpath + "/Osm Themes", filename + ".zip");
                            if (file.createNewFile()) {
                                file.createNewFile();
                            }
                            FileOutputStream fileOutput = new FileOutputStream(file);
                            InputStream inputStream = urlConnection.getInputStream();
                            int totalSize = urlConnection.getContentLength();
                            int downloadedSize = 0;
                            byte[] buffer = new byte[1024];
                            int bufferLength = 0;
                            while ((bufferLength = inputStream.read(buffer)) > 0) {
                                fileOutput.write(buffer, 0, bufferLength);
                                downloadedSize += bufferLength;
                                Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
                                dsize = downloadedSize;
                                tsize = totalSize;
                            }
                            fileOutput.close();
                            if (downloadedSize == totalSize) {
                                filepath = file.getPath();

                                increasedowncount();

                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            nm.cancel(m);

                        } catch (IOException e) {
                            filepath = null;
                            nm.cancel(m);
                            e.printStackTrace();

                        }
                        Log.i("filepath:", " " + filepath);

                        //       MediaScannerConnection.scanFile(MainActivity.this, new String[]{filepath.toString()}, new String[]{"zip"}, null);


                    }

                    if (dsize == tsize && filepath!= null) {


                        //    nm.cancelAll();
                        nm.cancel(m);
                        //        final NotificationCompat.Builder c = new NotificationCompat.Builder(MainActivity.this);
                        final int q = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
//                        File file = new File(filepath);


                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                Toast toast = Toast.makeText(getContext(), "Theme Saved at " + filepath.toString(), Toast.LENGTH_SHORT);
                                toast.show();


                            }
                        });


//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                   Uri photoURI = FileProvider.getUriForFile(getActivity(), getContext().getPackageName() + ".provider", new File(filepath));
//                                 intent.setDataAndType(photoURI, "zip");
//                        } else {
//                                 File file1 = new File(filepath);
//                                 intent.setDataAndType(Uri.fromFile(file1), "zip");
//                        }
//
//                        PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);






                        nm.notify(m, b.build());


                    }
                    else {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {

                                Toast toast = Toast.makeText(getContext(), "Download error", Toast.LENGTH_LONG);
                                toast.show();


                            }
                        });
                    }





                }
            }).start();


        } else {
            Toast toast = Toast.makeText(getContext(), "Please check your internet connection.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void increasedowncount() {


        String update_url = "http://themeosm.parodigi.com/updatedowncount.php?id=" + idtmp + "&downcount=" + downcounttmp;

        StringRequest stringRequest = new StringRequest(update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
                try{
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);
                }
                catch (NullPointerException e){
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(stringRequest);
                }




    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
       try{
           switch (MainActivity.themecategory) {
               case "all":
                   getActivity().setTitle("All Themes");
                   break;
               case "GBWhatsApp":
                   getActivity().setTitle("GBWhatsApp");
                   break;
               case "YoWhatsApp":
                   getActivity().setTitle("YoWhatsApp");
                   break;
               case "GB Material Mod":
                   getActivity().setTitle("GB Material Mod");
                   break;
               case "Foud Mod":
                   getActivity().setTitle("Foud Mod");
                   break;

           }

       }
       catch (NullPointerException e){
           getActivity().setTitle("Osm Themes");
       }


    }

//    class MyTask implements Runnable
//    {
//        public void run() {
//
//            int SDK_INT = android.os.Build.VERSION.SDK_INT;
//            if (SDK_INT > 8) {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                        .permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//                try {
//                    URL url = new URL(filevalue);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setDoOutput(true);
//                    urlConnection.connect();
//                    String rootpath = Environment.getExternalStorageDirectory().toString();
//                    new File(rootpath + "/Osm Themes").mkdir();
//                    //File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
//                    String filename = themenamest;
//                    Log.i("Local filename:", "" + filename);
//                    File file = new File(rootpath + "/Osm Themes", filename + ".zip");
//                    if (file.createNewFile()) {
//                        file.createNewFile();
//                    }
//                    FileOutputStream fileOutput = new FileOutputStream(file);
//                    InputStream inputStream = urlConnection.getInputStream();
//                    int totalSize = urlConnection.getContentLength();
//                    int downloadedSize = 0;
//                    byte[] buffer = new byte[1024];
//                    int bufferLength = 0;
//                    while ((bufferLength = inputStream.read(buffer)) > 0) {
//                        fileOutput.write(buffer, 0, bufferLength);
//                        downloadedSize += bufferLength;
//                        Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
//                        dsize = downloadedSize;
//                        tsize = totalSize;
//                    }
//                    fileOutput.close();
//                    if (downloadedSize == totalSize) {
//                        filepath = file.getPath();
//
//                        increasedowncount();
//
//                    }
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//
//                } catch (IOException e) {
//                    filepath = null;
//                    e.printStackTrace();
//
//                }
//                Log.i("filepath:", " " + filepath);
//
//                //       MediaScannerConnection.scanFile(MainActivity.this, new String[]{filepath.toString()}, new String[]{"zip"}, null);
//
//
//            }
//
//            if (dsize == tsize) {
//
//                //    nm.cancelAll();
//                nm.cancel(m);
//                //        final NotificationCompat.Builder c = new NotificationCompat.Builder(MainActivity.this);
//                final int q = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
//
//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
////                        File file = new File(filepath);
//
//
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//
//                        Toast toast = Toast.makeText(getContext(), "Theme Saved at " + filepath.toString(), Toast.LENGTH_LONG);
//                        toast.show();
//
//
//                    }
//                });
//
//
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                                   Uri photoURI = FileProvider.getUriForFile(getActivity(), getContext().getPackageName() + ".provider", new File(filepath));
////                                 intent.setDataAndType(photoURI, "zip");
////                        } else {
////                                 File file1 = new File(filepath);
////                                 intent.setDataAndType(Uri.fromFile(file1), "zip");
////                        }
////
////                        PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
//
//
//                nm.notify(m, b.build());
//
//
//            }
//
//        }
//
//    }
}
