package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
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
        private ServerSocket serverSocket = null;
        private Socket socket = null;

        @Override
        public void run() {
            /** Keep listening until exception occurs or a socket is returned.*/
            while (true) {
                try {
                    serverSocket = new ServerSocket(8888);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Error creating server socket or accepting a client", e);
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with the connection
                    // in a seperate thread.
                    manageServersConnectedSocket(socket);
                }
                /** We could close the server socket here if we wanted to establish a connection with
                 only one client and just keep the socket open. In this case our server is
                 waiting for other connections as well.
                 */
            }
        }
    }

    public abstract void manageServersConnectedSocket(Socket socket);

    private class Client extends Thread {

        private Socket socket = null;
        private InetSocketAddress endPoint;

        @Override
        public void run() {
            // TODO: must implement
        }
    }

    private class ChatManager extends Thread {
        @Override
        public void run() {
            // TODO: must implement
        }
    }

}
