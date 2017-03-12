package beer.happy_hour.drinking.repository;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.model.ListItem;
import beer.happy_hour.drinking.model.ShoppingCartSingleton;

/**
 * Created by brcon on 11/03/2017.
 */

public class ListItemRepositorySingleton {

    private static ListItemRepositorySingleton instance;

    private List<ListItem> list;

    private List<ListItem> filteredList;

    private ListItemRepositorySingleton(){

        list = new ArrayList<ListItem>();
        filteredList = new ArrayList<ListItem>();

    }

    public static ListItemRepositorySingleton getInstance(){
        if(instance == null){
            synchronized (ListItemRepositorySingleton.class) {
                if(instance == null){
                    instance = new ListItemRepositorySingleton();
                }
            }
        }
        return instance;
    }

    public void add(ListItem listItem){
        list.add(listItem);
    }

    public List<ListItem> getList() {
        return list;
    }

    public List<ListItem> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<ListItem> filteredList) {
        this.filteredList = filteredList;
    }

    public void resetFilteredList(){
        this.filteredList = list;
    }

    public int getFIlteredListSize(){
        return this.filteredList.size();
    }

    public ListItem getItem(int position){
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

    public ListItem getFilteredItem(int position){
        return filteredList.get(position);
    }

    public int getSize() {
        return list.size();
    }

    public void addToFilteredList(ListItem listItem) {
        filteredList.add((listItem));
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
