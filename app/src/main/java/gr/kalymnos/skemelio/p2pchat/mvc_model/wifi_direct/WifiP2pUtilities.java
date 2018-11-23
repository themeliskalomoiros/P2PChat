package gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pDevice;

public interface WifiP2pUtilities {

    static String getReasonText(int reason) {
        switch (reason) {
            case WifiP2pManager.P2P_UNSUPPORTED:
                return "Operation failed because p2p is unsupported on the device.";
            case WifiP2pManager.BUSY:
                return "Operation failed because the framework is busy and unable to service the request.";
            case WifiP2pManager.ERROR:
                return "Operation failed due to an internal error. ";
            default:
                return "Unknown reason";
        }
    }

    static String getDeviceStatus(int status) {
        switch (status) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.FAILED:
                return "Failed";
            default:
                return "Unknown";
        }
    }
}
