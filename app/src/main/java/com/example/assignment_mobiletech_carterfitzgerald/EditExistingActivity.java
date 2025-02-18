package com.example.assignment_mobiletech_carterfitzgerald;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.location.Geocoder;
import android.location.Location;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class EditExistingActivity extends AppCompatActivity {

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    private double latitude;
    public double longitude;
    public String address;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_existing);
        requestPermissions();

        Intent intent = getIntent();

        if (intent != null) {
            String activityName = intent.getStringExtra("activityName");
            TextView textView = findViewById(R.id.textView3);
            textView.setText(activityName);
            String title = intent.getStringExtra("title");
            EditText titleText = findViewById(R.id.details1);
            titleText.setText(title);
            String summary = intent.getStringExtra("summary");
            EditText summaryText = findViewById(R.id.details2);
            summaryText.setText(summary);
            String time = intent.getStringExtra("times");
            EditText timeText = findViewById(R.id.details3);
            timeText.setText(time);
            String date = intent.getStringExtra("dates");
            EditText dateText = findViewById(R.id.details4);
            dateText.setText(date);
            String contact = intent.getStringExtra("contact");
            EditText contactText = findViewById(R.id.details5);
            contactText.setText(contact);
            String spinner = intent.getStringExtra("spinner");
            EditText spinnerText = findViewById(R.id.details6);
            spinnerText.setText(spinner);
        }

    }

    public void openUploadImageActivity(View view){
            Intent intent = getIntent();
            String activityName = intent.getStringExtra("activityName");


        Intent intent1 = new Intent(this, UploadImageActivity.class);
            intent1.putExtra("activityName", activityName);
            intent1.putExtra("key", key);
            startActivity(intent1);
            finish();
    }

    public void showCurrentLocation(View view) {
        getLatLngAddress();
    }

    public boolean requestPermissions() {
        int REQUEST_PERMISSION = 3000;
        String permissions[] = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        boolean grantFinePermission =
                ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED;
        boolean grantCoarsePermission =
                ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED;

        if (!grantFinePermission && !grantCoarsePermission) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
        } else if (!grantFinePermission) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[0]}, REQUEST_PERMISSION);
        } else if (!grantCoarsePermission) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[1]}, REQUEST_PERMISSION);
        }

        return grantFinePermission && grantCoarsePermission;
    }

    public synchronized void getLatLngAddress() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(EditExistingActivity.this,
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                createLocationRequestLocationCallback();
                                startLocationUpdates();
                            }
                        });
    }

    public void createLocationRequestLocationCallback() {
        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                latitude = locationResult.getLastLocation().getLatitude();
                longitude = locationResult.getLastLocation().getLongitude();
            }
        };
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLatLngAddress();
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(EditExistingActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                });
    }

    public void uploadMultipleValuesToRealtimeDB(String contact, String dates,
                                                 double latitude, double longitude, String location,
                                                 String summary, String times, String title) {
        Intent intent = getIntent();
        String activityName = intent.getStringExtra("activityName");

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(activityName);
        key = dbRef.push().getKey();
        dbRef.child(key).child("contact").setValue(contact);
        dbRef.child(key).child("dates").setValue(dates);
        dbRef.child(key).child("imgFilename").setValue(key);
        dbRef.child(key).child("latLng").child("latitude").setValue(latitude);
        dbRef.child(key).child("latLng").child("longitude").setValue(longitude);
        dbRef.child(key).child("location").setValue(location);
        dbRef.child(key).child("summary").setValue(summary);
        dbRef.child(key).child("times").setValue(times);
        dbRef.child(key).child("title").setValue(title);
    }

    public void onClickUpdateButton(View view) {
        Intent intent = getIntent();
        String locationName = intent.getStringExtra("spinner");

        if (locationName.equals("Library")) {
            latitude = -35.237928620561014;
            longitude = 149.08360484608554;
        } else if (locationName.equals("Refractory")) {
            latitude = -35.23849600894474;
            longitude = 149.0844213224768;
        } else if (locationName.equals("Building 6")) {
            latitude = -35.236240246167554;
            longitude = 149.08397494549197;
        } else if (locationName.equals("UC Hub")) {
            latitude = -35.23809441627641;
            longitude = 149.0845431173291;
        } else if (locationName.equals("UC Lodge")) {
            latitude = -35.23816810315772;
            longitude = 149.0827951359983;
        } else if (locationName.equals("Current Location")) {
            getCurrentLocation();
        }

        String contact = intent.getStringExtra("contact");
        String date = intent.getStringExtra("dates");
        String summary = intent.getStringExtra("summary");
        String time = intent.getStringExtra("times");
        String title = intent.getStringExtra("title");

        uploadMultipleValuesToRealtimeDB(contact, date, latitude, longitude, locationName, summary, time, title);
        openUploadImageActivity(view);
    }
}

