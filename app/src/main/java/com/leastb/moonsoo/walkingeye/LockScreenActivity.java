package com.leastb.moonsoo.walkingeye;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by wisebody on 2017. 6. 3..
 */

public class LockScreenActivity extends Activity {
    static String TAG = "LockSceenActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lockscreen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Log.d(TAG,"start");
    }

}
