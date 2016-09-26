package com.smile.hungry;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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

import static com.smile.hungry.Utils.readUrl;

public class FreeFood extends AppCompatActivity implements OnMapReadyCallback {
    private final static String TAG = FreeFood.class.getSimpleName();

    private final static int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private boolean mAlreadyCentered = false;

    private Map<Marker, Long> foodSourcesIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_food);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        foodSourcesIds = new HashMap<>();

        Thread thread = new Thread(refreshing);
        thread.start();
    }

    private Runnable refreshing = new Runnable() {
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
                            Marker marker = mMap.addMarker(new MarkerOptions().position(city).title(foodSource.getName())
                                    .snippet(foodSource.getDescription().
                                            substring(0, Math.min(foodSource.getDescription().length(), 32))));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // We don't want the parent activity to be recreated
        if (id == R.id.refresh) {
            mMap.clear();
            new Thread(refreshing).start();
        }

        return super.onOptionsItemSelected(item);
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
                Intent intent = new Intent(FreeFood.this, FoodSourceDetails.class);
                intent.putExtra("food_source_id", foodSourcesIds.get(marker));
                startActivity(intent);
            }
        });
    }
}
