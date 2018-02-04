package com.pixectra.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by yugan on 1/10/2018.
 */

public class DashboardFragment extends Fragment {

    ViewPager viewPager;
    CircleIndicator circleIndicator;
    int[] images = {R.drawable.demo,R.drawable.demo,R.drawable.demo};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_fragment, null);

        viewPager = view.findViewById(R.id.home_viewpager);
        viewPager.setPadding(120,24,120,0);
        SlideViewAdapter slideViewAdapter = new SlideViewAdapter(getActivity(), images);
        viewPager.setAdapter(slideViewAdapter);
        viewPager.setCurrentItem(1,true);

        circleIndicator = view.findViewById(R.id.viewpager_indicator);
        circleIndicator.setViewPager(viewPager);
        slideViewAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        return view;
    }

    private class SlideViewAdapter extends PagerAdapter{

        Activity activity;
        int[] images;

        public SlideViewAdapter(Activity activity, int[] images) {
            this.activity = activity;
            this.images = images;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            LayoutInflater layoutInflater = (activity).getLayoutInflater();
            View itemView = layoutInflater.inflate(R.layout.home_viewpager_item_layout,container,false);
            ImageView imageView = itemView.findViewById(R.id.sliding_image);
            imageView.setImageResource(images[position]);

            container.addView(itemView);
            return itemView;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }
}
