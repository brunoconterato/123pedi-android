package beer.happy_hour.drinking.load_stock_data;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import beer.happy_hour.drinking.Constants;

/**
 * Created by brcon on 28/03/2017.
 */

public class DownloadImageFragment extends Fragment {

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks {
        void onPreExecuteImage();
        void onCancelledImage();
        void onPostExecuteImage();
        void onErrorImage();
    }

    private TaskCallbacks callbacks;
    private DownloadImageTask task;

    public DownloadImageFragment() {

    }

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d("On Attach","on attach");

        task = DownloadImageTask.getInstance(context);
        callbacks = (TaskCallbacks) context;
        task.setCallback((DownloadImageFragment.TaskCallbacks) context);
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        if(task.getStatus().equals(AsyncTask.Status.PENDING)) {
            task.execute();
        }
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }
}
