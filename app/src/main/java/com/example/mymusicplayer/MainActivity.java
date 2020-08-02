package com.example.mymusicplayer;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private SongsAdapter songsAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final FloatingActionButton addBtn = findViewById(R.id.add_Btn);

        final RecyclerView recyclerView = findViewById(R.id.recycler_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));



        songsAdapter = new SongsAdapter(this, songs);
        songsAdapter.setListener(new SongsAdapter.MySongListener() {
            @Override
            public void OnSongClicked(View view, int position) {

                Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                Intent service = new Intent(MainActivity.this, MusicService.class);
                Song song = songs.get(position);
                service.putExtra("position", position);
                service.putExtra("songs_list", songs);
                service.putExtra("command", "new_instance");

                intent.putExtra("position", position);
                intent.putExtra("songs_list", songs);

                startService(service);
                Pair pair = new Pair<View, String>(view.findViewById(R.id.song_cover_iv), "coverTrans");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pair);
                    startActivity(intent, options.toBundle());
                }
            }
        });
        recyclerView.setAdapter(songsAdapter);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int pos_drag = viewHolder.getAdapterPosition();
                int pos_target = target.getAdapterPosition();
                Collections.swap(songs, pos_drag, pos_target);
                songsAdapter.notifyItemMoved(pos_drag, pos_target);

//                Intent service = new Intent(MainActivity.this, MusicService.class);
//                service.putExtra("songs_list",songs);
//                service.putExtra("command", "songs_update");
//                startService(service);
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
//                Intent service = new Intent(MainActivity.this, MusicService.class);
//                service.putExtra("songs_list",songs);
//                service.putExtra("command", "songs_update");
//                startService(service);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void showAlertDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this,R.style.Theme_MaterialComponents_Dialog_Alert);
        View view = getLayoutInflater().inflate(R.layout.add_song_dialog,null);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(view);
        dialog.show();
        WindowManager.LayoutParams params =   dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(params);
    }


}