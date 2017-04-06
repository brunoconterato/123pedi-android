package beer.happy_hour.drinking.load_stock_data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import beer.happy_hour.drinking.database_handler.ItemsDatabaseHandler;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.List_Item.ListItem;
import beer.happy_hour.drinking.repository.ListItemRepository;

/**
 * Created by brcon on 28/03/2017.
 */


/**
 * Singleton Implementation
 */
public class LoadStockTask extends AsyncTask<String, Void, List<Item>> {

    private ItemsDatabaseHandler databaseHandler;
    private static LoadStockTask instance;
    private boolean initialized = false;
    private LoadStockFragment.TaskCallbacks callback;
    private ListItemRepository repository;

    private LoadStockTask(Context context){
        repository = ListItemRepository.getInstance();
        databaseHandler = new ItemsDatabaseHandler(context);
    }

    public static LoadStockTask getInstance(Context context) {
        if(instance == null){
            synchronized (LoadStockTask.class) {
                if(instance == null){
                    instance = new LoadStockTask(context);
                }
            }
        }
        return instance;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setCallback(LoadStockFragment.TaskCallbacks callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        if (callback != null) {
            callback.onPreExecute();
        }
    }

    /**
     * Note that we do NOT call the callback object's methods
     * directly from the background thread, as this could result
     * in a race condition.
     */
    @Override
    protected List<Item> doInBackground(String... strings) {
        initialized = true;

        try {
            Log.d("Entrou", "LoadStockTask");
            String stringJSON = loadJSON(strings[0]);

            //deserialize generic collection (like a List, in this case)
            Type listType = new TypeToken<List<Item>>(){}.getType();
            List<Item> listItems = new Gson().fromJson(stringJSON, listType);

            repository = ListItemRepository.getInstance();

            for(Item item : listItems){
                Log.d("ToString : ", item.toString());
                repository.add(new ListItem(item));

                if(!databaseHandler.hasItem(item))
                    databaseHandler.addItem(item);
            }

            return listItems;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCancelled() {
        if (callback != null) {
            callback.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(List<Item> listItem) {
        if (callback != null) {
            callback.onPostExecute();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {

        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {

            response.append(line);
        }

        in.close();

        Log.d("JSON : ", response.toString());

        return response.toString();
    }
}