package com.apps.philipps.audiosurf.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.apps.philipps.audiosurf.Backend;
import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.Coins;
import com.apps.philipps.source.OptionManager;
import com.apps.philipps.source.SaveData;

import java.util.ArrayList;

/**
 * OptionManager Activity
 */
public class Options extends Activity {
    private TextView coinsText;
    SaveData<ArrayList> saveOptions;
    SaveData<Integer> saveCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.as_options);
        saveOptions = new SaveData<>(Options.this);
        saveCredit = new SaveData<>(Options.this);

        ArrayList audiosurfOptions = saveOptions.readObject("audiosurfOptions");
        if (audiosurfOptions != null)
            Backend.options.setOptions(audiosurfOptions);
        initOptions();
        initLabels();
    }

    private void initLabels() {
        coinsText = (TextView) findViewById(R.id.asOptionsCoins);
        coinsText.setText(Coins.getAmount() + " Coins");
    }

    private void initOptions() {
        TableRow row = new TableRow(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.optionsButtons);
        for (int i = 0; i < Backend.options.size(); i++) {
            OptionManager.Option option = Backend.options.getOption(i);
            Button btn = new Button(this);
            int color = btn.getSolidColor();
            btn.setBackgroundColor((Boolean) option.Value ? 0xff00ff00 : color);
            btn.setText(option.Parameter.toString());
            btn.setId(i + 251);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    int id = b.getId() - 251;
                    OptionManager.Option option = Backend.options.getOption(id);
                    boolean bought = false;
                    if (!Backend.options.getValue(id)) {
                        bought = Coins.buy(option.Price);
                        saveCredit.writeObject("credit", Coins.getAmount());
                        Backend.options.set(id, bought);
                    }
                    int color = b.getSolidColor();
                    b.setBackgroundColor(Backend.options.getValue(id) ? 0xff00ff00 : color);
                    coinsText.setText(Coins.getAmount() + " Coins");
                    saveOptions.writeObject("audiosurfOptions", Backend.options.getOptions());
                }
            });
            layout.addView(btn);
        }
    }

}
