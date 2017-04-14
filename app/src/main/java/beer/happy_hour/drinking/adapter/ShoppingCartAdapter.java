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
import android.widget.ImageView;
import android.widget.TextView;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.InputFilterMinMax;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.database_handler.ItemsDatabaseHandler;
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

    private ItemsDatabaseHandler databaseHandler;

    private String SUBTOTAL_PREFIX = "Subtotal: R$";
    private String PRICE_PREFIX = "Preço: R$";

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
        TextView price_text_view = (TextView) row.findViewById(R.id.price);

        ImageView cart_item_image_view = (ImageView) row.findViewById(R.id.cart_item_image_view);

        subtotal_text_view = (SubtotalTextView) row.findViewById(R.id.subtotal);

        //Inicializando EditText
        quantity_editText = (EditText) row.findViewById(R.id.quantity_editText);

        final ListItem listItem = cart.getListItems().get(position);

        if (listItem.getQuantity() > 0 && listItem.getQuantity() <= Constants.MAX_ITEMS_QUANTITY)
            quantity_editText.setText(Integer.toString(listItem.getQuantity()));
        else
            quantity_editText.setText(Integer.toString(Constants.MAX_ITEMS_QUANTITY));


        quantity_editText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", Integer.toString(Constants.MAX_ITEMS_QUANTITY))});

        //Organizando comportamento dos botões
        addOne_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "Botão +");

                if(listItem.getQuantity() > 0 && listItem.getQuantity() < Constants.MAX_ITEMS_QUANTITY) {
                    listItem.incrementQuantity();
                    quantity_editText.setText(Integer.toString(listItem.getQuantity()));
                }
                else if(listItem.getQuantity() >= Constants.MAX_ITEMS_QUANTITY)
                    quantity_editText.setText(Integer.toString(Constants.MAX_ITEMS_QUANTITY));

                quantity_editText.setSelection(quantity_editText.getText().length());
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

                quantity_editText.setSelection(quantity_editText.getText().length());
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

                quantity_editText.setSelection(quantity_editText.getText().length());
            }
        });

        //Mostrando informações
        nome_text_view.setText(listItem.getItem().getProduct().getName());
        price_text_view.setText(String.format("%s%.2f", PRICE_PREFIX, listItem.getItem().getPrice()));

        subtotal_text_view.setText(String.format("%s%.2f", SUBTOTAL_PREFIX, listItem.getItem().getPrice() * listItem.getQuantity()));
        listItem.setSubtotalListener(subtotal_text_view);

        databaseHandler = new ItemsDatabaseHandler(context);
        if(databaseHandler.getImage(listItem.getItem()) != null) {
            Log.d("EntrouHere", "EntrouHere");
            cart_item_image_view.setImageBitmap(databaseHandler.getImage(listItem.getItem()));
        }

        return row;
    }

    @Override
    public int getCount(){
        return cart.getListItems().size();
    }
}