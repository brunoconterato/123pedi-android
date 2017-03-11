package beer.happy_hour.drinking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brcon on 11/03/2017.
 */

public class ShoppingCartSingleton {

    private List<ListItem> listItems;
    private List<Item> items;

    private static ShoppingCartSingleton instance;

    private ShoppingCartSingleton(){
        listItems = new ArrayList<ListItem>();
        items = new ArrayList<Item>();
    }

    public static ShoppingCartSingleton getInstance(){
        if(instance == null){
            synchronized (ShoppingCartSingleton.class) {
                if(instance == null){
                    instance = new ShoppingCartSingleton();
                }
            }
        }
        return instance;
    }

    public void addToCart(ListItem listItem){
        if(listItem.getQuantity() > 0)
            if(!listItems.contains(listItem))
                listItems.add(listItem);
            else{
                int indexListItem = listItems.indexOf(listItem);
                listItems.get(indexListItem).setQuantity(listItem.getQuantity());
            }
    }

    //Talvez haja problema nesse método
    //TODO: verificar se há problemas na deleção
    public void deleteFromCart(ListItem listItem){
        for(ListItem item : listItems)
        {
            if(item.equals(listItem)) {
                listItems.remove(item);
                item.setQuantity(0);
                //O Break serve para evitar o ConcurrentModificationException
                break;
            }

        }
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    @Override
    public String toString(){
        String str = new String();

        for(ListItem item : listItems){
            str += item.toString();
        }

        str += "\n";

        return str;
    }

}
