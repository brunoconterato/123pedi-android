package beer.happy_hour.drinking.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import beer.happy_hour.drinking.R;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItem implements Parcelable{

    private Item item;
    //Quantidade que o comprador deseja
    private int quantity;

    public ListItem(Item item) {
        this.item = item;
        this.quantity = 0;
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
    }

    public void incrementQuantity(){
        this.quantity++;
    }

    public void decrementQuantity(){
        this.quantity--;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    @Override
    public String toString(){
        String str = "Name: " + item.getProduct().getName() + "\n" +
                "Quantity: " + this.quantity + "\n" +
                "Category: " + item.getProduct().getCategory().getName();

        return str;
    }
}
