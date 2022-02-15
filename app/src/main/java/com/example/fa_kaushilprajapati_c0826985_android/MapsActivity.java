package com.example.fa_kaushilprajapati_c0826985_android;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.fa_kaushilprajapati_c0826985_android.databinding.ActivityMapsBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    LocationListener locationListener;
    Marker myMarker;
    public String loc;

    ArrayList<PlacesModel> placesModelArrayList = new ArrayList<>();
    DBHelper dbHelper = new DBHelper(MapsActivity.this);
    PlacesListAdapter placesListAdapter = new PlacesListAdapter(placesModelArrayList,dbHelper);

    private GoogleMap mMap;
    Button showPlaces,normal,satellite,hybrid,terrain;
    private ActivityMapsBinding binding;


    public void centerOnLocation(Location location,String title)
    {
        //mMap.clear();
        LatLng location1 = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(location1).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerOnLocation(lastLocation, "your Last Location");

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));
               // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20.0f));
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showPlaces = findViewById(R.id.showPlaces);
       // changeMap = findViewById(R.id.changeMap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        showPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        binding.normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        binding.satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        binding.hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        binding.terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        if(intent.getIntExtra("Place", 0)==0) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    centerOnLocation(location, "Your Favourite Place");
                }
            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerOnLocation(currentLocation,"Your Last Location");

            }
        }
            else{
                //Location favLocation = new Location(LocationManager.GPS_PROVIDER);
               // favLocation.setLatitude(MainActivity.location.get(intent.getIntExtra("Your Location",0)).latitude);
               // favLocation.setLongitude(MainActivity.location.get(intent.getIntExtra("Your Location",0)).longitude);
                //centerOnLocation(favLocation,MainActivity.arrayList.get(intent.getIntExtra("Your Location",0)));
            }
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    String FAVlocation ="";
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                        if(addresses.size()>0 && addresses!=null){
                            if(addresses.get(0).getAdminArea()!= null){
                                FAVlocation+=addresses.get(0).getAdminArea()+" ";
                            }if(addresses.get(0).getLocality()!=null){
                                FAVlocation+=addresses.get(0).getLocality()+" ";
                            }if(addresses.get(0).getSubLocality()!=null){
                                FAVlocation+=addresses.get(0).getSubLocality()+" ";
                            }
                        }
                        if(FAVlocation==""){
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-mm-yyyy");
                            FAVlocation+=sdf.format(new Date());
                        }
                        LatLng latLng1 = new LatLng(latLng.latitude,latLng.longitude);

                       myMarker =  mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(latLng1).title(FAVlocation).draggable(true));

                        myMarker.setPosition(latLng1);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));

                        dbHelper.insertPlaces(FAVlocation);

                        placesListAdapter.notifyDataSetChanged();
                        Toast.makeText(MapsActivity.this, "Favourite Location Added", Toast.LENGTH_SHORT).show();

                        MainActivity.arrayList.add(FAVlocation);

                        MainActivity.location.add(latLng1);

                        MainActivity.adapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });




    }




}