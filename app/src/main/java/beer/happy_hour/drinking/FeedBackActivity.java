package beer.happy_hour.drinking;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import beer.happy_hour.drinking.activity.CartActivity;
import beer.happy_hour.drinking.activity.MainActivity;
import beer.happy_hour.drinking.activity.UnluckilyActivity;
import beer.happy_hour.drinking.model.User;
import beer.happy_hour.drinking.model.UserFeedBack;
import beer.happy_hour.drinking.repository.ListItemRepository;

public class FeedBackActivity extends AppCompatActivity {

    private User user;
    private UserFeedBack userFeedBack;

    private RadioGroup feed_back_radio_group;
    private RadioButton autorize_contact_radio_button;
    private RadioButton deny_contact_radio_button;

    private TextView contact_feed_back_text_view;
    private TextView contact_info_feed_back_text_view;

    private EditText feed_back_edit_text;

    private Button send_and_return_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        user = User.getInstance();
        userFeedBack = UserFeedBack.getInstance();

        feed_back_radio_group = (RadioGroup) findViewById(R.id.feed_back_radio_group);
        autorize_contact_radio_button = (RadioButton) findViewById(R.id.autorize_contact_radio_button);
        deny_contact_radio_button = (RadioButton) findViewById(R.id.deny_contact_radio_button);

        contact_feed_back_text_view = (TextView) findViewById(R.id.contact_feed_back_text_view);
        contact_info_feed_back_text_view = (TextView) findViewById(R.id.contact_info_feed_back_text_view);

        feed_back_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int radioButtonId) {
                switch (radioButtonId){
                    case R.id.autorize_contact_radio_button:
                        contact_feed_back_text_view.setVisibility(TextView.VISIBLE);
                        contact_info_feed_back_text_view.setVisibility(TextView.VISIBLE);
                        userFeedBack.setAuthorize_contact_information(true);
                        break;
                    case R.id.deny_contact_radio_button:
                        contact_feed_back_text_view.setVisibility(TextView.GONE);
                        contact_info_feed_back_text_view.setVisibility(TextView.GONE);
                        userFeedBack.setAuthorize_contact_information(false);
                        break;
                }
            }
        });

        if(userFeedBack.isAuthorize_contact_information())
            autorize_contact_radio_button.setChecked(true);
        else
            deny_contact_radio_button.setChecked(true);

        contact_info_feed_back_text_view.setText(user.printBrief());

        feed_back_edit_text = (EditText) findViewById(R.id.feed_back_edit_text);
        feed_back_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userFeedBack.setFeedBack(feed_back_edit_text.getText().toString());
            }
        });
        feed_back_edit_text.setText(userFeedBack.getFeedBack());
        feed_back_edit_text.setSelection(feed_back_edit_text.getText().length());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, UnluckilyActivity.class));
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
                startActivity(new Intent(this, UnluckilyActivity.class));
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void sendDataAndReturnToMainActivity(View view) {
        resetAppData();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void resetAppData(){
        if(deny_contact_radio_button.isChecked())
            User.reset();

        ListItemRepository.resetRepositoryAndCart();
    }
}
