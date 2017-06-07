package com.leastb.moonsoo.walkingeye.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import net.daum.mf.speech.api.TextToSpeechClient;
import net.daum.mf.speech.api.TextToSpeechListener;
import net.daum.mf.speech.api.TextToSpeechManager;

/**
 * Created by wisebody on 2017. 6. 3..
 */

public class VoiceService extends Service implements TextToSpeechListener {
    private TextToSpeechClient ttsClient;
//    static String APIKEY ="3442a54f5e352782458d10f8dab3077d"; //내꺼
    static String APIKEY ="a1f5628a1e0da6d4c4ad107ca75bad6e"; //민성이꺼
    String strText;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        TextToSpeechManager.getInstance().initializeLibrary(this);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행

        ttsClient = new TextToSpeechClient.Builder()
                .setApiKey(APIKEY)              // 발급받은 api key
                .setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1)            // 음성합성방식
                .setSpeechSpeed(1.0)            // 발음 속도(0.5~4.0)
                .setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_READ_CALM)  //TTS 음색 모드 설정(여성 차분한 낭독체)
                .setListener(this)
                .build();
        try {
            strText = intent.getStringExtra("text");
        } catch (NullPointerException e ) {
            strText="";
        }
        ttsClient.play(strText);

//// 또는
//        ttsClient.setSpeechText(strText);   //뉴톤톡 하고자 하는 문자열을 미리 세팅.
//        ttsClient.play();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onFinished() {
        int intSentSize = ttsClient.getSentDataSize();      //세션 중에 전송한 데이터 사이즈
        int intRecvSize = ttsClient.getReceivedDataSize();  //세션 중에 전송받은 데이터 사이즈

        final String strInacctiveText = "handleFinished() SentSize : " + intSentSize + "     RecvSize : " + intRecvSize;
        if(strText.equals("실행하실 동작을 말씀해 주세요.")) {
            Intent intent = new Intent(
                    getApplicationContext(),//현재제어권자
                    VoiceListenService.class);
            getApplicationContext().startService(intent);
        }
        Log.i("VoiceService", strInacctiveText);
    }

    @Override
    public void onError(int i, String s) {
        Log.d("VoiceService", s);
    }
}
