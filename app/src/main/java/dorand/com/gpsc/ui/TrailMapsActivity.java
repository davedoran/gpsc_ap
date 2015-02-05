package dorand.com.gpsc.ui;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import com.dorand.gpsc.ui.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dorand.com.gpsc.service.intf.IGPError;
import dorand.com.gpsc.service.util.GPError;
import dorand.com.gpsc.service.util.GPJSON;
import dorand.com.gpsc.service.util.GPJSONUtils;

public class TrailMapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static JSONObject mGeoJson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        if ( mGeoJson == null ) {
            try {
                InputStream is = getAssets().open("ski.geojson");
                IGPError err = new GPError("setUpMap");
                mGeoJson = GPJSONUtils.parseStream(is, err);
            } catch (IOException e) {
            }
        }
        if ( mGeoJson != null ) {
            drawMap();
        }
    }

    private void drawMap() {
        JSONArray features = (JSONArray)mGeoJson.get(GPJSON.FEATURES);
        for ( Object o : features ) {
            if ( o instanceof JSONObject ) {
                JSONObject feature = (JSONObject)o;
                JSONObject properties = (JSONObject)feature.get(GPJSON.PROPS);
                JSONObject geometry = (JSONObject)feature.get(GPJSON.GEOMETRY);

                String featureType = (String)properties.get(GPJSON.TYPE);
                if ( featureType.equals(GPJSON.TRAIL)) {
                    drawTrail(geometry, properties);
                } else if ( featureType.equals(GPJSON.CABIN)) {
                    drawCabin(geometry, properties);
                } else if ( featureType.equals(GPJSON.PARKING)) {
                    drawParking(geometry, properties);
                }

            }
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    /*
     "geometry": {
                "type": "LineString",
                "coordinates": [
                    [
                        -75.82851727649535,
                        45.47151649631811,
                        0
                    ],
     */
    private void drawTrail(JSONObject geometry, JSONObject properties) {
        JSONArray coords = (JSONArray)geometry.get(GPJSON.COORDS);
        PolylineOptions opts = new PolylineOptions();
        LatLng last = null;
        for ( Object o : coords ) {
            JSONArray point = (JSONArray)o;
            double lng = Double.parseDouble((String)point.get(0));
            double lat = Double.parseDouble((String)point.get(1));
            LatLng latLng = new LatLng(lat, lng);
            if ( last != null ) {
                opts.add(last, latLng);
            }
            last = latLng;
        }
        opts.width(3);
        opts.color(Color.BLACK);
        mMap.addPolyline(opts);
    }

    private void drawCabin(JSONObject geometry, JSONObject properties) {

    }

    private void drawParking(JSONObject geometry, JSONObject properties) {

    }
}
