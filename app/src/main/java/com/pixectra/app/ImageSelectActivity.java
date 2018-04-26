package com.pixectra.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.pixectra.app.Adapter.SelectedItemsAdapter;
import com.pixectra.app.Fragments.ImageFragment;
import com.pixectra.app.Utils.CartHolder;
import com.pixectra.app.Utils.LogManager;

import java.util.HashMap;


public class ImageSelectActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 4;
    public HashMap<String, Boolean> selected;
    ViewPager viewPager;
TabLayout tabLayout;
    RecyclerView selectedImages;
    SelectedItemsAdapter selectedItemsAdapter;
    FloatingActionButton save;
    int w;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        selected = CartHolder.getInstance().getSelected();
        DisplayMetrics dm = new DisplayMetrics();
        ImageSelectActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels / 3;
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar3));
        getSupportActionBar().setElevation(0);
        save = findViewById(R.id.save_images);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CartHolder.getInstance().getSize(getIntent().getStringExtra("key")) >= getIntent().getIntExtra("minPics", 0)) {
                    LogManager.addToCart(CartHolder.getInstance().getDetails(getIntent().getStringExtra("key")));
                    CartHolder.getInstance().addToCart(getIntent().getStringExtra("key"));
                    Intent intent = new Intent(ImageSelectActivity.this, Checkout.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ImageSelectActivity.this, "Please Select "
                                    + (getIntent().getIntExtra("minPics", 0)
                                    - CartHolder.getInstance().getSize(getIntent().getStringExtra("key")))
                                    + " More Images"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
        selectedItemsAdapter = new SelectedItemsAdapter(this, getIntent().getStringExtra("key"));
        viewPager=findViewById(R.id.image_select_pager);
        viewPager.setOffscreenPageLimit(3);
        selectedImages = findViewById(R.id.selected_images_recycler);
        selectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectedImages.setAdapter(selectedItemsAdapter);
        if (CartHolder.getInstance().getSize(getIntent().getStringExtra("key")) > 0) {
            selectedImages.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            selectedItemsAdapter.notifyDataSetChanged();
        }
        setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout=findViewById(R.id.image_select_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons(tabLayout);
        CartHolder.getInstance().setOnImageChangedListner(new CartHolder.ImageChangedListner() {
            @Override
            public void onImageAdded(Object img, int size) {
                Log.v("image", "added " + size);

                if (size > 0) {
                    save.setVisibility(View.VISIBLE);
                    selectedImages.setVisibility(View.VISIBLE);
                    selectedItemsAdapter.notifyDataSetChanged();
                }
                if (getSupportActionBar() != null)
                    setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
            }

            @Override
            public void onImageDeleted(Object img, int size) {
                Log.v("image", "deleted " + size);
                selectedItemsAdapter.notifyDataSetChanged();
                if (size > 0) {
                    save.setVisibility(View.VISIBLE);
                    selectedImages.setVisibility(View.VISIBLE);

                } else {
                    save.setVisibility(View.GONE);
                    selectedImages.setVisibility(View.GONE);

                }
                if (getSupportActionBar() != null)
                    setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
            }

            @Override
            public void alreadyPresent(Object img) {
                Toast.makeText(ImageSelectActivity.this, "Image Already Present In Cart", Toast.LENGTH_SHORT).show();
                selectedItemsAdapter.notifyDataSetChanged();
            }
        });

       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/

       // signIn();

    }

   /* private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/

    void setToolbarText(int count) {
        if (getSupportActionBar() != null) {
            String max = "0";
            if (getIntent().getIntExtra("maxPics", 0) == Integer.MAX_VALUE) {
                max = getString(R.string.infinite);
            } else
                max = "" + getIntent().getIntExtra("maxPics", 0);

            getSupportActionBar().setTitle("Select Images (" + count + "/" + getIntent().getIntExtra("minPics", 0) + "-" + max + ")");

        }
    }
    void setTabIcons(TabLayout tabs){
        int[] icons = {R.drawable.device, R.drawable.fb, R.drawable.insta, R.drawable.google_photos};
        for (int i=0;i<4;i++)
            tabs.getTabAt(i).setIcon(icons[i]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0 && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack("albumactivity", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

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


}
