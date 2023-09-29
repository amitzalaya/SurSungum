package com.example.developeramitzalayamusic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SongPakage.SongAdapter;
import SongPakage.SongModel;

public class MusicFragment extends Fragment {
    RecyclerView recyclerView;
    SongAdapter adapter;
    ArrayList<SongModel> Songs =new ArrayList<>();
    MediaPlayer mediaPlayer;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPermission()== false){
            requestPermisstion();
            return;
        }



    }

    @SuppressLint({"MissingInflatedId", "UseRequireInsteadOfGet"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);



          String[] projetion={
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID


        };
       String selection= MediaStore.Audio.Media.IS_MUSIC + "!=0";




        Cursor cursor= getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projetion,selection,null,null);

        if (cursor!=null){
            while (cursor.moveToNext()){
           long pathcolomn= cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
           String titlecolomn = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
           String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
           String duration=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
           // Album fatch
           Uri musicalbum= ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.parseLong((album)));

           // uri song
                Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,pathcolomn);
                SongModel songModel= new SongModel(uri,titlecolomn,duration,musicalbum);


           Songs.add(songModel);


            }
        }


        mediaPlayer = new MediaPlayer();


        recyclerView = view.findViewById(R.id.music_rc);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SongAdapter(getContext(),Songs,mediaPlayer );
        recyclerView.setAdapter(adapter);










        return view;

    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.options,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        SearchSongs(searchView);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void SearchSongs(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText.toLowerCase());
                return true;
            }
        });

    }

    private void filterSongs(String query) {
        ArrayList<SongModel> filteredlist = new ArrayList<>();

        if (Songs.size()>0){
            for (SongModel songModel : Songs){
                if (songModel.getTitle().toLowerCase().contains(query)){
                    filteredlist.add(songModel);
                }
            }
            if (adapter!= null){
                adapter.filterSongs(filteredlist);
            }
        }

    }



    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }
    void requestPermisstion(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(getContext(), "please give the permission", Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

    }


}

