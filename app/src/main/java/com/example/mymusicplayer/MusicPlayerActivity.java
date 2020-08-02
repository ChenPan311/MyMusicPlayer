package com.example.mymusicplayer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MusicPlayerActivity extends AppCompatActivity{
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            album_cover.setClipToOutline(true);
        }
        song_name = findViewById(R.id.song_name_tv);
        author_name = findViewById(R.id.song_author_tv);

        position = getIntent().getIntExtra("position", 0);
        songs_list = getIntent().getParcelableArrayListExtra("songs_list");
        currentSong = songs_list.get(position);
        song_name.setText(currentSong.getName());
        author_name.setText(currentSong.getAuthor_name());
        Glide.with(this).load(currentSong.getAlbum_cover()).into(album_cover);

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
                Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
//                service.putExtra("songs_list",songs_list);
                service.putExtra("command", "next");
                startService(service);
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpSong(false);
                Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
//                service.putExtra("songs_list",songs_list);
                service.putExtra("command", "prev");
                startService(service);
            }
        });

        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
//                service.putExtra("songs_list", songs_list);
                service.putExtra("command", "play");
                startService(service);
            }
        });
    }

    public void setUpSong(boolean is_next) {
        if (is_next) {
            if (position == songs_list.size() - 1)
                position = 0;
            else position++;
        } else {
            if (position == 0)
                position = songs_list.size() - 1;
            else position--;
        }
        currentSong = songs_list.get(position);
        song_name.setText(currentSong.getName());
        author_name.setText(currentSong.getAuthor_name());
        Glide.with(this).load(currentSong.getAlbum_cover()).into(album_cover);
    }
    

    //
//    @Override
//    protected void onResume() {
//        Log.d("resume:","On Resume!");
//        currentSong = songs_list.get(position);
//        song_name.setText(currentSong.getName());
//        author_name.setText(currentSong.getAuthor_name());
//        Glide.with(this).load(currentSong.getAlbum_cover()).into(album_cover);
//        super.onResume();
//    }
}
