package com.pixectra.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.pixectra.app.Adapter.PhotobookRecyclerViewAdapter;

public class PosterActivity extends AppCompatActivity {

    String[] activityTitles = {"Flipbook", "PostCard", "Polaroids", "Photos","Posters"} ;

    //<-- Dummy image names
                              // -->

    String[] title = {"Kaneki", "Child", "Clothes", "Dummy"} ;



    TextView title_page;
    RecyclerView mrecyclerview;
    PhotobookRecyclerViewAdapter mposterRecyclerViewAdapter;




//<---------------Dummy image titles--------------------------------------------------------------------------


    String[] image_links = {"http://media.comicbook.com/2017/02/tokyoghoulseason3-234960-1280x0.jpg",
            "http://www.dummymag.com//media/img/dummy-logo.png",
            "http://www.essentialbaby.com.au/content/dam/images/2/8/7/r/0/image.related.articleLeadwide.620x349.36m36.png/1467682109866.jpg",
            "https://www.wikihow.com/images/thumb/c/c8/Make-a-Halloween-Dummy-Step-3.jpg/aid7602-v4-728px-Make-a-Halloween-Dummy-Step-3.jpg"};


    //------------------------------------------------------------------------------------------------>



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        title_page = findViewById(R.id.title_poster_activity);
        mrecyclerview = findViewById(R.id.poster_activity_recyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(PosterActivity.this));
        setuprecyclerview();
        //Type Of Activity
        int type=getIntent().getIntExtra("type",1);
        title_page.setText(activityTitles[type-1]);
    }


    /**
     * Method To Setup Recycler View
     */
    private void setuprecyclerview() {

        mposterRecyclerViewAdapter = new PhotobookRecyclerViewAdapter(PosterActivity.this, R.layout.photobook_recycler_view_card, image_links, title);
        mrecyclerview.setAdapter(mposterRecyclerViewAdapter);
    }
}

