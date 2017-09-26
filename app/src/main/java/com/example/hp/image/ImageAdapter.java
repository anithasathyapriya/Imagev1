package com.example.hp.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by HP on 13/9/2017.
 */

public class ImageAdapter extends  BaseAdapter {

    Context mcontext;
    ArrayList<String> list = new ArrayList<String>();
    //JSONArray jImgs;
    //int lengthImgs;


    public ImageAdapter(Context c , ArrayList<String> l)
    {
        mcontext=c;
        list=l;
    }

//    public ImageAdapter(Context c , JSONArray j)
//    {
//        mcontext=c;
//        jImgs=j;
//        lengthImgs = j.length();
//    }

   // public Bitmap[] imageIds = list.toArray(new Bitmap[list.size()]);

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
         return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int iconColor = Color.GREEN;
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.testimage,null);}

        ImageView img=(ImageView) convertView.findViewById(R.id.imageView2);
        ImageView imgview4=(ImageView) convertView.findViewById(R.id.imageView4);
        img.setImageBitmap(GetImagesFromCache(mcontext, list.get(position)));
//        TextView txt = (TextView) convertView.findViewById(R.id.textView);
//        txt.setText(list.get(position));

        //String  s=list[position].toString();

        //setting the color based on face value
        if(list.get(position).indexOf("cf") > 0)
        {
//            String[] arrayString =list.get(position).split("_");
//            String  str=arrayString[3];
//             str=  substring(str.indexOf("cf")+2,str.length());
            int start = list.get(position).lastIndexOf("cf") +  2;
            String text = list.get(position).substring(start, start+6);
            Log.i("Substring",text);
            String newText = text.replace("%", ".");
            Log.i("text",newText);
            double value = Double.parseDouble(newText);
            Log.i("value",String.valueOf(value));

            if(value>=0.0070)
                iconColor = Color.GREEN;
            else if((value<0.0070)&&(value>=0.0030))
                iconColor = Color.YELLOW;
            else
                iconColor = Color.RED;
        }

        imgview4.setBackgroundColor(iconColor);
        return  convertView;
    }

    // Getting the imgae from the cache memory
    public Bitmap GetImagesFromCache(Context c, String imgName){
        BufferedReader input = null;
        File file = null;
        try {
            file = new File(c.getCacheDir(), imgName); // Pass getFilesDir() and "MyFile" to read file

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }
            String i = buffer.toString();
            try {
                byte[] encodeByte = Base64.decode(i, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
