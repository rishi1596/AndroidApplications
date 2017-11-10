package tk.osmthemes;

/**
 * Created by jimeet29 on 10-07-2017.
 */

import java.util.ArrayList;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import static tk.osmthemes.R.id.downlink;
import static tk.osmthemes.R.id.likebtn;
import static tk.osmthemes.R.id.likecount;


public class ListAdapter extends ArrayAdapter<DataModel> {
    customButtonListener customListner;
    private ArrayList<DataModel> dataSet;
    private ArrayList<String> item_liked = new ArrayList<>();
    public String temp1;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
        public void onImageClickListner(int position,String value);
        public void onlikeImageClickListner(int position,String value);
        public void ondislikeImageClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;
    //private ArrayList<String> data = new ArrayList<String>();

    public ListAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.list_layout, data);
        this.dataSet = data;
        this.context = context;

    }

    private int lastPosition = -1;

//    public ListAdapter(Context context, ArrayList<String> dataItem) {
//        super(context, R.layout.list_layout, dataItem);
//        this.data = dataItem;
//        this.context = context;
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final DataModel dataModel = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_layout, null);
            viewHolder = new ViewHolder();

            viewHolder.themename = (TextView) convertView.findViewById(R.id.themename);
            viewHolder.madeby = (TextView) convertView.findViewById(R.id.madeby);
            viewHolder.downcount = (TextView) convertView.findViewById(R.id.downcount);
            viewHolder.likecount = (TextView) convertView.findViewById(likecount);
            viewHolder.dislikecount = (TextView) convertView.findViewById(R.id.dislikecount);

            viewHolder.ss1 = (ImageView) convertView.findViewById(R.id.img1);
            viewHolder.ss2 = (ImageView) convertView.findViewById(R.id.img2);
            viewHolder.ss3 = (ImageView) convertView.findViewById(R.id.img3);
            viewHolder.likebtn = (ImageView) convertView.findViewById(likebtn);
            viewHolder.dislikebtn = (ImageView) convertView.findViewById(R.id.dislikebtn);


            viewHolder.btndownload = (Button) convertView.findViewById(downlink);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        //final String temp = getItem(position).toString();
       // viewHolder.tempid =  Integer.parseInt(dataModel.getId());

        final String  ss1link,ss2link,ss3link,downlink;



        viewHolder.themename.setText(dataModel.getTheme_name());
        viewHolder.madeby.setText(dataModel.getMade_by());
        viewHolder.downcount.setText(dataModel.getDown_count());
        viewHolder.likecount.setText(dataModel.getLike_count());
        viewHolder.dislikecount.setText(dataModel.getDislike_count());
        ss1link = dataModel.getSs1();
        ss2link = dataModel.getSs2();
        ss3link = dataModel.getSs3();
        downlink = dataModel.getDown_link();
        // viewHolder.text.setText(temp);
        viewHolder.btndownload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    int downcounttmp = Integer.parseInt(dataModel.getDown_count());
                    downcounttmp++;
                    viewHolder.downcount.setText(""+downcounttmp);

                    String name = downlink + "#" + dataModel.getTheme_name() + "#" + dataModel.getId() + "#" + downcounttmp;
                    customListner.onButtonClickListner(position,name);
                }

            }
        });
        viewHolder.ss1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {

                    customListner.onImageClickListner(position,ss1link);
                }
            }
        });
        viewHolder.ss2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {

                    customListner.onImageClickListner(position,ss2link);
                }
            }
        });
        viewHolder.ss3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {

                    customListner.onImageClickListner(position,ss3link);
                }
            }
        });

        viewHolder.likebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {



                    //  Toast.makeText(context, dataModel.getId() + "   {     " ,Toast.LENGTH_LONG).show();
                    if(viewHolder.check == 0){
                        int likecount = Integer.parseInt(dataModel.getLike_count());
                        likecount++;
                        viewHolder.likecount.setText(""+likecount);
                        String name = likecount + "#" + dataModel.getId();

                       // Toast.makeText(context,position + "  " + temp,Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                               // viewHolder.likebtn.setImageResource(R.drawable.ic_thumb_up_red_24dp);



                            //viewHolder.likebtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_red_24dp, getContext().getTheme()));
                            viewHolder.check = 1;
                        } else {
                           // viewHolder.likebtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_red_24dp));
                            viewHolder.check = 1;
                        }
                        customListner.onlikeImageClickListner(position,name);
                    }
                    else {
                        Toast.makeText(context,"You cant use this action",Toast.LENGTH_LONG).show();
                    }


                }
            }
        });
        viewHolder.dislikebtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    if(viewHolder.check == 0) {

                        int dislikecount = Integer.parseInt(dataModel.getDislike_count());
                        dislikecount++;
                        viewHolder.dislikecount.setText(""+dislikecount);

                        String name = dislikecount + "#" + dataModel.getId();



                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                          //  viewHolder.dislikebtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down_red_24dp, getContext().getTheme()));
                            viewHolder.check = 1;
                        } else {
                          //  viewHolder.dislikebtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_down_red_24dp));
                            viewHolder.check = 1;
                        }
                        customListner.ondislikeImageClickListner(position,name);
                    }
                    else {
                        Toast.makeText(context,"You cant use this action",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        Glide
                .with(context)
                .load(ss1link)
                .into(viewHolder.ss1);
        Glide
                .with(context)
                .load(ss2link)
                .into(viewHolder.ss2);
        Glide
                .with(context)
                .load(ss3link)
                .into(viewHolder.ss3);



        return convertView;
    }

    public class ViewHolder {
        TextView themename,madeby,downcount,likecount,dislikecount;
        Button btndownload;
        ImageView ss1,ss2,ss3,likebtn,dislikebtn;
        int check = 0, tempid = 0;
       String likeditem;

    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
