package breathing.philipps.apps.com.breathing;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.implementations.BreathyGame;

/**
 * Created by mohse on 29.09.2017.
 */

public class Breathing extends BreathyGame {

    public Breathing() {
        price = 1;
        name = "Breathing";
    }

    @Override
    public void init(Context context, boolean bought) {
        game = new BreathingOption(context);
        options = new BreathingGame(context);
        this.bought = bought;
    }


    @Override
    public @RawRes
    Integer getPreview(){
        return R.raw.preview;
    }
}
