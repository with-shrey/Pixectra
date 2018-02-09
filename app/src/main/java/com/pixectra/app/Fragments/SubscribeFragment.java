package com.pixectra.app.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixectra.app.Adapter.PhotobookRecyclerViewAdapter;
import com.pixectra.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment {

    //<--dummy titles
    String[] title = {"Kaneki", "Child", "Clothes", "Dummy"};

    RecyclerView mrecyclerview;
    PhotobookRecyclerViewAdapter mposterRecyclerViewAdapter;

    //<--dummy image links
    String[] image_links = {"http://media.comicbook.com/2017/02/tokyoghoulseason3-234960-1280x0.jpg",
            "http://www.dummymag.com//media/img/dummy-logo.png",
            "http://www.essentialbaby.com.au/content/dam/images/2/8/7/r/0/image.related.articleLeadwide.620x349.36m36.png/1467682109866.jpg",
            "https://www.wikihow.com/images/thumb/c/c8/Make-a-Halloween-Dummy-Step-3.jpg/aid7602-v4-728px-Make-a-Halloween-Dummy-Step-3.jpg"};


    public SubscribeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscribe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //<--setting up recycler view
        if (getActivity().findViewById(R.id.recyclerview_poster_subscribe) != null) {
            mrecyclerview = getActivity().findViewById(R.id.recyclerview_poster_subscribe);
            mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            SetupRecyclerview();
        } else {
            Log.d("onViewCreated", "null view");
        }

    }


    private void SetupRecyclerview() {

        mposterRecyclerViewAdapter = new PhotobookRecyclerViewAdapter(getActivity(), R.layout.photobook_recycler_view_card, image_links, title);
        mrecyclerview.setAdapter(mposterRecyclerViewAdapter);
    }
}
