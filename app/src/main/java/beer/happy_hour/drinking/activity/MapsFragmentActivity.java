package beer.happy_hour.drinking.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.DeliveryPlace;

public class MapsFragmentActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap map;
    private LatLng myLocation;
    private DeliveryPlace deliveryPlace;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        deliveryPlace = DeliveryPlace.getInstance();
        myLocation = new LatLng(deliveryPlace.getLatitude(), deliveryPlace.getLongitude());
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
        map = googleMap;

        // Move the camera
        if (Math.abs(deliveryPlace.getLatitude() - 0) < 0.0001 && Math.abs(deliveryPlace.getLongitude() - 0) < 0.0001) {
            LatLng goiania = new LatLng(-16.666667, -49.25);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(goiania, 11));
        } else
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                map.clear();

                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                map.addMarker(markerOptions);

                //Capturando localização
                setMyLocation(latLng);

                //Setando Endereço
                setMyLocationAdress();
            }
        });

//        map.setOnMyLocationChangeListener(myLocationChangeListener());

        map.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener());
    }

    public void setMyLocation(LatLng latLng) {
        myLocation = latLng;
    }

    public void setMyLocationAdress() {
        try {
            Geocoder geo = new Geocoder(MapsFragmentActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(myLocation.latitude, myLocation.longitude, 1);
            if (addresses.isEmpty()) {
                Log.d("Location", "Waiting for Location");
            } else {
                if (addresses.size() > 0) {
                    Log.d("Location", addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
//                    yourtextfieldname.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());

                    for (int i = 0; i <= 2; i++)
                        Log.d("AdressLine " + i, addresses.get(0).getAddressLine(i));

                    //get current adress line 1
                    deliveryPlace.setAdress(addresses.get(0).getAddressLine(0));
                    //get current city/state
                    deliveryPlace.setCityState(addresses.get(0).getAddressLine(1));
                    //get country
                    deliveryPlace.setCountryName(addresses.get(0).getCountryName());
                    //get postal code
                    deliveryPlace.setZipCode(addresses.get(0).getPostalCode());
                    //get place Name
                    deliveryPlace.setFeatureName(addresses.get(0).getFeatureName());
                    //get latitude
                    deliveryPlace.setLatitude(addresses.get(0).getLatitude());
                    //get longitude
                    deliveryPlace.setLongitude(addresses.get(0).getLongitude());
                    //get country code
                    deliveryPlace.setCountryCode(addresses.get(0).getCountryCode());
                    //get state
                    deliveryPlace.setAdminArea(addresses.get(0).getAdminArea());
                    //get city
                    deliveryPlace.setLocality(addresses.get(0).getLocality());
                    //get "bairro"
                    deliveryPlace.setSubLocality(addresses.get(0).getSubLocality());
                    //get street
                    deliveryPlace.setThoroughfare(addresses.get(0).getThoroughfare());
                    //get aproximate number
                    deliveryPlace.setSubThoroughfare(addresses.get(0).getSubThoroughfare());

                    Log.d("DeliveryPlace", deliveryPlace.toString());

                    Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();

                    Log.d("getAddressLine(0)", addresses.get(0).getAddressLine(0));
                    Log.d("getAddressLine(1)", addresses.get(0).getAddressLine(1));
                    Log.d("getAddressLine(2)", addresses.get(0).getAddressLine(2));
                    Log.d("getAdminArea", addresses.get(0).getAdminArea());
                    Log.d("getCountryCode", addresses.get(0).getCountryCode());
                    Log.d("getCountryName", addresses.get(0).getCountryName());
                    Log.d("getFeatureName", addresses.get(0).getFeatureName());
                    Log.d("getLocality", addresses.get(0).getLocality());
                    Log.d("getPostalCode", addresses.get(0).getPostalCode());
                    Log.d("getSubAdminArea", addresses.get(0).getSubAdminArea());
                    Log.d("getSubLocality", addresses.get(0).getSubLocality());
                    Log.d("getSubThoroughfare", addresses.get(0).getSubThoroughfare());
                    Log.d("getThoroughfare", addresses.get(0).getThoroughfare());
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener() {
        return new GoogleMap.OnMyLocationButtonClickListener() {

            @Override
            public boolean onMyLocationButtonClick() {
//                // Getting LocationManager object from System Service LOCATION_SERVICE
//                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//                // Creating a criteria object to retrieve provider
//                Criteria criteria = new Criteria();
//
//                // Getting the name of the best provider
//                String provider = locationManager.getBestProvider(criteria, true);
//
//                // Getting Current Location
//                Location location = locationManager.getLastKnownLocation(provider);

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {
                    // Getting latitude of the current location
                    double latitude = location.getLatitude();

                    // Getting longitude of the current location
                    double longitude = location.getLongitude();

                    myLocation = new LatLng(latitude, longitude);

                    map.clear();

                    Marker marker;
                    marker = map.addMarker(new MarkerOptions().position(myLocation));

                    // Animating to the location position
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));

                    setMyLocationAdress();

                    return true;
                } else
                    return false;

            }
        };
    }

    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        // Disconnecting the client invalidates it.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Get last known recent location.
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    //Só implementar se for usar o AsyncTask GetAddressTask
    public void callBackDataFromAsyncTask(DeliveryPlace address) {

    }


    public void updateDeliveryPlace(View view) {
        startActivity(new Intent(this, FinalizeActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FinalizeActivity.class));
    }
}
