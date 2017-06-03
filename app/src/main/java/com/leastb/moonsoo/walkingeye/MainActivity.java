package com.leastb.moonsoo.walkingeye;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.leastb.moonsoo.walkingeye.Services.CameraService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.leastb.moonsoo.walkingeye.Adapter.TabPagerAdapter;
import com.leastb.moonsoo.walkingeye.Services.ScreenService;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private TabPagerAdapter mPagerAdapter;
//    TextView textResult;
//
//    ArrayList<Node> listNote;
private BroadcastReceiver receiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int index = bundle.getInt(CameraService.INDEX);
            mPagerAdapter.tabFragment1.loagPicture(index);
        }
    }
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();
        Intent intent = new Intent(this, ScreenService.class);
        startService(intent);
//        btnRead = (Button)findViewById(R.id.readclient);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Tab One"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab Two"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab Three"));
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
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(CameraService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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