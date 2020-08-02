package com.example.mymusicplayer;


import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private Context context;
    private ArrayList<Song> songs;
    private MySongListener listener;
    // variable to track event time
    private long mLastClickTime = 0;

    //-----------View Holder-----------//
    public class SongViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView album_cover;
        TextView song_name;
        TextView author_name;
        TextView song_duration;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.song_cardview);
            album_cover=itemView.findViewById(R.id.song_cover_iv);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                album_cover.setClipToOutline(true);
            }
            song_name = itemView.findViewById(R.id.song_name_tv);
            author_name = itemView.findViewById(R.id.song_author_tv);
            song_duration = itemView.findViewById(R.id.song_time_duration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        // Preventing multiple clicks, using threshold of 1 second
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        listener.OnSongClicked(v,getAdapterPosition());
                    }
                }
            });
        }
    }
//-----------View Holder-----------//

    //-----------MySongInterface interface-----------//
    public interface MySongListener{
        public void OnSongClicked(View view, int position);
    }

    public void setListener(MySongListener listener) {
        this.listener = listener;
    }
//-----------MySongInterface interface-----------//


    public SongsAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_layout,parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, final int position) {
        Song song = songs.get(position);
        Glide.with(context).load(song.getAlbum_cover()).into(holder.album_cover);
        holder.song_duration.setText(String.valueOf(song.getSong_duration()));
        holder.author_name.setText(song.getAuthor_name());
        holder.song_name.setText(song.getName());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }


}
