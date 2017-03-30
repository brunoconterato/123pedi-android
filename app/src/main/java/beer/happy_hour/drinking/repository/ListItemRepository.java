package beer.happy_hour.drinking.repository;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.model.List_Item.ListItem;

/**
 * Created by brcon on 11/03/2017.
 */

/**
 * Singleton Implementation
 */
public class ListItemRepository {

    private static ListItemRepository instance;

    private List<ListItem> list;

    private boolean loaded = false;

    private boolean disconnected = false;

    private ListItemRepository(){

        list = new ArrayList<ListItem>();

    }

    public static ListItemRepository getInstance(){
        if(instance == null){
            synchronized (ListItemRepository.class) {
                if(instance == null){
                    instance = new ListItemRepository();
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

    public ListItem getItem(int position){
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

    public int getSize() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

}
