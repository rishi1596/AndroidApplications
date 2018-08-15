package rj.bkinfotech;

import android.support.v7.app.ActionBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jimeet29 on 21-12-2017.
 */

public class ReusableCodeAdmin {
    private String response=null;


    public void sendCredentials(String data,String url)
    {
        try {
            URL link = new URL(url);

            HttpURLConnection conn = (HttpURLConnection)link.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json;charset=utf-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setConnectTimeout(3000);
            conn.connect();

            Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
            writer.write(data);
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder= new StringBuilder();
            String input_line;
            while((input_line=reader.readLine())!=null)
            {
                builder.append(input_line).append("\n");
            }
            response = builder.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCredentialsResponse()
    {
        return response;
    }


}
