package beer.happy_hour.drinking.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.User;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class PaymentActivity extends AppCompatActivity {

    private static final String[] PICKER_DISPLAY_MONTHS_NAMES = new String[]{"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out",
            "Nov", "Dec"};
    private static final String[] MONTHS = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    private static final int MIN_YEAR = 2017;
    private static final int MAX_YEAR = 2099;
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
    private TextView payment_test_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentRadioGroup = (RadioGroup) findViewById(R.id.payment_radio_group);
        creditcard_radio_button = (RadioButton) findViewById(R.id.credit_card_radio_button);
        money_radio_button = (RadioButton) findViewById(R.id.money_radio_button);

        payment_test_text_view = (TextView) findViewById(R.id.payment_test_text_view);

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
                //TODO
            }
        });
        card_security_code_edit_text.addTextChangedListener(new CardSecurityCodeTextListener("###", card_security_code_edit_text));


        final java.util.Calendar instance = java.util.Calendar.getInstance();
        currentMonth = instance.get(java.util.Calendar.MONTH);
        currentYear = instance.get(java.util.Calendar.YEAR);

        monthNumberPicker = (NumberPicker) findViewById(R.id.monthNumberPicker);
        monthNumberPicker.setDisplayedValues(PICKER_DISPLAY_MONTHS_NAMES);

        monthNumberPicker.setMinValue(0);
        monthNumberPicker.setMaxValue(MONTHS.length - 1);

        yearNumberPicker = (NumberPicker) findViewById(R.id.yearNumberPicker);
        yearNumberPicker.setMinValue(MIN_YEAR);
        yearNumberPicker.setMaxValue(MAX_YEAR);

        monthNumberPicker.setValue(currentMonth);
        yearNumberPicker.setValue(currentYear);

        monthNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        yearNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        monthNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //TODO
            }
        });
        yearNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //TODO
            }
        });

        payment_linear_layout.setOrientation(LinearLayout.VERTICAL);
        payment_linear_layout.setVisibility(LinearLayout.GONE);

        money_change_edit_text = (EditText) findViewById(R.id.money_change_edit_text);
        money_change_edit_text.setVisibility(EditText.GONE);

        paymentRadioGroup.clearCheck();

        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.credit_card_radio_button: {
                        payment_test_text_view.setText("Cartão escolhido");

                        money_change_edit_text.setVisibility(EditText.GONE);
                        payment_linear_layout.setVisibility(LinearLayout.VISIBLE);

                        break;
                    }
                    case R.id.money_radio_button: {
                        payment_test_text_view.setText("Dinheiro escolhido");

                        payment_linear_layout.setVisibility(LinearLayout.GONE);
                        money_change_edit_text.setVisibility(EditText.VISIBLE);

                        break;
                    }
                }
            }
        });
    }

    private class CardNumberTextListener extends MaskEditTextChangedListener implements TextWatcher {

        User user;

        public CardNumberTextListener(String mask, EditText editText) {
            super(mask, editText);

            user = User.getInstance();

            //TODO
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //TODO
        }
    }

    private class CardSecurityCodeTextListener extends MaskEditTextChangedListener implements TextWatcher {

        User user;

        public CardSecurityCodeTextListener(String mask, EditText editText) {
            super(mask, editText);

            user = User.getInstance();

            //TODO
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //TODO
        }
    }
}
