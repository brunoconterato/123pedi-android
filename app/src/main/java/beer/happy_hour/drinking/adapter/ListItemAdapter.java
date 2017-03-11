package beer.happy_hour.drinking.adapter;

import android.content.Context;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItemAdapter extends ArrayAdapter<ListItem> implements Filterable {

    private final Context context;

    private List<ListItem> listItems;
    private List<ListItem> originalListItems;  //Cópia
    //Used for search
    private List<ListItem> filtered_listItems;

    private Filter listItemFilter;

    private ShoppingCartSingleton cart;

    public ListItemAdapter(Context context, List<ListItem> listItems){
        super(context, R.layout.list_item, listItems);

        this.listItems = new ArrayList<ListItem>(listItems);
        this.filtered_listItems = new ArrayList<ListItem>(listItems);
        this.originalListItems = new ArrayList<ListItem>(listItems);

        this.context = context;

        cart = ShoppingCartSingleton.getInstance();

        getFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item, parent, false);

        //Inicializando Botões
        Button addOne_button = (Button) row.findViewById(R.id.addOne_button);
        Button minusOne_button = (Button) row.findViewById(R.id.minusOne_button);;
        Button addToShoppingCart_button = (Button) row.findViewById(R.id.addToShoppingCart_button);

        final EditText quantity_editText;

        //Inicializando TextViews
        TextView nome_text_view = (TextView) row.findViewById(R.id.name);
        TextView brand_text_view = (TextView) row.findViewById(R.id.brand);;
        TextView manufacturer_text_view = (TextView) row.findViewById(R.id.manufacturer);;
        TextView price_text_view = (TextView) row.findViewById(R.id.price);;

        //Inicializando EditText
        quantity_editText = (EditText) row.findViewById(R.id.quantity_editText);

        final ListItem listItem = listItems.get(position);

        if (cart.getListItems().contains(listItem)) {
            int listItemIndexInCart = cart.getListItems().indexOf(listItem);
            quantity_editText.setText(Integer.toString(cart.getListItems().get(listItemIndexInCart).getQuantity()));
        }

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
                    if (Integer.parseInt(quantity_editText.getText().toString()) > 0) {
                        listItem.decrementQuantity();
                        quantity_editText.setText(Integer.toString(listItem.getQuantity()));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("Error", "Não há inteiro definido");
                }
            }
        });

        addToShoppingCart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addToCart(listItem);
                Log.d("Click", "Botão cart");
                Log.d("Adicionado ao carrinho", listItem.toString());
                Log.d("Carrinho", cart.toString());
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
        manufacturer_text_view.setText(listItem.getItem().getProduct().getManufacturer());
        price_text_view.setText(Double.toString(listItem.getItem().getPrice()));


        return row;
    }

    /*
     * USO: salvar o estado da ListItem
     *
     */
    public List<ListItem> getListItems() {
        return listItems;
    }

    @Override
    public Filter getFilter() {
        Log.d("Entrou: ", "getFilter");

        if (listItemFilter == null) {
            listItemFilter = new ListItemFilter();
        }

        return listItemFilter;
    }

    /**
     * Sobrescrevemos para obter a lista final filtrada quando busca é realizada
     * @param pos
     * @return
     */
    @Override
    public ListItem getItem(int pos){
        return listItems.get(pos);
    }

    /**
     *
     * Sobrecrevemos para atualizar o size da lista filtrada quando busca é realizada
     *
     * @return int
     */
    @Override
    public int getCount(){
        return listItems.size();
    }

    private class ListItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("Entrou: ", "performFiltering");

            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<ListItem> tempList = new ArrayList<ListItem>();



                //Verifica se a busca foi por categoria
                //Uso interno da app pelos botões de categoria
                if(constraint.toString().startsWith(Constants.SEARCH_CATEGORY_HASH)){
                    String searchCategoryHash = Constants.SEARCH_CATEGORY_HASH;

                    String categoryConstraint = constraint.toString().substring(searchCategoryHash.length());
                    Log.d("categoryConstraint",categoryConstraint);

                    for (ListItem listItem : listItems) {

                        if(listItem.getItem().getProduct().getCategory().getName().contains(categoryConstraint.toLowerCase())){
                            Log.d("Aviso: ", "filtro de categoria encontrado: você tem um produto dea categoria!");
                            Log.d("Nome produto: ", listItem.getItem().getProduct().getName());
                            tempList.add(listItem);
                        }
                    }
                }
                else {
                    //zera a lista
                    listItems = originalListItems;

                    // search content in friend list
                    for (ListItem listItem : listItems) {
                        if (listItem.getItem().getProduct().getName().toLowerCase().contains(constraint.toString().toLowerCase())
                                || listItem.getItem().getProduct().getBrand().toLowerCase().contains(constraint.toString().toLowerCase())
                                || listItem.getItem().getProduct().getManufacturer().toLowerCase().contains(constraint.toString().toLowerCase())
                                ) {
                            Log.d("Aviso: ", "filtro encontrado: você tem um produto!");
                            Log.d("Nome produto: ", listItem.getItem().getProduct().getName());
                            tempList.add(listItem);
                        }
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;

            } else {
                filterResults.count = originalListItems.size();
                filterResults.values = originalListItems;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param filterResults filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            Log.d("Entrou: ", "publishResults");

            if (filterResults.count == 0)
                notifyDataSetInvalidated();
            else{
                listItems = (ArrayList<ListItem>) filterResults.values;
            }

            Log.d("Filtered: ", filtered_listItems.toString());
                notifyDataSetChanged();
        }
    }
}
