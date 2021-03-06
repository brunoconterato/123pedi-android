package beer.happy_hour.drinking.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.Constants;
import beer.happy_hour.drinking.activity.SearchTabsActivity;
import beer.happy_hour.drinking.database_handler.ItemsDatabaseHandler;
import beer.happy_hour.drinking.InputFilterMinMax;
import beer.happy_hour.drinking.R;
import beer.happy_hour.drinking.listener.SubtotalTextViewOnlyValue;
import beer.happy_hour.drinking.model.List_Item.ListItem;
import beer.happy_hour.drinking.repository.ListItemRepository;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItemAdapter extends ArrayAdapter<ListItem> implements Filterable {

    public interface OnPopupShowListener {
        void OnPopupShow();
    }

    private final Context context;
    private Filter listItemFilter;
    private ListItemRepository listItemRepository;
    private List<ListItem> filteredList;
    private ItemsDatabaseHandler databaseHandler;

    private OnPopupShowListener listener;

    private final String PRICE_PREFIX = "R$ ";
    private String SUBTOTAL_PREFIX = "Subtotal: R$";

    public ListItemAdapter(Context context) {
        super(context, R.layout.list_item, ListItemRepository.getInstance().getList());

        this.context = context;

        listItemRepository = ListItemRepository.getInstance();

        filteredList = listItemRepository.getList();
    }

    public void setPopupShowListener(OnPopupShowListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.list_item, parent, false);

        //Inicializando Botões
        Button addOne_button = (Button) row.findViewById(R.id.cart_addOne_button);
        Button minusOne_button = (Button) row.findViewById(R.id.cart_minusOne_button);

        final EditText quantity_editText;

        //Inicializando TextViews
        TextView nome_text_view = (TextView) row.findViewById(R.id.name);
        TextView price_text_view = (TextView) row.findViewById(R.id.price);

        ImageView brief_item_image_view = (ImageView) row.findViewById(R.id.brief_item_image_view);

        //Inicializando EditText
        quantity_editText = (EditText) row.findViewById(R.id.quantity_editText);

        final ListItem listItem = filteredList.get(position);

        if(listItem.getQuantity() > 0 && listItem.getQuantity() <= Constants.MAX_ITEMS_QUANTITY)
            quantity_editText.setText(Integer.toString(listItem.getQuantity()));
        else if(listItem.getQuantity() > Constants.MAX_ITEMS_QUANTITY)
            quantity_editText.setText(Integer.toString(Constants.MAX_ITEMS_QUANTITY));
        else
            quantity_editText.setText("");

        //Organizando comportamento dos botões
        addOne_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "Botão +");

                if(listItem.getQuantity() >= 0 && listItem.getQuantity() < Constants.MAX_ITEMS_QUANTITY) {
                    listItem.incrementQuantity();
                    quantity_editText.setText(Integer.toString(listItem.getQuantity()));
                }
                else if(listItem.getQuantity() >= Constants.MAX_ITEMS_QUANTITY)
                    quantity_editText.setText(Integer.toString(Constants.MAX_ITEMS_QUANTITY));
                else
                    quantity_editText.setText("");

                quantity_editText.setSelection(quantity_editText.getText().length());
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
                    else
                        quantity_editText.setText("");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.e("Error", "Não há inteiro definido");
                }

                quantity_editText.setSelection(quantity_editText.getText().length());
            }
        });

        quantity_editText.setSelection(quantity_editText.getText().length());

        quantity_editText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", Integer.toString(Constants.MAX_ITEMS_QUANTITY))});

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


        databaseHandler = new ItemsDatabaseHandler(context);
        if(databaseHandler.getImage(listItem.getItem()) != null) {
            Log.d("EntrouHere", "EntrouHere");
            brief_item_image_view.setImageBitmap(databaseHandler.getImage(listItem.getItem()));
        }


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Clicked","Product: " + listItem.getItem().getProduct().getName());

                dismissKeyboard((Activity)context);

                try {
                    Activity activity = (Activity) context;

                    LinearLayout viewGroup = (LinearLayout) activity.findViewById(R.id.popup);
                    LayoutInflater layoutInflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

                    ImageView photo_popup_image_view = (ImageView) layout.findViewById(R.id.photo_popup_image_view);
                    databaseHandler = new ItemsDatabaseHandler(context);
                    if(databaseHandler.getImage(listItem.getItem()) != null) {
                        photo_popup_image_view.setImageBitmap(databaseHandler.getImage(listItem.getItem()));
                    }

                    TextView product_name_popup = (TextView) layout.findViewById(R.id.product_name_popup);
                    product_name_popup.setText(listItem.getItem().getProduct().getName());

                    TextView description_popup = (TextView) layout.findViewById(R.id.description_popup);
                    description_popup.setText(listItem.getItem().getProduct().getDescription());

                    TextView price_popup = (TextView) layout.findViewById(R.id.price_popup);
                    price_popup.setText(String.format("R$ %.2f",listItem.getItem().getPrice()));

                    final NumberPicker quantity_popup_number_picker = (NumberPicker) layout.findViewById(R.id.quantity_popup_number_picker);
                    quantity_popup_number_picker.setMinValue(0);
                    quantity_popup_number_picker.setMaxValue(99);
                    quantity_popup_number_picker.setValue(listItem.getQuantity());
                    quantity_popup_number_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                    quantity_popup_number_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                            listItem.setQuantityAndUpdateCart(newValue);
                            quantity_editText.setText(Integer.toString(listItem.getQuantity()));
                        }
                    });

                    SubtotalTextViewOnlyValue subtotal_popup = (SubtotalTextViewOnlyValue) layout.findViewById(R.id.subtotal_popup);
                    subtotal_popup.setText(String.format("R$ %.2f", listItem.getItem().getPrice() * listItem.getQuantity()));
                    listItem.setSubtotalListener(subtotal_popup);

                    final PopupWindow popup = new PopupWindow(context);
                    popup.setOnDismissListener((SearchTabsActivity)activity);
                    popup.setWidth( (int) getDeviceWidth()* 8 / 10);
                    popup.setHeight( (int) getDeviceHeight()* 85 / 100 );
                    popup.setContentView(layout);
                    popup.setFocusable(true);

                    popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

                    listener.OnPopupShow();

                    Button popup_dismiss_button = (Button) layout.findViewById(R.id.popup_dismiss_button);

                    popup_dismiss_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup.dismiss();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return row;
    }

//    private PopupWindow setupListItemPopUp(ListItem listItem, View row) {
//
//
//    }

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

    private int getDeviceWidth() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        return screenWidth;
    }

    private int getDeviceHeight() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        return screenHeight;
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}