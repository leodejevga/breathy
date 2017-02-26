package com.apps.philipps.audiosurf.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Game Activity
 */
public class Game extends Activity implements IObserver {
    TextView data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as_game);
        data = (TextView) findViewById(R.id.data);
        AppState.inGame = AppState.recordData = true;
        BreathData.addObserver(this);
    }

    private void setValue(){
        Integer[] values = BreathData.get(0,10);
        String text = "";
        for (Integer i : values)
            if(i!=null)
                text+=" " + i;
        data.setText(text);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppState.inGame = AppState.recordData = false;
    }

    @Override
    public void call(Object message) {
        setValue();
    }

    //TODO: Hier wird das Spiel ausgeführt. OpenGL.
}
