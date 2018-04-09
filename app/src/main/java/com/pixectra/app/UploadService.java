package com.pixectra.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.pixectra.app.Utils.CartHolder;
import com.pixectra.app.Utils.ImageController;

public class UploadService extends Service {
    ImageController uploader;

    public UploadService() {

    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (uploader == null) {
            uploader = new ImageController(intent.getStringExtra("key"), getApplicationContext());
            uploader.placeOrder(CartHolder.getInstance().getCheckout());
        } else {
            Toast.makeText(this, "Retrying", Toast.LENGTH_LONG).show();

            if (intent.getIntExtra("photo", -1) == 1)
                uploader.processUpload(intent.getIntExtra("i", 0)
                        , intent.getIntExtra("j", 0));
            else if (intent.getIntExtra("photo", -1) == 0)
                uploader.uploadVideos(intent.getIntExtra("i", 0));
            else if (intent.getIntExtra("photo", -1) == 2) {
                uploader.uploadUserDataAndStatus();
            } else
                uploader.startUpload();
        }
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Starting Upload In Background", Toast.LENGTH_SHORT).show();
        if (uploader == null) {
            uploader = new ImageController(intent.getStringExtra("key"), getApplicationContext());
            uploader.placeOrder(CartHolder.getInstance().getCheckout());
        } else {
            Toast.makeText(this, "Retrying", Toast.LENGTH_LONG).show();

            if (intent.getIntExtra("photo", -1) == 1)
                uploader.processUpload(intent.getIntExtra("i", 0)
                        , intent.getIntExtra("j", 0));
            else if (intent.getIntExtra("photo", -1) == 0)
                uploader.uploadVideos(intent.getIntExtra("i", 0));
            else if (intent.getIntExtra("photo", -1) == 2) {
                uploader.uploadUserDataAndStatus();
            } else
                uploader.startUpload();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
