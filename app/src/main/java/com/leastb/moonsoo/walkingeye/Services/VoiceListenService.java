package com.leastb.moonsoo.walkingeye.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;

import java.util.ArrayList;


/**
 * Created by wisebody on 2017. 6. 6..
 */

public class VoiceListenService extends Service implements SpeechRecognizeListener {
    static String APIKEY ="3442a54f5e352782458d10f8dab3077d";
    static String TAG="VoiceListenService";
    SpeechRecognizerClient client;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"create");
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setApiKey(APIKEY).     // 발급받은 api key
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WORD)
                .setUserDictionary("시작\n종료\n실행\n중지");// optional

        client = builder.build();
        client.setSpeechRecognizeListener(this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        client.startRecording(false);
        Log.d(TAG,"start");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Destroy");
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onReady() {
        Log.d(TAG,"Ready");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG,"begin speech");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG,"end speech");
    }

    @Override
    public void onError(int i, String s) {

        Log.d(TAG,"Error" + s);
    }

    @Override
    public void onPartialResult(String s) {
        Log.d(TAG,"partial " +s);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> texts =    results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs =     results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);
        Log.d(TAG, "result");
        for(String s : texts) {
            if(s.equals("시작")||s.equals("실행"))
            {
                client.stopRecording();
                Intent intent = new Intent(
                        getApplicationContext(),//현재제어권자
                        CameraService.class); // 이동할 컴포넌트
                getApplicationContext().startService(intent); // 서비스 시작\
            }
            else if(s.equals("종료")||s.equals("중지"))
            {
                client.stopRecording();
                Intent intent = new Intent(
                        getApplicationContext(),//현재제어권자
                        CameraService.class); // 이동할 컴포넌트
                getApplicationContext().stopService(intent); // 서비스 시작\
            }
        }

    }

    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onFinished() {
        Log.d("Voice","finish");
    }
}
