package beer.happy_hour.drinking.listener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import beer.happy_hour.drinking.model.List_Item.ListItem;

/**
 * Created by brcon on 14/04/2017.
 */

public class QuantityEditText extends android.support.v7.widget.AppCompatEditText implements ListItem.QuantityChangeListener {

    public QuantityEditText(Context context) {
        super(context);
    }

    public QuantityEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuantityEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onValueChanged(int newValue) {
        this.setText(String.valueOf(newValue));
    }
}
