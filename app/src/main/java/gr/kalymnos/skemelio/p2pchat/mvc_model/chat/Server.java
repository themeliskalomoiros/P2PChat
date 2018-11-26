package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Server extends Thread {
    private static final String TAG = "Skemelio Server";
    static final int PORT = 8888;

    private static volatile int instanceCounter = 0;

    interface OnServerAcceptConnectionListener {
        void onServerAcceptConnection(Socket socket);
    }

    private ServerSocket serverSocket = null;
    private List<Socket> sockets = new ArrayList<>(); // Every common socket with every client connected to this server.
    private OnServerAcceptConnectionListener callback;

    public Server() {
        Log.d(TAG,"Created instance"+(++instanceCounter));
        initializeServerSocket();
    }

    @Override
    public void run() {
        while (true) {
            /** Keep listening until exception occurs or a socket is returned.*/

            Socket socket = null;

            try {
                socket = serverSocket.accept();
                Log.d(TAG, "Serversocket accepted connection");
            } catch (IOException e) {
                Log.e(TAG, "Error accepting a client", e);
            }

            if (socket != null) {
                // A connection was accepted.

                /*
                 * Keep track of all the sockets so if you want to send a message,
                 * send it through every socket, so all clients get that message
                 * (common chat room between server and all of its clients).
                 * */
                Log.d(TAG,"Socket acceptet from server is not null.");
                sockets.add(socket); //

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

    boolean areAllSocketsClosed() {
        for (Socket socket : sockets) {
            if (!socket.isClosed()) {
                return false;
            }
        }
        return true;
    }

    void removeOnServerAcceptConnectionListener() {
        callback = null;
    }

    List<OutputStream> getAllOutputStreams() {
        if (sockets != null && sockets.size() > 0) {
            List<OutputStream> outs = new ArrayList<>();
            try {
                for (Socket socket : sockets) {
                    outs.add(socket.getOutputStream());
                }
            } catch (IOException e) {
                Log.e(TAG, "Could not get output stream from socket", e);
            }
            return outs;
        }
        return null;
    }

    static void cleanInstance(Server server) {
        server.interrupt();
        server = null;
    }
}
