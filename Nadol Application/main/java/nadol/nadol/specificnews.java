package nadol.nadol;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jimeet29 on 15-05-2017.
 */
public class specificnews extends AppCompatActivity {

    DBController controller = new DBController(this);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;

    String newstitle = "N/A";
    String newscontent = "Team A";
    String newstime = "0";
    int newsimage = R.drawable.news;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String s= getIntent().getStringExtra("newsid");
        int nid = Integer.parseInt(s);
        System.out.print("Zodiac "+nid);
        setContentView(R.layout.specificnews);


        ImageView newsimagesc = (ImageView)findViewById(R.id.newsimage);
        TextView newstitletv = (TextView) findViewById(R.id.newstitle);
        TextView newscontenttv = (TextView) findViewById(R.id.newscontent);
        TextView newstimetv = (TextView) findViewById(R.id.newstime);

        ArrayList<HashMap<String, String>> news = controller.getspecificnews(nid);

        assert newsimagesc != null;
        newsimagesc.setImageResource(newsimage);
        assert newstitletv != null;
        newstitletv.setText(newstitle);
        assert newscontenttv != null;
        newscontenttv.setText(newscontent);
        assert newstimetv != null;
        newstimetv.setText(newstime);

        try {
            //assert mnotv != null;
            newstitletv.setText(news.get(0).get("newstitle"));
            newscontenttv.setText(news.get(0).get("newscontent"));
            newstimetv.setText(news.get(0).get("newstime"));

            String url = String.valueOf(news.get(0).get("newsimage"));

            Picasso.with(specificnews.this).load(url).resize(200, 120).placeholder(R.drawable.news)   // optional
                    .error(R.drawable.news).into(newsimagesc);
        }catch (Exception e) {
            e.printStackTrace();
        }
//get record fromdb and display
    }
}
