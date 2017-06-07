package com.leastb.moonsoo.walkingeye.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.leastb.moonsoo.walkingeye.ApplicationClass;
import com.leastb.moonsoo.walkingeye.Util.DB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by wisebody on 2017. 5. 29..
 */

public class CameraService extends Service {
    @Nullable
    public boolean loop;
    Calendar cal;
//    static String PI_IP = "192.168.43.244";
static String PI_IP = "192.168.43.247";
    int index;
    public static final String NOTIFICATION = "com.leastb.moonsoo.walkingeye.Fragment";
    public static final String INDEX = "index";
    //서비스 바인더 내부 클래스 선언
    public class MainServiceBinder extends Binder {
        public CameraService getService() {
            return CameraService.this; //현재 서비스를 반환.
        }
    }

    private final IBinder mBinder = new MainServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cal = Calendar.getInstance();
        Log.d("test", "서비스의 onCreate");
        index = 0;

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");
                    if(!loop) {
                        loop = true;
                        Intent intent1 = new Intent(
                                getApplicationContext(),//현재제어권자
                                VoiceService.class); // 이동할 컴포넌트
                        intent1.putExtra("text","실시간 감시를 시작합니다");
                        getApplicationContext().startService(intent1); // 서비스 시작
                        Toast.makeText(CameraService.this.getApplicationContext(),"실시간 감시를 시작합니다.",Toast.LENGTH_SHORT).show();
                        String id = ApplicationClass.ID;
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH)+1;
                        int day = cal.get(Calendar.DATE);
                        long time = System.currentTimeMillis();
                         registWatchDay(id, Integer.toString(year), Integer.toString(month), Integer.toString(day));
                        cameraStart(Long.toString(time), id, Integer.toString(year), Integer.toString(month), Integer.toString(day));
                        registImage(Long.toString(time), id, Integer.toString(year), Integer.toString(month), Integer.toString(day));
                    }
                    else
                    {
                        Intent intent1 = new Intent(
                                getApplicationContext(),//현재제어권자
                                VoiceService.class); // 이동할 컴포넌트
                        intent1.putExtra("text","이미 실시간 감시가 진행중입니다.");
                        getApplicationContext().startService(intent1); // 서비스 시작
                        Toast.makeText(CameraService.this.getApplicationContext(),"이미 실시간 감시가 진행중입니다.",Toast.LENGTH_SHORT).show();
                    }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loop) {
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    VoiceService.class); // 이동할 컴포넌트
            intent.putExtra("text","실시간 감시를 종료합니다.");
            getApplicationContext().startService(intent); // 서비스 시작
            Toast.makeText(CameraService.this.getApplicationContext(), "실시간 감시를 종료합니다.", Toast.LENGTH_SHORT).show();
            loop = false;
            index = 0;
        }
        else {
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    VoiceService.class); // 이동할 컴포넌트
            intent.putExtra("text","실시간 감시가 실행 중이지 않습니다.");
            getApplicationContext().startService(intent); // 서비스 시작
            Toast.makeText(CameraService.this.getApplicationContext(), "실시간 감시가 실행 중이지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void publishResults(String index) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("index", index);
        sendBroadcast(intent);
    }


    protected String cameraCall(String index, String id, String year, String month, String day)
    {
        try {
            String data = URLEncoder.encode("index", "UTF-8") + "=" + URLEncoder.encode(index, "UTF-8");
            data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8");
            data += "&" + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8");
            data += "&" + URLEncoder.encode("day", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8");
            URL url = new URL("http://"+PI_IP+"/leastb/camera.php");
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
            return sb.toString().trim();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    private void registImage(String index, String id, String year, String month, String day){

        class RegistTask extends AsyncTask<String, Void, String> {
            String index, id;
            String year, month, day;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                index = (String)params[0];
                id = (String)params[1];
                year = (String)params[2];
                month = (String)params[3];
                day = (String)params[4];
                String[] posts = {index, id, year, month, day};
                DB db = new DB("registImage.php");
                String result = db.post(posts);
                Log.d("CameraService", result);
                return result;
            }
        }
        RegistTask task = new RegistTask();
        task.execute(index, id, year, month, day);
    }
    private void registWatchDay(String id, String year, String month, String day){

        class RegistTask extends AsyncTask<String, Void, String> {
            String id;
            String year, month, day;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                id = (String)params[0];
                year = (String)params[1];
                month = (String)params[2];
                day = (String)params[3];
                String[] posts = {id, year, month, day};
                DB db = new DB("registWatchDay.php");
                String result = db.post(posts);
                Log.d("CameraService", result);
                return result;
            }
        }
        RegistTask task = new RegistTask();
        task.execute(id, year, month, day);
    }
    private void cameraStart(String index, String id, String year, String month, String day){

        class CameraTask extends AsyncTask<String, Void, String> {
            String index;
            String id;
            String year, month, day;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(loop) {
                    publishResults(id+year+month+day+"-"+index);
                    long time = System.currentTimeMillis();
                    registWatchDay(ApplicationClass.ID, Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH)+1), Integer.toString(cal.get(Calendar.DATE)));
                    cameraStart(Long.toString(time), ApplicationClass.ID, Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH)+1), Integer.toString(cal.get(Calendar.DATE)));
                    registImage(Long.toString(time), ApplicationClass.ID, Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH)+1), Integer.toString(cal.get(Calendar.DATE)));
                }

            }

            @Override
            protected String doInBackground(String... params) {

                index = (String)params[0];
                id = (String)params[1];
                year = (String)params[2];
                month = (String)params[3];
                day = (String)params[4];
                String result = cameraCall(index, id, year, month, day);
                Log.d("CameraService", result);
                return result;
            }
        }
        CameraTask task = new CameraTask();
        task.execute(index, id, year, month, day);
    }
}
