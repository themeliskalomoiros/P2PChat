package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.skemelio.p2pchat.R;
import gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct.ToastActionListener;
import gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct.WifiP2pUtils;
import gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct.WiFiP2pReceiver;
import gr.kalymnos.skemelio.p2pchat.mvc_views.device_list.DeviceListViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.device_list.DeviceListViewMvcImp;

import static android.net.wifi.p2p.WifiP2pManager.Channel;

public class DeviceListActivity extends AppCompatActivity implements DeviceListViewMvc.OnDeviceClickListener, WifiP2pManager.ChannelListener {

    private DeviceListViewMvc viewMvc;

    private WifiP2pDeviceList deviceList;

    private WifiP2pManager manager;
    private Channel channel;
    private boolean retryChannel = false;
    private boolean isWifiP2pEnabled = false;

    private BroadcastReceiver receiver;
    private final IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        initializeWifiReceiver();
        addActionsToIntentFilter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.device_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.discover_peers:
                if (!isWifiP2pEnabled) {
                    showToast("Wifi p2p is not enabled!");
                    return true;
                }
                manager.discoverPeers(channel, new ToastActionListener(this, "Device descovery initiated."));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeviceClicked(int position) {
        if (deviceListIncludesItems()) {
            WifiP2pDevice device = getClickedDeviceFromList(position);
            if (device != null) {
                if (device.status == WifiP2pDevice.AVAILABLE){
                    connectTo(device);
                }else if (device.status == WifiP2pDevice.CONNECTED){
                    manager.cancelConnect(channel,new ToastActionListener(this,"Initiated canceled connection"));
                }
            }
        }
    }

    private void connectTo(WifiP2pDevice device) {
        if (manager != null && channel != null) {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
            /**
             * From the docs:
             *
             * If all the devices in a network support Wi-Fi Direct,
             * you can use the connect() method on each device
             * because the method then creates the group and selects a group owner automatically.
             *
             * If you want the device running your app to serve as the group owner for a network
             * that includes legacy devices—that is, devices that don't support Wi-Fi Direct
             * —you follow the same sequence of steps as in the Connect to a Peer section,
             * except you create a new WifiP2pManager.ActionListener using createGroup()
             * instead of connect().
             * */
            manager.connect(channel, config, new ToastActionListener(this, "Connection Initiated."));
        }
    }

    private WifiP2pDevice getClickedDeviceFromList(int position) {
        int iterations = 0;
        for (WifiP2pDevice device : deviceList.getDeviceList()) {
            if (position == iterations++) {
                return device;
            }
        }
        return null;
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public void setDeviceList(WifiP2pDeviceList deviceList) {
        this.deviceList = deviceList;
    }

    public void updateDeviceList() {
        if (deviceListIncludesItems()) {
            List<String> deviceNames = new ArrayList<>();
            for (WifiP2pDevice device : deviceList.getDeviceList()) {
                deviceNames.add(device.deviceName + "\n Status: "
                        + WifiP2pUtils.getDeviceStatus(device.status));
            }
            viewMvc.bindDevices(deviceNames);
        }
    }

    private boolean deviceListIncludesItems() {
        return deviceList != null && deviceList.getDeviceList() != null && deviceList.getDeviceList().size() > 0;
    }

    private void initializeViewMvc() {
        viewMvc = new DeviceListViewMvcImp(LayoutInflater.from(this), null);
        viewMvc.setOnDeviceClickListener(this);
    }

    private void setupUi() {
        initializeViewMvc();
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void initializeWifiReceiver() {
        manager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), this);
        receiver = new WiFiP2pReceiver(manager, channel, this);
    }

    private void addActionsToIntentFilter() {
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        if (manager != null && !retryChannel) {
            showToast("Channel lost. Trying again");
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            showLongToast("Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.");
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
