package beer.happy_hour.drinking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCart;

/**
 * Created by brcon on 19/03/2017.
 */

public class BriefItemsAdapter extends ArrayAdapter<ListItem> {

    private Context context;
    private ShoppingCart cart;

    public BriefItemsAdapter(@NonNull Context context) {
        super(context, R.layout.brief_item, ShoppingCart.getInstance().getListItems());

        this.context = context;
        this.cart = ShoppingCart.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.brief_item, parent, false);

        TextView brief_item_name_text_view = (TextView) row.findViewById(R.id.brief_item_name_text_view);
        TextView brief_item_brand_text_view = (TextView) row.findViewById(R.id.brief_item_brand_text_view);
        TextView brief_item_quantity_text_view = (TextView) row.findViewById(R.id.brief_item_quantity_text_view);
        TextView brief_item_price_text_view = (TextView) row.findViewById(R.id.brief_item_price_text_view);
        TextView brief_item_subtotal_text_view = (TextView) row.findViewById(R.id.brief_item_subtotal_text_view);

        final ListItem listItem = cart.getListItems().get(position);

        brief_item_name_text_view.setText(listItem.getItem().getProduct().getName());
        brief_item_brand_text_view.setText(listItem.getItem().getProduct().getBrand());
        brief_item_quantity_text_view.setText(Double.toString(listItem.getQuantity()));
        brief_item_price_text_view.setText(Double.toString(listItem.getItem().getPrice()));
        brief_item_subtotal_text_view.setText(Double.toString(listItem.getItem().getPrice() * listItem.getQuantity()));

        return row;
    }
}
