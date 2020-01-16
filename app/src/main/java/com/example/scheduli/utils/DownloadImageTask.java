package com.example.scheduli.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;


/**
 *
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }


    @Override
    protected Bitmap doInBackground(String... urls) {
        if (!urls[0].isEmpty()) {
            String urlOfImage = urls[0];
            Bitmap image = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                image = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Download Task", "Failed to download image " + e.getMessage());
            }
            return image;
        }

        return null;
    }

    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            imageView.setImageBitmap(result);
        }
    }
}
