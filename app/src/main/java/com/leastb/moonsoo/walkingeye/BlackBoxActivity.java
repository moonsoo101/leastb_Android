package com.leastb.moonsoo.walkingeye;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leastb.moonsoo.walkingeye.Util.DB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wisebody on 2017. 6. 8..
 */

public class BlackBoxActivity extends Activity {
    ImageView imgView;
    String url;
    ProgressBar progressBar;
    TextView dateT_View, locationT_View;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackbox);
        imgView = (ImageView) findViewById(R.id.imgView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        dateT_View = (TextView) findViewById(R.id.date);
        locationT_View = (TextView) findViewById(R.id.location);
        String imgName = getIntent().getStringExtra("imgName");
        int isAccident = getIntent().getIntExtra("isAccident",0);
        getCoordi(imgName);
        String[] arr = imgName.split("-");
        long time = Long.parseLong(arr[1]);
        Date date = new Date(time);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strDate = sdfNow.format(date);
        dateT_View.setText(strDate);
        if(isAccident ==1)
            checkGIF(imgName);
        else
        {
            url = "http://13.124.33.214/darknet/" + imgName + ".jpg";
            Glide.with(this).load(url).into(imgView);
            progressBar.setVisibility(View.GONE);
        }
    }
    private void getCoordi(String imgName){

        class GetCoordiTask extends AsyncTask<String, Void, String> {
            String imgName;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("result");
                    String latitude="", longitude="";
                    if(jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            latitude = c.getString("latitude");
                            longitude = c.getString("longitude");
                        }
                    }
                    if(latitude.equals("null"))
                        locationT_View.setText("위치가 측정되지 않은 사진입니다.");
                    else
                    getLocation(latitude, longitude);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            @Override
            protected String doInBackground(String... params) {
                Log.d("result","back");
                imgName = (String) params[0];
                String[] posts = {imgName};
                DB db = new DB("getImageLoc.php");
                String result = db.post(posts);
                Log.d("result",result);
                return result;
            }
        }
       GetCoordiTask task = new GetCoordiTask();
        task.execute(imgName);
    }
    private void getLocation(String latitude, String longitude){

        class GetLocTask extends AsyncTask<String, Void, String> {
            String latitude, longitude;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    String location = jsonObj.getString("fullName");
                    locationT_View.setText(location);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            @Override
            protected String doInBackground(String... params) {
                Log.d("result","back");
                latitude = (String) params[0];
                longitude = (String) params[1];
                DB db = new DB("");
                String result = db.getLocation("https://apis.daum.net/local/geo/coord2addr?apikey=d60942a7d1be4500fe58c3abd4d58110&longitude="+longitude+"&latitude="+latitude+"&inputCoordSystem=WGS84&output=json");
                Log.d("location result",result);
                return result;
            }
        }
        GetLocTask task = new GetLocTask();
        task.execute(latitude, longitude);
    }
    private void checkGIF(String imgName){

        class CheckGIFTask extends AsyncTask<String, Void, String> {
            String imgName;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                url = "http://13.124.33.214/darknet/" + imgName + ".gif";
                Glide.with(getApplicationContext()).load(url).into(imgView);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            protected String doInBackground(String... params) {
                Log.d("result","back");
                imgName = (String) params[0];
                String[] posts = {imgName};
                DB db = new DB("checkGIF.php");
                String result = db.postYolo(posts);
                Log.d("result",result);
                return result;
            }
        }
        CheckGIFTask task = new CheckGIFTask();
        task.execute(imgName);
    }
}
