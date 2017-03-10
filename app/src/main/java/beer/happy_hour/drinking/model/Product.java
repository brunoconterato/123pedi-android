package beer.happy_hour.drinking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brcon on 06/03/2017.
 */

public class Product implements Parcelable{

    private String name;
    private String brand;
    private String manufacturer;

    public Product() {}

    public Product(Parcel in) {
        name = in.readString();
        brand = in.readString();
        manufacturer = in.readString();
    }


    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(name);
        out.writeString(brand);
        out.writeString(manufacturer);
    }

    // Creator
    public static final Parcelable.Creator
            CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
