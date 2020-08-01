package com.example.mymusicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,Observer{
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private NotificationManager manager;
    private ArrayList<Song> songs;
    private int position;

    private RemoteViews remoteViews;
    private Notification notification;

    final int NOTIF_ID=1;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.reset();

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String channelId = "channel_id";
        String channelName = "channel_name";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);

        remoteViews = new RemoteViews(getPackageName(),R.layout.music_notification);

        Intent playIntent = new Intent(this,MusicService.class);
        playIntent.putExtra("command","play");
        PendingIntent playPendingIntent = PendingIntent.getService(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.play_pause_notif,playPendingIntent);
        remoteViews.setImageViewResource(R.id.play_pause_notif,R.drawable.ic_notif_pause);

        Intent nextIntent = new Intent(this,MusicService.class);
        nextIntent.putExtra("command","next");
        PendingIntent nextPendingIntent = PendingIntent.getService(this,1,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_notif,nextPendingIntent);

        Intent prevIntent = new Intent(this,MusicService.class);
        prevIntent.putExtra("command","prev");
        PendingIntent prevPendingIntent = PendingIntent.getService(this,2,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.prev_notif,prevPendingIntent);

        Intent closeIntent = new Intent(this,MusicService.class);
        closeIntent.putExtra("command","close");
        PendingIntent closePendingIntent = PendingIntent.getService(this,3,closeIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.close_notification,closePendingIntent);

//        Intent pauseIntent = new Intent(this,MusicService.class);
//        pauseIntent.putExtra("command","pause");
//        PendingIntent pausePendingIntent = PendingIntent.getService(this,3,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.play_pause_notif,pausePendingIntent);
//        remoteViews.setImageViewResource(R.id.play_pause_notif,R.drawable.ic_notif_play);

        builder.setCustomContentView(remoteViews);
        builder.setSmallIcon(R.drawable.logo);
        builder.setOnlyAlertOnce(true);
        notification = builder.build();

//        setUpNotificationData(remoteViews,songs.get(position),notification);

        startForeground(NOTIF_ID,notification);

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String command = intent.getStringExtra("command");
        switch (command) {
            case "new_instance":
                if(!mediaPlayer.isPlaying()) {
                    position = intent.getIntExtra("position",0);
                    songs = intent.getParcelableArrayListExtra("songs_list");
                    setUpNotificationData(remoteViews,songs.get(position),notification);
                    try {
                        mediaPlayer.setDataSource(songs.get(position).getSong_link());
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "play":
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    remoteViews.setImageViewResource(R.id.play_pause_notif, R.drawable.ic_notif_pause);
                    manager.notify(NOTIF_ID,notification);
                }else {
                    mediaPlayer.pause();
                    remoteViews.setImageViewResource(R.id.play_pause_notif,R.drawable.ic_notif_play);
                    manager.notify(NOTIF_ID,notification);
                }
                break;
//            case "pause":
//                if(mediaPlayer.isPlaying())
//                    mediaPlayer.pause();
//                remoteViews.setImageViewResource(R.id.play_pause_notif,R.drawable.ic_notif_play);
            case "next":
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                playSong(true);
                setUpNotificationData(remoteViews,songs.get(position),notification);
                break;
            case "prev":
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                playSong(false);
                setUpNotificationData(remoteViews,songs.get(position),notification);
                break;
            case "close":
                stopSelf();


        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
            if(mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
       playSong(true);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();

    }

    public void setUpNotificationData(RemoteViews remoteViews,Song song,Notification notification){
        remoteViews.setTextViewText(R.id.song_name_notif,song.getName());
        remoteViews.setTextViewText(R.id.song_author_notif,song.getAuthor_name());
        NotificationTarget notificationTarget = new NotificationTarget(
                MusicService.this,
                R.id.cover_iv,
                remoteViews,
                notification,
                NOTIF_ID);
        Glide.with(MusicService.this).asBitmap().load(song.getAlbum_cover()).into(notificationTarget);
    }

    private void playSong(boolean is_next){
        if(is_next) {
            position++;
            if (position == songs.size())
                position = 0;
        }
        else {
            position--;
            if(position<0)
                position=songs.size()-1;
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(songs.get(position).getSong_link());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        songs = (ArrayList<Song>) arg;
    }
}
