package beer.happy_hour.drinking.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.User;

public class UnluckilyActivity extends AppCompatActivity {

    private AppCompatCheckBox contact_request_checkbox;
    private AppCompatCheckBox email_request_checkbox;
    private TextView contact_unluckily_text_view;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unluckily);

        user = User.getInstance();

        contact_unluckily_text_view = (TextView) findViewById(R.id.contact_unluckily_text_view);
        contact_unluckily_text_view.setText(user.printBrief());

        contact_request_checkbox = (AppCompatCheckBox) findViewById(R.id.contact_request_checkbox);
        email_request_checkbox = (AppCompatCheckBox) findViewById(R.id.email_request_checkbox);

        contact_request_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                    email_request_checkbox.setEnabled(true);
            }
        });
    }

    public void sendDataAndReturnToMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BriefActivity.class));
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
            case R.id.cart_menu_item:
                startActivity(new Intent(this, CartActivity.class));
                return (true);
            case android.R.id.home:
                startActivity(new Intent(this, BriefActivity.class));
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void goToFeedBackActivity(View view) {
        startActivity(new Intent(this, FeedBackActivity.class));
    }
}
