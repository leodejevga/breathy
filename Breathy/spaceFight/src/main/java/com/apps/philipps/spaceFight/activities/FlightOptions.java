package com.apps.philipps.spaceFight.activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.OptionManager;
import com.apps.philipps.source.SaveData;
import com.apps.philipps.spaceFight.GameStats;
import com.apps.philipps.spaceFight.R;

public class FlightOptions extends AppCompatActivity {

    public final static String TAG = "FlightOptions_save";
    private OptionManager<Boolean, Boolean> options;
    private SaveData<OptionManager<Boolean, Boolean>> save;

    private Button buyDay;
    private Button buyAlien;
    private CheckBox selectDay;
    private CheckBox selectAlien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_options);

        save = new SaveData<>(this);
        options = save.readObject(TAG);
        if (options == null) {
            options = new OptionManager<>();
            options.add(false, false, 200); // bought, selected
            options.add(false, false, 500);
        }
        initViews();
    }

    private void initViews() {
        buyDay = (Button) findViewById(R.id.day_buy);
        buyAlien = (Button) findViewById(R.id.alien_buy);
        selectDay = (CheckBox) findViewById(R.id.day_select);
        selectAlien = (CheckBox) findViewById(R.id.alien_select);

        if (options.getParameter(0)) {
            buyDay.setEnabled(false);
            buyDay.setText("Day Skin");
            buyDay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            buyDay.setTextColor(ContextCompat.getColor(FlightOptions.this, R.color.text));
        }
        else{
            selectDay.setEnabled(false);
            selectDay.setText(options.getOption(0).getPrice() + " Coins ");
            buyDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Coins.buy(options.getOption(0).getPrice(), FlightOptions.this)){
                        buyDay.setClickable(false);
                        buyDay.setText("Day Skin");
                        selectDay.setEnabled(true);
                        buyDay.setTextColor(ContextCompat.getColor(FlightOptions.this, R.color.text));
                        buyDay.setBackgroundColor(ContextCompat.getColor(FlightOptions.this, R.color.colorAccent));
                        options.getOption(0).setParameter(true);
                    }
                }
            });
        }
        if (options.getParameter(1)) {
            buyAlien.setEnabled(false);
            buyAlien.setText("Alien Skin");
            buyAlien.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            buyAlien.setTextColor(ContextCompat.getColor(FlightOptions.this, R.color.text));
        }
        else{
            selectAlien.setEnabled(false);
            selectAlien.setText(options.getOption(1).getPrice() + " Coins ");
            buyAlien.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Coins.buy(options.getOption(0).getPrice(), FlightOptions.this)){
                        buyAlien.setClickable(false);
                        selectAlien.setEnabled(true);
                        buyAlien.setText("Alien Skin");
                        buyAlien.setTextColor(ContextCompat.getColor(FlightOptions.this, R.color.text));
                        buyAlien.setBackgroundColor(ContextCompat.getColor(FlightOptions.this, R.color.colorAccent));
                        options.getOption(1).setParameter(true);
                    }
                }
            });
        }
        GameStats.day = options.getValue(0);
        GameStats.aliens = options.getValue(1);

        selectDay.setChecked(GameStats.day);
        selectAlien.setChecked(GameStats.aliens);

        selectDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                options.getOption(0).setValue(isChecked);
                GameStats.day = options.getValue(0);
            }
        });

        selectAlien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                options.getOption(1).setValue(isChecked);
                GameStats.aliens = options.getValue(1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save.writeObject(TAG, options);
    }

}
