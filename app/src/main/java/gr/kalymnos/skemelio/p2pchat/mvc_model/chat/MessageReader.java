package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import gr.kalymnos.skemelio.p2pchat.pojos.Message;

public class MessageReader extends Thread {
    private static final String TAG = "Skemelio MessageReader";

    interface OnMessageReceivedListener {
        void onMessageReceived(Message message);
    }

    private InputStream in;
    private int len;
    private byte[] buffer = new byte[1024];

    private OnMessageReceivedListener callback;

    public MessageReader(@NonNull InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        // Keep listening to the stream until an exception occurs.
        while (true) {
            try {
                in.read(buffer);
                String text = new String(buffer);

                in.read(buffer);
                String sender = new String(buffer);

                Log.d(TAG, "Read message");
                if (callback != null) {
                    callback.onMessageReceived(new Message(text,sender));
                }
            } catch (IOException e) {
                Log.d(TAG, "Something went wrong with the stream.", e);
                break;
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
