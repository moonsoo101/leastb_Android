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
static String PI_IP = "192.168.43.244";
    int index;
    Thread thread;
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

    //콜백 인터페이스 선언
    public interface ICallback {
        public void recvData(); //액티비티에서 선언한 콜백 함수.
    }

    private ICallback mCallback;

    //액티비티에서 콜백 함수를 등록하기 위함.
    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }

    //액티비티에서 서비스 함수를 호출하기 위한 함수 생성
    public void myServiceFunc(){
        //서비스에서 처리할 내용
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
                        Toast.makeText(CameraService.this.getApplicationContext(),"실시간 감시를 시작합니다.",Toast.LENGTH_SHORT).show();
                        cameraStart(Long.toString(System.currentTimeMillis()), ApplicationClass.ID, Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH)+1), Integer.toString(cal.get(Calendar.DATE)));
                    }
                    else
                    {
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(CameraService.this.getApplicationContext(),"이미 실시간 감시가 진행중입니다.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        loop = false;
        index = 0;
        thread= null;
        Log.d("test", "서비스의 onDestroy");
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
                    cameraStart(Long.toString(System.currentTimeMillis()), ApplicationClass.ID, Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH)+1), Integer.toString(cal.get(Calendar.DATE)));
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
