package com.example.hp.image;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import static android.R.id.list;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    final static String host = "http://10.217.138.156/ImageGallery/Images.svc/folders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clear();

        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                ArrayList<String> list = new ArrayList<String>();
                JSONArray a = JSONParser.getJSONArrayFromUrl(host);
                try {
                    for (int i = 0; i < a.length(); i++) {
                        String S = a.getString(i);
                        list.add(S);
                    }
                } catch (Exception e) {
                    Log.e("EmpList", "JSONArray error");
                }
                return (list);

            }

            @Override
            protected void onPostExecute(List<String> result) {
                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.row, R.id.textView1, result);
                ListView list = (ListView) findViewById(R.id.listview1);
                list.setAdapter(adapter);
                list.setOnItemClickListener(MainActivity.this);

            }
            //File f=new File("C:/Users/HP/Downloads/PictureCollection/Flower.jpg");
            //Picasso.with(getApplicationContext()).load("http://10.217.138.156/ImageGallery/Images/111/20160825_090941.jpg").into(image);
            //Picasso.with(getApplicationContext()).load(f).into(image);
        }.execute();
    }
    public void onItemClick(AdapterView<?> av, View v, int position, long id)
    {
        String item = (String) av.getAdapter().getItem(position);
        //Toast.makeText(getApplicationContext(),item+"selected",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),test.class);
        intent.putExtra("Id",item);
        startActivity(intent);

    }

    public void clear() {
        File[] directory = getCacheDir().listFiles();
        if(directory != null){
            for (File file : directory ){
                file.delete();
            }
        }
    }

}
