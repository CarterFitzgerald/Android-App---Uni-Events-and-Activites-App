package com.example.assignment_mobiletech_carterfitzgerald;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.assignment_mobiletech_carterfitzgerald.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolygonOptions;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private FusedLocationProviderClient fusedLocationClient;
    private Marker userLocationMarker;

    private String activityName;

    private List<Marker> activityMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("ACTIVITY_NAME")) {
            activityName = intent.getStringExtra("ACTIVITY_NAME");
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint({"PotentialBehaviorOverride", "MissingPermission"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<LatLng> uc = new ArrayList<>();
        uc.add(new LatLng(-35.2353691, 149.0914746));
        uc.add(new LatLng(-35.2390062, 149.0901682));
        uc.add(new LatLng(-35.2400065, 149.0899252));
        uc.add(new LatLng(-35.2405206, 149.0898873));
        uc.add(new LatLng(-35.2415457, 149.089981));
        uc.add(new LatLng(-35.2420814, 149.0894008));
        uc.add(new LatLng(-35.2422377, 149.0769777));
        uc.add(new LatLng(-35.2419722, 149.0761428));
        uc.add(new LatLng(-35.2416669, 149.0757536));
        uc.add(new LatLng(-35.2411542, 149.074698));
        uc.add(new LatLng(-35.240974, 149.0750658));
        uc.add(new LatLng(-35.2382806, 149.0771161));
        uc.add(new LatLng(-35.2378117, 149.0774194));
        uc.add(new LatLng(-35.2336506, 149.0793414));
        uc.add(new LatLng(-35.2329108, 149.0798677));
        uc.add(new LatLng(-35.2327255, 149.0799932));
        uc.add(new LatLng(-35.2321494, 149.0803377));
        uc.add(new LatLng(-35.2315465, 149.0806325));
        uc.add(new LatLng(-35.2314683, 149.08069));
        uc.add(new LatLng(-35.2313886, 149.0808109));
        uc.add(new LatLng(-35.2313771, 149.0809849));
        uc.add(new LatLng(-35.231435, 149.0814239));
        uc.add(new LatLng(-35.2315127, 149.0818787));
        uc.add(new LatLng(-35.2316636, 149.082554));
        uc.add(new LatLng(-35.2317299, 149.0828045));
        uc.add(new LatLng(-35.2319656, 149.0835451));
        uc.add(new LatLng(-35.2323638, 149.0845016));
        uc.add(new LatLng(-35.233774, 149.0869296));
        uc.add(new LatLng(-35.2346604, 149.0896269));
        uc.add(new LatLng(-35.2348966, 149.0911233));
        uc.add(new LatLng(-35.2353691, 149.0914746));

        PolygonOptions uchPolygonOptions = new PolygonOptions().geodesic(true);
        for (LatLng latLng : uc) {
            uchPolygonOptions.add(latLng);
        }
        uchPolygonOptions.strokeColor(Color.
                BLUE);
        uchPolygonOptions.strokeWidth(15.0f);

        mMap.addPolygon(uchPolygonOptions);// Use builder to zoom in the map to the greatest possible zoom level
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : uc) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        // Set padding (space around the boundary)
        int padding = 10;
        mMap.moveCamera(CameraUpdateFactory.
                newLatLngBounds(bounds, padding));

        LatLng thrriwirri_entry = new LatLng(-35.2341, 149.0791);
        LatLng allowoona_entry = new LatLng(-35.2340, 149.0871);
        LatLng university_entry = new LatLng(-35.2385, 149.0904);
        LatLng kirinari_entry = new LatLng(-35.2423, 149.0853);
        LatLng uc_loc = new LatLng(-35.24, 149.08);

        int width = 100; // Width of the marker icon
        int height = 100; // Height of the marker icon

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.pegman);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        Marker thrriwirriEntry = mMap.addMarker(new MarkerOptions()
                .position(thrriwirri_entry)
                .rotation(0)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        );

        Marker allowoonaEntry = mMap.addMarker(new MarkerOptions()
                .position(allowoona_entry)
                .rotation(0)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        );

        Marker universityEntry = mMap.addMarker(new MarkerOptions()
                .position(university_entry)
                .rotation(0)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        );

        Marker kirinariEntry = mMap.addMarker(new MarkerOptions()
                .position(kirinari_entry)
                .rotation(0)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(thrriwirriEntry)) {
                    // Launch StreetViewActivity1 when marker is clicked
                    Intent intent = new Intent(MapsActivity.this, StreetViewActivity1.class);
                    startActivity(intent);
                    return true;
                }
                else if (marker.equals(allowoonaEntry)) {
                    // Launch StreetViewActivity1 when marker is clicked
                    Intent intent = new Intent(MapsActivity.this, StreetViewActivity2.class);
                    startActivity(intent);
                    return true;
                }
                else if (marker.equals(universityEntry)) {
                    // Launch StreetViewActivity1 when marker is clicked
                    Intent intent = new Intent(MapsActivity.this, StreetViewActivity3.class);
                    startActivity(intent);
                    return true;
                }
                else if (marker.equals(kirinariEntry)) {
                    // Launch StreetViewActivity1 when marker is clicked
                    Intent intent = new Intent(MapsActivity.this, StreetViewActivity4.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView title = infoWindow.findViewById(R.id.textViewTitle);
                TextView snippet = infoWindow.findViewById(R.id.textViewSnippet);
                ImageView image = infoWindow.findViewById(R.id.imageView);

                if (marker.equals(userLocationMarker)) {
                    title.setText("You are here");
                    String address = "Address";
                    snippet.setText("Where: " + address);
                    Drawable drawable = ContextCompat.getDrawable(MapsActivity.this, R.drawable.blue_marker);
                    Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
                    int desiredWidth = originalBitmap.getWidth() / 16;
                    int desiredHeight = originalBitmap.getHeight() / 16;
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, false);
                    image.setImageBitmap(scaledBitmap);
                } else {
                    // If the marker is an activity marker
                    // Retrieve details from the real-time database based on the marker title
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(activityName);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String titleFromDatabase = snapshot.child("title").getValue(String.class);
                                String timeFromDatabase = snapshot.child("times").getValue(String.class);
                                String dateFromDatabase = snapshot.child("dates").getValue(String.class);
                                String locationFromDatabase = snapshot.child("location").getValue(String.class);

                                title.setText(titleFromDatabase);
                                snippet.setText("When: " + timeFromDatabase + " and " + dateFromDatabase + "\n" +
                                            "Where: " + locationFromDatabase);
                                // Set the appropriate marker icon for activity markers
                                Drawable drawable = ContextCompat.getDrawable(MapsActivity.this, R.drawable.blue_marker);
                                Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
                                int desiredWidth = originalBitmap.getWidth() / 16;
                                int desiredHeight = originalBitmap.getHeight() / 16;
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, false);
                                image.setImageBitmap(scaledBitmap);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors
                        }
                    });
                }
                return infoWindow;
            }

            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (!marker.equals(userLocationMarker)) {
                    Intent intent = new Intent(MapsActivity.this, ActivityDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

        mMap.setMyLocationEnabled(true);
        addMarkerAtCurrentUserLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    // Remove previous marker if exists
                    if (userLocationMarker != null) {
                        userLocationMarker.remove();
                    }
                    // Add marker at the user's current location
                    BitmapDrawable bitmapdraw = (BitmapDrawable) ContextCompat.getDrawable(MapsActivity.this, R.drawable.red_dot);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);
                    userLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(userLatLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                            .title("Your Location"));
                    // Call showDirection() here after user location marker is initialized
                    showDirection();
                }
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(activityName);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    double latitude = snapshot.child("latLng").child("latitude").getValue(Double.class);
                    double longitude = snapshot.child("latLng").child("longitude").getValue(Double.class);
                    LatLng location = new LatLng(latitude, longitude);
                    Marker marker = addMarker(location);
                    activityMarkers.add(marker);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    public String getMapApiKey(Context context) {
        String apiKey = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.
                            GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            if (bundle != null) {
                apiKey = bundle.getString("com.google.android.geo.API_KEY");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    private Marker addMarker(LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        return mMap.addMarker(markerOptions);
    }

    @SuppressLint("MissingPermission")
    private void addMarkerAtCurrentUserLocation(OnSuccessListener<Location> successListener) {
        // Get the user's current location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, successListener);
    }

    public void showDirection() {
        LatLng currentLocation = userLocationMarker.getPosition();

        for (Marker marker : activityMarkers) {
            LatLng destination = marker.getPosition();
            String apiKey = getMapApiKey(this);
            drawRoute(apiKey, currentLocation, destination);
        }
    }

    public void drawRoute(String apiKey, LatLng origin, LatLng destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude + "&destination="
                + destination.latitude + "," + destination.longitude
                + "&mode=walking&key=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.BLUE);
                        polylineOptions.width(14);
                        JSONArray routes = null;
                        try {
                            routes = response.getJSONArray("routes");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 0; i < routes.length(); i++) {
                            try {
                                JSONObject route = routes.getJSONObject(i);
                                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                                String points = overviewPolyline.getString("points");
                                List<LatLng> path = PolyUtil.decode(points);
                                polylineOptions.addAll(path);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        mMap.addPolyline(polylineOptions);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

}