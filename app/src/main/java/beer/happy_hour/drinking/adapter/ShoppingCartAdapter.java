package beer.happy_hour.drinking.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import beer.happy_hour.drinking.InputFilterMinMax;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.listener.SubtotalTextView;
import beer.happy_hour.drinking.model.List_Item.ListItem;
import beer.happy_hour.drinking.model.List_Item.ShoppingCart;

/**
 * Created by brcon on 09/03/2017.
 */

public class ShoppingCartAdapter extends ArrayAdapter<ListItem> {

    private Context context;
    private ShoppingCart cart;

    private SubtotalTextView subtotal_text_view;

    public ShoppingCartAdapter(Context context) {
        super(context, R.layout.shopping_cart_item, ShoppingCart.getInstance().getListItems());

        this.context = context;
        this.cart = ShoppingCart.getInstance();

        cart = ShoppingCart.getInstance();
        Log.d("Carrinho", cart.toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.shopping_cart_item, parent, false);

        //Inicializando Botões
        Button addOne_button = (Button) row.findViewById(R.id.cart_addOne_button);
        Button minusOne_button = (Button) row.findViewById(R.id.cart_minusOne_button);
        ;
        Button deleteFromShoppingCart_button = (Button) row.findViewById(R.id.deleteFromShoppingCart_button);

        final EditText quantity_editText;

        //Inicializando TextViews
        TextView nome_text_view = (TextView) row.findViewById(R.id.name);
        TextView brand_text_view = (TextView) row.findViewById(R.id.brand);
        TextView price_text_view = (TextView) row.findViewById(R.id.price);

        subtotal_text_view = (SubtotalTextView) row.findViewById(R.id.subtotal);

        //Inicializando EditText
        quantity_editText = (EditText) row.findViewById(R.id.quantity_editText);

        final ListItem listItem = cart.getListItems().get(position);

        if (listItem.getQuantity() > 0)
            quantity_editText.setText(Integer.toString(listItem.getQuantity()));

        //Organizando comportamento dos botões
        addOne_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "Botão +");
                listItem.incrementQuantity();
//                cart.incrementItemQuantity(listItem);
                quantity_editText.setText(Integer.toString(listItem.getQuantity()));
            }
        });

        minusOne_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "Botão -");

                try {
                    if (Integer.parseInt(quantity_editText.getText().toString()) > 1) {
                        listItem.decrementQuantity();
//                        cart.decrementItemQuantity(listItem);
                        quantity_editText.setText(Integer.toString(listItem.getQuantity()));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("Error", "Não há inteiro definido");
                }
            }
        });

        deleteFromShoppingCart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItem.setQuantityAndUpdateCart(0);
                Log.d("Click", "Botão cart");
                notifyDataSetChanged();
            }
        });

        quantity_editText.setSelection(quantity_editText.getText().length());

        quantity_editText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "99")});

        quantity_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!quantity_editText.getText().toString().equals("")) {

                    if(Integer.parseInt(quantity_editText.getText().toString()) > 0) {
                        Log.d("After Text Changed: ", quantity_editText.getText().toString());

                        listItem.setQuantityAndUpdateCart(Integer.parseInt(quantity_editText.getText().toString()));
                        Log.d("New Quantity: ", Integer.toString(listItem.getQuantity()));
                    }
                } else {
                    listItem.setQuantityAndUpdateCart(0);
                }
            }
        });

        //Mostrando informações
        nome_text_view.setText(listItem.getItem().getProduct().getName());
        brand_text_view.setText(listItem.getItem().getProduct().getBrand());
        price_text_view.setText(Double.toString(listItem.getItem().getPrice()));

        subtotal_text_view.setText("Subtotal: " + Double.toString(listItem.getItem().getPrice() * listItem.getQuantity()));
        listItem.setListener(subtotal_text_view);

        return row;
    }

    @Override
    public int getCount(){
        return cart.getListItems().size();
    }
}