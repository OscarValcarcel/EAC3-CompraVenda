package com.example.oscarvalcarcel.eac3_compravenda;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Activem el sistema de localització
        LocationManager gestorLoc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Comprovem que s'han concedit els persmisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        //Demanem l'actualització de la posició cada 5 segons o quan ens movem 5 metres
        gestorLoc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
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
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        //Creem un LatLng a partir de la posicio
        LatLng posicio = new LatLng(location.getLatitude(),location.getLongitude());

        //Creem un marcador amb un titol, l'afegim al mapa i el mostrem
        mMap.addMarker(new MarkerOptions().position(posicio).title("Fes click al marcador per acceptar")).showInfoWindow();

        //Afegim la camera amb nivell de zoom i la desplacem a la posició que hem creat
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicio, 16));

        //Afegin un listener per al marcador
        mMap.setOnMarkerClickListener(this);







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

    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent i = new Intent(this,Afegir.class);
        i.putExtra("location",location);
        setResult(RESULT_OK, i);
        finish();


        /*String text = "Posició actual:\n" +
                "Latitud " + location.getLatitude() + "\n" +
                "Longitud " + location.getLongitude();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();*/

        return true;
    }


}
