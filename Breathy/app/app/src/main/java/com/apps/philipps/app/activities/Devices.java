package com.apps.philipps.app.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.philipps.app.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

public class Devices extends Activity {

    ArrayAdapter<String> devices;
    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        setResult(Activity.RESULT_CANCELED);
        initList();
    }

    private void initList() {
        devices = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView newDevicesListView = (ListView) findViewById(R.id.list_devices);
        newDevicesListView.setAdapter(devices);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        // Get a set of currently paired devices

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> breathyDevices = new ArrayList<>();
        for(BluetoothDevice bd: pairedDevices){
            if(bd.getBluetoothClass().hashCode() == BluetoothClass.Device.Major.UNCATEGORIZED)
                breathyDevices.add(bd);
        }

        // If there are paired devices, add each one to the ArrayAdapter
        if (breathyDevices.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : breathyDevices) {
                devices.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            Toast.makeText(this, "Keine Geräte in der Nähe gefunden", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            btAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(BluetoothDevice.EXTRA_DEVICE, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (btAdapter != null) {
            btAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    devices.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (devices.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    devices.add(noDevices);
                }
            }
        }
    };
}
