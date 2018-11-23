package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import gr.kalymnos.skemelio.p2pchat.pojos.Message;

public abstract class ChatService {
    private static final String TAG = "ChatService";

    protected Context context;
    private Server server;
    private Client client;
    private MessageReader messageReader;

    protected ChatService(@NonNull Context context) {
        this.context = context;
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

}
