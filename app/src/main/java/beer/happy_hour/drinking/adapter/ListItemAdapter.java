package beer.happy_hour.drinking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import beer.happy_hour.drinking.model.List_Item.ListItem;
import beer.happy_hour.drinking.repository.ListItemRepository;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItemAdapter extends ArrayAdapter<ListItem> implements Filterable {

    private final Context context;

    private Filter listItemFilter;

    private ListItemRepository listItemRepository;

    private List<ListItem> filteredList;

    public ListItemAdapter(Context context) {
        super(context, R.layout.list_item, ListItemRepository.getInstance().getList());

        this.context = context;

        listItemRepository = ListItemRepository.getInstance();

        filteredList = listItemRepository.getList();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item, parent, false);

        //Inicializando Botões
        Button addOne_button = (Button) row.findViewById(R.id.cart_addOne_button);
        Button minusOne_button = (Button) row.findViewById(R.id.cart_minusOne_button);

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
//        if (!quantity_editText.getText().toString().equals(""))
        quantity_editText.setText(Integer.toString(listItem.getQuantity()));

//        if (cart.getListItems().contains(listItem)) {
//            int listItemIndexInCart = cart.getListItems().indexOf(listItem);
//            quantity_editText.setText(Integer.printBrief(cart.getListItems().get(listItemIndexInCart).getQuantity()));
//        }
//        else
//        {
//            int listItemIndexInLIRepository = listItemRepository.getFilteredList().indexOf(listItem);
//            quantity_editText.setText(Integer.printBrief(listItemRepository.getFilteredList().get(listItemIndexInLIRepository).getQuantity()));
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
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("Error", "Não há inteiro definido");
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
        manufacturer_text_view.setText(listItem.getItem().getProduct().getManufacturer());
        price_text_view.setText(Double.toString(listItem.getItem().getPrice()));


        return row;
    }

    public void reload(){
        filteredList = listItemRepository.getList();
    }

    /*
     * USO: salvar o estado da ListItem
     *
     */
    public List<ListItem> getListItems() {
        return listItemRepository.getList();
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
        return listItemRepository.getItem(position);
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

                    for (ListItem listItem : listItemRepository.getList()) {
                        if (listItem.getItem().getProduct().getCategory().getName().contains(categoryConstraint.toLowerCase())) {
                            Log.d("Aviso: ", "filtro de categoria encontrado: você tem um produto dea categoria!");
                            Log.d("Nome produto: ", listItem.getItem().getProduct().getName());
                            tempList.add(listItem);
                        }
                    }
                } else {
                    // search content in friend list
                    for (ListItem listItem : listItemRepository.getList()) {
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
//                    Neste caso todos os valores serão adicionados à busca:
                    filterResults.count = listItemRepository.getSize();
                    filterResults.values = listItemRepository.getList();

//                    Neste caso nenhum valor será adicionado à busca:
//                    tempList = new ArrayList<ListItem>();
//                    filterResults.count = 0;
//                    filterResults.values = tempList;
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
//                Neste caso todos os valores serão adicionados à busca:
                filteredList = listItemRepository.getList();
                notifyDataSetInvalidated();

//                Neste caso enhum valor será adicionado à busca:
//                filteredList = new ArrayList<ListItem>();
//                notifyDataSetInvalidated();
            }
            else{
//                listItemRepository.setFilteredList( (ArrayList<ListItem>) filterResults.values );
                filteredList = (ArrayList<ListItem>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
}