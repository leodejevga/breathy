package com.apps.philipps.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.apps.philipps.app.Backend;
import com.apps.philipps.app.R;
import com.apps.philipps.audiosurf.AudioSurf;

public class Menu extends AppCompatActivity {

    ImageButton games;
    ImageButton options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backend.init(this);
        initActivity();
    }

    private void initActivity() {
        games = (ImageButton) findViewById(R.id.mainGames);
        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, SelectGame.class);
                startActivity(i);
            }
        });
        options = (ImageButton) findViewById(R.id.mainOptions);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Options.class);
                startActivity(i);
            }
        });
    }
}
