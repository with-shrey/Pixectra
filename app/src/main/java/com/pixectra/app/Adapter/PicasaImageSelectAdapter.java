package com.pixectra.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixectra.app.Models.Images;
import com.pixectra.app.Models.PicasaAlbumExtra;
import com.pixectra.app.R;
import com.pixectra.app.Utils.PicasaAlbumActivity;

import java.util.List;

/**
 * Created by Sanath on 3/25/2018.
 */

public class PicasaImageSelectAdapter extends ImageSelectAdapter {

    private List<PicasaAlbumExtra> albumExtras;
    private String accessToken;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public PicasaImageSelectAdapter(Context context, String key, int maxPics, List<Images> data,
                                    List<PicasaAlbumExtra> albumExtras) {
        super(context, key, maxPics, data);
        this.albumExtras = albumExtras;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_item, parent, false);
        return new PicasaHolder(view);
    }


    class PicasaHolder extends myViewHolder{

        public PicasaHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            //super.onClick(view);
            Intent intent = new Intent(c, PicasaAlbumActivity.class);
            intent.putExtra("key", key);
            intent.putExtra("maxP", maxP);
            intent.putExtra("albumId", albumExtras.get(getAdapterPosition()).getAlbumId());
            intent.putExtra("albumName", albumExtras.get(getAdapterPosition()).getAlbumName());
            intent.putExtra("accessToken", accessToken);
            Activity activity = (Activity)c;
            activity.startActivity(intent);
        }
    }
}
