package uk.co.yasinahmed.simplejsonparser;

// Created by yasinahmed on 03/04/2018.

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

// Perhaps using Lambda would have been a more elegant solution.

class FetchImageFromURL {

    private class ImageDownload {
        static final int SUCCESS = 1;
        static final int FAILURE = 0;
    }

    private Handler handler;

    FetchImageFromURL(String url, final ImageView surgeryIcon, final HashMap<Integer, Bitmap> imageCache, final int imageCacheKey) {

        // Initialise the UI thread so it can listen for messages from background threads
        handler = new Handler(Looper.getMainLooper()) {

            // This does the listening from the background thread passing in messages
            @Override
            public void handleMessage(Message inputMessage) {
                super.handleMessage(inputMessage);

                if (inputMessage.what == ImageDownload.SUCCESS) {
                    // Gets the object from the incoming Message.
                    Bitmap image = (Bitmap) inputMessage.obj;
                    surgeryIcon.setImageBitmap(image);

                    // Set the hashMap for imageCache
                    imageCache.put(imageCacheKey, image);

                } else if (inputMessage.what == ImageDownload.FAILURE) {
                    // If the image can't be retrieved from web then set a dummy image to imageView
                    surgeryIcon.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        };

        // The background task
        handleBackgroundTask(url);
    }

    private void handleBackgroundTask(final String url) {

        Runnable backgroundTask = new Runnable() {
            @Override
            public void run() {

                Message completeMessage;

                try {
                    InputStream inputStream = new java.net.URL(url).openStream();
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    // This message is picked up by the handleMessage method inside Handler (UI Thread)
                    completeMessage = handler.obtainMessage(ImageDownload.SUCCESS, image);
                    completeMessage.sendToTarget();

                } catch (IOException e) {
                    Log.d("Image", e.toString());

                    completeMessage = handler.obtainMessage(ImageDownload.FAILURE, null);
                    completeMessage.sendToTarget();
                }
            }
        };

        Thread backgroundThread = new Thread(backgroundTask);
        backgroundThread.start();
    }

}
