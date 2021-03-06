package com.pixectra.app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixectra.app.ImageSelectActivity;
import com.pixectra.app.Models.Images;
import com.pixectra.app.Models.PicasaAlbumExtra;
import com.pixectra.app.R;
import com.pixectra.app.Utils.GlideHelper;
import com.pixectra.app.Utils.PicasaAlbumFragment;

import java.util.List;

/**
 * Created by Sanath on 3/25/2018.
 */

public class PicasaImageSelectAdapter extends RecyclerView.Adapter<PicasaImageSelectAdapter.PicasaHolder> {

    private List<PicasaAlbumExtra> albumExtras;
    List<Images> data;
    private String accessToken;
    String key;
    int maxP;
    int w;
    Context mContext;
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public PicasaImageSelectAdapter(Context context, String key, int maxPics, List<Images> data,
                                    List<PicasaAlbumExtra> albumExtras) {
        this.albumExtras = albumExtras;
        maxP = maxPics;
        this.key = key;
        this.data = data;
        mContext = context;
        DisplayMetrics dm = new DisplayMetrics();
        ((ImageSelectActivity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = (dm.widthPixels / 3) - (int) (context.getResources().getDimension(R.dimen.image_cell_padding) * 3);
    }

    @NonNull
    @Override
    public PicasaImageSelectAdapter.PicasaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_row, parent, false);
        return new PicasaHolder(view);
    }

    @Override
    public void onBindViewHolder(PicasaImageSelectAdapter.PicasaHolder holder1, int position) {
        PicasaHolder holder = holder1;
        holder.nameView.setText(albumExtras.get(position).getAlbumName());
        int numberOfPics = Integer.parseInt(albumExtras.get(position).getNumberOfPics());
        String photosText;
        if(numberOfPics == 1)
            photosText = numberOfPics + " Photo";
        else
            photosText = numberOfPics + " Photos";
        holder.picsNumView.setText(photosText);

        GlideHelper.load(mContext, data.get(position).getThumbnail(), holder.image
                , null, 200, 200);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class PicasaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView nameView, picsNumView;

        PicasaHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.gallery_title);
            image = itemView.findViewById(R.id.galleryImage);
            picsNumView = itemView.findViewById(R.id.gallery_count);
            itemView.setOnClickListener(this);
            itemView.getLayoutParams().height = w;
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
            fragmentTransaction = ((ImageSelectActivity) mContext).getSupportFragmentManager().beginTransaction();
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
