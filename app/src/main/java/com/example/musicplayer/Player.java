package com.example.musicplayer;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

public class Player extends AppCompatActivity {

    private ImageButton playPause;
    private ImageButton previous;
    private ImageButton next;
    private TextView name;
    private TextView data;
    private Playlist playlist;
    private PlayerService playerService;
    private boolean serviceStatus;
    private boolean isPaused;
    private int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        playPause=findViewById(R.id.ibPlayPause);
        next=findViewById(R.id.ibNext);
        previous=findViewById(R.id.ibPrevious);
        name=findViewById(R.id.tvSongNamePlayer);
        data=findViewById(R.id.tvSongDescPlayer);
        Bundle bundle= getIntent().getExtras();
        playlist=(Playlist) bundle.get(FragmentSongs.PLAYLIST_EXTRA);
        position=bundle.getInt(FragmentSongs.POSITION_EXTRA);
        name.setText(playlist.getSongs().get(position).getName());
        data.setText(playlist.getSongs().get(position).getGroup());


        final Intent intent = new Intent(this, PlayerService.class);
        if (!isMyServiceRunning(PlayerService.class)) {
            startService(intent);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        else {
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }




        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.previousSong();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.nextSong();
            }
        });

    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            playerService=binder.getService();
            serviceStatus=true;
            playerService.setPlaylist(playlist);
            playerService.setPosition(position);
            playerService.play();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    public void actualizar(){
        Song s=playerService.getSong();

    }


}
