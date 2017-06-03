package com.leastb.moonsoo.walkingeye;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wisebody on 2017. 6. 3..
 */

public class LockScreenActivity extends Activity implements View.OnTouchListener {
    static String TAG = "LockSceenActivity";
    private static final String IMAGEVIEW_TAG = "드래그 이미지";
    CircleImageView Btn_drag;
    float dX, dY;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lockscreen);
        Btn_drag = (CircleImageView) findViewById(R.id.Btn_drag);
        Btn_drag.setOnTouchListener(this);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Log.d(TAG,"start");
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int eventaction = event.getAction();

        switch(eventaction)
        {
            case MotionEvent.ACTION_DOWN : // 손가락이 스크린에 닿았을 때
            {
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                Log.d(TAG,"down");
                Log.d(TAG,"getx : "+view.getX()+" raw : "+event.getRawX());
            }
                break;
            case MotionEvent.ACTION_MOVE : // 닿은 채로 손가락을 움직일 때
            {
                view.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                Log.d(TAG,"move");
            }
                break;
            case MotionEvent.ACTION_UP : // 닿았던 손가락을 스크린에서 뗄 때
            {
                Log.d(TAG,"up");
            }
                break;
        }

        return true;
    }
}
