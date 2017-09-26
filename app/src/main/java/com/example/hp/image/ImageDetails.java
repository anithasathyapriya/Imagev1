package com.example.hp.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class ImageDetails extends AppCompatActivity {

    final static String host = "http://10.217.138.156/ImageGallery/Images.svc/folders/image";
    public Bitmap bitmapimage;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        Bundle b = getIntent().getExtras();
        final String folder = b.get("fName").toString();
        final int index = (int) b.get("Index");
        final int count = (int) b.get("count");
        //Display selected  image
        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(Void... params) {
                JSONObject a = JSONParser.getJSONFromUrl(host + "/" + folder + index);
                return a;
            }

            protected void onPostExecute(JSONObject a) {
                try {
                    name = a.getString("Name");
                    JSONArray img = a.getJSONObject("img").getJSONArray("Data");
                    byte[] by = new byte[img.length()];
                    for (int i = 0; i < img.length(); i++) {
                        by[i] = (byte) (((int) img.get(i)) & 0xFF);
                    }
                    bitmapimage = BitmapFactory.decodeByteArray(by, 0, by.length);
                    ImageView imgVIew = (ImageView) findViewById(R.id.imageView);
                    TextView tv = (TextView) findViewById(R.id.textView);
                    imgVIew.setImageBitmap(bitmapimage);
                    tv.setText(name);
                } catch (JSONException e) {
                    Log.i("JSON", e.getMessage());
                }
            }
        }.execute();

        // action for delete butoon click
        Button btn=(Button) findViewById(R.id.btnDelete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),test.class);
                String  imgName = name.replace("%", "a");
                intent.putExtra("Id",folder);
                intent.putExtra("img", imgName);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}

