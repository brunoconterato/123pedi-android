package beer.happy_hour.drinking.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import beer.happy_hour.drinking.R;

/**
 * Created by brcon on 05/03/2017.
 */

public class Item implements Parcelable{

    private double price;
    private Product product;

    public Item(Parcel in) {
        price = in.readDouble();

        product = (Product) in.readParcelable(Product.class.getClassLoader());
    }

    public Double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString(){
        String str = "Price: " + Double.toString(price) + "\n" +
                "Product Name: " + product.getName() + "\n";

        str += "\n";
        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(price);

        out.writeParcelable(product, flags);
    }

    // Creator
    public static final Parcelable.Creator
            CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}