package beer.happy_hour.drinking.load_stock_data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

import beer.happy_hour.drinking.database_handler.ItemsDatabaseHandler;
import beer.happy_hour.drinking.model.Item;
import beer.happy_hour.drinking.model.List_Item.ListItem;
import beer.happy_hour.drinking.repository.ListItemRepository;

/**
 * Created by brcon on 02/04/2017.
 */

/**
 * Singleton Implementation
 */
public class DownloadImageTask extends AsyncTask<Void, Void, Void> {

    private ItemsDatabaseHandler databaseHandler;
    private ListItemRepository repository;
    private static DownloadImageTask instance;
    private DownloadImageFragment.TaskCallbacks callback;
    private boolean initialized = false;
    private Context context;

    private DownloadImageTask(Context context) {
        repository = ListItemRepository.getInstance();
        databaseHandler = new ItemsDatabaseHandler(context);
    }

    public static DownloadImageTask getInstance(Context context){
        if(instance == null){
            synchronized (DownloadImageTask.class) {
                if(instance == null){
                    instance = new DownloadImageTask(context);
                }
            }
        }

        instance.setContext(context);
        return instance;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        initialized = true;

        //Stage 1 - Download from web
        for (ListItem listItem : repository.getList()) {
            Item item = listItem.getItem();
            String urldisplay = item.getProduct().getImage_url();

            Log.d("urldisplay", urldisplay);

            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                callback.onErrorImage();
            }

            //Stage 2 - Insert into database
            databaseHandler.updateItemImage(item, image);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        Log.d("DownloadImageTask","onPost");
        if (callback != null) {
            callback.onPostExecuteImage();
        }
    }

    public void setCallback(DownloadImageFragment.TaskCallbacks callback) {
        this.callback = callback;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}