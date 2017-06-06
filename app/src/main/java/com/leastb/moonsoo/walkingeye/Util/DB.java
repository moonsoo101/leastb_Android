package com.leastb.moonsoo.walkingeye.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class DB {
    String link;
    public DB(String php)
    {
        link = "http://ec2-13-124-108-18.ap-northeast-2.compute.amazonaws.com/leastb/"+php;
    }

    public String post(String[] posts)
    {
        Log.d("result","post");
        try {
            String data="";
            for(int i =1;i<=posts.length;i++) {
                if(i==1)
                    data = URLEncoder.encode("post"+i, "UTF-8") + "=" + URLEncoder.encode(posts[i-1], "UTF-8");
                else
                    data += "&" + URLEncoder.encode("post"+i, "UTF-8") + "=" + URLEncoder.encode(posts[i-1], "UTF-8");
            }

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            Log.d("string",sb.toString());
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
}



