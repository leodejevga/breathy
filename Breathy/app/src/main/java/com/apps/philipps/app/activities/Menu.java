package com.apps.philipps.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.apps.philipps.app.Backend;
import com.apps.philipps.app.R;

import com.apps.philipps.audiosurf.AudioSurfHandler;
import com.apps.philipps.source.interfaces.IHandler;

public class Menu extends AppCompatActivity {
    ListView gamesList;
    ArrayAdapter<IHandler> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backend.init();
        initGamesList();
    }

    private void initGamesList() {
        Backend.games.add(new AudioSurfHandler(this));
        gamesList = (ListView) findViewById(R.id.games);
        adapter = new ArrayAdapter<IHandler>(this, android.R.layout.simple_list_item_1, Backend.games);
        gamesList.setAdapter(adapter);
        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IHandler game = Backend.games.get(position);
                game.getGame().start();
            }
        });
    }
}
