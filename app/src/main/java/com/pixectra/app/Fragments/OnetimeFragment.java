package com.pixectra.app.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class OnetimeFragment extends Fragment {
    private String type;

  //<--dummy titles
  private ArrayList<Product> data;
    private RecyclerView mrecyclerview;
    private PhotobookRecyclerViewAdapter mposterRecyclerViewAdapter;

    public static Fragment newInstance(String s) {
        OnetimeFragment fragment = new OnetimeFragment();
        Bundle args = new Bundle();
        args.putString("category", s);
        fragment.setArguments(args);
        return fragment;
    }

    public OnetimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("category");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onetime, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data=new ArrayList<>();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("CommonData").child("PhotoBooks").child(type);
        Toast.makeText(getActivity(), ref.toString(), Toast.LENGTH_SHORT).show();
        ref.keepSynced(true);
        //<--setting up recycler view
        mrecyclerview = getActivity().findViewById(R.id.recyclerview_poster_onetime);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //SetupRecyclerview();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot temp:dataSnapshot.getChildren()){
                    Product product = temp.getValue(Product.class);
                    data.add(product);
                }
                view.findViewById(R.id.onetime_progress).setVisibility(View.GONE);
                SetupRecyclerview();
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.findViewById(R.id.onetime_progress).setVisibility(View.GONE);
            }
        });
    }


    private void SetupRecyclerview(){


        mposterRecyclerViewAdapter = new PhotobookRecyclerViewAdapter(getActivity(), R.layout.photobook_recycler_view_card, data,0);
        mrecyclerview.setAdapter(mposterRecyclerViewAdapter);
    }


}
