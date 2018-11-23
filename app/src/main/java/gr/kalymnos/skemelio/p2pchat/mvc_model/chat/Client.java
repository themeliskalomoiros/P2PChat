package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class Client extends Thread {
    private static final String TAG = "Client";
    private static final int TIMEOUT_MILLI = 500;

    interface OnClientConnectionListener {
        void onClientConnected(Socket socket);
    }

    private OnClientConnectionListener callback;
    private Socket socket = new Socket();
    private InetAddress serverAddress; // The group owners address, he is the server.

    Client(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    @Override
    public void run() {
        // TODO: must implement
        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(serverAddress, Server.PORT), TIMEOUT_MILLI);
            if (callback != null) {
                callback.onClientConnected(socket);
            }
        } catch (IOException e) {
            Log.e(TAG, "Something went wrong with the socket", e);
        }
    }

    void setOnClientConnectionListener(OnClientConnectionListener listener) {
        callback = listener;
    }
}
