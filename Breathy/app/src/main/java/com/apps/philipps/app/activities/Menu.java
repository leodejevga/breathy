package com.apps.philipps.app.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.apps.philipps.app.Backend;
import com.apps.philipps.app.R;
import com.apps.philipps.audiosurf.AudioSurf;

/**
 * Main Activity.
 */
public class Menu extends AppCompatActivity {

    private ImageButton games;
    private ImageButton options;
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

    @Override
    protected void onStart() {
        super.onStart();

        if (!Backend.bluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 3); //3 is the request to enable bluetooth
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: //Secure Bt
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                }
                break;
            case 2: //Unsecure Bt
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                }
                break;
            case 3: // Enable Bt
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, "Bluetooth ausgeschaltet",Toast.LENGTH_SHORT).show();
                }
        }
    }
}
