package SongPakage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developeramitzalayamusic.PlaySong;
import com.example.developeramitzalayamusic.R;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.Viewholder>{

    Context context;
    ArrayList<SongModel>songs;
    MediaPlayer mediaPlayer;



    public SongAdapter(Context context, ArrayList<SongModel> songs,MediaPlayer mediaPlayer) {
        this.context = context;
        this.songs = songs;
        this.mediaPlayer= mediaPlayer;



    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.music_list,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        SongModel songModel= songs.get(position);
        holder.title.setText(songModel.getTitle());
        holder.duration.setText(getDuration(Integer.parseInt(songModel.getDuration())));
        holder.title.setSelected(true);




        String titleimg= songModel.getTitleimg();
        if(titleimg!=null){

                holder.titleimg.setImageURI(Uri.parse(titleimg));

            }
        if (holder.titleimg.getDrawable()==null){
            holder.titleimg.setImageResource(R.drawable.icon);
        }


        
        // click on item

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, PlaySong.class);
                intent.putExtra("position",position);
                intent.putExtra("Song_Name",songModel.getTitle());
                intent.putExtra("Song_image",songModel.getTitleimg());
                intent.putExtra("Duration",songModel.getDuration());
                intent.putExtra("songs",songs);
                context.startActivity(intent);
                }
        });

    }



    @SuppressLint("DefaultLocale")
    private String getDuration(int totalDuration){
        String totalDurationnumber;

        int hours = totalDuration/(1000*60*60);
        int minuat = (totalDuration%(1000*60*60))/(1000*60);
        int seconds = ((totalDuration%(1000*60*60)%(1000*60*60)))%(1000*60)/1000;

        if (hours<1){
            totalDurationnumber= String.format("%02d:%02d",minuat,seconds);
        }
        else {
            totalDurationnumber= String.format("%1d:%02d:%02d",hours,minuat,seconds);

        }
        return totalDurationnumber;


    }


    @Override
    public int getItemCount() {
        if (songs != null) {
            return songs.size();
        } else {
            return 0;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterSongs(ArrayList<SongModel> searchsongs){
        songs =searchsongs;
        notifyDataSetChanged();
    }




    public static class Viewholder extends RecyclerView.ViewHolder {

        ImageView titleimg;
        TextView title,duration;
        public  Viewholder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.music_name);
            titleimg= itemView.findViewById(R.id.musicimg);
            duration= itemView.findViewById(R.id.duration);
        }
    }

}
