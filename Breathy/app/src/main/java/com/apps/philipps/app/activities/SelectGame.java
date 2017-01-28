package com.apps.philipps.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.apps.philipps.app.Backend;
import com.apps.philipps.app.R;

import com.apps.philipps.audiosurf.AudioSurf;
import com.apps.philipps.source.interfaces.IGame;

public class SelectGame extends AppCompatActivity {
    ListView gamesList;
    ArrayAdapter<IGame> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);
        initGamesList();
    }

    private void initGamesList() {
        gamesList = (ListView) findViewById(R.id.games);
        adapter = new ArrayAdapter<IGame>(this, android.R.layout.simple_list_item_1, Backend.games);
        gamesList.setAdapter(adapter);
        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Backend.selected = Backend.games.get(position);
                Toast.makeText(SelectGame.this, "Game " + Backend.selected + " is selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void gameOptions(View view) {
        if(Backend.selected != null)
            Backend.selected.startOptions();
    }

    public void startGame(View view) {
        if(Backend.selected != null)
            Backend.selected.startGame();
    }
}
