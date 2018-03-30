package com.pixectra.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixectra.app.Utils.GlideHelper;
import com.pixectra.app.Utils.SessionHelper;

import java.util.HashMap;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * Created by Yugansh on 1/10/2018.
 */

public class UserProfileFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
ImageView profilePic;
    TextView userName, remaining;
CardView address;
    CardView linkAccount, account, order, subscription;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        HashMap<String,String> data=new SessionHelper(getActivity()).getUserDetails();
        profilePic=view.findViewById(R.id.user_image);
        userName=view.findViewById(R.id.user_name);
        address=view.findViewById(R.id.address_card_view);
        linkAccount=view.findViewById(R.id.link_account_card_view);
        account = view.findViewById(R.id.account_card_view);
        order = view.findViewById(R.id.order_card_view);
        subscription = view.findViewById(R.id.subscription_card_view);
        remaining = view.findViewById(R.id.subscription_remaining);

        Branch.getInstance().loadRewards(new Branch.BranchReferralStateChangedListener() {
            @Override
            public void onStateChanged(boolean changed, BranchError error) {
                remaining.setText("Credits Earned " + Branch.getInstance().getCredits());
            }
        });


        if (data.get(SessionHelper.User_Image) != null)
            GlideHelper.load(getActivity(), Uri.parse(data.get(SessionHelper.User_Image)), profilePic
                    , (ProgressBar) view.findViewById(R.id.progress_user_img));
        userName.setText(data.get(SessionHelper.User_Name));
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),SelectAddressActivity.class);
                startActivity(intent);
            }
        });
        linkAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),LinkedAccounts.class);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), orders_placed.class);
                startActivity(intent);
            }
        });
        subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), subscription_list.class);
                startActivity(intent);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
