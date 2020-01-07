package com.example.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

import java.io.IOException;
import java.net.MalformedURLException;


public class PlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final String TAG= "com.pacosignes.PlayerService";
    private MediaPlayer player;
    private Playlist playlist;
    private IBinder iBinder;
    private Handler handler;
    private boolean isPaused;
    private int position;

    @Override
    public void onCreate() {
        super.onCreate();
        iBinder=new LocalBinder();
        player=new MediaPlayer();
        handler=new Handler();
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
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
    public void setPosition(int position){
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
        play();
    }
    public void play(){
        if(isPaused){
            player.start();
            isPaused=false;
        }else {
            player.reset();
            try {
                player.setDataSource(playlist.getSongs().get(position).getPath());
                player.prepareAsync();

            } catch (MalformedURLException murle){

            }catch (IOException ioe){

            }
        }
    }
    public void pause(){
        player.pause();
        isPaused=true;
    }
    public void nextSong(){
        position++;
        if (position==playlist.getSongs().size()) position=0;
        play();
    }

    public Song getSong(){
        return playlist.getSongs().get(position);
    }
    public void stop(){
        stopSelf();
    }
}
