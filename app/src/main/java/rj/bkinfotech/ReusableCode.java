package rj.bkinfotech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jimeet29 on 02-12-2017.
 */
@Deprecated
public class ReusableCode {

    String webLink,data;

    private int response_server;
    URL url;

    public void server_call(String LINK,String DATA)
    {
        webLink = LINK;
        data = DATA;

        new ServerHit().execute(data);
    }
    /*public void server_response(int res) {
        response = res;
    }*/

    public int get_server_response() {
        return response_server;
    }

    public class ServerHit extends AsyncTask<String, Void, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }



        @Override
        protected Integer doInBackground(String... params) {

            int jsonResponse;
            String jsonData = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try{
                url = new URL(webLink);
                urlConnection =(HttpURLConnection)url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json;charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8"));
                writer.write(jsonData);
                writer.close();

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while((inputLine=reader.readLine())!= null)
                {
                    response.append(inputLine + "\n");
                }
                jsonResponse = Integer.parseInt(response.toString().trim());

                System.out.println("Response"+jsonResponse);
                response_server = jsonResponse;

                return jsonResponse;

            }catch (Exception e)
            {
                Log.d("Resuable Code",e.toString());
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
                super.onPostExecute(s);

                try {
                    Log.d("Response",s.toString());
                }catch (Exception e)
                {
                    Log.d("Resuable Code",e.toString());
                }

            }
        }
    }


