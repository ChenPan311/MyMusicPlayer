package com.example.mymusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recycler_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<Song> songs = new ArrayList<>();
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                300,
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                340,
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "http://web.unec.edu.az/uploads/dletrack/media/19257-twenty-one-pilots-stressed-out.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                550,
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "http://www.filefactory.com/file/44ulwz4wra7/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                400,
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "http://www.filefactory.com/file/5h1tdbdizxrj/Avicii_-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                350,
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "http://www.filefactory.com/file/5bbfhnu5fodr/NF_%u2013%20Let%20You%20Down%20%uD83C%uDFB5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                300,
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                340,
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "http://web.unec.edu.az/uploads/dletrack/media/19257-twenty-one-pilots-stressed-out.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                550,
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "http://www.filefactory.com/file/44ulwz4wra7/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                400,
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "http://www.filefactory.com/file/5h1tdbdizxrj/Avicii_-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                350,
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "http://www.filefactory.com/file/5bbfhnu5fodr/NF_%u2013%20Let%20You%20Down%20%uD83C%uDFB5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                300,
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                340,
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "http://web.unec.edu.az/uploads/dletrack/media/19257-twenty-one-pilots-stressed-out.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                550,
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "http://www.filefactory.com/file/44ulwz4wra7/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                400,
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "http://www.filefactory.com/file/5h1tdbdizxrj/Avicii_-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                350,
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "http://www.filefactory.com/file/5bbfhnu5fodr/NF_%u2013%20Let%20You%20Down%20%uD83C%uDFB5.mp3"));

        final SongsAdapter songsAdapter = new SongsAdapter(this,songs);
        songsAdapter.setListener(new SongsAdapter.MySongListener() {
            @Override
            public void OnSongClicked(View view, int position) {
                Intent intent = new Intent(MainActivity.this,MusicPlayerActivity.class);
                Song song = songs.get(position);
                intent.putExtra("song",song);
                Pair pair = new Pair<View,String>(view.findViewById(R.id.song_cover_iv),"coverTrans");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pair);
                    startActivity(intent,options.toBundle());
                }
            }
        });
        recyclerView.setAdapter(songsAdapter);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int pos_drag = viewHolder.getAdapterPosition();
                int pos_target = target.getAdapterPosition();
                Collections.swap(songs, pos_drag, pos_target);
                songsAdapter.notifyItemMoved(pos_drag, pos_target);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Song song = (Song) songs.get(viewHolder.getAdapterPosition());
                final int lastPos = viewHolder.getAdapterPosition();
                songs.remove(viewHolder.getAdapterPosition());
                songsAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Snackbar.make(recyclerView, "Song Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                songs.add(lastPos, song);
                                songsAdapter.notifyItemInserted(lastPos);
                            }
                        }).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
}