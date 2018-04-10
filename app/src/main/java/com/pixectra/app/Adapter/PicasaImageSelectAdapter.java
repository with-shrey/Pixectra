package com.pixectra.app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixectra.app.Models.Images;
import com.pixectra.app.Models.PicasaAlbumExtra;
import com.pixectra.app.R;
import com.pixectra.app.Utils.PicasaAlbumFragment;

import java.util.List;

/**
 * Created by Sanath on 3/25/2018.
 */

public class PicasaImageSelectAdapter extends ImageSelectAdapter {

    private List<PicasaAlbumExtra> albumExtras;
    private String accessToken;
    private FragmentActivity fragmentActivity;
    String key;
    int maxP;
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public PicasaImageSelectAdapter(Context context, String key, int maxPics, List<Images> data,
                                    List<PicasaAlbumExtra> albumExtras) {
        super(context, key, maxPics, data);
        this.albumExtras = albumExtras;
        maxP = maxPics;
        this.key = key;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picasa_album_item, parent, false);
        return new PicasaHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder1, int position) {
        super.onBindViewHolder(holder1, position);
        PicasaHolder holder = (PicasaHolder) holder1;
        holder.nameView.setText(albumExtras.get(position).getAlbumName());
        int numberOfPics = Integer.parseInt(albumExtras.get(position).getNumberOfPics());
        String photosText;
        if(numberOfPics == 1)
            photosText = numberOfPics + " Photo";
        else
            photosText = numberOfPics + " Photos";
        holder.picsNumView.setText(photosText);
    }


    class PicasaHolder extends myViewHolder{

        TextView nameView, picsNumView;

        PicasaHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.picasa_album_name);
            picsNumView = itemView.findViewById(R.id.picasa_photo_count);
        }


        @Override
        public void onClick(View view) {
            PicasaAlbumFragment fragment = new PicasaAlbumFragment();
            Bundle bundle = new Bundle();
            bundle.putString("albumName", albumExtras.get(getAdapterPosition()).getAlbumName());
            bundle.putString("key", key);
            bundle.putInt("maxPics", maxP);
            bundle.putString("albumId", albumExtras.get(getAdapterPosition()).getAlbumId());
            bundle.putString("accessToken", accessToken);
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction;
            FragmentActivity fragmentActivity = (FragmentActivity)c;
            fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_left, R.anim.slide_in_left,
                    R.anim.slide_out_left
            );
            String tag = "picasaAlbumActivity";
            fragmentTransaction.replace(R.id.show_more_picasa, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }
    }
}
