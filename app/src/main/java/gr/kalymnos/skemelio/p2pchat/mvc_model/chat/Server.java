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
    private Socket socket = null;   // TODO: Remove this field.
    private OnServerAcceptConnectionListener callback;

    public Server() {
        initializeServerSocket();
    }


    @Override
    public void run() {
        while (true) {
            /** Keep listening until exception occurs or a socket is returned.*/
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
        // TODO: Remove this method, when a client A gets a socket and then a Client B gets a socket, then if the Client A asks for its outputstream from here it will only take the ClientB socket.
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
