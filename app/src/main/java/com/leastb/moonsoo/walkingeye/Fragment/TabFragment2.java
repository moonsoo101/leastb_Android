package com.leastb.moonsoo.walkingeye.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leastb.moonsoo.walkingeye.Adapter.BlackBoxRecyclerAdapter;
import com.leastb.moonsoo.walkingeye.ApplicationClass;
import com.leastb.moonsoo.walkingeye.DTO.BlackBoxDTO;
import com.leastb.moonsoo.walkingeye.EventDecorator;
import com.leastb.moonsoo.walkingeye.R;
import com.leastb.moonsoo.walkingeye.Util.DB;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TabFragment2 extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.calendarView)
    MaterialCalendarView widget;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    BlackBoxRecyclerAdapter blackBoxRecyclerAdapter;
    ArrayList<BlackBoxDTO> blackBoxDTOs = new ArrayList<>();
    ArrayList<CalendarDay> accidentDay = new ArrayList<>();
    ArrayList<CalendarDay> normalDay = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        ButterKnife.bind(this, view);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.setSelectedDate(Calendar.getInstance());
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        blackBoxRecyclerAdapter = new BlackBoxRecyclerAdapter(blackBoxDTOs, R.layout.blackbox_item);
        recyclerView.setAdapter(blackBoxRecyclerAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        searchAccidentDay(ApplicationClass.ID);
        Calendar cal = Calendar.getInstance();
        getBlackBox(ApplicationClass.ID, Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH) + 1), Integer.toString(cal.get(Calendar.DATE)));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchAccidentDay(ApplicationClass.ID);
                Calendar cal = Calendar.getInstance();
                getBlackBox(ApplicationClass.ID, Integer.toString(cal.get(Calendar.YEAR)), Integer.toString(cal.get(Calendar.MONTH) + 1), Integer.toString(cal.get(Calendar.DATE)));
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {

        getBlackBox(ApplicationClass.ID, Integer.toString(date.getYear()), Integer.toString(date.getMonth() + 1), Integer.toString(date.getDay()));
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions

    }


    private void searchAccidentDay(String id) {

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
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            if (c.getInt("isAccident") == 1)
                                accidentDay.add(CalendarDay.from(c.getInt("year"), c.getInt("month") - 1, c.getInt("day")));
                            else
                                normalDay.add(CalendarDay.from(c.getInt("year"), c.getInt("month") - 1, c.getInt("day")));
                        }
                        widget.addDecorator(new EventDecorator(Color.GREEN, normalDay));
                        widget.addDecorator(new EventDecorator(Color.RED, accidentDay));
                        widget.invalidateDecorators();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                id = (String) params[0];
                String[] posts = {id};
                DB db = new DB("getBlackBoxDayList.php");
                String result = db.post(posts);
                Log.d("result", result);
                return result;
            }
        }
        searchData task = new searchData();
        task.execute(id);
    }

    private void getBlackBox(String id, String year, String month, String day) {

        class SearchData extends AsyncTask<String, Void, String> {
            String id;
            String year, month, day;

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
                    if (blackBoxDTOs.size() > 0)
                        blackBoxDTOs.clear();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            blackBoxDTOs.add(new BlackBoxDTO(c.getString("imgName"), c.getInt("isAccident"), ""));
                        }
                    }
                    blackBoxRecyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                id = (String) params[0];
                year = (String) params[1];
                month = (String) params[2];
                day = (String) params[3];
                String[] posts = {id, year, month, day};
                DB db = new DB("getBlackBox.php");
                String result = db.post(posts);
                Log.d("result", result);
                return result;
            }
        }
        SearchData task = new SearchData();
        task.execute(id, year, month, day);
    }

}