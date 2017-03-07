package beer.happy_hour.drinking.model;

/**
 * Created by brcon on 05/03/2017.
 */

public class Item {

//    private int id;
//    private int product_id;
//    private int retailer_id;
//    private int quantity;
//    private Date expiration_date;
    private double price;
//    private double min_selling_price;
//    private double cost_price;
//    private Date created_at;
//    private Date updated_at;
//    private Retailer retailer;
    private Product product;

    public Double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }
}