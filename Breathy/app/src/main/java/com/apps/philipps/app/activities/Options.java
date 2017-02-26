package com.apps.philipps.app.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

import com.apps.philipps.app.Backend;
import com.apps.philipps.app.BluetoothService;
import com.apps.philipps.app.R;
import com.apps.philipps.app.UserData;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.SaveData;
import com.apps.philipps.source.interfaces.IObserver;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Options extends AppCompatActivity implements IObserver{
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private AppCompatActivity activitiy;
    private AudioManager audioManager;
    private Button btButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activitiy = this;
        setContentView(R.layout.activity_options);
        final Button sendAnEmailToDoctor = (Button) findViewById(R.id.sendemailtodoctor);
        final Button saveAndBack = (Button) findViewById(R.id.saveandback);
        BluetoothService.observe(this);
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
            userData.setEmailOfDoctor(((EditText) findViewById(R.id.doctoremail)).getText().toString());
            //TODO write Erfahrung
            SaveData<UserData> saveData = new SaveData<>(activitiy.getApplicationContext());
            saveData.writeObject("userdata", userData);
            Toast.makeText(activitiy.getApplicationContext(), "User data saved", Toast.LENGTH_LONG).show();
            finish();
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
            btButton = (Button) findViewById(R.id.activate_bt_button);
            if(!Backend.bluetoothEnabled())
                btButton.setText(R.string.activate_bt);
            else if(!Backend.bluetoothConnected())
                btButton.setText(R.string.connect_bt);
            else
                btButton.setVisibility(View.GONE);

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
        UserData userData = saveData.readObject("userdata");
        if(userData!=null){
            EditText editText = (EditText) findViewById(R.id.name);
            editText.setText(userData.getName());
            editText = (EditText) findViewById(R.id.age);
            editText.setText(userData.getAge() + "");
            editText = (EditText) findViewById(R.id.email);
            editText.setText(userData.getEmail());
            editText = (EditText) findViewById(R.id.doctoremail);
            editText.setText(userData.getEmailOfDoctor());
        }
    }

    private boolean btclicked = false;
    @Override
    protected void onStart() {
        super.onStart();
        if(btclicked) {
            if (Backend.bluetoothEnabled())
                btButton.setText(R.string.connect_bt);
            else if(Backend.bluetoothConnected())
                btButton.setVisibility(View.GONE);
        }
    }

    public void connectBluetooth(View view) {
        if(Backend.bluetoothEnabled()){
            Backend.bluetoothResume();
            Intent i = new Intent(this, Devices.class);
            startActivityForResult(i, BluetoothService.REQUEST_CONNECT_DEVICE_SECURE);
        }
        else if (!Backend.bluetoothEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, BluetoothService.REQUEST_ENABLE_BT);
        }
        btclicked = true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BluetoothService.REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case BluetoothService.REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case BluetoothService.REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(BluetoothDevice.EXTRA_DEVICE);
        if (address != null) {
            // Get the BluetoothDevice object
            BluetoothDevice device = Backend.getAdapter().getRemoteDevice(address);
            // Attempt to connect to the device
            Backend.connectDevice(device, secure);
        }
    }

    @Override
    public void call(Object message) {
        Integer msg = (Integer) message;
        if(msg == BluetoothService.STATE_CONNECTED)
            btButton.setVisibility(View.GONE);
        else if(msg == BluetoothService.STATE_NONE)
            btButton.setVisibility(View.VISIBLE);

    }
}
