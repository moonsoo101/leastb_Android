package com.leastb.moonsoo.walkingeye.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leastb.moonsoo.walkingeye.Adapter.BlackBoxRecyclerAdapter;
import com.leastb.moonsoo.walkingeye.Adapter.WatchDayAdapter;
import com.leastb.moonsoo.walkingeye.ApplicationClass;
import com.leastb.moonsoo.walkingeye.DTO.BlackBoxDTO;
import com.leastb.moonsoo.walkingeye.DTO.WatchDayDTO;
import com.leastb.moonsoo.walkingeye.EventDecorator;
import com.leastb.moonsoo.walkingeye.R;
import com.leastb.moonsoo.walkingeye.Util.DB;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Junyoung on 2016-06-23.
 */

public class TabFragment3 extends Fragment {
    SwipeRefreshLayout refreshLayout;
    RecyclerView watchDayRecy;
    LinearLayoutManager linearLayoutManager;
    WatchDayAdapter watchDayAdapter;
    ArrayList<WatchDayDTO> watchDayDTOs = new ArrayList<>();
//    mapView.setDaumMapApiKey("3442a54f5e352782458d10f8dab3077d");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        watchDayRecy = (RecyclerView) view.findViewById(R.id.watchDayRecy);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        watchDayRecy.setLayoutManager(linearLayoutManager);
        watchDayAdapter = new WatchDayAdapter(watchDayDTOs, R.layout.watchday_item);
        watchDayRecy.setAdapter(watchDayAdapter);
        searchWatchDay(ApplicationClass.ID);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchWatchDay(ApplicationClass.ID);
            }
        });
        return view;
    }
    private void searchWatchDay(String id){

        class searchData extends AsyncTask<String, Void, String> {
            String id;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArray = jsonObj.getJSONArray("result");
                    if (watchDayDTOs.size()>0)
                        watchDayDTOs.clear();
                    if(jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            watchDayDTOs.add(new WatchDayDTO(c.getInt("WDindex"),c.getString("year")+"년 "+c.getString("month")+"월 "+c.getString("day")+"일",c.getString("count")));
                        }
                        watchDayAdapter.notifyDataSetChanged();
                    }
                    refreshLayout.setRefreshing(false);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                id = (String) params[0];
                String[] posts = {id};
                DB db = new DB("getWatchDay.php");
                String result = db.post(posts);
                Log.d("result",result);
                return result;
            }
        }
        searchData task = new searchData();
        task.execute(id);
    }
}