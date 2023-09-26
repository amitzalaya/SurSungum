package com.example.developeramitzalayamusic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    NavigationBarView navigationBarView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBarView= findViewById(R.id.buttom_navi);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new MusicFragment()).commit();

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp= null;

                switch (item.getItemId()){
                    case R.id.music:
                        temp= new MusicFragment();
                        break;
                    case R.id.video:
                        temp= new VideoFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,temp).commit();
                return true;
            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case  R.id.exit:
                finish();
                break;
            case R.id.contactdeveloper:
                startActivity(new Intent(MainActivity.this,ContactActivity.class));
               break;
            case R.id.shareapp:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,"Download The App, https://play.google.com/store/apps/details?id=com.developeramitzalayamusic");
                startActivity(Intent.createChooser(share,"SurSungum-MediaPlayer"));
                break;
            case R.id.reviewapp:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.developeramitzalayamusic"));
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


}


