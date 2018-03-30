package com.pixectra.app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixectra.app.ImageSelectActivity;
import com.pixectra.app.Models.Images;
import com.pixectra.app.Models.PicasaAlbumExtra;
import com.pixectra.app.R;
import com.pixectra.app.Utils.AlbumActivity;
import com.pixectra.app.Utils.PicasaAlbumFragment;

import java.util.List;

/**
 * Created by Sanath on 3/25/2018.
 */

public class PicasaImageSelectAdapter extends ImageSelectAdapter {

    private List<PicasaAlbumExtra> albumExtras;
    private String accessToken;
    private FragmentActivity fragmentActivity;

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

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    class PicasaHolder extends myViewHolder{

        public PicasaHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            //super.onClick(view);
            /*Intent intent = new Intent(c, PicasaAlbumFragment.class);
            intent.putExtra("key", key);
            intent.putExtra("maxP", maxP);
            intent.putExtra("albumId", albumExtras.get(getAdapterPosition()).getAlbumId());
            intent.putExtra("albumName", albumExtras.get(getAdapterPosition()).getAlbumName());
            intent.putExtra("accessToken", accessToken);
            Activity activity = (Activity)c;
            activity.startActivity(intent);*/
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
