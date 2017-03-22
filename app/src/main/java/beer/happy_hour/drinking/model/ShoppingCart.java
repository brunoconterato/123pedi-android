package beer.happy_hour.drinking.model;

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

    public int getItemsQuantity() {
        return listItems.size();
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
        if (listener != null) {
            listener.onValueChanged(total);
        }
    }

    public void incrementItemQuantity(ListItem listItem) {
        if (listItems.contains(listItem)) {
            listItem.incrementQuantity();
            total += listItem.getItem().getPrice();

            if (listener != null) {
                listener.onValueChanged(total);
            }
        }
    }

    public void decrementItemQuantity(ListItem listItem) {
        if (listItems.contains(listItem)) {
            listItem.decrementQuantity();
            total -= listItem.getItem().getPrice();

            if (listener != null) {
                listener.onValueChanged(total);
            }
        }
    }

    public void addToCart(ListItem listItem){
        if(listItem.getQuantity() > 0)
            if (!listItems.contains(listItem)) {
                listItems.add(listItem);
                total += listItem.getItem().getPrice() * listItem.getQuantity();

                if (listener != null) {
                    listener.onValueChanged(total);
                }
            }
            else{
                int indexListItem = listItems.indexOf(listItem);
                listItems.get(indexListItem).setQuantity(listItem.getQuantity());

                total += listItem.getItem().getPrice() * listItem.getQuantity();

                if (listener != null) {
                    listener.onValueChanged(total);
                }
            }
    }

    //Talvez haja problema nesse método
    //TODO: verificar se há problemas na deleção
    public void deleteFromCart(ListItem listItem){
        for(ListItem item : listItems)
        {
            if(item.equals(listItem)) {
                total -= item.getItem().getPrice() * item.getQuantity();
                item.setQuantity(0);
                listItems.remove(item);

                total += listItem.getItem().getPrice() * listItem.getQuantity();

                if (listener != null) {
                    listener.onValueChanged(total);
                }

                //O Break serve para evitar o ConcurrentModificationException
                break;
            }
        }
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
