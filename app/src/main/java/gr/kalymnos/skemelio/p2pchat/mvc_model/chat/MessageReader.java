package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import gr.kalymnos.skemelio.p2pchat.pojos.Message;

public class MessageReader extends Thread {
    private static final String TAG = "Skemelio MessageReader";

    interface OnMessageReceivedListener {
        void onMessageReceived(Message message);
    }

    private ObjectInputStream objectIn;
    private OnMessageReceivedListener callback;

    public MessageReader(@NonNull InputStream in) {
        try {
            Log.d(TAG,"Attempt to create objectIn");
            objectIn = new ObjectInputStream(in);
            Log.d(TAG,"objectIn created");
        } catch (IOException e) {
            Log.e(TAG, "Error creating ObjectInputStream", e);
        }
    }

    @Override
    public void run() {
        // Keep listening to the stream until an exception occurs.
        while (true) {
            try {
                Message message = (Message) objectIn.readObject();
                Log.d(TAG,"Read message");
                if (callback != null)
                    callback.onMessageReceived(message);
            } catch (IOException e) {
                Log.d(TAG, "Something went wrong with the stream.", e);
                break;
            } catch (ClassNotFoundException e) {
                Log.d(TAG, "Class of a serialized object cannot be found.", e);
            }
        }
    }

    void addOnMessageReceivedListener(OnMessageReceivedListener listener) {
        callback = listener;
    }

    void removeOnMessageReceivedListener() {
        callback = null;
    }
}
