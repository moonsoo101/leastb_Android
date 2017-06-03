package com.leastb.moonsoo.walkingeye.BroadCaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.leastb.moonsoo.walkingeye.LockScreenActivity;
import com.leastb.moonsoo.walkingeye.MainActivity;
import com.leastb.moonsoo.walkingeye.Services.ScreenService;
import com.leastb.moonsoo.walkingeye.Services.VoiceService;

public class StartReceiver extends BroadcastReceiver {

static String TAG = "StartReceiver";

    @Override

    public void onReceive(Context context, Intent intent) {



        String action= intent.getAction();



        //수신된 action값이 시스템의 '부팅 완료'가 맞는지 확인..

        if( action.equals("android.intent.action.BOOT_COMPLETED") ){
            Intent i = new Intent(context, ScreenService.class);
            context.startService(i);

//          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        }

    }

}
