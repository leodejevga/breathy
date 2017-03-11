package com.apps.philipps.audiosurf.activities;

import android.os.Bundle;
import android.util.Log;

import com.apps.philipps.audiosurf.MyRenderer;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.abstracts.AbstractGameActivity;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Game Activity
 */
public class Game extends AbstractGameActivity implements IObserver {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppState.inGame = AppState.recordData = true;
        BreathData.addObserver(this);

        gameRenderer = new MyRenderer(this, this);
    }

    private void setValue(){
        Log.d("Data received", BreathData.getAsString(0,10));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppState.inGame = AppState.recordData = false;
    }


    @Override
    public void call(Object... messages) {
        if(messages[0].equals("Draw"))
            Log.d("Draw", ((MyRenderer) gameRenderer).getFramerate() + " fps");
        else
            setValue();
    }

    //TODO: Hier wird das Spiel ausgeführt. OpenGL.
}
