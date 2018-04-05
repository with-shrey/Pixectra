package com.pixectra.app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.pixectra.app.Utils.CartHolder;


/**
 * Created by prashu on 4/4/2018.
 */

public class VideoSelection extends AppCompatActivity {
    VideoView mVideoView;
    Button movetocart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vedioupload);
        Button btn = findViewById(R.id.uploadvedio);
        movetocart = findViewById(R.id.countinue_video_cart);
        movetocart.setVisibility(View.GONE);
        mVideoView = findViewById(R.id.videoView);
        movetocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoSelection.this, Checkout.class);
                startActivity(intent);
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Video File"), 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    CartHolder.getInstance().getVideo().add(new Pair<>(CartHolder.getInstance().getDetails
                            (getIntent().getStringExtra("key")), selectedImageUri));
                    movetocart.setVisibility(View.VISIBLE);
                    mVideoView.setVisibility(View.VISIBLE);
                    mVideoView.setVideoURI(selectedImageUri);
                    mVideoView.start();
                    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                            mediaPlayer.seekTo(0);
                            mediaPlayer.start();
                        }
                    });

                }
            }
        }
    }
}



