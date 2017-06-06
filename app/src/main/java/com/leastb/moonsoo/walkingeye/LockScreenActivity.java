package com.leastb.moonsoo.walkingeye;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.leastb.moonsoo.walkingeye.Util.DpPx;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wisebody on 2017. 6. 3..
 */

public class LockScreenActivity extends Activity implements View.OnTouchListener {
    static String TAG = "LockSceenActivity";
    private static final String IMAGEVIEW_TAG = "드래그 이미지";
    FrameLayout Btn_drag_cont;
    ImageView Btn_drag, unlock_back, play_back;
    float dX;
    float origin;
    DpPx dpPx;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(true);
        setContentView(R.layout.lockscreen);
        Btn_drag_cont = (FrameLayout) findViewById(R.id.Btn_drag_cont);
        Btn_drag = (ImageView) findViewById(R.id.Btn_drag);
        unlock_back = (ImageView) findViewById(R.id.unlock_back);
        play_back = (ImageView) findViewById(R.id.play_back);
        Btn_drag_cont.setOnTouchListener(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Log.d(TAG,"start");
    }
    @Override
    public void onResume()
    {
        super.onResume();

    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int eventaction = event.getAction();

        switch(eventaction)
        {
            case MotionEvent.ACTION_DOWN : // 손가락이 스크린에 닿았을 때
            {

                origin =view.getX();
                dX = view.getX() - event.getRawX();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                view.getLayoutParams().height = dpPx.dpToPx(50);
                view.getLayoutParams().width = dpPx.dpToPx(50);
                params.bottomMargin = dpPx.dpToPx(30);
                view.requestLayout();
                unlock_back.setVisibility(View.VISIBLE);
                play_back.setVisibility(View.VISIBLE);
                Log.d(TAG,"down");
                Log.d(TAG,"getx : "+view.getX()+" raw : "+event.getRawX());
                Log.d(TAG,"unlock_back : "+unlock_back.getX()+" play_back : "+play_back.getX()+dpPx.dpToPx(50));
            }
                break;
            case MotionEvent.ACTION_MOVE : // 닿은 채로 손가락을 움직일 때
            {
                view.animate()
                        .x(event.getRawX() + dX)
                        .setDuration(0)
                        .start();
                Log.d(TAG,"move");
                Rect unlock_rectf = new Rect();
                Rect play_rectf = new Rect();
                   unlock_back.getGlobalVisibleRect(unlock_rectf);
                    play_back.getGlobalVisibleRect(play_rectf);
                Log.d(TAG,"btn right : "+ (view.getX()+dpPx.dpToPx(50))+" btn left : "+view.getX() +" unlock_back : "+unlock_rectf.left+" play_back : "+play_rectf.right);
            }
                break;
            case MotionEvent.ACTION_UP : // 닿았던 손가락을 스크린에서 뗄 때
            {
                view.animate()
                        .x(origin)
                        .setDuration(0)
                        .start();
                Log.d(TAG,"up");
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                view.getLayoutParams().height = dpPx.dpToPx(60);
                view.getLayoutParams().width = dpPx.dpToPx(60);
                params.bottomMargin = dpPx.dpToPx(25);
                view.requestLayout();

                unlock_back.setVisibility(View.GONE);
                play_back.setVisibility(View.GONE);
            }
                break;
        }

        return true;
    }
    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
