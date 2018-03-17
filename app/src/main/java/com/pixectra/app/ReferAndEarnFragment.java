package com.pixectra.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.refer_earn_fragment, null);
//        view.findViewById(R.id.refer_earn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String uid = user.getUid();
//                String link = "http://pixectra.com?user=" + "universal&coupon=DISC10";
//                FirebaseDynamicLinks.getInstance().createDynamicLink()
//                        .setLink(Uri.parse(link))
//                        .setDynamicLinkDomain("v2xqe.app.goo.gl")
//                        .setAndroidParameters(
//                                new DynamicLink.AndroidParameters.Builder("com.pixectra.app")
//                                        .setMinimumVersion(1)
//                                        .build())
////                        .setIosParameters(
////                                new DynamicLink.IosParameters.Builder("com.example.ios")
////                                        .setAppStoreId("123456789")
////                                        .setMinimumVersion("1.0.1")
////                                        .build())
//                        .buildShortDynamicLink()
//                        .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
//                            @Override
//                            public void onSuccess(ShortDynamicLink shortDynamicLink) {
//                                Uri mInvitationUrl = shortDynamicLink.getShortLink();
//                                Log.d("prashu", mInvitationUrl.toString());
//                                Toast.makeText(getActivity(), mInvitationUrl.toString(), Toast.LENGTH_SHORT).show();
//                                String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//                                String subject = String.format("%s wants you to make memories with Pixectra", referrerName);
//                                String invitationLink = mInvitationUrl.toString();
//                                String msg = "the perfect app for you and your memories. The app that helps you free your photos and puts them into a monthly photo book Use my referrer link: "
//                                        + invitationLink;
//                                String msgHtml = String.format("<p>Let's Explore yourself with Pixectra! Use my "
//                                        + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);
//
//                                Intent intent = new Intent(Intent.ACTION_SEND);
//                                intent.setType("text/plain"); // only email apps should handle this
//                                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//                                intent.putExtra(Intent.EXTRA_TEXT, msg);
//                                intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
//                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                                    startActivity(intent);
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//
//            }
//        });
        // Hook up your share button to initiate sharing
        view.findViewById(R.id.refer_earn).setOnClickListener(new View.OnClickListener() {
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
        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(getActivity(), "Check this out!", "Hey friend - I know you'll love this: ")
                .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy link", "Link added to clipboard!")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER);

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
                // The link will be available in sharedLink
            }
        });
    }

}
