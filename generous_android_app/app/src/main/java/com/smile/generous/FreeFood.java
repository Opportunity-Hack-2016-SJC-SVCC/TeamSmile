package com.smile.generous;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.smile.generous.Utils.readUrl;

public class FreeFood extends FragmentActivity implements OnMapReadyCallback {
    private final static String TAG = FreeFood.class.getSimpleName();

    private final static int MY_LOCATION_REQUEST_CODE = 1;
    public final static int ACTIVITY_RESULT_SAVED = 2;
    private GoogleMap mMap;
    private boolean mAlreadyCentered = false;

    private FloatingActionButton mFloatingActionButton;

    private Map<Marker, Long> foodSourcesIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_food);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.add_place);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FreeFood.this, FoodSourceAddEdit.class);
                startActivityForResult(intent, ACTIVITY_RESULT_SAVED);
            }
        });

        foodSourcesIds = new HashMap<>();

        new Thread(refreshing).start();
    }

    Runnable refreshing = new Runnable() {
        String jsonData = "";

        @Override
        public void run() {
            try {
                jsonData = readUrl("http://139.59.212.15:3045/api/food_source/?format=json");
                Gson gson = new Gson();

                Type listType = new TypeToken<List<FoodSource>>() {}.getType();
                final List<FoodSource> foodSources = new Gson().fromJson(jsonData, listType);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (FoodSource foodSource : foodSources) {
                            final LatLng city = new LatLng(foodSource.getLongitude(), foodSource.getLatitude());

                            MarkerOptions markerOptions = new MarkerOptions().position(city).title(foodSource.getName());
                            if (foodSource.getDescription() != null && !foodSource.getDescription().isEmpty()) {
                                markerOptions.snippet(foodSource.getDescription().
                                        substring(0, Math.min(foodSource.getDescription().length(), 32)));
                            }

                            Marker marker = mMap.addMarker(markerOptions);
                            foodSourcesIds.put(marker, foodSource.getId());
                        }
                    }
                });
            }

            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(FreeFood.this, "There was some connection problem. " +
                                "Please try again later",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    Objects.equals(permissions[0], Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(FreeFood.this, "You need to grant location permission to use this app",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mFloatingActionButton.hide();
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mFloatingActionButton.show();
            }
        });

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!mAlreadyCentered) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                    mAlreadyCentered = true;
                }
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(FreeFood.this, FoodSourceAddEdit.class);
                intent.putExtra("food_source_id", foodSourcesIds.get(marker));
                startActivityForResult(intent, ACTIVITY_RESULT_SAVED);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == RESULT_OK && (requestCode == ACTIVITY_RESULT_SAVED) ) {
            mMap.clear();
            new Thread(refreshing).start();
        }
    }
}
