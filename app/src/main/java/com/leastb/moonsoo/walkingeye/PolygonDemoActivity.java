package com.leastb.moonsoo.walkingeye;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.leastb.moonsoo.walkingeye.DTO.WatchDayDTO;
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
    MapPoint start=null;
    MapPoint end=null;
    Button add;
    int size;
    int id;
    private MapPoint[] mPolyline2Points;
    private boolean isMapViewInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_nested_mapview);
        id = getIntent().getIntExtra("id",0);
        Log.d("ssibal",Integer.toString(id));
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey("3442a54f5e352782458d10f8dab3077d");
        mMapView.setMapViewEventListener(this);
        searchCoordi(Integer.toString(id));
//        mPolyline2Points = new MapPoint[]{
//                MapPoint.mapPointWithWCONGCoord(475334.0, 1101210.0),
//                MapPoint.mapPointWithWCONGCoord(474300.0,1104123.0),
//                MapPoint.mapPointWithWCONGCoord(474300.0,1104123.0),
//                MapPoint.mapPointWithWCONGCoord(473873.0,1105377.0),
//                MapPoint.mapPointWithWCONGCoord(473302.0,1107097.0),
//                MapPoint.mapPointWithWCONGCoord(473126.0,1109606.0),
//                MapPoint.mapPointWithWCONGCoord(473063.0,1110548.0),
//                MapPoint.mapPointWithWCONGCoord(473435.0,1111020.0),
//                MapPoint.mapPointWithWCONGCoord(474068.0,1111714.0),
//                MapPoint.mapPointWithWCONGCoord(475475.0,1112765.0),
//                MapPoint.mapPointWithWCONGCoord(476938.0,1113532.0),
//                MapPoint.mapPointWithWCONGCoord(478725.0,1114391.0),
//                MapPoint.mapPointWithWCONGCoord(479453.0,1114785.0),
//                MapPoint.mapPointWithWCONGCoord(480145.0,1115145.0),
//                MapPoint.mapPointWithWCONGCoord(481280.0,1115237.0),
//                MapPoint.mapPointWithWCONGCoord(481777.0,1115164.0),
//                MapPoint.mapPointWithWCONGCoord(482322.0,1115923.0),
//                MapPoint.mapPointWithWCONGCoord(482832.0,1116322.0),
//                MapPoint.mapPointWithWCONGCoord(483384.0,1116754.0),
//                MapPoint.mapPointWithWCONGCoord(484401.0,1117547.0),
//                MapPoint.mapPointWithWCONGCoord(484893.0,1117930.0),
//                MapPoint.mapPointWithWCONGCoord(485016.0,1118034.0)
//        };
        add = (Button)findViewById(R.id.draw);
        add.setVisibility(View.INVISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPolyline1();
                add.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void searchCoordi(String id){

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
                        int count = 0;
                        Double latitude=0D;
                        Double longitude=0D;
                        polyline1 = new MapPolyline();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            if(!c.getString("latitude").equals("null")) {
                                count++;
                                latitude = Double.parseDouble(c.getString("latitude"));
                                longitude = Double.parseDouble(c.getString("longitude"));
                                polyline1.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
                            }
                        }
                        if(count>0) {
                            add.setVisibility(View.VISIBLE);
                            end = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                        }
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
                DB db = new DB("getCoordi.php");
                String result = db.post(posts);
                Log.d("result",result);
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
//                addPolyline2();
                return true;
            }
            case MENU_REMOVE_POLYLINES: {
                mMapView.removeAllPolylines();
                return true;
            }
            case MENU_ADD_CIRCLES : {
                addCircles();
                return true;
            }
            case MENU_REMOVE_CIRCLE : {
                mMapView.removeAllCircles();

                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void addCircles() {
        MapCircle circle1 = new MapCircle(
        		MapPoint.mapPointWithGeoCoord(37.537094, 127.005470), // center
        		500, // radius
        		Color.argb(128, 255, 0, 0), // strokeColor
        		Color.argb(128, 0, 255, 0) // fillColor
        );
        circle1.setTag(1234);
        mMapView.addCircle(circle1);
        MapCircle circle2 = new MapCircle(
        		MapPoint.mapPointWithGeoCoord(37.551094, 127.019470), // center
        		1000, // radius
        		Color.argb(128, 255, 0, 0), // strokeColor
        		Color.argb(128, 255, 255, 0) // fillColor
        );
        circle2.setTag(5678);
        mMapView.addCircle(circle2);
        
     // 지도뷰의 중심좌표와 줌레벨을 Circle이 모두 나오도록 조정.
        MapPointBounds[] mapPointBoundsArray = { circle1.getBound(), circle2.getBound() };
        MapPointBounds mapPointBounds = new MapPointBounds(mapPointBoundsArray);
        int padding = 50; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    private void addPolyline2(MapPoint start, MapPoint end, int size) {

        MapPOIItem existingPOIItemStart = mMapView.findPOIItemByTag(10001);
        if (existingPOIItemStart != null) {
            mMapView.removePOIItem(existingPOIItemStart);
        }

        MapPOIItem existingPOIItemEnd = mMapView.findPOIItemByTag(10002);
        if (existingPOIItemEnd != null) {
            mMapView.removePOIItem(existingPOIItemEnd);
        }

        MapPolyline existingPolyline = mMapView.findPolylineByTag(2000);
        if (existingPolyline != null) {
            mMapView.removePolyline(existingPolyline);
        }

        MapPOIItem poiItemStart = new MapPOIItem();
        poiItemStart.setItemName("Start");
        poiItemStart.setTag(10001);
        poiItemStart.setMapPoint(start);
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

        MapPolyline polyline2 = new MapPolyline(size-3);
        polyline2.setTag(2000);
        polyline2.setLineColor(Color.argb(128, 0, 0, 255));
        polyline2.addPoints(mPolyline2Points);
        mMapView.addPolyline(polyline2);

        MapPointBounds mapPointBounds = new MapPointBounds(mPolyline2Points);
        int padding = 200; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
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
//        polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.537229, 127.005515));
//        polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.545024,127.03923));
//        polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.527896,127.036245));
//        polyline1.addPoint(MapPoint.mapPointWithGeoCoord(37.541889,127.095388));

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
