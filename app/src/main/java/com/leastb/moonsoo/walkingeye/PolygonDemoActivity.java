package com.leastb.moonsoo.walkingeye;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.leastb.moonsoo.walkingeye.Util.DB;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PolygonDemoActivity extends Activity implements MapView.MapViewEventListener {

    private static final int MENU_ADD_POLYLINE1 = Menu.FIRST;
    private static final int MENU_ADD_POLYLINE2 = Menu.FIRST + 1;
    private static final int MENU_REMOVE_POLYLINES = Menu.FIRST + 2;
    private static final int MENU_ADD_CIRCLES = Menu.FIRST + 3;
    private static final int MENU_REMOVE_CIRCLE = Menu.FIRST + 4;
    MapPolyline polyline1;
    private MapView mMapView;
    MapPoint end = null;
    Button add;
    int id;
    private boolean isMapViewInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_nested_mapview);
        id = getIntent().getIntExtra("id", 0);
        Log.d("ssibal", Integer.toString(id));
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey("3442a54f5e352782458d10f8dab3077d");
        mMapView.setMapViewEventListener(this);
        searchCoordi(Integer.toString(id));

        add = (Button) findViewById(R.id.draw);
        add.setVisibility(View.INVISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPolyline1();
                add.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void searchCoordi(String id) {

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
                        int count = 0;
                        Double latitude = 0D;
                        Double longitude = 0D;
                        polyline1 = new MapPolyline();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            if (!c.getString("latitude").equals("null")) {
                                count++;
                                latitude = Double.parseDouble(c.getString("latitude"));
                                longitude = Double.parseDouble(c.getString("longitude"));
                                polyline1.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
                            }
                        }
                        if (count > 0) {
                            add.setVisibility(View.VISIBLE);
                            end = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                id = (String) params[0];
                String[] posts = {id};
                DB db = new DB("getCoordi.php");
                String result = db.post(posts);
                Log.d("result", result);
                return result;
            }
        }
        searchData task = new searchData();
        task.execute(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ADD_POLYLINE1, Menu.NONE, "Add Polyline1");
        menu.add(0, MENU_ADD_POLYLINE2, Menu.NONE, "Add Polyline2");
        menu.add(0, MENU_REMOVE_POLYLINES, Menu.NONE, "Remove All Polylines");
        menu.add(0, MENU_ADD_CIRCLES, Menu.NONE, "Add Circles");
        menu.add(0, MENU_REMOVE_CIRCLE, Menu.NONE, "Remove All Circles");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isMapViewInitialized) {
            Toast.makeText(this, "MapView is not initialized", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }

        final int itemId = item.getItemId();

        switch (itemId) {
            case MENU_ADD_POLYLINE1: {
                addPolyline1();
                return true;
            }
            case MENU_ADD_POLYLINE2: {

                return true;
            }
            case MENU_REMOVE_POLYLINES: {
                mMapView.removeAllPolylines();
                return true;
            }
            case MENU_ADD_CIRCLES: {

                return true;
            }
            case MENU_REMOVE_CIRCLE: {
                mMapView.removeAllCircles();

                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }



    private void addPolyline1() {
        MapPolyline existingPolyline = mMapView.findPolylineByTag(1000);
        if (existingPolyline != null) {
            mMapView.removePolyline(existingPolyline);
        }

        MapPOIItem poiItemStart = new MapPOIItem();
        poiItemStart.setItemName("Start");
        poiItemStart.setTag(10001);
        poiItemStart.setMapPoint(polyline1.getPoint(0));
        poiItemStart.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItemStart.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        poiItemStart.setShowCalloutBalloonOnTouch(false);
        poiItemStart.setCustomImageResourceId(R.drawable.custom_poi_marker_start);
        poiItemStart.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(29, 2));
        mMapView.addPOIItem(poiItemStart);

        MapPOIItem poiItemEnd = new MapPOIItem();
        poiItemEnd.setItemName("End");
        poiItemEnd.setTag(10001);
        poiItemEnd.setMapPoint(end);
        poiItemEnd.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        poiItemEnd.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        poiItemEnd.setShowCalloutBalloonOnTouch(false);
        poiItemEnd.setCustomImageResourceId(R.drawable.custom_poi_marker_end);
        poiItemEnd.setCustomImageAnchorPointOffset(new MapPOIItem.ImageOffset(29, 2));
        mMapView.addPOIItem(poiItemEnd);
        polyline1.setTag(1000);
        polyline1.setLineColor(Color.argb(128, 255, 51, 0));


        mMapView.addPolyline(polyline1);

        MapPointBounds mapPointBounds = new MapPointBounds(polyline1.getMapPoints());
        int padding = 100; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        isMapViewInitialized = true;
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}
