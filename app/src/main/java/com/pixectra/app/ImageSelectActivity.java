package com.pixectra.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pixectra.app.Fragments.ImageFragment;

public class ImageSelectActivity extends AppCompatActivity {
ViewPager viewPager;
TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar3));
        getSupportActionBar().setElevation(0);
        viewPager=findViewById(R.id.image_select_pager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout=findViewById(R.id.image_select_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons(tabLayout);
    }

    void setTabIcons(TabLayout tabs){
        int[] icons={R.drawable.ic_letter,R.drawable.facebook,R.drawable.instagram,R.drawable.google_photos};
        for (int i=0;i<4;i++)
            tabs.getTabAt(i).setIcon(icons[i]);
    }
    class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
