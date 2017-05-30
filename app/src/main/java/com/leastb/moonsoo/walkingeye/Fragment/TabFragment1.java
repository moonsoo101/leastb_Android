package com.leastb.moonsoo.walkingeye.Fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.leastb.moonsoo.walkingeye.CameraService;
import com.leastb.moonsoo.walkingeye.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Junyoung on 2016-06-23.
 */

public class TabFragment1 extends Fragment {
    View view;
    ImageView imageView;
    Button startBtn, stopBtn;
    boolean conn;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int index = bundle.getInt(CameraService.INDEX);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        imageView = (ImageView)view.findViewById(R.id.img);
        startBtn = (Button) view.findViewById(R.id.startBtn);
        stopBtn = (Button) view.findViewById(R.id.stopBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(),//현재제어권자
                        CameraService.class); // 이동할 컴포넌트
                conn = true;
                getActivity().startService(intent); // 서비스 시작

            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conn) {
                    Intent intent = new Intent(
                            getActivity(),//현재제어권자
                            CameraService.class); // 이동할 컴포넌트
                    getActivity().stopService(intent); // 서비스 종료
                    conn = false;
                }
            }
        });
        return view;
    }
    public void loagPicture(int index)
    {
        String url = "http://13.124.33.214/darknet/image"+index+".jpg";
        Picasso.with(getActivity()).load(url).into(imageView);
    }
}