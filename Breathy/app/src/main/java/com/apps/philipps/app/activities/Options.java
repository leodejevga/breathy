package com.apps.philipps.app.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.apps.philipps.app.R;
import com.apps.philipps.source.SaveData;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Options extends AppCompatActivity {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    AppCompatActivity activitiy;
    AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activitiy = this;
        setContentView(R.layout.activity_options);
        final Button sendAnEmailToDoctor = (Button) findViewById(R.id.sendemailtodoctor);
        final Button saveAndBack = (Button) findViewById(R.id.saveandback);

        sendAnEmailToDoctor.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       sendAnEmailToDoctor();
                                                   }
                                               }
        );
        saveAndBack.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               try {
                                                   saveandback();
                                               } catch (IOException e) {
                                                   Toast.makeText(activitiy.getApplicationContext(), "Can not store data to Cache", Toast.LENGTH_LONG).show();
                                               }
                                           }
                                       }
        );
        initControls();
        getUserData();
    }


    private void sendAnEmailToDoctor() {
        if (!vadilateName())
            Toast.makeText(Options.this, "Name should not be empty", Toast.LENGTH_LONG).show();
        if (!vadilateEmail(findViewById(R.id.email)))
            Toast.makeText(Options.this, "Email is invalid", Toast.LENGTH_LONG).show();
        if (!vadilateEmail(findViewById(R.id.doctoremail)))
            Toast.makeText(Options.this, "Email of doctor is invalid", Toast.LENGTH_LONG).show();

        if (vadilateName() && vadilateEmail(findViewById(R.id.email)) && vadilateEmail(findViewById(R.id.doctoremail))) {
            String doctorEmail = ((EditText) findViewById(R.id.doctoremail)).getText().toString();
            String mail = ((EditText) findViewById(R.id.email)).getText().toString();
            String name = ((EditText) findViewById(R.id.name)).getText().toString();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{doctorEmail});
            i.putExtra(Intent.EXTRA_SUBJECT, "Patient " + name + " email " + mail);
            i.putExtra(Intent.EXTRA_TEXT, "Breathy is so geil, und ich weiß nicht was ich senden soll");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(Options.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveandback() throws IOException {
        if (!vadilateName())
            Toast.makeText(Options.this, "Name should not be empty", Toast.LENGTH_LONG).show();
        if (!vadilateAge())
            Toast.makeText(Options.this, "Age is invalid", Toast.LENGTH_LONG).show();
        if (!vadilateEmail(findViewById(R.id.email)))
            Toast.makeText(Options.this, "Email is invalid", Toast.LENGTH_LONG).show();
        if (vadilateName() && vadilateAge() && vadilateEmail(findViewById(R.id.email))) {
            UserData userData = new UserData();
            userData.setName(((EditText) findViewById(R.id.name)).getText().toString());
            userData.setEmail(((EditText) findViewById(R.id.email)).getText().toString());
            userData.setAge(Integer.parseInt(((EditText) findViewById(R.id.age)).getText().toString()));
            //TODO write Erfahrung
            SaveData<UserData> saveData = new SaveData<>(activitiy.getApplicationContext());
            saveData.writeObject("userdata", userData);
            Toast.makeText(activitiy.getApplicationContext(), "User data saved", Toast.LENGTH_LONG).show();

            Intent i = new Intent(Options.this, Menu.class);
            startActivity(i);
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.beginner:
                if (checked)
                    break;
            case R.id.expert:
                if (checked)
                    break;
        }
    }

    private boolean vadilateName() {
        EditText name = (EditText) findViewById(R.id.name);
        return !name.getText().toString().equals("");
    }

    private boolean vadilateEmail(View view) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        EditText email = (EditText) view;
        if (email.getText().equals(""))
            return false;

        Matcher matcher = pattern.matcher(email.getText());
        return matcher.matches();
    }

    private boolean vadilateAge() {
        EditText age = (EditText) findViewById(R.id.age);
        try {
            int a = Integer.parseInt(age.getText().toString());
            Log.e("a", a + "");
            if (a > 0 && a < 200)
                return true;
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    private void initControls() {
        try {
            SeekBar volumeSeekbar = (SeekBar) findViewById(R.id.soundseekbar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                final MediaPlayer myMediaPlayer = MediaPlayer.create(Options.this, R.raw.mario);

                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                    myMediaPlayer.stop();
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                    myMediaPlayer.start();
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserData() {
        SaveData<UserData> saveData = new SaveData<>(activitiy.getApplicationContext());
        Log.i("Data", saveData.toString());
        try {
            UserData userData = saveData.readObject("userdata");
            EditText editText = (EditText) findViewById(R.id.name);
            editText.setText(userData.getName());
            editText = (EditText) findViewById(R.id.age);
            editText.setText(userData.getAge() + "");
            editText = (EditText) findViewById(R.id.email);
            editText.setText(userData.getEmail());
            //TODO read Erfahrung
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
