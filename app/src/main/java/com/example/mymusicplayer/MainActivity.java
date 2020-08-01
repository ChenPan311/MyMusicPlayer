package com.example.mymusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recycler_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                300,
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                340,
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                550,
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                400,
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                350,
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                300,
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                340,
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                550,
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                400,
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                350,
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                300,
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                340,
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                550,
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                400,
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                350,
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));
        songs.add(new Song("Blinding Lights",
                "The Weekend",
                300,
                "https://upload.wikimedia.org/wikipedia/he/e/e6/The_Weeknd_-_Blinding_Lights.png",
                "https://modenine.net/file/uploads/2020/05/The_Weeknd_-_Blinding_Lights_Ft_Chromatics-(Modenine.Net).mp3"));
        songs.add(new Song("Stressed Out",
                "Twenty One Pilots",
                340,
                "https://upload.wikimedia.org/wikipedia/he/4/48/TOP_Stressed_Out.jpg",
                "https://www.mboxdrive.com/Stressed%20Out%20by%20twenty%20one%20pilots%20lyrics.mp3"));
        songs.add(new Song("Boulevard Of Broken Dreams",
                "Green Day",
                550,
                "https://upload.wikimedia.org/wikipedia/he/c/c5/Green_Day_-_Boulevard_Of_Broken_Dreams.jpg",
                "https://www.mboxdrive.com/Green%20Day%20-%20Boulevard%20of%20Broken%20Dreams.mp3"));
        songs.add(new Song("SOS",
                "Avicii",
                400,
                "https://m.media-amazon.com/images/I/71vfRyK6lGL._SS500_.jpg",
                "https://www.mboxdrive.com/Avicii%20-%20SOS%20ft.%20Aloe%20Blacc.mp3"));
        songs.add(new Song("Let You Down",
                "NF",
                350,
                "https://upload.wikimedia.org/wikipedia/en/4/42/NF_let_you_down_single_cover.jpg",
                "https://www.mboxdrive.com/NF%20%E2%80%93%20Let%20You%20Down%20%F0%9F%8E%B5.mp3"));


        final SongsAdapter songsAdapter = new SongsAdapter(this,songs);
        songsAdapter.setListener(new SongsAdapter.MySongListener() {
            @Override
            public void OnSongClicked(View view, int position) {

                final int NOTIF_ID=1;

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                String channelId = "channel_id";
                String channelName = "channel_name";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                }


                Intent intent = new Intent(MainActivity.this,MusicPlayerActivity.class);

                Song song = songs.get(position);
                intent.putExtra("song",song);
                intent.putExtra("position",position);
                intent.putExtra("songs_list",songs);
                //intent.putExtra("command","new_instance");

                PendingIntent playPendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.music_notification);
                remoteViews.setOnClickPendingIntent(R.id.play_pause_notif,playPendingIntent);
                remoteViews.setTextViewText(R.id.song_name_notif,song.getName());
                remoteViews.setTextViewText(R.id.song_author_notif,song.getAuthor_name());

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,channelId);
                builder.setSmallIcon(R.drawable.logo);
                builder.setCustomContentView(remoteViews);
                builder.setOnlyAlertOnce(true);
                Notification notification = builder.build();
                //-------loading cover image into the notification----//
                NotificationTarget notificationTarget = new NotificationTarget(
                        MainActivity.this,
                        R.id.cover_iv,
                        remoteViews,
                        notification,
                        NOTIF_ID);
                Glide.with(MainActivity.this).asBitmap().load(song.getAlbum_cover()).into(notificationTarget);

                manager.notify(NOTIF_ID,notification);

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