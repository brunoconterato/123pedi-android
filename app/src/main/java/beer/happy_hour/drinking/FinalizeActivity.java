package beer.happy_hour.drinking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import beer.happy_hour.drinking.model.DeliveryPlace;

public class FinalizeActivity extends AppCompatActivity {

    //Location Variables (Not Google Map API)
    Button get_location_button;
    // GPSTracker class
    GPSTracker gps;

    DeliveryPlace deliveryPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize);

        //Not from google mapFragment api
        get_location_button = (Button) findViewById(R.id.get_location_button);

        // show location button click event
        get_location_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(FinalizeActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });

        TextView adress_text_view = (TextView) findViewById(R.id.adress_text_view);
        TextView citystate_text_view = (TextView) findViewById(R.id.citystate_text_view);
        TextView country_text_view = (TextView) findViewById(R.id.country_text_view);
        TextView zipcode_text_view = (TextView) findViewById(R.id.zipcode_text_view);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            deliveryPlace = bundle.getParcelable("hasLocation");
            adress_text_view.setText(deliveryPlace.getAdress());
            citystate_text_view.setText(deliveryPlace.getCityState());
            country_text_view.setText(deliveryPlace.getCountry());
            zipcode_text_view.setText(deliveryPlace.getZipCode());
        } else
            deliveryPlace = new DeliveryPlace();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                return (true);
            case R.id.reset:
                return (true);
            case R.id.about:
                Toast.makeText(this, "About Toast!", Toast.LENGTH_LONG).show();
                return (true);
            case R.id.exit:
                finish();
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }

    public void goToMapActivity(View view) {
        startActivity(new Intent(this, MapsFragmentActivity.class));
        finish();
    }
}
