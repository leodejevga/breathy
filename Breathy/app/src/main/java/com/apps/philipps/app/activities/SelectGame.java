package com.apps.philipps.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.apps.philipps.app.Backend;
import com.apps.philipps.app.R;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.interfaces.IGame;

import org.w3c.dom.Text;

/**
 * Select Game Activity
 */
public class SelectGame extends AppCompatActivity {
    private Button buy;
    private TextView coins;
    private ListView gamesList;
    private ArrayAdapter<IGame> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);
        initGame();
    }

    private void initGame() {
        buy = (Button) findViewById(R.id.buttonBuy);
        coins = (TextView) findViewById(R.id.textCoins);


        gamesList = (ListView) findViewById(R.id.games);
        adapter = new ArrayAdapter<IGame>(this, android.R.layout.simple_list_item_1, Backend.games);
        gamesList.setAdapter(adapter);
        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Backend.selected = Backend.games.get(position);
                Toast.makeText(SelectGame.this, "Game " + Backend.selected + " is selected", Toast.LENGTH_LONG).show();
                buy.setVisibility(Backend.selected.isBought()?View.INVISIBLE:View.VISIBLE);
            }
        });
    }

    /**
     * Start Options Listener
     *
     * @param view the Button that was clicked
     */
    public void gameOptions(View view) {
        if(Backend.selected != null)
            Backend.selected.startOptions();
    }

    /**
     * Start the game Listener
     *
     * @param view the Button that was clicked
     */
    public void startGame(View view) {
        if(Backend.selected != null)
            Backend.selected.startGame();
    }

    /**
     * Buy the game Listener.
     *
     * @param view the Button that was clicked
     */
    public void buyGame(View view) {
        Backend.selected.buy();
        coins.setText(Coins.getAmount() + " Coins");
        buy.setVisibility(Backend.selected.isBought()?View.INVISIBLE:View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        coins.setText(Coins.getAmount() + " Coins");
    }
}
