package com.pixectra.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Models.Banner;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by yugan on 1/10/2018.
 */

public class DashboardFragment extends Fragment {

    ViewPager viewPager;
    CircleIndicator circleIndicator;
    ArrayList<Banner> images;
    int i;
    SlideViewAdapter slideViewAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_fragment, null);
        CardView photobook = view.findViewById(R.id.photobook);
        CardView flipbook = view.findViewById(R.id.flipbook);
        CardView postcard = view.findViewById(R.id.postcard);
        CardView polaroid = view.findViewById(R.id.polaroid);
        CardView photos = view.findViewById(R.id.photos);
        CardView posters = view.findViewById(R.id.posters);
        images=new ArrayList<>();
        photobook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PhotobookActivity.class);
                startActivity(intent);
            }
        });
        flipbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosterActivity(1);
            }
        });
        postcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosterActivity(2);
            }
        });
        polaroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosterActivity(3);
            }
        });
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosterActivity(4);
            }
        });
        posters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosterActivity(5);
            }
        });
        viewPager = view.findViewById(R.id.home_viewpager);
        viewPager.setPadding(40, 24, 40, 5);
        slideViewAdapter = new SlideViewAdapter(getActivity());

        circleIndicator = view.findViewById(R.id.viewpager_indicator);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("CommonData").child("Banner");
        //<--setting up recycler view
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                images.clear();
                for (DataSnapshot temp:dataSnapshot.getChildren()){
                   images.add(temp.getValue(Banner.class));
                }
                viewPager.setAdapter(slideViewAdapter);
                viewPager.setOffscreenPageLimit(images.size());
                viewPager.setCurrentItem(1, true);
                circleIndicator.setViewPager(viewPager);
                slideViewAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                view.findViewById(R.id.onetime_progress).setVisibility(View.GONE);
            }
        });
        ref.keepSynced(true);
        i = images.size() / 2;
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 2000, 3000); // delay
        return  view;
    }
    Timer timer;
    boolean increment=true;
    class RemindTask extends TimerTask {

        @Override
        public void run() {
            if (i == images.size())
                increment=false;
            if (i == 0)
                increment = true;
            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            (getActivity()).runOnUiThread(new Runnable() {
                public void run() {
                    if (increment)
                    viewPager.setCurrentItem(i++,true);
                    else
                        viewPager.setCurrentItem(i--,true);
                }
            });

        }
    }

    void startPosterActivity(int id){
        Intent intent=new Intent(getActivity(),PosterActivity.class);
        intent.putExtra("type",id);
        startActivity(intent);
    }

    private class SlideViewAdapter extends PagerAdapter {

        Activity activity;


        public SlideViewAdapter(Activity activity) {
            this.activity = activity;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater layoutInflater = (activity).getLayoutInflater();
            View itemView = layoutInflater.inflate(R.layout.home_viewpager_item_layout, container, false);
            ImageView imageView = itemView.findViewById(R.id.sliding_image);
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(activity).load(images.get(position).getImage()).apply(requestOptions)

                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //imageView.setImageResource(R.drawable.ic_picture);

                           // imageView.progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                           // holder.progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);

            container.addView(itemView);
            return itemView;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }
}
