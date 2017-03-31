package beer.happy_hour.drinking.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.DeliveryPlace;
import beer.happy_hour.drinking.model.User;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class OrderDetailsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    private DeliveryPlace deliveryPlace;
    private User user;

    private EditText name_edit_text;
    private EditText phone_edit_text;
    private EditText email_edit_text;

    private TextView address_brief_text_view;

    private EditText complement_edit_text;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2 * 1000; /* 2 sec */

    private Boolean isLocationSetted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        deliveryPlace = DeliveryPlace.getInstance();
        user = User.getInstance();

        name_edit_text = (EditText) findViewById(R.id.name_edit_text);
        phone_edit_text = (EditText) findViewById(R.id.phone_edit_text);
        email_edit_text = (EditText) findViewById(R.id.email_edit_text);

        name_edit_text.setText(user.getName());
        name_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                user.setName(name_edit_text.getText().toString());
                Log.d("Nome", user.getName());
            }
        });

        phone_edit_text.setText(user.getPhone());
        phone_edit_text.addTextChangedListener(new UserPhoneTextListener("(##) # ####-####", phone_edit_text));

        email_edit_text.setText(user.getEmail());
        email_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                user.setEmail(email_edit_text.getText().toString());
                Log.d("Email", user.getEmail());
            }
        });

//        MaskEditTextChangedListener maskPhone = new MaskEditTextChangedListener("(##) # ####-####", phone_edit_text);
//        phone_edit_text.addTextChangedListener(maskPhone);

        address_brief_text_view = (TextView) findViewById(R.id.adress_brief_text_view);
        address_brief_text_view.setText(deliveryPlace.printOrderDetails());

        complement_edit_text = (EditText) findViewById(R.id.complement_edit_text);
        complement_edit_text.setText(deliveryPlace.getComplement());
        complement_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                deliveryPlace.setComplement(complement_edit_text.getText().toString());
                Log.d("Complement : ", "" + deliveryPlace.getComplement());
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                return (true);
            case R.id.cart_menu_item:
                startActivity(new Intent(this, CartActivity.class));
                return (true);
            case android.R.id.home:
                startActivity(new Intent(this, CartActivity.class));
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }

    public void goToMapActivity(View view) {
        startActivity(new Intent(this, MapsFragmentActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, CartActivity.class));
    }

    public void logValidatePhone(View view) {
        Log.d("isValidPhone", Boolean.toString(user.isValidPhone(phone_edit_text.getText().toString())));
    }

    public void logValidateEmail(View view) {
        Log.d("isValidEmail", Boolean.toString(user.isValidEmail(email_edit_text.getText().toString())));
    }

    public void goToPaymentActivity(View view) {
        startActivity(new Intent(this, PaymentActivity.class));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(OrderDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderDetailsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            ActivityCompat.requestPermissions(OrderDetailsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null && !deliveryPlace.wasObteinedInMap()) {
            // Print current location if not null
            new LocationSetter().execute(mCurrentLocation);
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setAddress_brief_text_view() {
        Log.d("Height", Integer.toString(address_brief_text_view.getHeight()));
        Log.d("Order Details", deliveryPlace.printOrderDetails());
        Log.d("Line count", Integer.toString(address_brief_text_view.getLineCount()));

        if (!deliveryPlace.wasObteinedInMap()) {
            address_brief_text_view.setText(deliveryPlace.printOrderDetails());

            ViewGroup.LayoutParams params = address_brief_text_view.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            address_brief_text_view.setLayoutParams(params);
        }
    }

    private class UserPhoneTextListener extends MaskEditTextChangedListener implements TextWatcher {

        User user;

        public UserPhoneTextListener(String mask, EditText editText) {
            super(mask, editText);

            user = User.getInstance();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            user.setPhone(phone_edit_text.getText().toString());
            Log.d("Phone", user.getPhone());
        }
    }

    private class LocationSetter extends AsyncTask<Location, Void, Void> {

        @Override
        protected Void doInBackground(Location... locations) {
            try {
                Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(locations[0].getLatitude(), locations[0].getLongitude(), 1);

                if (addresses.isEmpty()) {
                    Log.d("Location", "Waiting for Location");
                } else {
                    if (addresses.size() > 0) {
                        Log.d("Location", addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());

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
                        Log.d("getSubLocality", addresses.get(0).getSubLocality());
                        Log.d("getSubThoroughfare", addresses.get(0).getSubThoroughfare());
                        Log.d("getThoroughfare", addresses.get(0).getThoroughfare());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // getFromLocation() may sometimes fail
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void adress) {
            setAddress_brief_text_view();
        }
    }
}
