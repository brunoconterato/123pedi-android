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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCart;
import beer.happy_hour.drinking.repository.ListItemRepositorySingleton;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItemAdapter extends ArrayAdapter<ListItem> implements Filterable {

    private final Context context;

//    private List<ListItem> listItems;
//    private List<ListItem> originalListItems;  //Cópia
    //Used for search

    private Filter listItemFilter;

    private ListItemRepositorySingleton listItemRepositorySingleton;
    private ShoppingCart cart;

    private List<ListItem> filteredList;

    public ListItemAdapter(Context context) {
        super(context, R.layout.list_item, ListItemRepositorySingleton.getInstance().getList());

//        this.listItems = listItemRepositorySingleton.getList();
//        this.originalListItems = listItemRepositorySingleton.getList();

        this.context = context;

        listItemRepositorySingleton = ListItemRepositorySingleton.getInstance();
        cart = ShoppingCart.getInstance();

        filteredList = listItemRepositorySingleton.getList();

        getFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item, parent, false);

        //Inicializando Botões
        Button addOne_button = (Button) row.findViewById(R.id.cart_addOne_button);
        Button minusOne_button = (Button) row.findViewById(R.id.cart_minusOne_button);
        Button addToShoppingCart_button = (Button) row.findViewById(R.id.addToShoppingCart_button);

        final EditText quantity_editText;

        //Inicializando TextViews
        TextView nome_text_view = (TextView) row.findViewById(R.id.name);
        TextView brand_text_view = (TextView) row.findViewById(R.id.brand);
        TextView manufacturer_text_view = (TextView) row.findViewById(R.id.manufacturer);
        TextView price_text_view = (TextView) row.findViewById(R.id.price);

        //Inicializando EditText
        quantity_editText = (EditText) row.findViewById(R.id.quantity_editText);

        final ListItem listItem = filteredList.get(position);

//        int listItemIndexInLIRepository = filteredList.get(position).indexOf(listItem);
        quantity_editText.setText(Integer.toString(listItem.getQuantity()));

//        if (cart.getListItems().contains(listItem)) {
//            int listItemIndexInCart = cart.getListItems().indexOf(listItem);
//            quantity_editText.setText(Integer.printBrief(cart.getListItems().get(listItemIndexInCart).getQuantity()));
//        }
//        else
//        {
//            int listItemIndexInLIRepository = listItemRepositorySingleton.getFilteredList().indexOf(listItem);
//            quantity_editText.setText(Integer.printBrief(listItemRepositorySingleton.getFilteredList().get(listItemIndexInLIRepository).getQuantity()));
//        }

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
                    else if(Integer.parseInt(quantity_editText.getText().toString()) == 0){
                        cart.deleteFromCart(listItem);
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
                if(listItem.getQuantity() > 0) {
                    cart.addToCart(listItem);
                    Log.d("Click", "Botão cart");
                    Log.d("Adicionado ao carrinho", listItem.toString());
                    Log.d("Carrinho", cart.toString());
                }
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

                    if(Integer.parseInt(quantity_editText.getText().toString()) > 0) {
                        Log.d("After Text Changed: ", quantity_editText.getText().toString());

                        listItem.setQuantity(Integer.parseInt(quantity_editText.getText().toString()));
                        Log.d("New Quantity: ", Integer.toString(listItem.getQuantity()));
                    }
                    else{
                        listItem.setQuantity(0);
                        cart.deleteFromCart(listItem);
                    }
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
        return listItemRepositorySingleton.getList();
    }

    @Override
    public Filter getFilter() {
        Log.d("Entrou: ", "getFilter");

        if (listItemFilter == null) {
            listItemFilter = new ListItemFilter();
        }


        Log.d("Saindo ", "getFilter");
        return listItemFilter;
    }

    /**
     * Sobrescrevemos para obter a lista final filtrada quando busca é realizada
     * @param position
     * @return
     */
    @Override
    public ListItem getItem(int position){
        return listItemRepositorySingleton.getItem(position);
    }

    /**
     *
     * Sobrecrevemos para atualizar o size da lista filtrada quando busca é realizada
     *
     * @return int
     */
    @Override
    public int getCount(){
        return filteredList.size();
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
                if (constraint.toString().startsWith(Constants.SEARCH_CATEGORY_HASH)) {
                    String searchCategoryHash = Constants.SEARCH_CATEGORY_HASH;

                    String categoryConstraint = constraint.toString().substring(searchCategoryHash.length());
                    Log.d("categoryConstraint", categoryConstraint);

                    for (ListItem listItem : listItemRepositorySingleton.getList()) {
                        if (listItem.getItem().getProduct().getCategory().getName().contains(categoryConstraint.toLowerCase())) {
                            Log.d("Aviso: ", "filtro de categoria encontrado: você tem um produto dea categoria!");
                            Log.d("Nome produto: ", listItem.getItem().getProduct().getName());
                            tempList.add(listItem);
                        }
                    }
                } else {
                    // search content in friend list
                    for (ListItem listItem : listItemRepositorySingleton.getList()) {
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
                //Alguns valores foram filtrados
                if (tempList.size() > 0) {
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                //Nenhum valor foi filtrado
                else {
                    filterResults.count = listItemRepositorySingleton.getSize();
                    filterResults.values = listItemRepositorySingleton.getList();
//                    listItemRepositorySingleton.resetFilteredList();
                }
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

            if (filterResults.count == 0) {
//                listItemRepositorySingleton.resetFilteredList();
                filteredList = listItemRepositorySingleton.getList();
                notifyDataSetInvalidated();
            }
            else{
//                listItemRepositorySingleton.setFilteredList( (ArrayList<ListItem>) filterResults.values );
                filteredList = (ArrayList<ListItem>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
}