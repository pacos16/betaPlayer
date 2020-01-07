package com.example.musicplayer;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FragmentPlayer extends Fragment implements IDuracion {

    private ImageButton btPlayPause;
    private ImageButton btPrevious;
    private ImageButton btNext;
    private ImageButton btMinimize;
    private TextView tvName;
    private TextView tvData;
    private SeekBar seekBar;
    private Playlist playlist;
    private PlayerService playerService;
    private boolean isPaused;
    private int position;
    private int duration;
    private int milis;
    private Handler handler;
    private FragmentPlayer fplayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.player,container,false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btPlayPause =getActivity().findViewById(R.id.ibPlayPause);
        btNext =getActivity().findViewById(R.id.ibNext);
        btPrevious =getActivity().findViewById(R.id.ibPrevious);
        tvName =getActivity().findViewById(R.id.tvSongNamePlayer);
        tvData =getActivity().findViewById(R.id.tvSongDescPlayer);
        seekBar=getActivity().findViewById(R.id.seekBar);
        btMinimize=getActivity().findViewById(R.id.ibMinimize);
        actualizar(playlist.getSongs().get(position));
        isPaused=false;
        handler=new Handler();
        fplayer=this;

        btMinimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    FragmentSongs fragmentSongs=new FragmentSongs();
                    fragmentSongs.setPlaylist(playlist);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragmentSongs).commit();

            }
        });

        btPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!playerService.isPlaying()){
                    btPlayPause.setImageResource(R.drawable.baseline_pause_circle_filled_black_18dp);
                    playerService.resume();
                    isPaused=false;
                }else {
                    btPlayPause.setImageResource(R.drawable.baseline_play_circle_filled_black_18dp);
                    playerService.pause();
                    isPaused=true;
                }
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.previousSong();
                isPaused=false;
                actualizar(playerService.getSong());
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerService.nextSong();
                isPaused=false;
                actualizar(playerService.getSong());

            }
        });


        final Intent intent = new Intent(getActivity(), PlayerService.class);
        if (!isMyServiceRunning(PlayerService.class)) {
            getActivity().startService(intent);
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        else {
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }



        ScheduledExecutorService s= Executors.newSingleThreadScheduledExecutor();
        MyTask task=new MyTask();
        s.scheduleAtFixedRate(task,0,200, TimeUnit.MILLISECONDS);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    playerService.seekTo(progress);
                }

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(playerService.getCurrentPosition());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(playerService.getCurrentPosition());
            }
        });
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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
            playerService.setPlaylistAndPosition(playlist,position);
            isPaused = !playerService.isPlaying();
            playerService.changeSong();
            playerService.setIDuracionListener(fplayer);
            seekBar.setProgress(0);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };

    public void setSongAttributtes(Playlist playlist,int position){
        this.playlist=playlist;
        this.position=position;

    }

    private void actualizar(Song s){
        tvName.setText(s.getName());
        tvData.setText(s.getGroup());
        if(isPaused){
            btPlayPause.setImageResource(R.drawable.baseline_play_circle_filled_black_18dp);
        }else{
            btPlayPause.setImageResource(R.drawable.baseline_pause_circle_filled_black_18dp);
        }
    }
    public void stopPlayer(){

        if(playerService!=null)playerService.stop();
    }

    public class MyTask implements Runnable{

        @Override
        public void run() {

                if(playerService!=null) {
                    milis = playerService.getCurrentPosition();
                    seekBar.setProgress(milis);
                    handler.post(this);
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerService.stopService();
    }

    @Override
    public void setDuracion(int i) {
        duration=i;
        seekBar.setMax(duration);
    }
}
