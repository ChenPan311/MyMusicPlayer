package com.example.mymusicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private final IBinder binder = new LocalBinder();
    public static boolean isRunnig;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private NotificationManager manager;
    private ArrayList<Song> songs;
    public static int sPosition;
    private int duration;

    private ImageButton playPauseBtn;
    private TextView songName;
    private TextView songAuthor;
    private ImageView songCover;
    private SongsAdapter adapter;

    private RemoteViews remoteViews;
    private Notification notification;

    final int NOTIF_ID = 1;


    public class LocalBinder extends Binder {
        MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();
        isRunnig=true;

        Log.d("service","On Create Service!");

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String channelId = "channel_id";
        String channelName = "channel_name";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

        remoteViews = new RemoteViews(getPackageName(), R.layout.music_notification);

        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.putExtra("command", "play");
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.play_pause_notif, playPendingIntent);


        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.putExtra("command", "next");
        nextIntent.putExtra("songs_list", songs);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 1, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_notif, nextPendingIntent);

        Intent prevIntent = new Intent(this, MusicService.class);
        prevIntent.putExtra("command", "prev");
        prevIntent.putExtra("songs_list", songs);
        PendingIntent prevPendingIntent = PendingIntent.getService(this, 2, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.prev_notif, prevPendingIntent);

        Intent closeIntent = new Intent(this, MusicService.class);
        closeIntent.putExtra("command", "close");
        PendingIntent closePendingIntent = PendingIntent.getService(this, 3, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.close_notification, closePendingIntent);

        Intent musicPlayerIntent = new Intent(this, MusicService.class);
        musicPlayerIntent.putExtra("command", "musicPlayer");
        PendingIntent musicPlayerPendingIntent = PendingIntent.getService(this, 4, musicPlayerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.song_cardview, musicPlayerPendingIntent);


        builder.setCustomBigContentView(remoteViews);
        builder.setSmallIcon(R.drawable.logo);
        builder.setOnlyAlertOnce(true);
        notification = builder.build();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String command = intent.getStringExtra("command");
        switch (command) {
            case "new_instance":
                setUpSong(intent);
                startForeground(NOTIF_ID, notification);
                break;
            case "songs_update":
                songs = intent.getParcelableArrayListExtra("songs_list");
                break;
            case "play":
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    playPauseBtn.setImageResource(R.drawable.ic_pause_btn);
                    remoteViews.setImageViewResource(R.id.play_pause_notif, R.drawable.ic_notif_pause);
                } else {
                    mediaPlayer.pause();
                    playPauseBtn.setImageResource(R.drawable.ic_play_btn);
                    remoteViews.setImageViewResource(R.id.play_pause_notif, R.drawable.ic_notif_play);
                }
                manager.notify(NOTIF_ID, notification);
                break;
            case "next":
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                else {
                    remoteViews.setImageViewResource(R.id.play_pause_notif, R.drawable.ic_notif_pause);
                    manager.notify(NOTIF_ID, notification);
                }
                playSong(true);
                setUpNotificationData(remoteViews, songs.get(sPosition), notification);
                setSongView(songName, songAuthor, songCover);
                break;
            case "prev":
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                else {
                    remoteViews.setImageViewResource(R.id.play_pause_notif, R.drawable.ic_notif_play);
                    manager.notify(NOTIF_ID, notification);
                }
                playSong(false);
                setUpNotificationData(remoteViews, songs.get(sPosition), notification);
                setSongView(songName, songAuthor, songCover);
                break;
            case "close":
                playPauseBtn.setImageResource(R.drawable.ic_play_btn);
                isRunnig=false;
                stopSelf();
                break;
            case "musicPlayer":
                Intent intent1 = new Intent(MusicService.this, MainActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
        mediaPlayer.release();
        isRunnig = false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playSong(true);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        duration = mediaPlayer.getDuration();
        mediaPlayer.start();
        //setPlayPauseBtnImage(playPauseBtn);

    }

    public void setUpSong(Intent intent) {
        mediaPlayer.stop();
        songs = intent.getParcelableArrayListExtra("songs_list");
        sPosition = intent.getIntExtra("position", 0);
        setUpNotificationData(remoteViews, songs.get(sPosition), notification);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.get(sPosition).getSong_link());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUpNotificationData(RemoteViews remoteViews, Song song, Notification notification) {
        remoteViews.setTextViewText(R.id.song_name_notif, song.getName());
        remoteViews.setTextViewText(R.id.song_author_notif, song.getAuthor_name());
        NotificationTarget notificationTarget = new NotificationTarget(
                MusicService.this,
                R.id.cover_iv,
                remoteViews,
                notification,
                NOTIF_ID);
        Glide.with(MusicService.this).asBitmap().load(song.getAlbum_cover()).into(notificationTarget);
    }

    public void playSong(boolean is_next) {
        if (is_next) {
            sPosition++;
            if (sPosition == songs.size())
                sPosition = 0;
        } else {
            sPosition--;
            if (sPosition < 0)
                sPosition = songs.size() - 1;
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(songs.get(sPosition).getSong_link());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentPosition() {
        return sPosition;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public int getSongDuration() {
        return duration;
    }

    public void updateSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void setAdapter(SongsAdapter songsAdapter) {
        this.adapter = songsAdapter;
    }

    public void setPlayPauseBtnImage(ImageButton playPauseBtn) {
        if (mediaPlayer.isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.ic_pause_btn);
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_play_btn);
        }
    }

    public void setSongView(TextView nameSong, TextView authorSong, ImageView songCover) {
        nameSong.setText(songs.get(sPosition).getName());
        authorSong.setText(songs.get(sPosition).getAuthor_name());
        Glide.with(this).load(songs.get(sPosition).getAlbum_cover()).into(songCover);
    }


    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void setPlayPauseBtn(ImageButton ppBtn) {
        this.playPauseBtn = ppBtn;
    }

    public void setSongCover(ImageView imageCover) {
        this.songCover = imageCover;
    }

    public void setSongName(TextView songName) {
        this.songName = songName;
    }

    public void setSongAuthor(TextView songAuthor) {
        this.songAuthor = songAuthor;
    }
}
