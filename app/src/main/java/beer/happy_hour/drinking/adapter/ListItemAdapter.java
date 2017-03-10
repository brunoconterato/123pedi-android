package beer.happy_hour.drinking.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.ListItem;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItemAdapter extends ArrayAdapter<Item> {

    private final Context context;

    private final List<Item> items;

    public ListItemAdapter(Context context, List<Item> items){

        super(context, R.layout.list_item, items);

        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item, parent, false);

        Item item = items.get(position);
        final ListItem listItem = new ListItem(row, item);

        final EditText quantity_editText = (EditText) row.findViewById(R.id.quantity_editText);

        Button addOne_button = (Button) row.findViewById(R.id.addOne_button);
        Button minusOne_button = (Button) row.findViewById(R.id.minusOne_button);
        Button addToShoppingCart_button = (Button) row.findViewById(R.id.addToShoppingCart_button);

        //Organizando comportamento dos botões
        addOne_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click","Botão +");
                listItem.incrementQuantity();
                quantity_editText.setText(Integer.toString(listItem.getQuantity()));
            }
        });

        minusOne_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click","Botão -");
                if(Integer.parseInt(quantity_editText.getText().toString()) > 0) {
                    listItem.decrementQuantity();
                    quantity_editText.setText(Integer.toString(listItem.getQuantity()));
                }
            }
        });

        addToShoppingCart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click","Botão cart");
            }
        });

        quantity_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(quantity_editText.getText() != null) {
                    listItem.setQuantity(Integer.parseInt(quantity_editText.getText().toString()));
                    Log.d("New Quantity: ", Integer.toString(listItem.getQuantity()));
                }
            }
        });


        //Declarando as TextViews
        TextView nome_text_view = (TextView) row.findViewById(R.id.name);
        TextView brand_text_view = (TextView) row.findViewById(R.id.brand);
        TextView manufacturer_text_view = (TextView) row.findViewById(R.id.manufacturer);
        TextView price_text_view = (TextView) row.findViewById(R.id.price);

        //Mostrando informações
        nome_text_view.setText(item.getProduct().getName());
        brand_text_view.setText(item.getProduct().getBrand());
        manufacturer_text_view.setText(item.getProduct().getManufacturer());
        price_text_view.setText(Double.toString(item.getPrice()));

        return row;
    }

    /*
     * USO: salvar o estado da ListItem
     *
     */
    public List<Item> getItems() {
        return items;
    }

}
