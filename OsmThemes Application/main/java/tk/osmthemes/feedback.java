package tk.osmthemes;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jimeet29 on 31-07-2017.
 */

public class feedback extends Fragment {

    EditText feedbackmsg;
    Button submitfeedback;
    String msg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.feedback, container, false);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        feedbackmsg = (EditText)getActivity().findViewById(R.id.feedback);
        submitfeedback = (Button)getActivity().findViewById(R.id.submitfeedback);
        submitfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = feedbackmsg.getText().toString();
                if(msg.length()>=4)
                {
                    JSONObject msgobject = new JSONObject();
                    try
                    {
                        //System.out.println("Mobile No In FEEDBACK form"+PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("mobileno", ""));
                        //msgobject.put("Profile_email", PreferenceManager.getDefaultSharedPreferences(getContext()).getString("Profile_email", ""));
                        msgobject.put("feedbackmsg",msg);

                        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable())
                        {
                            if (msgobject.length() > 0) {

                                new sendfeedbackmsg().execute(String.valueOf(msgobject));
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "There is no active network connection!", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Please Enter Feedback Message",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("FeedBack");
    }

    private class sendfeedbackmsg extends AsyncTask<String,String,String> {

        ProgressDialog process = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            process.setMessage("Loading Please Wait!");
            process.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            String JsonResponse = null;
            String JsonDATA = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://themeosm.parodigi.com/insertfeedback.php");
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setConnectTimeout(3000);
                urlConnection.connect();

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();

                InputStream inputstream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if(inputstream == null)
                {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputstream));
                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    builder.append(inputLine + "\n");
                if (builder.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = builder.toString().trim();
                //response data
                System.out.println("Response"+JsonResponse);
                //process.hide();
                //send to post execute
                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
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
        protected void onPostExecute(String s) {
            process.dismiss();
            super.onPostExecute(s);

            try
            {
                if (s.equals("0"))
                {
                    Toast.makeText(getActivity(), "Thank You for your valuable feedback", Toast.LENGTH_SHORT).show();
                    feedbackmsg.setText("");
                }
                else
                {
                    Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
