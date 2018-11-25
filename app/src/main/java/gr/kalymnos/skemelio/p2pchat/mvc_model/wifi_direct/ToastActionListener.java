package gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class ToastActionListener implements WifiP2pManager.ActionListener {


    private final Context context;
    private final String successMsg;

    public ToastActionListener(@NonNull Context context, String successMsg) {
        this.context = context;
        this.successMsg = successMsg;
    }

    @Override
    public void onSuccess() {
        Toast.makeText(context, successMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(int reason) {
        Toast.makeText(context, WifiP2pUtils.getReasonText(reason),Toast.LENGTH_LONG).show();
    }
}
