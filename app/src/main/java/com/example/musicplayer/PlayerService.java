package com.example.musicplayer;

import android.app.Service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;

import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;


import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

import java.io.IOException;
import java.net.MalformedURLException;



public class PlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener  {

    public static final String TAG= "com.pacosignes.PlayerService";
    private MediaPlayer player;
    private Playlist playlist;
    private IBinder iBinder;
    private boolean isPaused;
    private int position;
    private int duration;
    private IDuracion listener;

    @Override
    public void onCreate() {
        super.onCreate();
        iBinder=new LocalBinder();
        player=new MediaPlayer();
        isPaused=false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.setWakeMode(PlayerService.this, PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

        return START_NOT_STICKY;
    }
    public void seekTo(int position){
        player.seekTo(position);
    }

    public int getCurrentPosition(){
        return player.getCurrentPosition();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i("omg","wtf");
        //nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        duration=mp.getDuration();
        listener.setDuracion(mp.getDuration());
    }

    public void setPlaylistAndPosition(Playlist p,int position){
        this.playlist=p;
        this.position=position;


    }

    public class LocalBinder extends Binder{
        public PlayerService getService(){
            return PlayerService.this;
        }
    }

    public void previousSong(){
        position--;
        if(position<0) position=playlist.getSongs().size()-1;

        changeSong();
    }

    public void resume(){
        if(isPaused){
            player.start();
            isPaused=false;
        }

    }

    public void changeSong(){
        player.reset();
        try {
            player.setDataSource(playlist.getSongs().get(position).getPath());
            player.prepareAsync();
            isPaused=false;

        } catch (MalformedURLException murle){

        }catch (IOException ioe){

        }

    }

    public void pause(){
        player.pause();
        isPaused=true;
    }
    public void nextSong(){
        position++;
        if (position==playlist.getSongs().size()) position=0;
        changeSong();
    }

    public Song getSong(){
        return playlist.getSongs().get(position);
    }
    public void stopService(){

        stopSelf();
    }
    public void stop(){
        player.stop();
    }
    public boolean isPlaying(){
        return player.isPlaying();
    }
    public int getDuration(){
        return duration;
    }
    public void setIDuracionListener(IDuracion listener){
        this.listener=listener;
    }
}
