package beer.happy_hour.drinking.listener;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import beer.happy_hour.drinking.model.shopping_cart.ListItem;

/**
 * Created by brcon on 15/03/2017.
 */

public class SubtotalTextView extends android.support.v7.widget.AppCompatTextView implements ListItem.SubtotalChangeListener {

    private String BASE_SUBTOTAL_TEXT = "Subtotal: ";

    public SubtotalTextView(Context context) {
        super(context);
    }

    public SubtotalTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SubtotalTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onValueChanged(double newValue) {
        this.setText(BASE_SUBTOTAL_TEXT + Double.toString(newValue));
    }
}
