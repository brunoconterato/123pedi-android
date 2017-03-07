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

public class LoadStockJSONTask extends AsyncTask<String, Void, List<Item>> {

    public LoadStockJSONTask(Listener listener) {

        mListener = listener;
    }

    public interface Listener {

        void onLoaded(List<Item> item);

        void onError();
    }

    private Listener mListener;

    @Override
    protected List<Item> doInBackground(String... strings) {
        try {

            String stringJSON = loadJSON(strings[0]);

            //deserialize generic collection (like a List, in this case)
            Type listType = new TypeToken<List<Item>>(){}.getType();
            List<Item> listItems = new Gson().fromJson(stringJSON, listType);

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

            mListener.onLoaded(listItem);

        } else {

            mListener.onError();
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