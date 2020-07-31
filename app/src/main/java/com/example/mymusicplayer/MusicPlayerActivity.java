package com.example.mymusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MusicPlayerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player_layout);

        ImageButton back_btn = findViewById(R.id.back_btn);
        ImageView album_cover = findViewById(R.id.song_cover_iv);
        TextView song_name = findViewById(R.id.song_name_tv);
        TextView author_name = findViewById(R.id.song_author_tv);

        Song song = (Song) getIntent().getSerializableExtra("song");
        song_name.setText(song.getName());
        author_name.setText(song.getAuthor_name());
        Glide.with(this).load(song.getAlbum_cover()).into(album_cover);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
