package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class FragmentSongs extends Fragment implements ISongListener {
    public static final String PLAYLIST_EXTRA= "com.pacosignes.playlist_extra";
    public static final String POSITION_EXTRA= "com.pacosignes.position_extra";
    private Playlist playlist;
    private ArrayList<Song> songs;
    private RecyclerView recyclerView;
    private AdapterSong adapterSong;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout= inflater.inflate(R.layout.rv_songs,container,false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapterSong=new AdapterSong(this.getContext(),playlist,this);
        recyclerView=getView().findViewById(R.id.rvSongs);
        recyclerView.setAdapter(adapterSong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));

    }


    public void setPlaylist(Playlist playlist){
        this.playlist=playlist;
        this.songs=playlist.getSongs();
    }

    @Override
    public void onSelectedSong(Playlist p, int position) {

        /*
        Intent playerIntent=new Intent(this.getContext(),Player.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(PLAYLIST_EXTRA,playlist);
        bundle.putInt(POSITION_EXTRA,position);
        playerIntent.putExtras(bundle);
        getActivity().startActivity(playerIntent);
         */

        boolean fragmentActivo=false;
        for (Fragment f:getActivity().getSupportFragmentManager().getFragments()
             ) {
            if(f instanceof FragmentPlayer){
                ((FragmentPlayer) f).stopPlayer();
                ((FragmentPlayer) f).setSongAttributtes(playlist,position);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,f).commit();
                fragmentActivo=true;
            }
        }

        if(!fragmentActivo){
            FragmentPlayer fragmentPlayer=new FragmentPlayer();
            fragmentPlayer.setSongAttributtes(playlist,position);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragmentPlayer).commit();

        }



    }
}
