package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct.WifiP2pUtils;
import gr.kalymnos.skemelio.p2pchat.pojos.Message;

public abstract class ChatService {
    private static final String TAG = "ChatService";

    protected Context context;
    private Server server = null;
    private Client client = null;
    private ChatMessageReceiver chatMessageReceiver = null;

    protected ChatService(@NonNull Context context) {
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
        static final String TAG = "Client";
        static final int TIMEOUT_MILLI = 500;

        Socket socket = new Socket();
        InetAddress serverAddress; // The group owners address, he is the server.

        Client(InetAddress serverAddress) {
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

    private class ChatMessageReceiver extends Thread {
        static final String TAG = "ChatMessageReceiver";

        InputStream in;
        byte[] buffer = new byte[1024];

        ChatMessageReceiver(@NonNull Socket socket) {
            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occured when creating the input stream, " +
                        "the socket is closed, " +
                        "the socket is not connected, " +
                        "or the socket input has been shutdown using shutdownInput()", e);
            }
        }

        @Override
        public void run() {
            // Keep listening to the inputStream until an exception occurs.
            while (true) {
                try {
                    in.read(buffer);
                    // Broadcast the message locally, so stakeholders should be informed.
                    broadcastMessage(createMessageFrom(buffer));
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        private void broadcastMessage(Message message) {
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
            manager.sendBroadcast(createMessageReceivedIntent(message));
        }

        private Intent createMessageReceivedIntent(Message message) {
            Bundle extras = new Bundle();
            extras.putParcelable(ChatConstants.Extras.EXTRA_MESSAGE, message);
            Intent intent = new Intent(ChatConstants.Actions.ACTION_MESSAGE_RECEIVED);
            intent.putExtras(extras);
            return intent;
        }

        private Message createMessageFrom(byte[] buffer) {
            String messageText = new String(buffer);
            String sender = WifiP2pUtils.getDeviceBluetoothName(context.getContentResolver());
            return new Message(messageText, sender);
        }
    }

    private class ChatMessageSender extends Thread {
        private static final String TAG = "ChatMessageSender";
        OutputStream out;
        Message message;

        ChatMessageSender(@NonNull Socket socket, Message message) {
            try {
                out = socket.getOutputStream();
                this.message = message;
            } catch (IOException e) {
                Log.e(TAG, "An error occured when creating the output stream " +
                        "or if the socket is not connected", e);
            }
        }

        @Override
        public void run() {
            writeMessage();
        }

        private void writeMessage() {
            try {
                ObjectOutputStream objectOut = new ObjectOutputStream(out);
                objectOut.writeObject(message);
            } catch (InvalidClassException e) {
                Log.e(TAG, "Something is wrong with a class used by serialization.", e);
            } catch (NotSerializableException e) {
                Log.e(TAG, "Some object to be serialized does not implement the java.io.Serializable interface.", e);
            } catch (IOException e) {
                Log.e(TAG, "Something went wrong with the OutputStream.", e);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not close output stream.", e);
                    }
                }
            }
        }
    }

}
