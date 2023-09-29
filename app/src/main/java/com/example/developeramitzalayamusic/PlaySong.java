package com.example.developeramitzalayamusic;

import static com.example.developeramitzalayamusic.R.drawable.icon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import SongPakage.SongModel;

public class PlaySong extends AppCompatActivity {
    ImageView playsong, previewsong, nextsong, repeatemode,playback;
    TextView title, currentduration, totalduration;
    RoundedImageView songimg;
    ArrayList<SongModel>songs= new ArrayList<>();
    MediaPlayer mediaPlayer;
    ConstraintLayout playerview;
    Handler handler;
    int position;
    SeekBar seekBar;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        getSupportActionBar().hide();
        title = findViewById(R.id.title);
        currentduration = findViewById(R.id.currentduration);
        totalduration = findViewById(R.id.totalduration);
        songimg = findViewById(R.id.song_img);
        repeatemode = findViewById(R.id.repeate);
        nextsong = findViewById(R.id.nextsong);
        previewsong = findViewById(R.id.preview_songs);
        playsong = findViewById(R.id.playsong);
        seekBar = findViewById(R.id.seekBar);
        playerview = findViewById(R.id.playerView);

        playback = findViewById(R.id.playback);
        handler = new Handler();



        mediaPlayer = new MediaPlayer();



        // get data from preview activity
        Intent intent = getIntent();
        songs = (ArrayList<SongModel>) getIntent().getSerializableExtra("songs");
        position = intent.getIntExtra("position", 0);



        String Songname = intent.getStringExtra("Song_Name");
        String SongDuration = intent.getStringExtra("Duration");
        String Songimage = intent.getStringExtra("Song_image");



        // set songname
        title.setText(Songname);
        // set duration
        totalduration.setText(SongDuration(Integer.parseInt(SongDuration)));

        // set album

       try {
           if (songimg != null) {
               songimg.setImageURI(Uri.parse(Songimage));

           }
       }catch (Exception e){
       }
        if(songimg.getDrawable() == null) {
              songimg.setImageResource(icon);
        }




        //  Current Song play
        String songPath = songs.get(position).getPath();

        Uri uri = Uri.parse(songPath);

        if (songPath != null) {
            mediaPlayer = MediaPlayer.create(this, uri);
            if (mediaPlayer != null) {
                title.setSelected(true);
                mediaPlayer.start();
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
            }

            assert mediaPlayer != null;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    position %= songs.size();

                    if (position<(songs.size()-1)){
                             position= position+1;
                             // set autoplay song
                             String Songpath = songs.get(position).getPath();

                             Uri nextsong = Uri.parse(Songpath);
                             if (Songpath!= null) {
                                 mediaPlayer = MediaPlayer.create(getApplicationContext(), nextsong);
                                 mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                     @Override
                                     public void onPrepared(MediaPlayer mp) {
                                         mediaPlayer.setOnCompletionListener(this::onPrepared);
                                         title.setSelected(true);
                                         mediaPlayer.start();
                                         seekBar.setProgress(0);
                                         handler.postDelayed(runnable, 700);
                                         seekBar.setMax(mediaPlayer.getDuration());
                                         playsong.setImageResource(R.drawable.ic_baseline_pause_circle_24);

                                     }
                                 });
                             }

                             // set autoplay album
                             String autonextalbum = songs.get(position).getTitleimg();
                             try {
                                 if (songimg != null) {
                                     songimg.setImageURI(Uri.parse(autonextalbum));


                                 }
                             }catch (Exception e){}


                             if (songimg.getDrawable() == null) {
                                 songimg.setImageResource(icon);


                             }

                             // auto play song  name
                             String autoSongName = songs.get(position).getTitle();
                             title.setText(autoSongName);

                             //  auto play song duration
                             String autoSongDuration = songs.get(position).getDuration();
                             totalduration.setText(SongDuration(Integer.parseInt(autoSongDuration)));

                         }else {
                             position = 0;
                         }

                }
            });



        }


        // pause button on click listener
        playsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    title.setSelected(false);
                    playsong.setImageResource(R.drawable.ic_baseline_play_circle_24);
                    mediaPlayer.pause();

                } else {
                    playsong.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                    title.setSelected(true);
                    mediaPlayer.start();
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentduration.setText(SongDuration(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(runnable,700);

                }

            }
        });


        // next play song
        nextsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!= songs.size() - 1) {
                    position = position + 1;

                } else {
                    position = 0;
                }
                // set album in next song

                String nextalbum = songs.get(position).getTitleimg();
                  try {
                      if (songimg != null) {
                          songimg.setImageURI(Uri.parse(nextalbum));
                      }
                  }catch (Exception e){}


                if (songimg.getDrawable() == null) {
                    songimg.setImageResource(icon);

                }
                // play next song
                String Songpath = songs.get(position).getPath();

                Uri nextsong = Uri.parse(Songpath);
                if (Songpath!= null){
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), nextsong);
                    if (mediaPlayer!= null){
                        mediaPlayer.start();
                        title.setSelected(true);
                        mediaPlayer.setScreenOnWhilePlaying(true);
                        seekBar.setProgress(0);
                        handler.postDelayed(runnable,700);
                        seekBar.setMax(mediaPlayer.getDuration());
                        playsong.setImageResource(R.drawable.ic_baseline_pause_circle_24);
                    }



                }


                // change name
                String nextSongName = songs.get(position).getTitle();
                title.setText(nextSongName);

                // change duration
                String nextSongDuration = songs.get(position).getDuration();
                totalduration.setText(SongDuration(Integer.parseInt(nextSongDuration)));


            }
        });

        // set onclicklistener on preview button

        previewsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() + 1) {
                    position = position - 1;
                } else {
                    position = 0;
                }

                String Songpath = songs.get(position).getPath();

                Uri previewsong = Uri.parse(Songpath);
                if (Songpath!= null){
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), previewsong);
                    if (mediaPlayer!= null){
                        // play song preview
                        mediaPlayer.start();
                        title.setSelected(true);
                        seekBar.setProgress(0);
                        handler.postDelayed(runnable,700);
                        seekBar.setMax(mediaPlayer.getDuration());
                        playsong.setImageResource(R.drawable.ic_baseline_pause_circle_24);


                    }

                }



                // change name
                String perviewSongName = songs.get(position).getTitle();
                title.setText(perviewSongName);


                // change duration
                String previewSongDuration = songs.get(position).getDuration();
                totalduration.setText(SongDuration(Integer.parseInt(previewSongDuration)));

                // Change Album
                String previewalbum = songs.get(position).getTitleimg();

                    if (songimg != null) {
                        songimg.setImageURI(Uri.parse(previewalbum));
                    }


                if (songimg.getDrawable() == null) {
                    songimg.setImageResource(icon);

                }

            }
            
        });

        // repeat songs

        repeatemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isLooping()){
                    repeatemode.setImageResource(R.drawable.ic_baseline_repeat_one_on_24);
                    Toast.makeText(PlaySong.this, "Repeat Mode On", Toast.LENGTH_SHORT).show();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    seekBar.setProgress(0);
                }else {
                    repeatemode.setImageResource(R.drawable.ic_baseline_repeat_24);
                    Toast.makeText(PlaySong.this, "Repeat Mode Off", Toast.LENGTH_SHORT).show();
                    mediaPlayer.setLooping(false);
                    mediaPlayer.isPlaying();
                }

            }


        });

         // playback btn
        playback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(PlaySong.this,MainActivity.class);
            startActivity(intent);


            }
        });



        // update our Seekbaar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!= null&& fromUser){
                    mediaPlayer.seekTo(progress);
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
        updateSeekbar();

    }



    private void updateSeekbar() {
        try {
            if (mediaPlayer!= null &&mediaPlayer.isPlaying()){
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                currentduration.setText(SongDuration(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(runnable,700);
            }
        }catch (Exception e){

        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateSeekbar();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private String SongDuration(int totalDuration) {
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