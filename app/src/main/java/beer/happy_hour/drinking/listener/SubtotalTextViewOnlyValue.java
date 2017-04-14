package beer.happy_hour.drinking.listener;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import beer.happy_hour.drinking.model.List_Item.ListItem;

/**
 * Created by brcon on 15/03/2017.
 */

public class SubtotalTextViewOnlyValue extends SubtotalTextView {

    public SubtotalTextViewOnlyValue(Context context) {
        super(context);
    }

    public SubtotalTextViewOnlyValue(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SubtotalTextViewOnlyValue(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onValueChanged(double newValue) {
        this.setText(String.format("R$ %.2f", newValue));
    }
}
