package beer.happy_hour.drinking.model.shopping_cart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brcon on 11/03/2017.
 */

public class ShoppingCart {

    private static ShoppingCart instance;
    private List<ListItem> listItems;
    private double total;
    private TotalChangeListener listener;

    private ShoppingCart() {
        listItems = new ArrayList<ListItem>();
    }

    public static ShoppingCart getInstance() {
        if(instance == null){
            synchronized (ShoppingCart.class) {
                if(instance == null){
                    instance = new ShoppingCart();
                }
            }
        }
        return instance;
    }

    /**
     * Visibilidade de pacote: só pode ser acessado por objetos ListItem
     * Único método de ListItem que o pode acessar: setQUantity;
     *
     * @param listItem
     */
    void updateCart(ListItem listItem) {
        if (listItem.getQuantity() > 0) {
            if (!inCart(listItem))
                listItems.add(listItem);
        } else {  //item zerado
            if (inCart(listItem))
                listItems.remove(listItem);
        }

        recalculateTotal();
    }

    private void recalculateTotal() {
        total = 0;

        for (ListItem listItem : listItems)
            total += listItem.getQuantity() * listItem.getItem().getPrice();

        if (listener != null) {
            listener.onValueChanged(total);
        }
    }

    public int getItemsQuantity() {
        return listItems.size();
    }

    public double getTotal() {
        return total;
    }

    private boolean inCart(ListItem listItem) {
        return listItems.contains(listItem);
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    @Override
    public String toString(){
        String str = new String();

        for(ListItem item : listItems){
            str += item.toString();
        }

        str += "\n";

        return str;
    }

    /**
     * Sets a listener on the cart. The listener will be modified when the
     * total changes.
     *
     * @param listener The
     */
    public void setListener(TotalChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Callbacks
     */
    public interface TotalChangeListener {
        /**
         * Called when the value of the int changes.
         *
         * @param newValue The new value.
         */
        void onValueChanged(double newValue);
    }
}
