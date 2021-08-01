package com.example.movieclient.activity;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.movieclient.utils.NetworkConnection;
import com.example.movieclient.R;
import com.example.movieclient.bean.Cinema;
import com.example.movieclient.bean.User;
import com.example.movieclient.utils.SP;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private NetworkConnection http;
    private ProgressDialog dialog;
    private String postcode;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        userId = SP.getInstance(this).getInt("ID");
        postcode =  getIntent().getStringExtra("CODE");
        http = new NetworkConnection();
        dialog = new ProgressDialog(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Map");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_over);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
        dialog.show();
        new AddressTask().execute();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    private class AddressTask extends AsyncTask<String,Void,String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String url = NetworkConnection.USER_PATH +"/" + userId;
            String url2 =  NetworkConnection. CINEMA_PATH +"/findByPostcode/" + postcode;
            String personInfo = http.getRequest(url);
            String cinemaObj = http.getRequest(url2);
            return new String[]{personInfo,cinemaObj};
        }

        @Override
        protected void onPostExecute(String obj[]) {
            if (!TextUtils.isEmpty(obj[0])) {
                User user =     new Gson().fromJson(obj[0], User.class);
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    List<Address> list = geocoder.getFromLocationName(user.getStreetname(), 10);
                    if (list.size()>0){
                        Address address = list.get(0);
                        double  latitude = address.getLatitude();
                        double   longitude = address.getLongitude();
                        LatLng home = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(home).title(user.getStreetname())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 18));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            if (!TextUtils.isEmpty(obj[1])) {
                Type type = new TypeToken<List<Cinema>>(){}.getType();
                List<Cinema> list = new Gson().fromJson(obj[1], type);
                for (int i = 0; i < list.size(); i++) {
                    String name = list.get(i).getCinemaName();
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> result = geocoder.getFromLocationName(name, 10);
                        if (result.size()>0){
                            for (int j = 0; j < result.size(); j++) {
                                Address address = result.get(j);
                                if (address!=null) {
                                    double  latitude = address.getLatitude();
                                    double  longitude = address.getLongitude();
                                    LatLng cinemaLatlng = new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(cinemaLatlng).title(name)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cinemaLatlng, 18));
                                }
                            }


                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
            dialog.dismiss();

        }
    }
}
