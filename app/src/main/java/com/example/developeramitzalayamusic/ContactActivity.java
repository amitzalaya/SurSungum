package com.example.developeramitzalayamusic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class ContactActivity extends AppCompatActivity {
    LottieAnimationView instagramid,facebookid,linkdinid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        instagramid = findViewById(R.id.instagram_id);
        linkdinid = findViewById(R.id.linkdin_id);
        facebookid = findViewById(R.id.facebook_id);

        instagramid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instaid = new Intent(Intent.ACTION_VIEW);
                instaid.setData(Uri.parse("https://instagram.com/pyaraprogramer?utm_source=qr&igshid=MzNlNGNkZWQ4Mg=="));
                startActivity(instaid);
            }
        });

        linkdinid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent linkdinid = new Intent(Intent.ACTION_VIEW);
                linkdinid.setData(Uri.parse("https://www.linkedin.com/in/amit-zalaya-13444826a"));
                startActivity(linkdinid);

            }
        });
        facebookid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookid = new Intent(Intent.ACTION_VIEW);
                facebookid.setData(Uri.parse("https://www.facebook.com/amit.zalaya.7?mibextid=ZbWKwL "));
                startActivity(facebookid);

            }
        });



    }
}