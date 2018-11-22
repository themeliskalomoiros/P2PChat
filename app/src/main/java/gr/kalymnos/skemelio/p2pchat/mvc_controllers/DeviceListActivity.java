package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import gr.kalymnos.skemelio.p2pchat.mvc_model.WiFiDirectBroadcastReceiver;
import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvcImp;

import static android.net.wifi.p2p.WifiP2pManager.Channel;

public class DeviceListActivity extends AppCompatActivity implements DeviceListViewMvc.OnDeviceClickListener, WifiP2pManager.ChannelListener {

    private DeviceListViewMvc viewMvc;

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
    public void onDeviceClicked(int position) {
        // TODO: Must implement
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
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
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            retryChannel = true;
            manager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
