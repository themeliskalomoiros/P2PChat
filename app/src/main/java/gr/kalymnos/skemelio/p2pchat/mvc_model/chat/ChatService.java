package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ChatService {
    private static final String TAG = "ChatService";

    protected Context context;
    private Server server = null;
    private Client client = null;
    private ChatManager chatManager = null;

    protected ChatService(Context context) {
        this.context = context;
    }

    private class Server extends Thread {
        static final int PORT = 8888;

        private ServerSocket serverSocket = null;
        private Socket socket = null;

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
                    manageServerConnectedSocket(socket);
                }
                /** We could close the server socket here if we wanted to establish a connection with
                 only one client and just keep the socket open. In this case our server is
                 waiting for other connections as well.
                 */
            }
        }
    }

    public abstract void manageServerConnectedSocket(Socket socket);

    private class Client extends Thread {
        private static final String TAG = "Client";
        private static final int TIMEOUT_MILLI = 500;

        private Socket socket = new Socket();
        private InetAddress serverAddress; // The group owners address, he is the server.

        public Client(InetAddress serverAddress) {
            this.serverAddress = serverAddress;
        }

        @Override
        public void run() {
            // TODO: must implement
            try {
                socket.bind(null);
                socket.connect(new InetSocketAddress(serverAddress, Server.PORT), TIMEOUT_MILLI);
                manageClientConnectedSocket(socket);
            } catch (IOException e) {
                Log.e(TAG, "Something went wrong with the socket", e);
            }
        }
    }

    protected abstract void manageClientConnectedSocket(Socket socket);

    private class ChatManager extends Thread {
        @Override
        public void run() {
            // TODO: must implement
        }
    }

}