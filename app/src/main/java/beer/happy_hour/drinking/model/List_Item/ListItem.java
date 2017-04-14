package beer.happy_hour.drinking.model.List_Item;

import android.os.Parcel;
import android.os.Parcelable;

import beer.happy_hour.drinking.model.Item;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItem implements Parcelable{

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    private Item item;
    private int quantity;  //Quantidade que o comprador deseja
    private SubtotalChangeListener listener;

    private double subtotal;
    private ShoppingCart cart;

    public ListItem(Item item) {
        this.item = item;
        this.quantity = 0;
        this.subtotal = 0;

        cart = ShoppingCart.getInstance();
    }

    public ListItem(Parcel in) {
        this.quantity = in.readInt();
        this.item = (Item) in.readParcelable(Item.class.getClassLoader());

        cart = ShoppingCart.getInstance();
    }

    private void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
        if (listener != null)
            listener.onValueChanged(subtotal);
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Único método em ListItem que pode usar o carrinho, de uma única maneira:
     * usando o método ShoppingCart.updateCart()
     *
     * @param newQuantity
     */
    public void setQuantityAndUpdateCart(int newQuantity) {
        if (newQuantity > 0)
            quantity = newQuantity;
        else
            quantity = 0;

        setSubtotal(item.getPrice() * newQuantity);
        cart.updateCart(this);
    }

    public void incrementQuantity(){
        if(quantity <= 99)
            setQuantityAndUpdateCart(quantity + 1);
    }

    public void decrementQuantity(){
        if(quantity >= 1)
            setQuantityAndUpdateCart(quantity - 1);
    }

    public Item getItem() {
        return item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(quantity);

        out.writeParcelable(item, flags);
    }

    @Override
    public String toString(){
        String str = "Name: " + item.getProduct().getName() + "\n" +
                "Quantity: " + this.quantity + "\n" +
                "Category: " + item.getProduct().getCategory().getName();

        return str;
    }

    public void setSubtotalListener(SubtotalChangeListener listener) {
        this.listener = listener;
    }

    public interface SubtotalChangeListener {
        void onValueChanged(double newValue);
    }
}
