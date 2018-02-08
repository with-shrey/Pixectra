package com.pixectra.app;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements UserProfileFragment.OnFragmentInteractionListener {

    DashboardFragment dashboardFragment;
    UserProfileFragment userProfileFragment;
    ReferAndEarnFragment referAndEarnFragment;

    FirebaseAuth auth, mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setOffscreenPageLimit(3);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LActivity.class));
            finish();
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();
        }
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(1, true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.refer_earn);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.dashboard);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.settings);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refer_earn:
                        viewPager.setCurrentItem(0, true);
                        return true;
                    case R.id.dashboard:
                        viewPager.setCurrentItem(1, true);
                        return true;
                    case R.id.settings:
                        viewPager.setCurrentItem(2, true);
                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (referAndEarnFragment == null) {
                        referAndEarnFragment = new ReferAndEarnFragment();
                        return referAndEarnFragment;
                    }
                    return referAndEarnFragment;
                case 1:
                    if (dashboardFragment == null) {
                        dashboardFragment = new DashboardFragment();
                        return dashboardFragment;
                    }
                    return dashboardFragment;
                case 2:
                    if (userProfileFragment == null) {
                        userProfileFragment = new UserProfileFragment();
                        return userProfileFragment;
                    }
                    return userProfileFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
