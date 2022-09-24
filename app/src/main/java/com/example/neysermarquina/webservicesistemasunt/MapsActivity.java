package com.example.neysermarquina.webservicesistemasunt;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    private Marker marcador1;
    double lat = 0.0;
    double lng = 0.0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
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
        mMap.setMyLocationEnabled(true);
        miUbicacion();


    }

    private void agregarMarcador(double lat, double lng) {

        LatLng coordenadas = new LatLng(22.213554,-97.858321);

        //LatLng coordenadas = new LatLng(lat,lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);

        if (marcador != null) marcador.remove();
        Geocoder geocoder =new Geocoder(getApplicationContext());
        try{
            List<Address> addressList = geocoder.getFromLocation(22.2133912, -97.8581897, 1);
            String str  = addressList.get(0).getLocality()+" ";
            Address Dircalle = addressList.get(0);
            String direccion = (Dircalle).getAddressLine(0);
            str += addressList.get(0).getCountryName();

            int largo_imagen=150;
            int ancho_imagen=40;

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.control);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap iconopequeño = Bitmap.createScaledBitmap(b,ancho_imagen,largo_imagen,false);

            marcador = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title("Direccion: " + direccion)
                    .snippet("Por el momento no contamos con local")
                    .flat(true)
                    .anchor((0.5f), 1f)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(iconopequeño)));

                   //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.control)));
                     //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 16));
                     mMap.animateCamera(miUbicacion);

            LatLng ref = new LatLng(22.215211,-97.857544);
            marcador1 = mMap.addMarker(new MarkerOptions()
                    .position(ref)
                    .title("Plaza de armas")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.plaza)));
            LatLng ref1 = new LatLng(22.215110,-97.857554);
            LatLng ref2 = new LatLng(22.214825,-97.857710);
            LatLng ref3 = new LatLng(22.214412,-97.857935);
            LatLng ref4 = new LatLng(22.213627,-97.858358);
            LatLng ref5 = new LatLng(22.213585,-97.858377);

            mMap.addPolyline(new PolylineOptions()
                    .add(ref)
                    .add(ref1)
                    .add(ref2)
                    .add(ref3)
                    .add(ref4)
                    .add(ref5)
                    .add(coordenadas)
                    .width(4f)
                    .color(Color.RED)

            );

        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }







    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);

        }

    }

    LocationListener lcListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            //actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

        private void miUbicacion(){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //GPSPROVIDER
            actualizarUbicacion(location);
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER,15000,0,lcListener);
        }


}
