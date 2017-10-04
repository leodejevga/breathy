package breathing.philipps.apps.com.breathing;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.implementations.BreathyGameComponent;

/**
 * Created by mohse on 29.09.2017.
 */

public class BreathingGame extends BreathyGameComponent {
    public BreathingGame(Context context) {
        this.context = context;
    }

    @Override
    public boolean start() {
        if(context==null)
            return false;
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
        return true;
    }
}
