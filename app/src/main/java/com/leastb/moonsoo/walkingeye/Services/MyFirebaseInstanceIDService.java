package com.leastb.moonsoo.walkingeye.Services;

/**
 * Created by wisebody on 2017. 5. 15..
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.leastb.moonsoo.walkingeye.ApplicationClass;
import com.leastb.moonsoo.walkingeye.LoginActivity;
import com.leastb.moonsoo.walkingeye.MainActivity;
import com.leastb.moonsoo.walkingeye.Util.DB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
    }

}
