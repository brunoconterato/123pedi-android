package beer.happy_hour.drinking.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import beer.happy_hour.drinking.GeocodeJSONParser;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.DeliveryPlace;

public class MapsFragmentActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        SearchView.OnQueryTextListener {

    private GoogleMap map;
    private LatLng myLocation;
    private DeliveryPlace deliveryPlace;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

//    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
//    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    //    private Button mBtnFind;
//    private EditText etPlace;
    private SearchView searchView;

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

        //SearchView for Search
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) findViewById(R.id.map_search_view);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
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
//                map.clear();

                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

//                // Placing a marker on the touched position
//                map.addMarker(markerOptions);

                //Capturando localização
                setMyLocation(latLng);

                //Setando Endereço
                setMyLocationAdress();
            }
        });

        map.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener());

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                myLocation = map.getCameraPosition().target;

                setMyLocationAdress();
            }
        });
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

//                    Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();

                    Log.d("getAddressLine(0)", addresses.get(0).getAddressLine(0));
                    Log.d("getAddressLine(1)", addresses.get(0).getAddressLine(1));
                    Log.d("getAddressLine(2)", addresses.get(0).getAddressLine(2));
                    Log.d("getAdminArea", addresses.get(0).getAdminArea());
                    Log.d("getCountryCode", addresses.get(0).getCountryCode());
                    Log.d("getCountryName", addresses.get(0).getCountryName());
                    Log.d("getFeatureName", addresses.get(0).getFeatureName());
                    Log.d("getLocality", addresses.get(0).getLocality());
                    Log.d("getPostalCode", addresses.get(0).getPostalCode());
//                    Log.d("getSubAdminArea", addresses.get(0).getSubAdminArea());
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

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                new LocationGetter().execute();

                return true;
            }
        };
    }

    public class LocationGetter extends AsyncTask<Void, Void, Location> {

        @Override
        protected Location doInBackground(Void... voids) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            return location;
        }

        @Override
        protected void onPostExecute(Location location) {
            if (location != null) {
                // Getting latitude of the current location
                double latitude = location.getLatitude();

                // Getting longitude of the current location
                double longitude = location.getLongitude();

                myLocation = new LatLng(latitude, longitude);

//                map.clear();

                Marker marker;
//                marker = map.addMarker(new MarkerOptions().position(myLocation));

                // Animating to the location position
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));

                setMyLocationAdress();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
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
//        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

//    // Trigger new location updates at interval
//    protected void startLocationUpdates() {
//        // Create the location request
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
//        // Request location updates
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public void returnToDeliveryPlace(View view) {
        startActivity(new Intent(this, FinalizeActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FinalizeActivity.class));
    }

    //Implementation for SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String location) {

        if (location == null || location.equals("")) {
            Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
            return false;
        }

        String url = "https://maps.googleapis.com/maps/api/geocode/json?";

        try {
            // encoding special characters like space in the user input place
            location = URLEncoder.encode(location, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String address = "address=" + location;

        String sensor = "sensor=false";

        // url , from where the geocoding data is fetched
        url = url + address + "&" + sensor;

        // Instantiating DownloadTask to get places from Google Geocoding service
        // in a non-ui thread
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading the geocoding places
        downloadTask.execute(url);

        return true;
    }

    //Implementation for SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    //MapSearch Method
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception in url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * For map search
     * <p>
     * A class, to download Places from Geocoding webservice
     */
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {

            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /**
     * For map search
     * <p>
     * A class to parse the Geocoding Places in non-ui thread
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> placesList) {

            // Clears all the existing markers
//            map.clear();

            for (int i = 0; i < placesList.size(); i++) {

//                // Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places placesList
                HashMap<String, String> hmPlace = placesList.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("formatted_address");

                LatLng latLng = new LatLng(lat, lng);

//                // Setting the position for the marker
//                markerOptions.position(latLng);
//
//                // Setting the title for the marker
//                markerOptions.title(name);
//
//                // Placing a marker on the touched position
//                map.addMarker(markerOptions);

                // Locate the first location
                if (i == 0)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    }
}
