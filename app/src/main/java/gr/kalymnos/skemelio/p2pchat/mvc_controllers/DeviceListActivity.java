package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvcImp;

import static android.net.wifi.p2p.WifiP2pManager.Channel;

public class DeviceListActivity extends AppCompatActivity implements DeviceListViewMvc.OnDeviceClickListener {

    private DeviceListViewMvc viewMvc;

    private WifiP2pManager manager;
    private Channel channel;
    private boolean isWifiP2pEnabled = false;

    private BroadcastReceiver receiver;
    private final IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc();
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void addActionsToFilter() {
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    @Override
    public void onDeviceClicked(int position) {
        // TODO: Must implement
    }

    private void initializeViewMvc() {
        viewMvc = new DeviceListViewMvcImp(LayoutInflater.from(this), null);
        viewMvc.setOnDeviceClickListener(this);
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
}
