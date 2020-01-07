package com.example.musicplayer;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.model.Playlist;

public class Player extends AppCompatActivity {

    private ImageButton playPause;
    private ImageButton previous;
    private ImageButton next;
    private Playlist playlist;
    private PlayerService playerService;
    private boolean serviceStatus;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        playPause=findViewById(R.id.ibPlayPause);
        next=findViewById(R.id.ibNext);
        previous=findViewById(R.id.ibPrevious);


        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }



    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            playerService=binder.getService();
            serviceStatus=true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };


}
