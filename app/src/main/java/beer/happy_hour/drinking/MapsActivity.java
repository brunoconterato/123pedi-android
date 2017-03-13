package beer.happy_hour.drinking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity {

    MapsFragmentActivity mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = new MapsFragmentActivity();
    }


}
