package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
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
import gr.kalymnos.skemelio.p2pchat.mvc_model.WiFiDirectBroadcastReceiver;
import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvcImp;

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

                manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        showToast("Discovery Initiated");
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        showLongToast("Discovery failed, reason code: " + reasonCode);
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeviceClicked(int position) {
        // TODO: Must implement
        if (deviceListIncludesItems()) {
            WifiP2pDevice device = getClickedDeviceFromList(position);
            if (device != null) {
                showToast(device.deviceName);
            }

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
                deviceNames.add(device.deviceName);
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
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
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
