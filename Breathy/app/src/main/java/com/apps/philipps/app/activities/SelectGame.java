package com.apps.philipps.app.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.apps.philipps.app.Backend;
import com.apps.philipps.app.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.Coins;
import com.apps.philipps.source.SaveData;
import com.apps.philipps.source.interfaces.IGame;
import com.apps.philipps.source.interfaces.IObserver;

import dalvik.system.BaseDexClassLoader;

/**
 * Select Game Activity
 */
public class SelectGame extends AppCompatActivity {
    private Button buy;
    private TextView coins;
    private ListView gamesList;
    private ArrayAdapter<IGame> adapter;
    private VideoView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);
        initGame();
    }

    private void initGame() {
        buy = (Button) findViewById(R.id.buttonBuy);
        coins = (TextView) findViewById(R.id.textCoins);
        preview = (VideoView) findViewById(R.id.videoView);
        gamesList = (ListView) findViewById(R.id.games);
        adapter = new ArrayAdapter<IGame>(this, android.R.layout.simple_list_item_1, Backend.games);
        gamesList.setAdapter(adapter);
        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Backend.selected = Backend.games.get(position);
                buy.setVisibility(Backend.selected.isBought() ? View.INVISIBLE : View.VISIBLE);
                Integer previewData = Backend.selected.getPreview();
                if (previewData != null) {
                    String videoPath = "android.resource://" + getResources().getResourcePackageName(R.raw.preview) + "/" + previewData;
                    Uri uri = Uri.parse(videoPath);
                    preview.setVisibility(View.VISIBLE);
                    preview.setVideoURI(uri);
                    preview.start();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Backend.cacheManager.saveCreditToCache();
    }

    /**
     * Start OptionManager Listener
     *
     * @param view the Button that was clicked
     */
    public void gameOptions(View view) {
        if (Backend.selected != null)
            Backend.selected.startOptions();
    }

    /**
     * Start the game Listener
     *
     * @param view the Button that was clicked
     */
    public void startGame(View view) {
        if (Backend.selected == null)
            Toast.makeText(this, "Please select one game", Toast.LENGTH_SHORT).show();
        else if (AppState.btState != AppState.BtState.Connected)
            Toast.makeText(this, "You are not connected to a Breathy device", Toast.LENGTH_SHORT).show();
        else {
            Backend.selected.startGame();
        }
    }

    /**
     * Buy the game Listener.
     *
     * @param view the Button that was clicked
     */
    public void buyGame(View view) {
        Backend.selected.buy(getApplicationContext());
        coins.setText(Coins.getAmount() + " Coins");
        Backend.cacheManager.saveCreditToCache();
        Backend.cacheManager.saveIGameStatusToCache(Backend.selected.getName(), Backend.selected.isBought());
        buy.setVisibility(Backend.selected.isBought() ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        coins.setText(Coins.getAmount() + " Coins");
    }

}
