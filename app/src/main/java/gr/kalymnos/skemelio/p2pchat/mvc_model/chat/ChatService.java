package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
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
    private Server server;
    private Client client;
    private MessageReceiver messageReceiver;

    protected ChatService(@NonNull Context context) {
        this.context = context;
    }

    private class ChatMessageSender extends Thread {
        private static final String TAG = "ChatMessageSender";
        ObjectOutputStream objectOut;
        Message message;

        ChatMessageSender(@NonNull OutputStream out, Message message) {
            try {
                objectOut = new ObjectOutputStream(out);
                this.message = message;
            } catch (IOException e) {
                Log.e(TAG, "Error creating ObjectOutputStream.", e);
            }
        }

        @Override
        public void run() {
            writeMessage();
        }

        private void writeMessage() {
            try {
                objectOut.writeObject(message);
            } catch (InvalidClassException e) {
                Log.e(TAG, "Something is wrong with a class used by serialization.", e);
            } catch (NotSerializableException e) {
                Log.e(TAG, "Some object to be serialized does not implement the java.io.Serializable interface.", e);
            } catch (IOException e) {
                Log.e(TAG, "Something went wrong with the OutputStream.", e);
            } finally {
                if (objectOut != null) {
                    try {
                        objectOut.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing the stream.", e);
                    }
                }
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

}
