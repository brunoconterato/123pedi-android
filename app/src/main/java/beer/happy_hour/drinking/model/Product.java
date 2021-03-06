package beer.happy_hour.drinking.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Blob;

/**
 * Created by brcon on 06/03/2017.
 */

public class Product implements Parcelable{

    private String name;
    private String brand;
    private String manufacturer;
    private String image_url;

    private String description;

    private Category category;

    public Product() {}

    public Product(Parcel in) {
        name = in.readString();
        brand = in.readString();
        manufacturer = in.readString();
        image_url = in.readString();

        category = (Category) in.readParcelable(Category.class.getClassLoader());
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

    public Category getCategory() {
        return category;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
        out.writeString(image_url);
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

    public String getDescription() {
        return description;
    }
}
