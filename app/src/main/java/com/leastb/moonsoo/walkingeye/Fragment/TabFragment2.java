package com.leastb.moonsoo.walkingeye.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.leastb.moonsoo.walkingeye.ApplicationClass;
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

/**
 * Created by Junyoung on 2016-06-23.
 */

public class TabFragment2 extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    @BindView(R.id.calendarView)
    MaterialCalendarView widget;
    @BindView(R.id.textView)
    TextView textView;
    ArrayList<CalendarDay> accidentDay = new ArrayList<>();
    ArrayList<CalendarDay> normalDay = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        ButterKnife.bind(this, view);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.setSelectedDate(Calendar.getInstance());
        searchAccidentDay(ApplicationClass.ID);
        textView.setText(getSelectedDatesString());
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date, boolean selected) {
        textView.setText(getSelectedDatesString());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //noinspection ConstantConditions

    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
    private void searchAccidentDay(String id){

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
                    if(jsonArray.length()>0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            if(c.getInt("isAccident")==1)
                                accidentDay.add(CalendarDay.from(c.getInt("year"),c.getInt("month")-1,c.getInt("day")));
                            else
                                normalDay.add(CalendarDay.from(c.getInt("year"),c.getInt("month")-1,c.getInt("day")));
                        }
                        widget.addDecorator(new EventDecorator(Color.RED, accidentDay));
                        widget.addDecorator(new EventDecorator(Color.GREEN, normalDay));
                        widget.invalidateDecorators();
                    }
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
                DB db = new DB("getBlackBoxDayList.php");
                String result = db.post(posts);
                Log.d("result",result);
                return result;
            }
        }
        searchData task = new searchData();
        task.execute(id);
    }

}