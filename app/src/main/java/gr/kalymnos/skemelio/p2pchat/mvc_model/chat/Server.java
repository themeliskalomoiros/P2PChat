package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Server extends Thread {
    private static final String TAG = "Skemelio Server";
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
        initializeServerSocket();

        while (true) {
            try {
                socket = serverSocket.accept();
                Log.d(TAG, "accepted connection");
            } catch (IOException e) {
                Log.e(TAG, "Error accepting a client", e);
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with the connection
                // in a seperate thread.
                if (callback != null) {
                    callback.onServerAcceptConnection(socket);
                }

                try {
                    serverSocket.close();
                } catch (IOException e) {
                    Log.d(TAG, "Could not close() the server socket");
                }
            }
        }
    }

    private void initializeServerSocket() {
        try {
            serverSocket = new ServerSocket(PORT);
            Log.d(TAG, "Created server socket");
        } catch (IOException e) {
            Log.e(TAG, "Error creating server socket or accepting a client", e);
        }
    }

    void addOnServerAcceptConnectionListener(OnServerAcceptConnectionListener listener) {
        callback = listener;
    }

    void removeOnServerAcceptConnectionListener() {
        callback = null;
    }

    OutputStream getOutputStream() {
        if (socket != null) {
            try {
                return socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Could not get output stream from socket", e);
            }
        }
        return null;
    }
}
