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

import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;

/**
 * Created by brcon on 09/03/2017.
 */

public class ShoppingCartAdapter extends ArrayAdapter<ListItem> {

    private Context context;
    private ShoppingCartSingleton cart;

    public ShoppingCartAdapter(Context context, ShoppingCartSingleton cart){
        super(context, R.layout.shopping_cart_item, cart.getListItems());

        this.context = context;
        this.cart = ShoppingCartSingleton.getInstance();

        cart = ShoppingCartSingleton.getInstance();
        Log.d("Carrinho", cart.toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.shopping_cart_item, parent, false);

        //Inicializando Botões
        Button addOne_button = (Button) row.findViewById(R.id.addOne_button);
        Button minusOne_button = (Button) row.findViewById(R.id.minusOne_button);;
        Button deleteFromShoppingCart_button = (Button) row.findViewById(R.id.deleteFromShoppingCart_button);

        final EditText quantity_editText;

        //Inicializando TextViews
        TextView nome_text_view = (TextView) row.findViewById(R.id.name);
        TextView brand_text_view = (TextView) row.findViewById(R.id.brand);
        TextView price_text_view = (TextView) row.findViewById(R.id.price);
        TextView subtotal_text_view = (TextView) row.findViewById(R.id.subtotal);

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
                cart.deleteFromCart(listItem);
                Log.d("Click", "Botão cart");
                notifyDataSetChanged();
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
                if (quantity_editText.getText() != null) {

                    Log.d("After Text Changed: ", quantity_editText.getText().toString());

                    listItem.setQuantity(Integer.parseInt(quantity_editText.getText().toString()));
                    Log.d("New Quantity: ", Integer.toString(listItem.getQuantity()));
                }
            }
        });

        //Mostrando informações
        nome_text_view.setText(listItem.getItem().getProduct().getName());
        brand_text_view.setText(listItem.getItem().getProduct().getBrand());
        price_text_view.setText(Double.toString(listItem.getItem().getPrice()));
        subtotal_text_view.setText("Subtotal: " + Double.toString(listItem.getItem().getPrice() * listItem.getQuantity()));

        return row;
    }

    @Override
    public int getCount(){
        return cart.getListItems().size();
    }
}