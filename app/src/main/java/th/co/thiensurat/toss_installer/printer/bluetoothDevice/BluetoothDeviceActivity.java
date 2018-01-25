package th.co.thiensurat.toss_installer.printer.bluetoothDevice;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import th.co.thiensurat.toss_installer.MainActivity;
import th.co.thiensurat.toss_installer.R;
import th.co.thiensurat.toss_installer.utils.AnimateButton;
import th.co.thiensurat.toss_installer.utils.Constance;
import th.co.thiensurat.toss_installer.utils.CustomDialog;

public class BluetoothDeviceActivity extends AppCompatActivity {

    private int position;
    private BluetoothDevice device;
    private List<String> s;
    private static String address;
    private CustomDialog customDialog;
    private BluetoothSocket bluetoothSocket;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> stringArrayAdapter;
    private static BluetoothSocket bluetoothSocketStatic;
    private ArrayAdapter<BluetoothDevice> bluetoothDeviceArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device);
        setUpView();
        setUpInstance();
    }

    TextView textViewTitle;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btn_search) Button buttonSearch;
    @BindView(R.id.list_device) ListView listViewDevice;
    private void setUpView() {
        ButterKnife.bind(this);
        textViewTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textViewTitle.setText("อุปกรณ์บลูทูธ");
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonSearch.setOnClickListener( onSearch() );

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        listViewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                if (bluetoothAdapter == null) {
                    return;
                }

                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                new ConnectTask(bluetoothDeviceArrayAdapter.getItem(i)).execute();
                //device = bluetoothDeviceArrayAdapter.getItem(i);

                /*Thread connectThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constance.UUID));
                            bluetoothSocket.connect();
                        } catch (IOException ex) {
                            //runOnUiThread(socketErrorRunnable);
                            try {
                                bluetoothSocket.close();
                            } catch (IOException e) {

                            }
                            bluetoothSocket = null;
                            return;
                        } finally {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    bluetoothSocketStatic = bluetoothSocket;
                                    address = bluetoothDeviceArrayAdapter.getItem(position).getAddress();
                                    Intent intent = new Intent();
                                    intent.putExtra(Constance.EXTRA_DEVICE_ADDRESS, address);
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();

                                }
                            });
                        }
                    }
                });
                connectThread.start();*/
            }
        });
    }

    public static BluetoothSocket getSocket() {
        return bluetoothSocketStatic;
    }

    public static String getAddress() {
        return address;
    }

    private void setUpInstance() {
        customDialog = new CustomDialog(this);
        initialDevice();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    private void initialDevice() {
        flushData();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            customDialog.dialogFail("อุปกรณ์ไม่รองรับการเชื่อมต่อบลูทูธ!!");
        } else {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }

            stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_list);
            listViewDevice.setAdapter(stringArrayAdapter);
        }
    }

    private void flushData() {
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                bluetoothSocket = null;
            }

            if (bluetoothAdapter != null) {
                bluetoothAdapter.cancelDiscovery();
            }

            if (bluetoothDeviceArrayAdapter != null) {
                bluetoothDeviceArrayAdapter.clear();
                bluetoothDeviceArrayAdapter = null;
            }

            if (stringArrayAdapter != null) {
                stringArrayAdapter.clear();
                stringArrayAdapter.notifyDataSetChanged();
                stringArrayAdapter.notifyDataSetInvalidated();
                stringArrayAdapter = null;
            }

            s.clear();
            finalize();
        } catch (Exception ex) {

        } catch (Throwable e) {

        }
    }

    private View.OnClickListener onSearch() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSearch.startAnimation(new AnimateButton().animbutton());
                initialDevice();

                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                registerReceiver(receiver, filter);
                bluetoothAdapter.startDiscovery();
            }
        };
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //customDialog.dialogLoading();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    if (bluetoothDeviceArrayAdapter == null) {
                        bluetoothDeviceArrayAdapter = new ArrayAdapter<BluetoothDevice>(getApplicationContext(), R.layout.layout_list);
                    }

                    if (bluetoothDeviceArrayAdapter.getPosition(device) < 0) {
                        bluetoothDeviceArrayAdapter.add(device);
                        stringArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                        stringArrayAdapter.notifyDataSetInvalidated();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //customDialog.dialogDimiss();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        }

        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class ConnectTask extends AsyncTask<URL, Integer, Long> {
        BluetoothDevice device;

        public ConnectTask(BluetoothDevice device) {
            this.device = device;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //customDialog.dialogLoading();
        }

        @Override
        protected Long doInBackground(URL... urls) {
            long result = 0;
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constance.UUID));
                bluetoothSocket.connect();
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            //customDialog.dialogDimiss();
            if (bluetoothSocket != null && aLong == 1) {
                bluetoothSocketStatic = bluetoothSocket;
                /*address = device.getAddress();
                setResult(RESULT_OK);
                finish();*/

                address = device.getAddress();
                Intent intent = new Intent();
                intent.putExtra(Constance.EXTRA_DEVICE_ADDRESS, address);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                customDialog.dialogFail("ไม่สามารถ\nเชื่อมต่ออุปกรณ์ได้");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            return false;
        }
        return true;
    }
}
