package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ChatService {
    private static final String TAG = "ChatService";

    protected Context context;
    protected Server server;
    protected Client client;

    protected ChatService(Context context) {
        this.context = context;
    }

    private class Server extends Thread {

        @Override
        public void run() {
            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8888);
                Socket client = serverSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Error creating server socket or accepting a client", e);
            }

        }
    }

    private class Client extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

}
