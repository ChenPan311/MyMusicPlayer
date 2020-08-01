package com.example.mymusicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {
    private ArrayList<Song> songs_list;
    private int position;
    private Song currentSong;
    private ImageView album_cover;
    private TextView song_name;
    private TextView author_name;

    //    MediaPlayer mp = new MediaPlayer();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_layout);

        final ImageButton play_pause_btn = findViewById(R.id.play_pause_btn);
        ImageButton back_btn = findViewById(R.id.back_btn);
        ImageButton next_btn = findViewById(R.id.next_btn);
        ImageButton prev_btn = findViewById(R.id.prev_btn);
        album_cover = findViewById(R.id.song_cover_iv);
        song_name = findViewById(R.id.song_name_tv);
        author_name = findViewById(R.id.song_author_tv);

        position = getIntent().getIntExtra("position",0);
        songs_list = getIntent().getParcelableArrayListExtra("songs_list");
        currentSong = songs_list.get(position);
        song_name.setText(currentSong.getName());
        author_name.setText(currentSong.getAuthor_name());
        Glide.with(this).load(currentSong.getAlbum_cover()).into(album_cover);
//        if(mp.isPlaying())
//            mp.stop();
//        mp.reset();
//        try {
//            mp.setDataSource(song.getSong_link());
//            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mp.prepareAsync();
//            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.start();
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mp.release();
                onBackPressed();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSong(true);
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSong(false);
            }
        });

//        play_pause_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               if(mp.isPlaying())
//                   mp.pause();
//               else
//                   mp.start();
//            }
//        });

    }

    public void setUpSong(boolean is_next){
        if(is_next){
            if(position==songs_list.size()-1)
                position=0;
            else position++;
        } else {
            if(position==0)
                position = songs_list.size()-1;
            else position--;
        }
        currentSong=songs_list.get(position);
        song_name.setText(currentSong.getName());
        author_name.setText(currentSong.getAuthor_name());
        Glide.with(this).load(currentSong.getAlbum_cover()).into(album_cover);
    }

}
