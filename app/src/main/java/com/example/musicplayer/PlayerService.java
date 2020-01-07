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

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final String TAG= "com.pacosignes.PlayerService";
    private MediaPlayer player;
    private Playlist playlist;
    private IBinder iBinder;
    private Handler handler;
    private boolean isPaused;

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

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public class LocalBinder extends Binder{
        public PlayerService getService(){
            return PlayerService.this;
        }
    }


}
