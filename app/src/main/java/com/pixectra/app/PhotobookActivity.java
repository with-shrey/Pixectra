package com.pixectra.app;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Fragments.OnetimeFragment;
import com.pixectra.app.Utils.LogManager;

import java.util.ArrayList;

public class PhotobookActivity extends AppCompatActivity {


    public ArrayList<String> tabTitles;
    ViewPager viewPager = null;
    TextView title;
    MyAdpater myAdpater;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photobook);
        tabTitles = new ArrayList<>();
        LogManager.viewContent("PhotoBooks", "PhotoBooks", "PhotoBook-Main");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("CommonData").child("PhotoBooks");
        ref.keepSynced(true);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tabTitles.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    tabTitles.add(dataSnapshot1.getKey());
                }
                Toast.makeText(PhotobookActivity.this, tabTitles.toString(), Toast.LENGTH_SHORT).show();
                myAdpater = new MyAdpater(getSupportFragmentManager());
                viewPager.setAdapter(myAdpater);
                TabLayout tabLayout = findViewById(R.id.tablayout_poster);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
                tabLayout.setTabTextColors(ContextCompat.getColorStateList(PhotobookActivity.this, R.color.colorwhite));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        title = findViewById(R.id.title_poster);
        viewPager = findViewById(R.id.poster_pager);
        //---------setup dummy title----
        title.setText("Photo Book");



        /*
        setting up TAB LAYOUT
        <--
         */


    }


    public class MyAdpater extends FragmentPagerAdapter {
        public MyAdpater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return OnetimeFragment.newInstance(tabTitles.get(position));
//            switch (position) {
//                case 0:
//                    return new SubscribeFragment();
//                case 1:
//                    return new OnetimeFragment();
//
//                default:
//                    return null;
//
//
//            }

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

        @Override
        public int getCount() {
            return tabTitles.size();
        }
    }

}