package beer.happy_hour.drinking;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import beer.happy_hour.drinking.model.DeliveryPlace;

/**
 * Created by brcon on 13/03/2017.
 */

public class GetAddressTask extends AsyncTask<String, Void, DeliveryPlace> {

    private MapsFragmentActivity activity;

    public GetAddressTask(MapsFragmentActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    protected DeliveryPlace doInBackground(String... params) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        DeliveryPlace deliveryPlace = new DeliveryPlace();

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(params[0]), Double.parseDouble(params[1]), 1);

            //get current Street name
            deliveryPlace.setAdress(addresses.get(0).getAddressLine(0));

            //get current city/state
            deliveryPlace.setCityState(addresses.get(0).getAddressLine(1));

            //get country
            deliveryPlace.setCountry(addresses.get(0).getCountryName());

            //get postal code
            deliveryPlace.setZipCode(addresses.get(0).getPostalCode());

            //get place Name
            deliveryPlace.setKnownName(addresses.get(0).getFeatureName());

            Log.d("Local de entrega", deliveryPlace.toString());

            return deliveryPlace;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * When the task finishes, onPostExecute() call back data to Activity UI and displays the address.
     *
     * @param address
     */
    @Override
    protected void onPostExecute(DeliveryPlace address) {
        // Call back Data and Display the current address in the UI
        activity.callBackDataFromAsyncTask(address);
    }
}