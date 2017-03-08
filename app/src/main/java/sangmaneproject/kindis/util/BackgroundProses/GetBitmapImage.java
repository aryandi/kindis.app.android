package sangmaneproject.kindis.util.BackgroundProses;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by DELL on 3/8/2017.
 */

public class GetBitmapImage extends AsyncTask<String, String, Bitmap> {
    Context context;
    String urlImage;

    private OnFetchFinishedListener listener;

    // the listener interface
    public interface OnFetchFinishedListener {
        void onFetchFinished(Bitmap bitmap);
    }

    public GetBitmapImage(Context context, String urlImage, OnFetchFinishedListener listener){
        this.context = context;
        this.urlImage = urlImage;
        this.listener = listener;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap theBitmap = null;
        try {
            theBitmap = Glide
                    .with(context)
                    .load(urlImage)
                    .asBitmap()
                    .into(100, 100)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return theBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        listener.onFetchFinished(bitmap);
    }
}
