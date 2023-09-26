package VideoPaakage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.developeramitzalayamusic.PlayVideo;
import com.example.developeramitzalayamusic.R;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.Videoholder> {

    Context context;
    ArrayList<VideoModel> videos;

    public VideoAdapter(Context context, ArrayList<VideoModel> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public Videoholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.videos_list,parent,false);
        return new Videoholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Videoholder holder, @SuppressLint("RecyclerView") int position) {

          VideoModel videoModel = videos.get(position);
          holder.Title.setText(videoModel.getTitle());
          holder.vduration.setText(getDuration(Integer.parseInt(videoModel.getDuration())));
          Glide.with(context).load(new File(videoModel.getThumbanail())).into(holder.thum);
          holder.Title.setSelected(true);

        // on item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideo.class);
                intent.putExtra("Video_Name",videoModel.getTitle());
                intent.putExtra("Video_Duration",videoModel.getDuration());
                Bundle bundle = new Bundle();
                intent.putExtra("position",position);
                bundle.putParcelableArrayList("videos",videos);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });

    }
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
        if (videos != null) {
            return videos.size();
        } else {
            return 0;
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterVideos(ArrayList<VideoModel>filtervideos){
        videos = filtervideos;
        notifyDataSetChanged();

    }

    public static class Videoholder extends RecyclerView.ViewHolder {
        TextView Title,vduration;
        ImageView thum;
        public Videoholder(@NonNull View itemView) {
            super(itemView);
            thum= itemView.findViewById(R.id.song_img);
            Title = itemView.findViewById(R.id.video_title);
            vduration = itemView.findViewById(R.id.video_duration);

        }
    }
}
