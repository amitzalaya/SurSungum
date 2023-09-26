package com.example.developeramitzalayamusic;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import VideoPaakage.VideoAdapter;
import VideoPaakage.VideoModel;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;


public class VideoFragment extends Fragment {
    RecyclerView videorc;
    VideoAdapter adapter;
    ArrayList<VideoModel> videos = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA
        };
        String selection = MediaStore.Video.Media.DATA +
                " <= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))};
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getContext().getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                String thumble= cursor.getString(thum);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);




                videos.add(new VideoModel(contentUri,name,duration,thumble));


                videorc = view.findViewById(R.id.video_rc);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                videorc.setLayoutManager(linearLayoutManager);
                adapter = new VideoAdapter(getContext(), videos);
                ScaleInAnimationAdapter scaleInAnimationAdapter= new ScaleInAnimationAdapter(adapter);
                scaleInAnimationAdapter.setDuration(1000);
                scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
                scaleInAnimationAdapter.setFirstOnly(false);
                videorc.setAdapter(scaleInAnimationAdapter);

            }
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.options,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        filteredvideos(searchView);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void filteredvideos(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               filterVideo(newText.toLowerCase());
                return true;
            }
        });
    }

    private void filterVideo(String query) {
        ArrayList<VideoModel> filtervideos = new ArrayList<>();
        if (videos.size()>0){
            for (VideoModel model : videos){
                if (model.getTitle().toLowerCase().contains(query)){
                    filtervideos.add(model);
                }
            }
            if (adapter!= null){
                adapter.filterVideos(filtervideos);
            }
        }
    }
}
