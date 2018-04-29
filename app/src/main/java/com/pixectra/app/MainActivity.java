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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Models.Version;
import com.pixectra.app.Utils.SessionHelper;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference version = database.getReference("Version");
        version.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Version version = dataSnapshot.getValue(Version.class);
                    if (version != null && version.getCode() > BuildConfig.VERSION_CODE) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("App Version " + version.getName() + " Is Available");
                        builder.setMessage("Changes: \n" + version.getDesc());
                        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                                    try {
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "Play Store And Browser Not Found", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton("LATER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            new SessionHelper(getApplicationContext()).logOutUser();
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
