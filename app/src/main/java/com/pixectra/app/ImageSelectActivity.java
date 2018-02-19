package com.pixectra.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pixectra.app.Fragments.ImageFragment;
import com.pixectra.app.Utils.CartHolder;

import java.util.Vector;

public class ImageSelectActivity extends AppCompatActivity {
ViewPager viewPager;
TabLayout tabLayout;
    RecyclerView selectedImages;
    Vector<Bitmap> selectedItems;
    SelectedItemsAdapter selectedItemsAdapter;
    FloatingActionButton save;
    int w;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        DisplayMetrics dm = new DisplayMetrics();
        ImageSelectActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels / 3;
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar3));
        getSupportActionBar().setElevation(0);
        selectedItems = CartHolder.getInstance().getAllImages(getIntent().getStringExtra("key"));
        save = findViewById(R.id.save_images);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CartHolder.getInstance().getSize(getIntent().getStringExtra("key")) == getIntent().getIntExtra("pics", 0)) {
//                    Intent intent=new Intent(ImageSelectActivity.this,Cart.class);
//                    startActivity(intent);
                } else {
                    Toast.makeText(ImageSelectActivity.this, "Please Select "
                                    + (getIntent().getIntExtra("pics", 0)
                                    - CartHolder.getInstance().getSize(getIntent().getStringExtra("key")))
                                    + " More Images"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
        selectedItemsAdapter = new SelectedItemsAdapter();
        viewPager=findViewById(R.id.image_select_pager);
        selectedImages = findViewById(R.id.selected_images_recycler);
        selectedImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectedImages.setAdapter(selectedItemsAdapter);
        if (selectedItems.size() > 0) {
            selectedImages.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            selectedItemsAdapter.notifyDataSetChanged();
        }
        setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout=findViewById(R.id.image_select_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons(tabLayout);
        CartHolder.getInstance().setOnImageChangedListner(new CartHolder.ImageChangedListner() {
            @Override
            public void onImageAdded(Bitmap img, int size) {
                if (size > 0) {
                    save.setVisibility(View.VISIBLE);
                    selectedImages.setVisibility(View.VISIBLE);
                    selectedItems.add(img);
                    selectedItemsAdapter.notifyDataSetChanged();
                }
                if (getSupportActionBar() != null)
                    setToolbarText(CartHolder.getInstance().getSize(getIntent().getStringExtra("key")));
            }

            @Override
            public void onImageDeleted(Bitmap img, int size) {
                selectedItems.remove(img);
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
            public void alreadyPresent(Bitmap img) {
                Toast.makeText(ImageSelectActivity.this, "Image Already Present In Cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void setToolbarText(int count) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Select Images (" + count + "/" + getIntent().getIntExtra("pics", 0) + ")");
    }
    void setTabIcons(TabLayout tabs){
        int[] icons = {R.drawable.device, R.drawable.facebook, R.drawable.instagram, R.drawable.google_photos};
        for (int i=0;i<4;i++)
            tabs.getTabAt(i).setIcon(icons[i]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler_item, parent, false);
            return new SelectedItemsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Glide.with(ImageSelectActivity.this).load(selectedItems.get(position)).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return selectedItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);

                itemView.getLayoutParams().width = (int) ImageSelectActivity.this.getResources().getDimension(R.dimen.selected_images_dimen);
                itemView.getLayoutParams().height = (int) ImageSelectActivity.this.getResources().getDimension(R.dimen.selected_images_dimen);
                image = itemView.findViewById(R.id.ListIcon);
                itemView.findViewById(R.id.image_loading_progress).setVisibility(View.GONE);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                CartHolder.getInstance().removeImage(getIntent().getStringExtra("key"), selectedItems.get(getAdapterPosition()));
            }
        }
    }
}
