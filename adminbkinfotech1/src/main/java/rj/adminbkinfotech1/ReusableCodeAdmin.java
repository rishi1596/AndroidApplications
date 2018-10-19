package rj.adminbkinfotech1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import rj.adminbkinfotech1.Constants.Constants;

/**
 * Created by jimeet29 on 21-12-2017.
 */

public class ReusableCodeAdmin {
    static private String response = null;

    public static void setApiRequest(String data, String url) {
        try {
            URL link = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(3000);
            conn.connect();

            Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();


            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String input_line;
            while ((input_line = reader.readLine()) != null) {
                builder.append(input_line).append("\n");
            }

            response = builder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getApiResponse() {
        String returnResponse = response;
        response = null;
        return returnResponse;
    }

    static JSONArray getEngineerNamesFromSharedPreferences(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
        String engineers = sp.getString(Constants.sharedPreferencesEngineerNames, null);
        try {
            if (engineers != null && !engineers.isEmpty()) {
                JSONArray engineers_array = new JSONArray(engineers);
                return engineers_array;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    static boolean isOtherDevice() {
        String manufacturer = Build.MANUFACTURER;

        for (int i = 0; i < Constants.devicesArr.length; i++) {
            if (manufacturer.equalsIgnoreCase(Constants.devicesArr[i])) {
                return true;
            }
        }
        return false;
    }

}
