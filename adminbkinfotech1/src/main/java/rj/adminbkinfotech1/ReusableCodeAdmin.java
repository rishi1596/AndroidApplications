package rj.adminbkinfotech1;

import android.content.Context;
import android.content.SharedPreferences;

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

}
