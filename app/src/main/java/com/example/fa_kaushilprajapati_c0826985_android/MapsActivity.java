package com.example.fa_kaushilprajapati_c0826985_android;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {
    private static final String TAG = "MapsActivity";
    LocationManager locationManager;
    LocationListener locationListener;
    Marker myMarker,finalMarker;
    public String loc;
    public LatLng favLOC;
    public LatLng currLOC;
    ArrayList<PlacesModel> placesModelArrayList = new ArrayList<>();
    DBHelper dbHelper = new DBHelper(MapsActivity.this);
    PlacesListAdapter placesListAdapter = new PlacesListAdapter(placesModelArrayList,dbHelper);

    private int id;
    PlacesModel placesModel;
    private GoogleMap mMap;
    Button showPlaces;
    private ActivityMapsBinding binding;


    public void centerOnLocation(Location location,String title)
    {
        //mMap.clear();
        currLOC = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currLOC).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLOC));

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
        PlacesModel placesModel = intent.getParcelableExtra("place");

        if(intent.getIntExtra("place", 0)==0) {
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
                Location favLocation = new Location(LocationManager.GPS_PROVIDER);
                favLocation.setLatitude(MainActivity.location.get(intent.getIntExtra("Your Location",0)).latitude);
                favLocation.setLongitude(MainActivity.location.get(intent.getIntExtra("Your Location",0)).longitude);
                centerOnLocation(favLocation,MainActivity.arrayList.get(intent.getIntExtra("Your Location",0)));
            }

            if(placesModel != null )
            {
                id = placesModel.id;
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(placesModel.Latitude);
                location.setLongitude(placesModel.Longitude);

                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(placesModel.address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).draggable(true));
               // drawLine();
            }


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {

                  marker.setTitle(com.example.fa_kaushilprajapati_c0826985_android.Address.getAddress(marker.getPosition().latitude,marker.getPosition().longitude, MapsActivity.this));
                if(placesModel != null) {
                    dbHelper.updatePlaces(placesModel, com.example.fa_kaushilprajapati_c0826985_android.Address.getAddress(marker.getPosition().latitude, marker.getPosition().longitude, MapsActivity.this), marker.getPosition().latitude, marker.getPosition().longitude);
                    placesListAdapter.notifyDataSetChanged();
                    Toast.makeText(MapsActivity.this, "Favourite Location Updated", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });

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
                        favLOC = new LatLng(latLng.latitude,latLng.longitude);

                        myMarker =  mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(favLOC).title(FAVlocation));
                        myMarker.setPosition(favLOC);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(favLOC));
                        dbHelper.insertPlaces(FAVlocation, latLng.latitude, latLng.longitude);
                        placesListAdapter.notifyDataSetChanged();
                        Toast.makeText(MapsActivity.this, "Favourite Location Added", Toast.LENGTH_SHORT).show();

                        MainActivity.arrayList.add(FAVlocation);

                        MainActivity.location.add(favLOC);
                        drawLine();
                        MainActivity.adapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

    }

    private void drawLine(){
        if (favLOC.longitude!= 0 && favLOC.latitude != 0 && currLOC.latitude != 0 && currLOC.longitude != 0) {
            PolylineOptions line =
                    new PolylineOptions().add(
                            new LatLng(favLOC.latitude, favLOC.longitude),
                            new LatLng(currLOC.latitude,
                                    currLOC.longitude))
                            .width(5).color(Color.RED);
            mMap.addPolyline(line);
             calculateDistance(new LatLng(currLOC.latitude, currLOC.longitude), new LatLng(favLOC.latitude, favLOC.longitude));
        }
    }
                        public void calculateDistance(LatLng StartP, LatLng EndP) {
                        int Radius = 6371;
                        double lat1 = StartP.latitude;
                        double lat2 = EndP.latitude;
                        double lon1 = StartP.longitude;
                        double lon2 = EndP.longitude;
                        double dLat = Math.toRadians(lat2 - lat1);
                        double dLon = Math.toRadians(lon2 - lon1);
                        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
                        double c = 2 * Math.asin(Math.sqrt(a));
                        double valueResult = Radius * c;
                        binding.distance.setVisibility(View.VISIBLE);
                        binding.distance.setText("Total Distance to the last marked Favourite place is :" + String.format("%.2f",valueResult) + "KM");
                    }

                    }
