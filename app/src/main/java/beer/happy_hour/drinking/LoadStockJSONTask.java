package beer.happy_hour.drinking;

/**
 * Created by brcon on 05/03/2017.
 */

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

import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.shopping_cart.ListItem;
import beer.happy_hour.drinking.repository.ListItemRepository;


/**
 * Singleton Implementation
 */
public class LoadStockJSONTask extends AsyncTask<String, Void, List<Item>> {

    private LoadListener listener;
    private ListItemRepository repository;

    private static LoadStockJSONTask instance;

    private boolean executed = false;

    private LoadStockJSONTask(){
        repository = ListItemRepository.getInstance();
    }

    public static LoadStockJSONTask getInstance(){
        if(instance == null){
            synchronized (LoadStockJSONTask.class) {
                if(instance == null){
                    instance = new LoadStockJSONTask();
                }
            }
        }
        return instance;
    }

    public void setListener(LoadListener listener) {
        this.listener = listener;
    }

    public boolean isExecuted() {
        return executed;
    }

    @Override
    protected List<Item> doInBackground(String... strings) {
        repository = ListItemRepository.getInstance();
        executed = true;

        try {
            Log.d("Entrou", "LoadStockJSONTask");
            String stringJSON = loadJSON(strings[0]);

            //deserialize generic collection (like a List, in this case)
            Type listType = new TypeToken<List<Item>>(){}.getType();
            List<Item> listItems = new Gson().fromJson(stringJSON, listType);

            for(Item item : listItems){
                Log.d("ToString : ", item.toString());
                repository.add(new ListItem(item));
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
    protected void onPostExecute(List<Item> listItem) {

        if (listItem != null) {
            repository.setLoaded(true);
            listener.onLoaded(listItem);

        } else {
            repository.setDisconnected(true);
            listener.onError();
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

    public interface LoadListener {

        void onLoaded(List<Item> item);

        void onError();
    }
}