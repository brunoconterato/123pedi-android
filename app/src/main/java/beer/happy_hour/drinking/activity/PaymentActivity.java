package beer.happy_hour.drinking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import beer.happy_hour.drinking.R;

public class PaymentActivity extends AppCompatActivity {

    RadioGroup paymentRadioGroup;
    RadioButton creditcard_radio_button;
    RadioButton money_radio_button;

    TextView payment_test_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentRadioGroup = (RadioGroup) findViewById(R.id.payment_radio_group);
        creditcard_radio_button = (RadioButton) findViewById(R.id.credit_card_radio_button);
        money_radio_button = (RadioButton) findViewById(R.id.money_radio_button);

        payment_test_text_view = (TextView) findViewById(R.id.payment_test_text_view);

        paymentRadioGroup.clearCheck();

        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.credit_card_radio_button: {
                        payment_test_text_view.setText("Cartão escolhido");
                        setPaymentForCard();
                        break;
                    }
                    case R.id.money_radio_button: {
                        payment_test_text_view.setText("Dinheiro escolhido");
                        setPaymentForMoney();
                        break;
                    }
                }
            }
        });

//        Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//
//                    }
//                }, mYear, mMonth, mDay);
//        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
//        datePickerDialog.show();

    }

    private void setPaymentForCard() {

    }

    private void setPaymentForMoney() {

    }
}
