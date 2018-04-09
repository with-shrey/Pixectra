package com.pixectra.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixectra.app.Models.Banner;
import com.pixectra.app.Utils.GlideHelper;
import com.pixectra.app.Utils.LogManager;

import java.util.ArrayList;
import java.util.Locale;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by yugan on 1/10/2018.
 */

public class ReferAndEarnFragment extends Fragment {

    BranchUniversalObject branchUniversalObject;
    ReferBanners mReferBanners;
    ArrayList<Banner> mBannerArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.refer_earn_fragment, null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("CommonData").child("Banner");
        //<--setting up recycler view
        mBannerArrayList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.refer_earn_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReferBanners = new ReferBanners();
        recyclerView.setAdapter(mReferBanners);
        final TextView remaining = view.findViewById(R.id.subscription_remaining);

        Branch.getInstance().loadRewards(new Branch.BranchReferralStateChangedListener() {
            @Override
            public void onStateChanged(boolean changed, BranchError error) {
                remaining.setText(String.format(Locale.getDefault(), "Credits Earned %d", Branch.getInstance().getCredits()));
            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBannerArrayList.clear();
                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    mBannerArrayList.add(temp.getValue(Banner.class));
                }
                mReferBanners.notifyDataSetChanged();
                ref.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                view.findViewById(R.id.onetime_progress).setVisibility(View.GONE);
            }
        });
        ref.keepSynced(true);
        view.findViewById(R.id.share_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                initiateSharing();
            }
        });
        contentLoaded();

        return view;
    }

    // This would be your own method where you've loaded the content for this page
    void contentLoaded() {
        // Initialize a Branch Universal Object for the page the user is viewing
        branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setTitle("Get The App Now")
                .setContentDescription("Download The App ")
                .setContentImageUrl("http://pixectra.com/img/logo.png");


        // Trigger a view on the content for analytics tracking
        branchUniversalObject.registerView();

        // List on Google App Indexing
        branchUniversalObject.listOnGoogleSearch(getActivity());
    }

    // This is the function to handle sharing when a user clicks the share button
    void initiateSharing() {
        // Create your link properties
        // More link properties available at https://dev.branch.io/getting-started/configuring-links/guide/#link-control-parameters
        LinkProperties linkProperties = new LinkProperties()
                .setFeature("sharing");

        // Customize the appearance of your share sheet
        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(getActivity()
                , "Download The App Now And Get Exciting Offers On Sign Up"
                , "Who doesn't love to share memories, bring your memories to life with pixectra's affordable photo printing service")
                .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy link", "Link added to clipboard!")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.INSTAGRAM)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK_MESSENGER);

        // Show the share sheet for the content you want the user to share. A link will be automatically created and put in the message.
        branchUniversalObject.showShareSheet(getActivity(), linkProperties, shareSheetStyle, new Branch.BranchLinkShareListener() {
            @Override
            public void onShareLinkDialogLaunched() {
            }

            @Override
            public void onShareLinkDialogDismissed() {
            }

            @Override
            public void onChannelSelected(String channelName) {
            }

            @Override
            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                LogManager.inviteLinkCreated(FirebaseAuth.getInstance().getCurrentUser().getUid(), sharedLink);
                // The link will be available in sharedLink
            }
        });
    }

    class ReferBanners extends RecyclerView.Adapter<ReferBanners.VH> {

        @NonNull
        @Override
        public ReferBanners.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_viewpager_item_layout, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReferBanners.VH holder, int position) {
            GlideHelper.load(getActivity(), mBannerArrayList.get(position).getImage()
                    , holder.mImageView, holder.progressBar);
        }

        @Override
        public int getItemCount() {
            return mBannerArrayList.size();
        }

        class VH extends RecyclerView.ViewHolder {
            ImageView mImageView;
            ProgressBar progressBar;

            public VH(View itemView) {
                super(itemView);
                itemView.getLayoutParams().height = (int) getActivity().getResources().getDimension(R.dimen.card_height);
                mImageView = itemView.findViewById(R.id.sliding_image);
                progressBar = itemView.findViewById(R.id.banner_loader);
            }
        }
    }

}
