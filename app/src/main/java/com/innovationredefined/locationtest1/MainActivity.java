package com.innovationredefined.locationtest1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.model.LatLng;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity{
    private LocationRequest mLocationRequest;
    private int LOCATION_PRIORITY;
    private long UPDATE_INTERVAL = 5 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100;    //Access code

    private double latitude;
    private double longitude;

    TextView textViewLatitudeNoPower;
    TextView textViewLongitudeNoPower;
    TextView textViewLatitudeLowPower;
    TextView textViewLongitudeLowPower;
    TextView textViewLatitudeBalancedPower;
    TextView textViewLongitudeBalancedPower;
    TextView textViewLatitudeHighAccuracy;
    TextView textViewLongitudeHighAccuracy;

    boolean hasLocationPremission;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonGetNoPowerLocation = findViewById(R.id.button_no_power);
        textViewLatitudeNoPower = findViewById(R.id.textView_latitude_no_power);
        textViewLongitudeNoPower = findViewById(R.id.textView_longitude_no_power);

        Button buttonGetLowPowerLocation = findViewById(R.id.button_low_power);
        textViewLatitudeLowPower = findViewById(R.id.textView_latitude_low_power);
        textViewLongitudeLowPower = findViewById(R.id.textView_longitude_low_power);

        Button buttonGetBalancedPowerLocation = findViewById(R.id.button_balanced_power);
        textViewLatitudeBalancedPower = findViewById(R.id.textView_latitude_balanced_power);
        textViewLongitudeBalancedPower = findViewById(R.id.textView_longitude_balanced_power);

        Button buttonGetHighAccuracyLocation = findViewById(R.id.button_high_accuracy);
        textViewLatitudeHighAccuracy = findViewById(R.id.textView_latitude_high_accuracy);
        textViewLongitudeHighAccuracy = findViewById(R.id.textView_longitude_high_accuracy);

        buttonGetNoPowerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCATION_PRIORITY = LocationRequest.PRIORITY_NO_POWER;
                requestPermissions();
                displayNewLocation(textViewLatitudeNoPower,textViewLongitudeNoPower,"No Power");
            }
        });

        buttonGetLowPowerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCATION_PRIORITY = LocationRequest.PRIORITY_LOW_POWER;
                requestPermissions();
                displayNewLocation(textViewLatitudeLowPower,textViewLongitudeLowPower,"Low Power");
            }
        });

        buttonGetBalancedPowerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCATION_PRIORITY = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                requestPermissions();
                displayNewLocation(textViewLatitudeBalancedPower,textViewLongitudeBalancedPower,"Balanced Power");
            }
        });

        buttonGetHighAccuracyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCATION_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
                requestPermissions();
                displayNewLocation(textViewLatitudeHighAccuracy,textViewLongitudeHighAccuracy,"High Accuracy");
            }
        });
    }




    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LOCATION_PRIORITY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions();
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //displayNewLocation();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    private void requestPermissions() {

        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    hasLocationPremission = true;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void displayNewLocation(TextView textViewLatitude, TextView textViewLongitude, String type) {
        startLocationUpdates();
        textViewLatitude.setText(String.valueOf(latitude));
        textViewLongitude.setText(String.valueOf(longitude));
        Toast.makeText(getBaseContext(), "Updating location "+type, Toast.LENGTH_SHORT).show();
    }

}
