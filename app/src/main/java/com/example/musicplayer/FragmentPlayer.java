package com.example.musicplayer;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

public class FragmentPlayer extends Fragment {

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
    private LocalBroadcastManager lbm;

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
        lbm = LocalBroadcastManager.getInstance(this.getActivity());
        lbm.registerReceiver(receiver, new IntentFilter(PlayerService.TAG));

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
        seekBar.setMax(duration);
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
            duration=playerService.getDuration();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            lbm.unregisterReceiver(receiver);
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

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getAction().equals(PlayerService.TAG)) {
                milis=intent.getIntExtra("com.pacosignes.TIME_MILIS",0);
                seekBar.setProgress(milis);
            }
        }
    };


    public void stopPlayer(){

        if(playerService!=null)playerService.stop();
    }

}
