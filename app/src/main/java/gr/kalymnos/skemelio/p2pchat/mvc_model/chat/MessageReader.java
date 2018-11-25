package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.support.annotation.NonNull;
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

    private ObjectInputStream objIn;
    private OnMessageReceivedListener callback;

    public MessageReader(@NonNull InputStream in) {
        try {
            objIn = new ObjectInputStream(in);
        } catch (IOException e) {
            Log.e(TAG, "Error creating ObjectInputStream", e);
        }
    }

    @Override
    public void run() {
        // Keep listening to the stream until an exception occurs.
        while (true) {
            try {
                Message message = (Message) objIn.readObject();
                if (callback != null) {
                    callback.onMessageReceived(message);
                }

            } catch (ClassNotFoundException e) {
                Log.e(TAG, "readObject() did not return a Message", e);
            } catch (IOException e) {
                e.printStackTrace();
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
