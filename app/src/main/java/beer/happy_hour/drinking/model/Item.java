package beer.happy_hour.drinking.model;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import beer.happy_hour.drinking.R;

/**
 * Created by brcon on 05/03/2017.
 */

public class Item {

    private double price;
    private Product product;

    public Double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString(){
        String str = "Price: " + Double.toString(price);

        return str;
    }
}