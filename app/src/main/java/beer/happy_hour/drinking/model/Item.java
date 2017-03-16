package beer.happy_hour.drinking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brcon on 05/03/2017.
 */

public class Item implements Parcelable {

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
    private int id;
    private double price;
    private int retailer_id;
    private Product product;

    public Item(Parcel in) {
        id = in.readInt();

        price = in.readDouble();

        retailer_id = in.readInt();

        product = (Product) in.readParcelable(Product.class.getClassLoader());
    }

    public int getRetailer_id() {
        return retailer_id;
    }

    public int getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        String str = "\nid" + Integer.toString(id) +
                "\nPrice: " + Double.toString(price) +
                "\nRetailer Id: " + retailer_id +
                "\nProduct Name: " + product.getName();

        str += "\n";
        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);

        out.writeDouble(price);

        out.writeInt(retailer_id);

        out.writeParcelable(product, flags);
    }
}