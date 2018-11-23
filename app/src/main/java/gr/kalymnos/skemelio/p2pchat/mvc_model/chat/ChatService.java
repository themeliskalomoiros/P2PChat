package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import gr.kalymnos.skemelio.p2pchat.pojos.Message;

// TODO: Must implement stopServer and stopClient some time.

public class ChatService implements Server.OnServerAcceptConnectionListener,
        Client.OnClientConnectionListener, MessageReader.OnMessageReceivedListener {
    private static final String TAG = "ChatService";

    private static ChatService instance = null;

    protected Context context;
    private Server server;
    private Client client;
    private MessageReader messageReader;

    private ChatService(Context context) {
        this.context = context;
    }

    public static synchronized ChatService getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new ChatService(context);
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
        initializeMessageReader(socket);
        messageReader.start();
    }

    @Override
    public void onMessageReceived(Message message) {
        broadcastMessage(message);
    }

    @Override
    public void onServerAcceptConnection(Socket socket) {
        initializeMessageReader(socket);
        messageReader.start();
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

    private void initializeMessageReader(Socket socket) {
        if (messageReader == null) {
            try {
                messageReader = new MessageReader(socket.getInputStream());
                messageReader.addOnMessageReceivedListener(this);
            } catch (IOException e) {
                Log.e(TAG, "Error creating MessageReager", e);
            }
        }
    }
}
