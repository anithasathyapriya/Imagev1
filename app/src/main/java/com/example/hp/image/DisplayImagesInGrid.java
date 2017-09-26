package com.example.hp.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import static android.R.id.input;

public class DisplayImagesInGrid extends AppCompatActivity
{

    final static String host = "http://10.217.138.156/ImageGallery/Images.svc/folders";
    public  Bitmap[] bitmapimages= new Bitmap[40];
    GridView gv;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images_in_grid);
        Bundle b = getIntent().getExtras();
        final String folder = b.get("Id").toString();

        new AsyncTask<Void, Void, Bitmap[]>() {
            @Override

            protected Bitmap[] doInBackground(Void... params) {
                JSONArray a = JSONParser.getJSONArrayFromUrl(host + "/" + folder);
                try {
                    for (int j = 0; j < a.length(); j++) {
                       // JSONObject obj = a.getJSONObject(j);
                       // name = obj.getString("Name");
                        //JSONArray jByte = obj.getJSONObject("img").getJSONArray("Data");
                        JSONArray jByte = a.getJSONObject(j).getJSONArray("Data");
                        byte[] by = new byte[jByte.length()];
                        for (int i = 0; i < jByte.length(); i++) {
                            by[i] = (byte) (((int) jByte.get(i)) & 0xFF);
                        }
                        bitmapimages[j] = BitmapFactory.decodeByteArray(by, 0, by.length);

                    }

                } catch (Exception e) {
                }
                return (bitmapimages);
            }

            protected void onPostExecute(Bitmap[] result) {
//                gv = (GridView) findViewById(R.id.gridView);
//            ImageAdapter adapter = new ImageAdapter(DisplayImagesInGrid.this, result);
//                gv.setAdapter(adapter);
//                /*gv.setBackgroundColor(Color.GREEN);
//                gv.setHorizontalSpacing(1);
//                gv.setVerticalSpacing(1);
//                gv.setPadding(20,20,20,0);*/
//
//                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Toast.makeText(getApplicationContext(), i + "selected", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getApplicationContext(),ImageDetails.class);
//                    intent.putExtra("fName",folder);
//                    intent.putExtra("Index",i);
//                    startActivity(intent);
//
//                }
//            });
        }
        }.execute();

    }
}
