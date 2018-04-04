package com.pixectra.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pixectra.app.Models.Product;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements UserProfileFragment.OnFragmentInteractionListener {

    DashboardFragment dashboardFragment;
    UserProfileFragment userProfileFragment;
    ReferAndEarnFragment referAndEarnFragment;

    FirebaseAuth auth, mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    ViewPager viewPager;

    BottomNavigationView bottomNavigationView;

    void add() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("CommonData");

        ref.child("PhotoBooks").setValue(null);
        ref.child("PhotoBooks").push().setValue(new Product("PhotoBooks", "6 X 4 ", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 25, 22, 119, "Book", 0, 0, 150));
        ref.child("PhotoBooks").push().setValue(new Product("PhotoBooks", "6 X 4 ", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 50, 45, 190, "Book", 0, 0, 220));
        ref.child("PhotoBooks").push().setValue(new Product("PhotoBooks", "6 X 4 ", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 100, 70, 250, "Book", 0, 0, 300));
        ref.child("PhotoBooks").push().setValue(new Product("PhotoBooks", "6 X 8 ", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 25, 22, 230, "Book", 0, 0, 270));
        ref.child("PhotoBooks").push().setValue(new Product("PhotoBooks", "6 X 8 ", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 50, 45, 340, "Book", 0, 0, 380));
        ref.child("PhotoBooks").push().setValue(new Product("PhotoBooks", "6 X 8 ", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 100, 70, 450, "Book", 0, 0, 490));
        ref.child("FlipBook").setValue(null);

        ref.child("FlipBook").push().setValue(new Product("FlipBook", "Small", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 1, 1, 200, "VideoBook", 0, 1, 250));
        ref.child("FlipBook").push().setValue(new Product("FlipBook", "Standard", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 1, 1, 300, "VideoBook", 0, 1, 400));
        ref.child("Photos").setValue(null);

        ref.child("Photos").push().setValue(new Product("Photos", "6 X 4", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", -1, 8, 80, "Set Of 8 <br>+ <small>Rs.8/PIC</small>", 8, 0, 95));
        ref.child("Photos").push().setValue(new Product("Photos", "6 X 8", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", -1, 8, 90, "Set Of 8 <br>+ <small> Rs.9/PIC</small>", 10, 0, 100));
        ref.child("Polaroids").setValue(null);

        ref.child("Polaroids").push().setValue(new Product("Polaroids", "Wallet Set", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", -1, 15, 99, "Set Of 15 <br>+ <small> Rs.6/Piece</small>", 6, 0, 120));
        ref.child("Polaroids").push().setValue(new Product("Polaroids", "Standard Set", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", -1, 15, 170, "Set Of 15 <br>+ <small> Rs.6/Piece</small>", 9, 0, 200));
        ref.child("PostCard").setValue(null);

        ref.child("PostCard").push().setValue(new Product("PostCard", "Photo PostCard", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 1, 1, 69, "Card", 0, 0, 100));
        ref.child("Posters").setValue(null);

        ref.child("Posters").push().setValue(new Product("Posters", "Small", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 1, 1, 59, "Poster", 0, 0, 70));
        ref.child("Posters").push().setValue(new Product("Posters", "Large", "http://blog.platinastudio.com/wp-content/uploads/2013/01/saloon1.jpg", 1, 1, 99, "Poster", 0, 0, 120));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

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
        Branch.getInstance(getApplicationContext()).loadRewards(new Branch.BranchReferralStateChangedListener() {
            @Override
            public void onStateChanged(boolean changed, BranchError error) {
                if (changed) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("You  Have Earned Credits\nUpdated Credits Are " + Branch.getInstance().getCredits());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });
        //rewards to refers
//        add();
        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setOffscreenPageLimit(3);
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
