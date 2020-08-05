package com.example.mymusicplayer;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {
    MusicService mService;
    boolean mBound = false;

    private ArrayList<Song> songs_list;
    private int position;
    private Song currentSong;

    private ImageView album_cover;
    private TextView song_name;
    private TextView author_name;
    private TextView song_total_duration;
    private TextView song_current_durration;
    private ImageButton back_btn;
    private ImageButton next_btn;
    private ImageButton prev_btn;
    public static ImageButton play_pause_btn;
    private SeekBar seekBarDuration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_layout);

        Log.d("state", "on create now");


        play_pause_btn = findViewById(R.id.play_pause_btn);
        back_btn = findViewById(R.id.back_btn);
        next_btn = findViewById(R.id.next_btn);
        prev_btn = findViewById(R.id.prev_btn);
        album_cover = findViewById(R.id.song_cover_iv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            album_cover.setClipToOutline(true);
        }
        song_name = findViewById(R.id.song_name_tv);
        author_name = findViewById(R.id.song_author_tv);
        song_total_duration = findViewById(R.id.total_duration_tv);
        song_current_durration = findViewById(R.id.current_duration);
        seekBarDuration = findViewById(R.id.song_progress);
    }

    @Override
    protected void onStart() {
        Log.d("state", "on start now");

        final Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

//        position = getIntent().getIntExtra("position", 0);
//        songs_list = getIntent().getParcelableArrayListExtra("songs_list");
//        currentSong = songs_list.get(position);
//        song_name.setText(currentSong.getName());
//        author_name.setText(currentSong.getAuthor_name());
//        Glide.with(this).load(currentSong.getAlbum_cover()).into(album_cover);
//
//

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                unbindService(connection);
                onBackPressed();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setUpSong(true);
                Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
//                service.putExtra("songs_list",songs_list);
                service.putExtra("command", "next");
                startService(service);
                //bindService(service,connection,Context.BIND_AUTO_CREATE);
            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setUpSong(false);
                Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
//                service.putExtra("songs_list",songs_list);
                service.putExtra("command", "prev");
                startService(service);
                //bindService(service,connection,Context.BIND_AUTO_CREATE);
            }
        });

        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(MusicPlayerActivity.this, MusicService.class);
//                service.putExtra("songs_list", songs_list);
                if (MusicService.isRunnig) {
                    service.putExtra("command", "play");
                    startService(service);
                }
                else {
                    service.putExtra("position", MusicService.sPosition);
                    service.putExtra("songs_list", songs_list);
                    service.putExtra("command", "new_instance");
                    startService(service);
                    bindService(service,connection,Context.BIND_AUTO_CREATE);

                }
            }
        });
        super.onStart();

    }

    @Override
    protected void onStop() {

        Log.d("state", "on stop now");

        mBound = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("state", "on destroy now");
        super.onDestroy();
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (mService != null) {
                mService.setPlayPauseBtn(play_pause_btn);
                mService.setSongName(song_name);
                mService.setSongAuthor(author_name);
                mService.setSongCover(album_cover);
                mService.setSongView(song_name, author_name, album_cover);
                songs_list = mService.getSongs();

                unbindService(connection);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mBound = false;
        }
    };


    public void setUpSong(boolean is_next) {
        mService.playSong(is_next);
        mService.setSongView(song_name, author_name, album_cover);
    }

    public String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        if (min < 10)
            timeLabel += "0" + min + ":";
        else timeLabel += min + ":";
        if (sec < 10)
            timeLabel += "0" + sec;
        else timeLabel += sec;
        return timeLabel;

    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(MusicPlayerActivity.this,MainActivity.class );
//        startActivity(intent);
//
//    }
}
