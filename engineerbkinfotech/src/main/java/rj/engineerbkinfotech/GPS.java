package rj.engineerbkinfotech;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rj.engineerbkinfotech.Constants.Constants;


/**
 * Created by jimeet29 on 04-02-2018.
 */

public class GPS extends Service {

    private static final String TAG = "GPS";
    public static Boolean isRunning = false;
    private Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    location_listener mlocation;
    FileOutputStream locfile;
    InputStream fis;
    File fileName;
    int count = 1, send_or_not = 0;
    String rootpath, readDataFromFile, date, time;
    StringBuilder finalData;
    SharedPreferences spGPS;
    JSONArray locArray;
    ConnectivityManager cm;
    NetworkInfo ni;

    public GPS() {

    }

    public GPS(Context context) {

        this.mContext = context;

    }

    @Override
    public void onCreate() {
        mlocation = new location_listener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        rootpath = Environment.getExternalStorageDirectory().toString();
        new File(rootpath + "/BK Infotech").mkdir();
        fileName = new File(rootpath + "/BK Infotech", "topsecret.txt");
        finalData = new StringBuilder();
        locArray = new JSONArray();


        getLocation();

        Log.d("on create", "create");
        super.onCreate();
    }

    public void getLocation() {
        try {
            Log.d("inside getlocation", "getlocation");
            //TODO uncomment in next update
            /*if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, mlocation);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
//                            Toast.makeText(mContext, "NET- \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                        }
                    }
                }*/


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Function to show settings alert dialog
     */
   /* public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }*/

    //TODO Uncomment in next update
   /* public void showSettingsAlert() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(EngineerActivity.fa, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
*/
    private void writefile(double lat, double lng, String dte, String tme) {

        try {
            cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            ni = cm.getActiveNetworkInfo();
            spGPS = getSharedPreferences("gps", 0);
            String data;
            //double lat1 = 19.225000,lng1 = 72.8514042;
            double prev_lat = Double.parseDouble(spGPS.getString("lat", String.valueOf(0)));
            double prev_lng = Double.parseDouble(spGPS.getString("lng", String.valueOf(0)));

            if (Math.abs(prev_lat - lat) > 0.001 || Math.abs(prev_lng - lng) > 0.001) {

                rootpath = Environment.getExternalStorageDirectory().toString();
                // new File(rootpath + "/BK Infotech").mkdir();

                locfile = new FileOutputStream(fileName, true);
                // if file doesnt exists, then create it
                if (!fileName.exists()) {
                    fileName.createNewFile();
                } else {

                    data = "{\"locno\":\"" + count + "\",\"lat\":\"" + lat + "\",\"lng\":\"" + lng + "\"," + "\"dte\":\"" + dte + "\",\"tme\":\"" + tme + "\"},";
                    //data = "{locno:"+count+",lat:"+lat+",lng:"+lng+","+"dte:"+dte+",tme:"+tme+"},";
                    // get the content in bytes
                    byte[] contentInBytes = data.getBytes();
                    locfile.write(contentInBytes);
                    locfile.flush();
                    locfile.close();
                    count++;
                    Log.d("Success", "locations");
                    SharedPreferences.Editor edit = spGPS.edit();
                    edit.putString("lat", String.valueOf(lat));
                    edit.putString("lng", String.valueOf(lng));
                    edit.apply();
                }
                if (ni != null && ni.isConnected() && ni.isAvailable()) {
                    //if(isInternetActive()) {
                    readfile();
                    //  }
                }

               /* JSONObject finalLocationWithDetails = new JSONObject();
                finalLocationWithDetails.put("username", getSharedPreferences("settings", 0).getString("username", null));
                finalLocationWithDetails.put("lat", lat);
                finalLocationWithDetails.put("lng", lng);
                finalLocationWithDetails.put("dte", dte);
                finalLocationWithDetails.put("tme", tme);
                Log.d("DATAAA", finalLocationWithDetails.toString());

                new SendLocation().execute(finalLocationWithDetails.toString());*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* String data;
        try {
            rootpath = Environment.getExternalStorageDirectory().toString();
           // new File(rootpath + "/BK Infotech").mkdir();

            locfile = new FileOutputStream(fileName,true);
            // if file doesnt exists, then create it
            if (!fileName.exists()) {
                fileName.createNewFile();
            }else {

                data = "{\"locno\":\""+count+"\",\"lat\":\""+lat+"\",\"lng\":\""+lng+"\","+"\"dte\":\""+dte+"\",\"tme\":\""+tme+"\"},";
               //data = "{locno:"+count+",lat:"+lat+",lng:"+lng+","+"dte:"+dte+",tme:"+tme+"},";
                // get the content in bytes
                byte[] contentInBytes = data.getBytes();
                locfile.write(contentInBytes);
                locfile.flush();
                locfile.close();
                count++;
                Log.d("Succ","asasda");
            }
            if(count%50==0)
            {
                readfile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
               /* write file
                inc counter
                        check counter(divide by 300 than make a server call )
                        send { uname : uname ,location [{ srno:1 , lat:value, lng:value}]}
                        eelse nothing*/
    }

    public boolean isInternetActive() {
        try {
            String command = "ping -i 3 -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void readfile() {
        try {
            JSONObject finalLocationWithDetails = new JSONObject();

            finalData.insert(0, "[");
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            //
            while ((readDataFromFile = br.readLine()) != null) {
                finalData.append(readDataFromFile);
            }

            br.close();
            finalData.replace(finalData.length() - 1, finalData.length(), "]");
            //   finalData.delete();
            Log.d("File Content", String.valueOf(finalData));
            //

            locArray.put(finalData);
            Log.d("locarray", locArray.toString());
            finalLocationWithDetails.put(Constants.strClientIdKey, Constants.clientId);
            finalLocationWithDetails.put("username", getSharedPreferences(Constants.sharedPreferencesFileNameSettings, 0).getString("username", null));
            finalLocationWithDetails.put("locations", finalData);
            Log.d("Data", finalLocationWithDetails.toString());

            new SendLocation().execute(finalLocationWithDetails.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class location_listener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            Log.d("lat", String.valueOf(latitude));
            Log.d("long", String.valueOf(longitude));
            writefile(latitude, longitude, date, time);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class SendLocation extends AsyncTask<String, Void, String> {
        URL link;
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            try {
                link = new URL("http://bkinfotech.in/app/insertLocation.php");
                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setConnectTimeout(3000);
                urlConnection.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(params[0]);
                writer.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String response, finalres;

                while ((response = br.readLine()) != null) {
                    sb.append(response).append("\n");
                }
                finalres = sb.toString().trim();
                Log.d("Response", finalres);


                if (finalres.equals("1")) {

                    while (locArray.length() != 0) {
                        locArray.remove(locArray.length());
                    }
                    fileName.delete();
                    finalData.delete(0, finalData.length());
                }
                /*
                if(finalres.equals("1"))
                {
                    while(locArray.length()!=0)
                    {
                        locArray.remove(locArray.length());
                    }
                    fileName.delete();
                }
                finalData.delete(0,finalData.length());*/
            } catch (Exception e) {
                Log.d("Gps Service", e.toString());
                e.printStackTrace();
            }
            return null;
        }
    }
}



