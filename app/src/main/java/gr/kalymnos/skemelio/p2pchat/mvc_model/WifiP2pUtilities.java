package gr.kalymnos.skemelio.p2pchat.mvc_model;

import android.net.wifi.p2p.WifiP2pManager;

public interface WifiP2pUtilities {
    static String getReasonText(int reason){
        switch (reason){
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
}
