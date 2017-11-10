package nadol.nadol;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by jimeet29 on 17-05-2017.
 */
public class MyAdapter extends SimpleAdapter {

    public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
        super(context, data, resource, from, to);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        // here you let SimpleAdapter built the view normally.
        View v = super.getView(position, convertView, parent);

        // Then we get reference for Picasso
        ImageView img = (ImageView) v.getTag();
        if(img == null){
            img = (ImageView) v.findViewById(R.id.newsimage);
            v.setTag(img); // <<< THIS LINE !!!!
        }
        // get the url from the data you passed to the `Map`
        //for(int i =0 ; i<)
        String url = String.valueOf(((Map)getItem(position)).get("newsimage"));


        // do Picasso
        //System.out.print("Hellooo"+url);
        /*Picasso.with(v.getContext()).load("http://i558.photobucket.com/albums/ss24/bissniss/icon-news.png").resize(80, 80).placeholder(R.drawable.firebase_lockup_400)   // optional
                .error(R.drawable.review).into(img);*/

        Picasso.with(v.getContext()).load(url).resize(80, 80).placeholder(R.drawable.news)   // optional
                .error(R.drawable.news).into(img);
 //TODO
        // return the view
        return v;
    }
}