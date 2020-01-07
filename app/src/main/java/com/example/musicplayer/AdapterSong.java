package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Song;



public class AdapterSong extends RecyclerView.Adapter<AdapterSong.SongViewHolder> {
    private Context context;

    private ISongListener listener;
    private Playlist playlist;

    public AdapterSong(Context context, Playlist playlist, ISongListener listener) {
        this.context = context;
        this.playlist=playlist;
        this.listener=listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_song,parent,false);
        return new SongViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        holder.bind(playlist.getSongs().get(position));

    }

    @Override
    public int getItemCount() {
        return playlist.getSongs().size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView tvName;
        private TextView tvData;
        private ImageView imgSong;
        private Song song;
        private ISongListener listener;


        public SongViewHolder(@NonNull View itemView,ISongListener listener) {
            super(itemView);
            this.listener=listener;
            tvName=itemView.findViewById(R.id.tvSongName);
            tvData=itemView.findViewById(R.id.tvSongData);
            itemView.setOnClickListener(this);
        }

        public void bind(Song s){
            song=s;
            tvName.setText(s.getName());
            tvData.setText(s.getGroup());
        }

        @Override
        public void onClick(View v) {
            listener.onSelectedSong(playlist,getAdapterPosition());
        }
    }
}
