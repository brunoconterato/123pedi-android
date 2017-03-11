package beer.happy_hour.drinking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brcon on 10/03/2017.
 */

public class Category implements Parcelable {

    private String name;

    protected Category(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    @Override
    public String toString(){
        String str = "Categoria: " + name + "\n";

        return str;
    }
}
