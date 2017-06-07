package com.leastb.moonsoo.walkingeye;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leastb.moonsoo.walkingeye.Util.DB;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wisebody on 2017. 6. 8..
 */

public class BlackBoxActivity extends Activity {
    ImageView imgView;
    String url;
    ProgressBar progressBar;
    TextView dateT_View;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackbox);
        imgView = (ImageView) findViewById(R.id.imgView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        dateT_View = (TextView) findViewById(R.id.date);
        String imgName = getIntent().getStringExtra("imgName");
        int isAccident = getIntent().getIntExtra("isAccident",0);
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
