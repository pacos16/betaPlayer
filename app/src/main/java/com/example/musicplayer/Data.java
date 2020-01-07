package com.example.musicplayer;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import android.provider.MediaStore;


import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;



import java.util.ArrayList;


public class Data {

    private ArrayList<Song> songs;
    private ArrayList<Playlist> playlists;
    private Context c;

    public Data(Context c){
        songs=new ArrayList<>();
        this.c=c;
        loadSongs();
        createInitialPlaylist();

    }




    public void loadSongs(){

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = c.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    Song s = new Song(name, artist,duration, url);
                    songs.add(s);

                } while (cursor.moveToNext());

            }

            cursor.close(); }
    }

    private void createInitialPlaylist(){
        this.playlists=new ArrayList<>();
        playlists.add(new Playlist(songs,"All Songs"));

    }


    public ArrayList<Playlist> getPlaylists(){
        return playlists;
    }
}
