package gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.net.InetAddress;

import gr.kalymnos.skemelio.p2pchat.mvc_controllers.DeviceListActivity;
import gr.kalymnos.skemelio.p2pchat.mvc_model.chat.ChatService;

import static android.net.wifi.p2p.WifiP2pManager.Channel;
import static android.net.wifi.p2p.WifiP2pManager.PeerListListener;

public class WiFiP2pReceiver extends BroadcastReceiver {
    private static final String TAG = "WiFiP2pReceiver";

    private WifiP2pManager manager;
    private Channel channel;
    private DeviceListActivity activity;

    private PeerListListener peerListListener = (deviceList) -> {
        activity.setDeviceList(deviceList);
        activity.updateDeviceList();
    };
    private WifiP2pManager.ConnectionInfoListener connectionListener = (info) -> {
        // InetAddress from WifiP2pInfo struct.
        InetAddress groupOwnerAddress = info.groupOwnerAddress;
        ChatService service = ChatService.getInstance(activity.getApplicationContext(), info);

        // After the group negotiation, we can determine the group owner
        // (server).
        if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a group owner thread and accepting
            // incoming connections.
            service.startServer();
        } else if (info.groupFormed) {
            // The other device acts as the peer (client). In this case,
            // you'll want to create a peer thread that connects
            // to the group owner.
            service.startClient(groupOwnerAddress);
        }
    };

    public WiFiP2pReceiver(WifiP2pManager manager, Channel channel,
                           DeviceListActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION: // Check to see if Wi-Fi is enabled and notify appropriate activity
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                activity.setIsWifiP2pEnabled(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
                break;

            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION: // Call WifiP2pManager.requestPeers() to get a list of current peers
                manager.requestPeers(channel, peerListListener);
                break;

            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION: // Respond to new connection or disconnections
                if (manager != null) {
                    NetworkInfo networkInfo = intent
                            .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                    if (networkInfo.isConnected()) {
                        // We are connected with the other device, request connection
                        // info to find group owner IP
                        manager.requestConnectionInfo(channel, connectionListener);
                    }
                }

                break;

            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION: // Respond to this device's wifi state changing
                break;
        }
    }
}
