package com.fiap.rodrigo.trabalhofinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.fiap.rodrigo.trabalhofinal.Model.Restaurante;
import com.fiap.rodrigo.trabalhofinal.dao.RestauranteDAO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import livroandroid.lib.utils.HttpHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private static String id;
    Restaurante r = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        Intent it = getIntent();
        Bundle params = it.getExtras();


        if (params != null) {
            r = new Restaurante();
            id = params.getString("mapaUnico").toString();

            r = RetornaRestaurante(id);
        }

    }

    public Restaurante RetornaRestaurante(String index) {
        RestauranteDAO dao = new RestauranteDAO(this);
        List<Restaurante> lista = new ArrayList<>();
        lista = dao.getRestaurante();
        Restaurante rest = new Restaurante();
        rest = lista.get(Integer.parseInt(index));

        return rest;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng fiap = new LatLng(-23.5740406, -46.6234089);
        //mMap.addMarker(new MarkerOptions().position(fiap).title("Voce").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurante))).showInfoWindow();
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiap, 13.0f));
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
        //mMap.addMarker(new MarkerOptions().position(fiap).title("Você"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(fiap));

        //colocarNoMapa();

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();

    }

   /* private void colocarNoMapa() {

        List<Restaurante> lst = new ArrayList<>();
        RestauranteDAO dao = new RestauranteDAO(this);
        lst = dao.getRestaurante();

        for (Restaurante item : lst) {
            LatLng posicao = new LatLng(
                    Double.parseDouble(item.getLatitude()),
                    Double.parseDouble(item.getLongitude()));

            mMap.addMarker(new MarkerOptions()
                    .position(posicao)
                    .title(item.getNome()));
        }

    }*/

    @Override
    public void onConnected(Bundle bundle) {
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
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUI();
    }

    private void updateUI() {

        if (null != mCurrentLocation) {
            //String lat = String.valueOf(mCurrentLocation.getLatitude());
            //String lng = String.valueOf(mCurrentLocation.getLongitude());

            LatLng eu = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            LatLng fiap = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(fiap).title("Meu Local").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurante))).showInfoWindow();

            if (r == null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiap, 9.0f));
            else
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiap, 11.0f));


            List<Restaurante> lst = new ArrayList<>();
            if (r == null) {
                RestauranteDAO dao = new RestauranteDAO(this);
                lst = dao.getRestaurante();
            } else {
                lst.add(r);
            }

            AdicionaNoMapa(lst, eu);


        } else {
            //falha
        }
    }

    public void AdicionaNoMapa(List<Restaurante> lst, LatLng position) {
        if (r == null) {
            for (Restaurante r : lst) {
                LatLng posicao = new LatLng(
                        Double.parseDouble(r.getLatitude()),
                        Double.parseDouble(r.getLongitude()));
               /* mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(r.getNome())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_adicionar)));*/
                mMap.addMarker(new MarkerOptions()
                        .position(posicao)
                        .title(r.getNome()));

            }
        } else {

            PolylineOptions line = new PolylineOptions();
            line.add(position);
            LatLng posicao2 = new LatLng(Double.parseDouble(r.getLatitude()), Double.parseDouble(r.getLongitude()));
            line.add(posicao2);
            mMap.addMarker(new MarkerOptions()
                    .position(posicao2)
                    .title(r.getNome()));
            line.color(Color.RED);
            Polyline polyline = mMap.addPolyline(line);
            polyline.setGeodesic(true);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();

        }
    }

    protected void startLocationUpdates() {
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
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }
}
