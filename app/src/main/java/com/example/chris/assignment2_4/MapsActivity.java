package com.example.chris.assignment2_4;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;


import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double latitude;
    private Double longitude;
    private RequestQueue queue;

    final TextView txtLatitude = (TextView) findViewById(R.id.txtLat);
    final TextView txtLongitude = (TextView) findViewById(R.id.txtLong);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final FusedLocationProviderClient myClient = LocationServices.getFusedLocationProviderClient(this);

        try
        {
            Task<Location> location = myClient.getLastLocation();


            location.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {


                    latitude = task.getResult().getLatitude();
                    longitude = task.getResult().getLongitude();

                    txtLatitude.setText(Double.toString(task.getResult().getLatitude()));
                    txtLongitude.setText(Double.toString(task.getResult().getLongitude()));




                    if(latitude != null && longitude != null)
                    {
                        LatLng myPos = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(myPos).title("My Current Position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));

                    }

                }
            });

        }catch (SecurityException ex)
        {
            ex.printStackTrace();
        }

        queue = Volley.newRequestQueue(getApplicationContext());

        String latstr = latitude.toString();
        String longstr = longitude.toString();

        String volleyQueryString = "select * from weather.forecast where woeid in " +
                "(SELECT woeid FROM geo.places WHERE text='( "+ latstr + "," + longstr + ")')";

        //couldn't figure out how to send above string to the api programatically and get the response
        //URL. Id have been able to use this reponse URL to make a jsonObjectRequest and get weather
        // for the users lat/long

        //also you cant add other UI elements onto a layout with a fragment. You can convert the
        // the fragment into a constraint layout and add textViews etc but it breaks the code.


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
}
