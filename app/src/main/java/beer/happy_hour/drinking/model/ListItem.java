package beer.happy_hour.drinking.model;

import android.os.Parcel;
import android.os.Parcelable;

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
    //Quantidade que o comprador deseja
    private int quantity;
    private SubtotalChangeListener listener;
    private double subtotal;

    public ListItem(Item item) {
        this.item = item;
        this.quantity = 0;
        this.subtotal = 0;
    }

    public ListItem(Parcel in) {
        this.quantity = in.readInt();
        this.item = (Item) in.readParcelable(Item.class.getClassLoader());
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity >= 0)
            this.quantity = quantity;
        else
            this.quantity = 0;

        subtotal = item.getPrice() * quantity;
        if (listener != null)
            listener.onValueChanged(subtotal);

    }

    public void incrementQuantity(){
        this.quantity++;
        subtotal = item.getPrice() * quantity;
        if (listener != null)
            listener.onValueChanged(subtotal);
    }

    public void decrementQuantity(){
        this.quantity--;
        subtotal = item.getPrice() * quantity;
        if (listener != null)
            listener.onValueChanged(subtotal);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        quantity = 0;

        subtotal = item.getPrice() * quantity;
        if (listener != null)
            listener.onValueChanged(subtotal);
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

    public void setListener(SubtotalChangeListener listener) {
        this.listener = listener;
    }

    public interface SubtotalChangeListener {
        void onValueChanged(double newValue);
    }
}
