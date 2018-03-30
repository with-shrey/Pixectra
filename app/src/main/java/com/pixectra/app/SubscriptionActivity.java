package com.pixectra.app;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pixectra.app.Adapter.SubscriptionAdaptor;
import com.pixectra.app.Models.SubscriptionDetails;

import java.util.ArrayList;

public class SubscriptionActivity extends AppCompatActivity  {


    float x1, x2;
    final int MIN_DISTANCE = 150;
    private float itemWidth;
    private float padding;
    private float firstItemWidth;
    private float allPixels;
    private int mLastPosition;
    int NUM_ITEMS;
    private static final String BUNDLE_LIST_PIXELS = "allPixels";
    LinearLayout linearLayout;

    RecyclerView recyclerView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        linearLayout = findViewById(R.id.layoutOne);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        itemWidth = 300;
        padding = (size.x - itemWidth) / 2;
        firstItemWidth = 300;
        //getResources().getDimension(R.dimen.padding_item_width);

        allPixels = 0;




// add a background color to the recyclerview
        recyclerView = findViewById(R.id.SubscriptionRecyclerView);

        final ArrayList<SubscriptionDetails> data = new ArrayList<>();

        SubscriptionDetails subscriptionDetails =
                new SubscriptionDetails("Title", "Sub-Title", "> First item<br/>"+
                        "> Second item<br/>"+
                        "> Third item");
                // new SubscriptionDetails("dummy21", "dummy22", "dummy23")


        SubscriptionDetails subscriptionDetails2 =
                // new SubscriptionDetails("Dummy 1", "dummy2", "dummy3")
                new SubscriptionDetails("Title2", "Sub-Title2", "> First item1<br/>"+
                        "> Second item2<br/>"+
                        "> Third item3");
        data.add(subscriptionDetails);
        data.add(subscriptionDetails2);


        NUM_ITEMS = 5;
        //data.size();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubscriptionActivity.this,
                LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        //  recyclerView.setAdapter(new SubscriptionAdaptor(data,SubscriptionActivity.this, SubscriptionActivity.this));


        recyclerView.setAdapter(new SubscriptionAdaptor(data, SubscriptionActivity.this, SubscriptionActivity.this));

        //  recyclerView.scrollToPosition();

     /*   recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                synchronized (this) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        calculatePositionAndScroll(recyclerView);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                allPixels += dx;
            }
        });

        recyclerView.setAdapter(new SubscriptionAdaptor(data,SubscriptionActivity.this, SubscriptionActivity.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        final RecyclerView recyclerView = findViewById(R.id.SubscriptionRecyclerView);

        ViewTreeObserver vto = recyclerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                calculatePositionAndScroll(recyclerView);
            }
        });
    }

    private void calculatePositionAndScroll(RecyclerView recyclerView) {
        int expectedPosition = Math.round((allPixels + padding - firstItemWidth) / itemWidth);
        // Special cases for the padding items
        if (expectedPosition == -1) {
            expectedPosition = 0;
        } else if (expectedPosition >= recyclerView.getAdapter().getItemCount() - 2) {
            expectedPosition--;
        }
        scrollListToPosition(recyclerView, expectedPosition);
    }

    private void scrollListToPosition(RecyclerView recyclerView, int expectedPosition) {
        float targetScrollPos = expectedPosition * itemWidth + firstItemWidth - padding;
        float missingPx = targetScrollPos - allPixels;
        if (missingPx != 0) {
            recyclerView.smoothScrollBy((int) missingPx, 0);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        allPixels = savedInstanceState.getFloat(BUNDLE_LIST_PIXELS);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat(BUNDLE_LIST_PIXELS, allPixels);
*/


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                    }


                    // Right to left swipe action
                    else {
                        Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}
