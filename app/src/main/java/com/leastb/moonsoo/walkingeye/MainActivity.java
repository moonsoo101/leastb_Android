package com.leastb.moonsoo.walkingeye;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.leastb.moonsoo.walkingeye.Services.CameraService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.leastb.moonsoo.walkingeye.Adapter.TabPagerAdapter;
import com.leastb.moonsoo.walkingeye.Services.VoiceService;
import com.leastb.moonsoo.walkingeye.Util.DB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    public TabPagerAdapter mPagerAdapter;
    private VoiceService voiceService;
    private boolean bound = false;
    //    TextView textResult;
//
//    ArrayList<Node> listNote;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String index = bundle.getString(CameraService.INDEX);
                mPagerAdapter.tabFragment1.loadPicture(index);
                Log.d("index", index);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarTranslucent(true);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();
//        Intent intent = new Intent(this, ScreenService.class);
//        startService(intent);
//        btnRead = (Button)findViewById(R.id.readclient);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("전방감시"));
        tabLayout.addTab(tabLayout.newTab().setText("블랙박스"));
        tabLayout.addTab(tabLayout.newTab().setText("이동 경로"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
        updateToDatabase(ApplicationClass.ID, FirebaseInstanceId.getInstance().getToken());

//        textResult = (TextView)findViewById(R.id.result);
//
//        listNote = new ArrayList<>();
//
//        btnRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                readAddresses();
//                textResult.setText("");
//                for(int i=0; i<listNote.size(); i++){
//                    textResult.append(i + " ");
//                    textResult.append(listNote.get(i).toString());
//                    textResult.append("\n");
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(CameraService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            Log.d("volume", "down");
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    VoiceService.class); // 이동할 컴포넌트
            intent.putExtra("text", "실행하실 동작을 말씀해 주세요.");
            getApplicationContext().startService(intent);

        }
        return super.onKeyDown(keyCode, event);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void updateToDatabase(String id, String token) {

        class SearchData extends AsyncTask<String, Void, String> {
            String id;
            String token;
            /*WeakReference<Activity> mActivityReference;
            public SearchData(Activity activity){
                this.mActivityReference = new WeakReference<Activity>(activity);
            }*/

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
                Log.d("result", "back");
                id = (String) params[0];
                token = (String) params[1];
                String[] posts = {id, token};
                DB db = new DB("register.php");
                String result = db.post(posts);
                Log.d("result", result);
                return result;
            }
        }
        SearchData task = new SearchData();
        task.execute(id, token);
    }


//    private void readAddresses() {
//        listNote.clear();
//        BufferedReader bufferedReader = null;
//
//        try {
//            bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                String[] splitted = line.split(" +");
//                if (splitted != null && splitted.length >= 4) {
//                    String ip = splitted[0];
//                    String mac = splitted[3];
//                    if (mac.matches("..:..:..:..:..:..")) {
//                        Node thisNode = new Node(ip, mac);
//                        listNote.add(thisNode);
//                    }
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally{
//            try {
//                bufferedReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    class Node {
//        String ip;
//        String mac;
//
//        Node(String ip, String mac){
//            this.ip = ip;
//            this.mac = mac;
//        }
//
//        @Override
//        public String toString() {
//            return ip + " " + mac;
//        }
//    }
}