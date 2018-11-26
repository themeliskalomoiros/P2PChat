package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import gr.kalymnos.skemelio.p2pchat.mvc_controllers.ChatActivity;
import gr.kalymnos.skemelio.p2pchat.mvc_model.wifi_direct.WifiP2pUtils;
import gr.kalymnos.skemelio.p2pchat.pojos.Message;

// TODO: Must implement stopServer and stopClient some time.

public class ChatService implements Server.OnServerAcceptConnectionListener,
        Client.OnClientConnectionListener, MessageReader.OnMessageReceivedListener {
    private static final String TAG = "ChatService";

    private static ChatService instance = null;

    private final WifiP2pInfo info;
    private Context context;
    private Server server;
    private Client client;
    private MessageReader messageReader;

    private ChatService(Context context, WifiP2pInfo info) {
        this.context = context;
        this.info = info;
    }

    public static synchronized ChatService getInstance(@NonNull Context context, @NonNull WifiP2pInfo info) {
        if (instance == null) {
            instance = new ChatService(context, info);
            Log.d(TAG, "Created ChatService instance");
        }
        return instance;
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

    @Override
    public void onClientConnected(Socket socket) {
        startMessageReader(socket);
        startChatActivity();
    }

    public void send(String msg) {
        Message message = new Message(msg, WifiP2pUtils.getDeviceBluetoothName(context.getContentResolver()));
        if (isServer()) {
            for (OutputStream out : server.getAllOutputStreams()) {
                new MessageWriter(out, message).start();
            }
        } else if (isClient()) {
            new MessageWriter(client.getOutputStream(), message).start();
        } else {
            throw new UnsupportedOperationException(TAG + ": Don't know if this device is a server, a client, both or none.");
        }
    }

    private boolean isClient() {
        return server == null && client != null;
    }

    private boolean isServer() {
        return server != null && client == null;
    }

    @Override
    public void onMessageReceived(Message message) {
        broadcastMessage(message);
    }

    @Override
    public void onServerAcceptConnection(Socket socket) {
        startMessageReader(socket);
        startChatActivity();
    }

    public void startServer() {
        if (server == null) {
            server = new Server();
            server.addOnServerAcceptConnectionListener(this);
            server.start();
        }
    }

    public void startClient(InetAddress serverAddress) {
        if (client == null) {
            client = new Client(serverAddress);
            client.addOnClientConnectionListener(this);
            client.start();
        }
    }

    private void startMessageReader(Socket socket) {
        if (messageReader == null) {
            try {
                messageReader = new MessageReader(socket.getInputStream());
                messageReader.addOnMessageReceivedListener(this);
                messageReader.start();
            } catch (IOException e) {
                Log.e(TAG, "Error creating MessageReager", e);
            }
        }
    }

    private void startChatActivity() {
        String deviceName = WifiP2pUtils.getDeviceBluetoothName(context.getContentResolver());
        Bundle extras = ChatActivity.createBundle(deviceName, info);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    public void cleanChatResources() {
        if (isClient()) {
            client.removeOnClientConnectionListener();
            client = null;
            --Client.instanceCounter;
        } else if (isServer() && server.areAllSocketsClosed()) {
            server.removeOnServerAcceptConnectionListener();
            server = null;
            --Server.instanceCounter;
        }
        messageReader.interrupt();
        messageReader.removeOnMessageReceivedListener();
        messageReader = null;
        --MessageReader.instanceCounter;
    }
}
