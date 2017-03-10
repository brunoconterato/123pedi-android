package beer.happy_hour.drinking.model;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import beer.happy_hour.drinking.R;

/**
 * Created by brcon on 09/03/2017.
 */

public class ListItem {

    private View view;

    private Item item;

    //TODO: Sera que da pra implementar os botoes e o EditText direto no ListItemAdapter? Não ficaria até melhor (guardando menos informação?)
    //Quantidade que o comprador deseja
    private int quantity;

//    private Button addOne_button;
//    private Button minusOne_button;
//    private Button addToShoppingCart_button;

//    private EditText quantity_editText;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity >= 0)
            this.quantity = quantity;
        else
            this.quantity = 0;
    }

//    public EditText getQuantity_editText() {
//        if(quantity_editText == null)
//            quantity_editText = (EditText) view.findViewById(R.id.quantity_editText);
//        return quantity_editText;
//    }

//    public void setQuantity_editText(EditText quantity_editText) {
//        this.quantity_editText = quantity_editText;
//    }

    public ListItem(View list_item_view, Item item) {
        this.view = list_item_view;
        this.item = item;
    }

//    public Button getAddOne_button() {
//        if(addOne_button == null)
//            addOne_button = (Button) view.findViewById(R.id.addOne_button);
//
//        return addOne_button;
//    }
//
//    public Button getMinusOne_button() {
//        if(minusOne_button == null)
//            minusOne_button = (Button) view.findViewById(R.id.minusOne_button);
//
//        return minusOne_button;
//    }
//
//    public Button getAddToShoppingCart_button() {
//        if(addToShoppingCart_button ==null)
//            addToShoppingCart_button = (Button) view.findViewById(R.id.addToShoppingCart_button);
//
//        return addToShoppingCart_button;
//    }

//    public void incrementQuantity(){
//        if(quantity_editText == null)
//            quantity_editText = (EditText) view.findViewById(R.id.quantity_editText);
//
//        this.quantity++;
//        this.quantity_editText.setText(Integer.toString(quantity));
//    }

    public void incrementQuantity(){
        this.quantity++;
    }

//    public void decrementQuantity(){
//        if(quantity > 0) {
//            if (quantity_editText == null)
//                quantity_editText = (EditText) view.findViewById(R.id.quantity_editText);
//
//            this.quantity--;
//            this.quantity_editText.setText(Integer.toString(quantity));
//        }
//    }

    public void decrementQuantity(){
        this.quantity--;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
