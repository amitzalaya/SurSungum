package com.example.developeramitzalayamusic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import VideoPaakage.VideoModel;

public class PlayVideo extends AppCompatActivity {
    TextView videotitle, cvd, ced,zoomcount;
    RelativeLayout zoomlayout;
    VideoView videoView;
    SeekBar vseekbar;
    Handler handler;
    ImageView lockscreen, previewvideo, playvideo, nextvideo, unlockscreen, rotation,forwordbtn,backwordbtn;
    ArrayList<VideoModel> videos = new ArrayList<>();
    int position;
    boolean isOpen = false;
    int MIN_WIDTH;
    private float scalefactor=1.0f;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    DisplayMetrics dm;


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        MIN_WIDTH = dm.widthPixels;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        videotitle = findViewById(R.id.videoTitle);
        cvd = findViewById(R.id.cvDuration);
        ced = findViewById(R.id.cveduration);
        lockscreen = findViewById(R.id.lockscreen);
        playvideo = findViewById(R.id.playvideo);
        previewvideo = findViewById(R.id.previewvideo);
        nextvideo = findViewById(R.id.nextvideo);
        videoView = findViewById(R.id.videoView);
        zoomlayout = findViewById(R.id.zoomlayout);
        unlockscreen = findViewById(R.id.lockbutton);
        rotation = findViewById(R.id.rotetion);
        vseekbar = findViewById(R.id.vseekBar);
        zoomcount = findViewById(R.id.zoomCount);
        forwordbtn = findViewById(R.id.forwordbtn);
        backwordbtn = findViewById(R.id.backwordbtn);
        handler = new Handler();

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        MIN_WIDTH = dm.widthPixels;

        mScaleGestureDetector = new ScaleGestureDetector(this, new MyScaleGestureListener());
        mGestureDetector = new GestureDetector(this, new MySimpleOnGestureListener());

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                mScaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });


        forwordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition()+ 30000);
            }
        });
        backwordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition()-10000  );


            }
        });








        zoomlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    hideControls();
                    isOpen = false;
                } else {
                    hideShowControls();
                    isOpen = true;
                }
            }
        });

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        videos = getIntent().getExtras().getParcelableArrayList("videos");

        String name = intent.getStringExtra("Video_Name");
        String duration = intent.getStringExtra("Video_Duration");

        // set name video

        videotitle.setText(name);

        // set duration
        ced.setText(getDuration(Integer.parseInt(duration)));

        // play current video
        String path = videos.get(position).getId();
        Uri uri = Uri.parse(path);
        vseekbar.setProgress(0);
        videoView.setVideoPath(String.valueOf(uri));
        videoView.start();


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (position != videos.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                // set name video
                String autoplayvideoName=videos.get(position).getTitle();
                videotitle.setText(autoplayvideoName);

                // set duration
                ced.setText(getDuration(Integer.parseInt(duration)));

                // play current video
                String path = videos.get(position).getId();
                Uri uri = Uri.parse(path);
                vseekbar.setProgress(0);
                videoView.setVideoPath(String.valueOf(uri));
                videoView.start();

            }
        });

            // preview button seton click listener
            previewvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playvideo.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                    videoView.pause();
                    if (position != videos.size() + 1) {
                        position = position - 1;
                    } else {
                        position = 0;
                    }
                    // preview video name
                    String previewvideoname = videos.get(position).getTitle();
                    videotitle.setText(previewvideoname);

                    // preview duration video
                    String prevideoduration = videos.get(position).getDuration();
                    ced.setText(getDuration(Integer.parseInt(prevideoduration)));
                    // preview play video
                    String path = videos.get(position).getId();
                    Uri uri = Uri.parse(path);
                    videoView.setVideoPath(String.valueOf(uri));
                    handler.postDelayed(updateVideoTime, 700);
                    vseekbar.setProgress(0);
                    videoView.requestFocus();
                    videoView.start();


                }
            });

            // next button on click listener
            nextvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playvideo.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                    videoView.pause();
                    if (position != videos.size() - 1) {
                        position = position + 1;
                    } else {
                        position = 0;
                    }
                    // next video name
                    String nextvideoname = videos.get(position).getTitle();
                    videotitle.setText(nextvideoname);

                    // next duration video
                    String nextvideoduration = videos.get(position).getDuration();
                    ced.setText(getDuration(Integer.parseInt(nextvideoduration)));
                    // next play video
                    String path = videos.get(position).getId();
                    Uri uri = Uri.parse(path);
                    videoView.setVideoPath(String.valueOf(uri));
                    handler.postDelayed(updateVideoTime, 700);
                    vseekbar.setProgress(0);
                    videoView.requestFocus();
                    videoView.start();


                }
            });


        // play button set on click listener
        playvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    playvideo.setImageResource(R.drawable.ic_baseline_play_circle_24);
                    videoView.pause();
                } else {
                    playvideo.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                    videoView.start();
                }

            }
        });

        // lockscreen set on click listener

        lockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unlockscreen.setVisibility(View.VISIBLE);
                hideControls();


            }
        });
        unlockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unlockscreen.setVisibility(View.INVISIBLE);
                hideShowControls();


            }
        });

        // screen rotation
        rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oreientation = getResources().getConfiguration().orientation;
                if (oreientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (oreientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }

            }
        });


        // update video seekBar

        vseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (videoView != null && fromUser) {
                    videoView.seekTo(progress);
                    seekBar.setProgress(progress);
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vseekbar.setProgress(0);
                vseekbar.setMax(videoView.getDuration());
                handler.postDelayed(updateVideoTime, 700);

            }
        });


    }
    private class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {


    }

    private class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // scale our video view

            scalefactor*= detector.getScaleFactor();
            scalefactor = Math.max(0.5f,Math.min(scalefactor,6.0f));
            videoView.setScaleX(scalefactor);
            videoView.setScaleY(scalefactor);
            zoomcount.setVisibility(View.VISIBLE);
            int percent = (int)(scalefactor*100);
            zoomcount.setText(" "+ percent +"%");




            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }

    }







    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            long currentPosition = videoView.getCurrentPosition();
            cvd.setText(milliSecondsToTimer(Math.toIntExact(currentPosition)));
            vseekbar.setProgress((int) currentPosition);
            handler.postDelayed(this, 100);
        }
    };

    private String milliSecondsToTimer(int currentPosition) {
        String currentdurationnumber;

        int hours = currentPosition/(1000*60*60);
        int minuat = (currentPosition%(1000*60*60))/(1000*60);
        int seconds = ((currentPosition%(1000*60*60)%(1000*60*60)))%(1000*60)/1000;

        if (hours<1){
            currentdurationnumber= String.format("%02d:%02d",minuat,seconds);
        }
        else {
            currentdurationnumber= String.format("%1d:%02d:%02d",hours,minuat,seconds);

        }
        return currentdurationnumber;
    }

    //hide controls
    @SuppressLint("ObsoleteSdkInt")
    private void hideControls(){
        videotitle.setVisibility(View.GONE);
        vseekbar.setVisibility(View.GONE);
        cvd.setVisibility(View.GONE);
        ced.setVisibility(View.GONE);
        lockscreen.setVisibility(View.GONE);
        playvideo.setVisibility(View.GONE);
        previewvideo.setVisibility(View.GONE);
        nextvideo.setVisibility(View.GONE);
        rotation.setVisibility(View.GONE);
        forwordbtn.setVisibility(View.GONE);
        backwordbtn.setVisibility(View.GONE);


        // hide navigation and status
        final Window window = this.getWindow();
        if (window == null){
            return;
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final View decorview = window.getDecorView();
        if (decorview!= null){
            int uiopation = decorview.getSystemUiVisibility();

            if (Build.VERSION.SDK_INT >=14){
                uiopation &= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >=16){
                uiopation &= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            if (Build.VERSION.SDK_INT >=19){
                uiopation &= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorview.setSystemUiVisibility(uiopation);
        }
    }
    private void hideShowControls(){
        videotitle.setVisibility(View.VISIBLE);
        vseekbar.setVisibility(View.VISIBLE);
        cvd.setVisibility(View.VISIBLE);
        ced.setVisibility(View.VISIBLE);
        lockscreen.setVisibility(View.VISIBLE);
        playvideo.setVisibility(View.VISIBLE);
        previewvideo.setVisibility(View.VISIBLE);
        nextvideo.setVisibility(View.VISIBLE);
        rotation.setVisibility(View.VISIBLE);
        forwordbtn.setVisibility(View.VISIBLE);
        backwordbtn.setVisibility(View.VISIBLE);


        // show navigation and status
        final Window window = this.getWindow();
        if (window!= null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final View decorview = window.getDecorView();
        if (decorview!= null){
            int uiopation = decorview.getSystemUiVisibility();

            if (Build.VERSION.SDK_INT >=14){
                uiopation &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if (Build.VERSION.SDK_INT >=16){
                uiopation &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            if (Build.VERSION.SDK_INT >=19){
                uiopation &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorview.setSystemUiVisibility(uiopation);
        }
    }


    // duration
    private String getDuration(int totalDuration){
        String totalDurationnumber;

        int hours = totalDuration/(1000*60*60);
        int minuat = (totalDuration%(1000*60*60))/(1000*60);
        int seconds = ((totalDuration%(1000*60*60)%(1000*60*60)))%(1000*60)/1000;

        if (hours<1){
            totalDurationnumber= String.format("%02d:%02d",minuat,seconds);
        }
        else {
            totalDurationnumber= String.format("%1d:%02d:%02d",hours,minuat,seconds);

        }
        return totalDurationnumber;


    }


}