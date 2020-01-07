package com.example.musicplayer.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {

    private ArrayList<Song> songs;
    private String name;
    public Playlist(){}
    public Playlist(String name){
        this.name=name;
        songs=new ArrayList<>();
    }

    public Playlist(ArrayList<Song> songs, String name) {
        this.songs = songs;
        this.name = name;
    }

    public void addSong(Song song){
        songs.add(song);
    }
    public void rmSong(Song song){
        songs.remove(song);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void setName(String name) {
        this.name = name;
    }
}
