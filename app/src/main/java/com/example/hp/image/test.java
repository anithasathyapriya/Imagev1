package com.example.hp.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class test extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final static String host = "http://10.217.138.156/ImageGallery/Images.svc/folders/images";
    public ArrayList<Bitmap> bitmapimages= new ArrayList<Bitmap>();
    public ArrayList<String> bitmapNames = new ArrayList<String>();
    //public CacheStore cs = new CacheStore();
    String imgName;
    String[] imgFaceValue=new String[4];
    int[] imgValues=new int[40];
    GridView gv;
    String name;
    String fol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Bundle b = getIntent().getExtras();
        final String folder = b.get("Id").toString();
        fol = folder;
        clear();
        // delelting the selected image from server
        if(b.containsKey("img"))
        {
            String img = b.getString("img");
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {
                    JSONParser.getJSONFromUrl("http://10.217.138.156/ImageGallery/Images.svc/folders/move/"+params[0]);
                    return null;
                }

            }.execute(img);
        }

        new AsyncTask<Void, Void, JSONArray>() {
            @Override
            //  getting all images in the folder
            protected JSONArray doInBackground(Void... params) {
                JSONArray a = JSONParser.getJSONArrayFromUrl(host + "/" + folder);
                return (a);
            }

            protected void onPostExecute(JSONArray result) {
                Bitmap bng = null;
                ImageClass c = new ImageClass();
                try {
                    for (int j = 0; j < result.length(); j++) {
                        JSONArray jByte = result.getJSONObject(j).getJSONObject("img").getJSONArray("Data");
                        String name = result.getJSONObject(j).getString("Name");
                        byte[] by = new byte[jByte.length()];
                        for (int i = 0; i < jByte.length(); i++) {
                            by[i] = (byte) (((int) jByte.get(i)) & 0xFF);
                        }
                        bng = BitmapFactory.decodeByteArray(by, 0, by.length);
                        c.img = bng;
                        c.name = name;
                        bitmapNames.add(name);
                        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                        bng.compress(Bitmap.CompressFormat.JPEG,100, baos);
                        byte [] b=baos.toByteArray();
                        String temp= Base64.encodeToString(b, Base64.DEFAULT);
                        File file;
                        FileOutputStream outputStream;
                        try {
                            file = new File(getApplicationContext().getCacheDir(), c.name);
                            outputStream = new FileOutputStream(file);
                            outputStream.write(temp.getBytes());
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                }
                gv = (GridView) findViewById(R.id.gridview1);
                ImageAdapter adapter = new ImageAdapter(test.this, bitmapNames);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(test.this);
            }
        }.execute();


    }

    //storing the images in the cache memory
    public void WriteImg(ImageClass imgC){
        String content = imgC.img.toString();
        File file;
        FileOutputStream outputStream;
        try {
            // file = File.createTempFile("MyCache", null, getCacheDir());
            file = new File(getApplicationContext().getCacheDir(),imgC.name);

            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Action for on click event of a image
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), i + "selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),ImageDetails.class);
        intent.putExtra("fName",fol);
        intent.putExtra("Index",i);
        intent.putExtra("count", bitmapNames.size());
        startActivity(intent);
        finish();
    }
// clear the cache memory
    public void clear() {
        File[] directory = getCacheDir().listFiles();
        if(directory != null){
            for (File file : directory ){
                file.delete();
            }
        }
    }
}
