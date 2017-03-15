package beer.happy_hour.drinking.listener;

import android.content.Context;
import android.util.AttributeSet;

import beer.happy_hour.drinking.model.ShoppingCartSingleton;

/**
 * Created by brcon on 14/03/2017.
 */

public class TotalTextView extends android.support.v7.widget.AppCompatTextView implements ShoppingCartSingleton.TotalChangeListener {

    private String BASE_PRICE_TEXT = "TOTAL: R$ ";

    public TotalTextView(Context context) {
        super(context);
    }

    public TotalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TotalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Called when the value of the int changes.
     *
     * @param newTotal The new value.
     */
    @Override
    public void onValueChanged(double newTotal) {
        this.setText(BASE_PRICE_TEXT + Double.toString(newTotal));
    }
}
