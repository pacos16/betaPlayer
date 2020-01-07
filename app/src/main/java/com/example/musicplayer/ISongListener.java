package com.example.musicplayer;

import com.example.musicplayer.model.Playlist;

public interface ISongListener {

    void onSelectedSong(Playlist p, int position);
}
