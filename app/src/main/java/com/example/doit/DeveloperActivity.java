package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DeveloperActivity extends AppCompatActivity {
    Button hirePranav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        hirePranav = findViewById(R.id.hirePranav);
        
        hirePranav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace {username} with the username of the profile you want to open
                String profileUrl = "https://www.instagram.com/pranav__singh2";
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setPackage("com.instagram.android");
                intent.setData(Uri.parse(profileUrl));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // If the Instagram app is not installed, open the profile in a web browser
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl));
                    startActivity(webIntent);
                }
            }
        });

    }
}