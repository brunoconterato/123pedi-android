package beer.happy_hour.drinking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.Payment;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class PaymentActivity extends AppCompatActivity {

    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[]{"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out",
            "Nov", "Dec"};
    private static final String[] MONTHS = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
            "Outubro", "Novembro", "Dezembro"};
    private static final int MIN_YEAR = 2017;
    private static final int MAX_YEAR = 2099;
    private Payment payment;
    private RadioGroup paymentRadioGroup;
    private RadioButton creditcard_radio_button;
    private RadioButton money_radio_button;
    private LinearLayout payment_linear_layout;
    private EditText card_number_edit_text;
    private EditText card_name_edit_text;
    private EditText card_security_code_edit_text;
    private EditText money_change_edit_text;
    private NumberPicker monthNumberPicker;
    private NumberPicker yearNumberPicker;
    private int currentYear;
    private int currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payment = Payment.getInstance();

        paymentRadioGroup = (RadioGroup) findViewById(R.id.payment_radio_group);
        creditcard_radio_button = (RadioButton) findViewById(R.id.credit_card_radio_button);
        money_radio_button = (RadioButton) findViewById(R.id.money_radio_button);

        payment_linear_layout = (LinearLayout) findViewById(R.id.payment_linear_layout);
        card_number_edit_text = (EditText) findViewById(R.id.card_number_edit_text);
        card_name_edit_text = (EditText) findViewById(R.id.card_name_edit_text);
        card_security_code_edit_text = (EditText) findViewById(R.id.card_security_code_edit_text);

        card_number_edit_text.addTextChangedListener(new CardNumberTextListener("#### #### #### ####", card_number_edit_text));
        card_name_edit_text.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                payment.setName(String.valueOf(card_name_edit_text.getText()));
            }
        });
        card_security_code_edit_text.addTextChangedListener(new CardSecurityCodeTextListener("###", card_security_code_edit_text));


        final java.util.Calendar instance = java.util.Calendar.getInstance();
        currentMonth = instance.get(java.util.Calendar.MONTH);
        currentYear = instance.get(java.util.Calendar.YEAR);

        Log.d("Current Month: ", String.valueOf(currentMonth));
        Log.d("Current Year: ", String.valueOf(currentYear));

        monthNumberPicker = (NumberPicker) findViewById(R.id.monthNumberPicker);
        monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);

        monthNumberPicker.setMinValue(0);
        monthNumberPicker.setMaxValue(MONTHS.length - 1);

        yearNumberPicker = (NumberPicker) findViewById(R.id.yearNumberPicker);
        yearNumberPicker.setMinValue(MIN_YEAR);
        yearNumberPicker.setMaxValue(MAX_YEAR);

        if (payment.getExpirationMonthIndex() == -1) {
            monthNumberPicker.setValue(currentMonth);
            payment.setExpirationMonthIndex(currentMonth);
        } else
            monthNumberPicker.setValue(payment.getExpirationMonthIndex());

        if (payment.getExpirationYear() == -1) {
            yearNumberPicker.setValue(currentYear);
            payment.setExpirationYear(currentYear);
        } else
            yearNumberPicker.setValue(payment.getExpirationYear());

        monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        monthNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                payment.setExpirationMonthIndex(monthNumberPicker.getValue());
            }
        });
        yearNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                payment.setExpirationYear(yearNumberPicker.getValue());
            }
        });

        payment_linear_layout.setOrientation(LinearLayout.VERTICAL);
        payment_linear_layout.setVisibility(LinearLayout.GONE);

        money_change_edit_text = (EditText) findViewById(R.id.money_change_edit_text);
        money_change_edit_text.setVisibility(EditText.GONE);

        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.credit_card_radio_button: {
                        payment.setMethod(Payment.METHOD_CREDIT_CARD);
                        money_change_edit_text.setVisibility(EditText.GONE);
                        payment_linear_layout.setVisibility(LinearLayout.VISIBLE);
                        break;
                    }
                    case R.id.money_radio_button: {
                        payment.setMethod(Payment.METHOD_MONEY);
                        payment_linear_layout.setVisibility(LinearLayout.GONE);
                        money_change_edit_text.setVisibility(EditText.VISIBLE);
                        break;
                    }
                }
            }
        });

        money_change_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                payment.setMoneyChange(money_change_edit_text.getText().toString());
            }
        });


        if (payment.getMethod() == Payment.METHOD_CREDIT_CARD)
            paymentRadioGroup.check(R.id.credit_card_radio_button);
        else if (payment.getMethod() == Payment.METHOD_MONEY)
            paymentRadioGroup.check(R.id.money_radio_button);
        else
            paymentRadioGroup.clearCheck();

        card_number_edit_text.setText(payment.getNumber());
        card_name_edit_text.setText(payment.getName());

        if (!payment.getSecurityCode().equals(""))
            card_security_code_edit_text.setText(payment.getSecurityCode());
        if (!payment.getMoneyChange().equals(""))
            money_change_edit_text.setText(payment.getMoneyChange());
    }

    public void goToBriefActivity(View view) {
        startActivity(new Intent(this, BriefActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OrderDetailsActivity.class));
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
            case R.id.cart_menu_item:
                startActivity(new Intent(this, CartActivity.class));
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

    public void testPayment(View view) {
        Log.d("Payment", payment.printBrief());
    }

    private class CardNumberTextListener extends MaskEditTextChangedListener implements TextWatcher {


        public CardNumberTextListener(String mask, EditText editText) {
            super(mask, editText);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            payment.setNumber(card_number_edit_text.getText().toString());
        }
    }

    private class CardSecurityCodeTextListener extends MaskEditTextChangedListener implements TextWatcher {

        public CardSecurityCodeTextListener(String mask, EditText editText) {
            super(mask, editText);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            payment.setSecurityCode(card_security_code_edit_text.getText().toString());
        }
    }
}
