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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Adapter.PhotobookRecyclerViewAdapter;
import com.pixectra.app.Models.Product;
import com.pixectra.app.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment {

    //<--dummy titles
    String[] title = {"Kaneki", "Child", "Clothes", "Dummy"};

    RecyclerView mrecyclerview;
    PhotobookRecyclerViewAdapter mposterRecyclerViewAdapter;
    ArrayList<Product> data;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data=new ArrayList<>();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("CommonData").child("PhotoBooks");
        ref.keepSynced(true);
        //<--setting up recycler view
        if (getActivity().findViewById(R.id.recyclerview_poster_subscribe) != null) {
            mrecyclerview = getActivity().findViewById(R.id.recyclerview_poster_subscribe);
            mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            SetupRecyclerview();
        } else {
            Log.d("onViewCreated", "null view");
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot temp:dataSnapshot.getChildren()){
                    data.add(temp.getValue(Product.class));
                }
                view.findViewById(R.id.subscribe_progress).setVisibility(View.GONE);
                mposterRecyclerViewAdapter.notifyDataSetChanged();
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.findViewById(R.id.subscribe_progress).setVisibility(View.GONE);
            }
        });
    }


    private void SetupRecyclerview() {

        mposterRecyclerViewAdapter = new PhotobookRecyclerViewAdapter(getActivity(), R.layout.photobook_recycler_view_card, data);
        mrecyclerview.setAdapter(mposterRecyclerViewAdapter);
    }
}
