package th.co.thiensurat.toss_installer.printer;

/**
 * Created by imrankst1221@gmail.com
 */

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import th.co.thiensurat.toss_installer.R;

public class DeviceList extends ListActivity {

    static private final int REQUEST_ENABLE_BT = 0*1000;
    static private BluetoothAdapter mBluetoothAdapter = null;
    static private ArrayAdapter<String> mArrayAdapter = null;

    static private ArrayAdapter<BluetoothDevice> btDevices = null;

    static private BluetoothSocket mbtSocket = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Bluetooth Devices");
        try {
            if (initDevicesList() != 0) {
                this.finish();
                return;
            }

        } catch (Exception ex) {
            this.finish();
            return;
        }

        IntentFilter btIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBTReceiver, btIntentFilter);
    }

    public static BluetoothSocket getSocket() {
        return mbtSocket;
    }

    private void flushData() {
        try {
            if (mbtSocket != null) {
                mbtSocket.close();
                mbtSocket = null;
            }

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
            }

            if (btDevices != null) {
                btDevices.clear();
                btDevices = null;
            }

            if (mArrayAdapter != null) {
                mArrayAdapter.clear();
                mArrayAdapter.notifyDataSetChanged();
                mArrayAdapter.notifyDataSetInvalidated();
                mArrayAdapter = null;
            }
            finalize();
        } catch (Exception ex) {

        } catch (Throwable e) {

        }
    }

    private int initDevicesList() {
        flushData();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "อุปกรณ์ไม่รองรับการเชื่อมต่อบลูทูธ!!", Toast.LENGTH_LONG).show();
            return -1;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout_list);
        setListAdapter(mArrayAdapter);
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        try {
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } catch (Exception ex) {
            return -2;
        }

        Toast.makeText(getApplicationContext(), "กำลังค้นหาอุปกรณบลูทูธ...", Toast.LENGTH_SHORT).show();

        return 0;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);
        switch (reqCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    Set<BluetoothDevice> btDeviceList = mBluetoothAdapter .getBondedDevices();
                    try {
                        if (btDeviceList.size() > 0) {
                            for (BluetoothDevice device : btDeviceList) {
                                if (btDeviceList.contains(device) == false) {
                                    btDevices.add(device);
                                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                                    mArrayAdapter.notifyDataSetInvalidated();
                                }
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
                break;
        }
        mBluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    if (btDevices == null) {
                        btDevices = new ArrayAdapter<BluetoothDevice>(getApplicationContext(), R.layout.layout_list);
                    }

                    if (btDevices.getPosition(device) < 0) {
                        btDevices.add(device);
                        mArrayAdapter.add(device.getName() + "\n"
                                + device.getAddress() + "\n" );
                        mArrayAdapter.notifyDataSetInvalidated();
                    }
                } catch (Exception ex) {
                  ex.fillInStackTrace();
                }
            }
        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mBluetoothAdapter == null) {
            return;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        Toast.makeText( getApplicationContext(), "เชื่อมต่อกับอุปกรณ์ " + btDevices.getItem(position).getName() , Toast.LENGTH_SHORT).show();
        Thread connectThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    boolean gotuuid = btDevices.getItem(position) .fetchUuidsWithSdp();
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                    mbtSocket = btDevices.getItem(position).createRfcommSocketToServiceRecord(uuid);
                    mbtSocket.connect();
                } catch (IOException ex) {
                    runOnUiThread(socketErrorRunnable);
                    try {
                        mbtSocket.close();
                    } catch (IOException e) {
                     e.printStackTrace();
                    }
                    mbtSocket = null;
                    return;
                } finally {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            }
        });
        connectThread.start();
        setResult(RESULT_OK);
        finish();
    }

    private Runnable socketErrorRunnable = new Runnable() {

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "ไม่สามารถเชื่อมต่ออุปกรณ์ได้", Toast.LENGTH_SHORT).show();
            mBluetoothAdapter.startDiscovery();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST, Menu.NONE, "กำลังค้นหา...");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case Menu.FIRST:
                initDevicesList();
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(mBTReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}