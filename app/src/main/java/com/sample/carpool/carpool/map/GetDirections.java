package com.sample.carpool.carpool.map;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.sample.carpool.carpool.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetDirections extends AsyncTask<Object,String,String> {

    private final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private String final_url;
    GoogleMap mMap;
    LatLngBounds latLngBounds;
    private static Polyline[] polylines;

    public GetDirections(LatLng origin, LatLng destination) {
        final_url = BASE_URL + "origin=" + origin.latitude + "," + origin.longitude + "&destination=" + destination.latitude + "," + destination.longitude + "&alternatives=true&region=in&key=AIzaSyBphoy0vCpUtrBIH4JkwPJyLIMJln6Ulvg";
    }

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        DownloadData downloadData = new DownloadData();
        System.out.println(final_url);
        return downloadData.readUrl(final_url);
    }

    @Override
    protected void onPostExecute(String s) {
        String[] strings;

        DataParser dataParser = new DataParser();
        JSONArray jsonArray;
        JSONObject jsonObject;
        JSONArray routes;
        JSONObject southwest, northeast;
        int[] colors = {Color.BLUE, Color.CYAN};

        try {
            jsonObject = new JSONObject(s);
            routes = jsonObject.getJSONArray("routes");
            southwest = routes.getJSONObject(0).getJSONObject("bounds").getJSONObject("southwest");
            northeast = routes.getJSONObject(0).getJSONObject("bounds").getJSONObject("northeast");
            latLngBounds = new LatLngBounds(new LatLng(southwest.getDouble("lat"),southwest.getDouble("lng")),new LatLng(northeast.getDouble("lat"),northeast.getDouble("lng")));

            polylines = new Polyline[routes.length()];

            for(int i=0;i<routes.length();i++) {
                jsonArray = routes.getJSONObject(i).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                strings = dataParser.getPaths(jsonArray);
                polylines[i] = displayDirections(strings,colors[Math.min(i,1)]);
            }

            polylineSelected(polylines[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Polyline displayDirections(String[] directionsList, int color)
    {
        PolylineOptions options = new PolylineOptions();
        for (String aDirectionsList : directionsList) {
            options.color(color);
            options.width(10);
            options.jointType(JointType.ROUND);
            options.clickable(true);
            options.addAll(PolyUtil.decode(aDirectionsList));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));
        return mMap.addPolyline(options);
    }

    public static void polylineSelected(Polyline polyline) {
        for(Polyline polyline1 : polylines) {
            if(polyline1.getId().equals(polyline.getId())) {
                polyline1.setColor(Color.BLUE);
                polyline1.setZIndex(5);
            }
            else {
                polyline1.setColor(Color.CYAN);
                polyline1.setZIndex(4);
            }
        }
    }
}
