package com.example.mymusicplayer;
import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String author_name;
    private int song_duration;
    private String album_cover;
    private String song_link;

    public Song(String name, String author_name, int song_duration, String album_cover, String song_link) {
        this.name = name;
        this.author_name = author_name;
        this.song_duration = song_duration;
        this.album_cover = album_cover;
        this.song_link = song_link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getSong_duration() {
        return song_duration;
    }

    public void setSong_duration(int song_duration) {
        this.song_duration = song_duration;
    }

    public String getAlbum_cover() {
        return album_cover;
    }

    public void setAlbum_cover(String album_cover) {
        this.album_cover = album_cover;
    }

    public String getSong_link() {
        return song_link;
    }

    public void setSong_link(String song_link) {
        this.song_link = song_link;
    }
}
