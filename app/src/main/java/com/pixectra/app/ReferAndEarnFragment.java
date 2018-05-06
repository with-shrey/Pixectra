package com.pixectra.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixectra.app.Models.Banner;
import com.pixectra.app.Utils.LogManager;
import com.pixectra.app.Utils.SessionHelper;

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
    ArrayList<Banner> mBannerArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.refer_earn_fragment, null);
        //<--setting up recycler view
        mBannerArrayList = new ArrayList<>();
        final TextView remaining = view.findViewById(R.id.subscription_remaining);

        Branch.getInstance().loadRewards(new Branch.BranchReferralStateChangedListener() {
            @Override
            public void onStateChanged(boolean changed, BranchError error) {
                remaining.setText(String.format(Locale.getDefault(), "Credits Earned %d", Branch.getInstance().getCredits()));
            }
        });
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
                .setCanonicalIdentifier(new SessionHelper(getActivity()).getUid())
                .setTitle("Get The App Now")
                .setContentDescription("We Are Pixectra And We Help You Bring Your Memories To Life ")
                .setContentImageUrl("https://cdn.branch.io/branch-assets/1524259015510-og_image.png");


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
                , "Who doesn't love to share memories, bring your memories to life with pixectra's affordable photo printing service" +
                "\nDon't Minimize App Before Signing Up To Successfully Get Credit Benefits On First Launch")
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
                LogManager.inviteLinkCreated(new SessionHelper(getActivity()).getUid(), sharedLink);
                // The link will be available in sharedLink
            }
        });
    }

}
