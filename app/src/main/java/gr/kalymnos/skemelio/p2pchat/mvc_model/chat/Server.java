package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server extends Thread {
    private static final String TAG = "Server";
    static final int PORT = 8888;

    interface OnServerAcceptConnectionListener {
        void onServerAcceptConnection(Socket socket);
    }

    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private OnServerAcceptConnectionListener callback;

    @Override
    public void run() {
        /** Keep listening until exception occurs or a socket is returned.*/
        while (true) {
            try {
                serverSocket = new ServerSocket(PORT);
                socket = serverSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Error creating server socket or accepting a client", e);
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with the connection
                // in a seperate thread.
                callback.onServerAcceptConnection(socket);
            }
            /** We could close the server socket here if we wanted to establish a connection with
             only one client and just keep the socket open. In this case our server is
             waiting for other connections as well.
             */
        }
    }

    void setOnServerAcceptConnectionListener(OnServerAcceptConnectionListener listener) {
        callback = listener;
    }

    void cancel() {

    }
}
