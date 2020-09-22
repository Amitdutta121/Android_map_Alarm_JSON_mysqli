package com.example.lab4;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    double globallat = 0.0;
    double globallng = 0.0;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    Button saveLocation;
    String IntentData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        saveLocation = findViewById(R.id.saveLocation);
        mapFragment.getMapAsync(this);

        Log.d("wwfuck","checking done");

        final MyHelper helper = new MyHelper(this);
        final SQLiteDatabase databaseWrite = helper.getWritableDatabase();


        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        IntentData = intent.getStringExtra("data");

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ContentValues values = new ContentValues();

                fusedLocationClient.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        values.put("LOCATION", "{latitude="+location.getLatitude()+", longitude="+location.getLongitude()+"}");
                        databaseWrite.update("employee",values,"_id = ?", new String[]{Integer.parseInt((IntentData.split(",")[3])+1)+""});
                        Toast.makeText(getApplicationContext(),"Database Updated",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


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

        // Add a marker in Sydney and move the camera

        Intent intent = getIntent();
        String IntentData = intent.getStringExtra("data");



        Log.d("CODEINM", IntentData);
        if (IntentData.split(",")[2].equalsIgnoreCase("noSave")){
            Log.d("CODEINM", "Checking no save");
            saveLocation.setVisibility(View.INVISIBLE);
            String latString = IntentData.split(",")[0];
            String lngString = IntentData.split(",")[1];
            double lat = Double.parseDouble(latString.substring(10, latString.length()));
            double lng = Double.parseDouble(lngString.substring(11, lngString.length()-1));
            LatLng sydney = new LatLng(lat,lng);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 9));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(MapsActivity.this, TimerActivity.class);
                    startActivity(intent);
                    return false;
                }
            });
        }else{
            saveLocation.setVisibility(View.VISIBLE);
            fusedLocationClient.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d("CODELOC", location.getLongitude()+"");
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 9));
                }
            });
        }
    }
}
