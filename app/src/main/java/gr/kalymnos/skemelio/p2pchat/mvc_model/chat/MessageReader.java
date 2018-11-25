package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

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
        // This thread supports its own interruption, if indeed interrupted it closes resources
        while (!Thread.currentThread().isInterrupted()) {
            // Keep listening to the stream until an exception occurs.
            try {

                in.read(buffer);
                String text = new String(buffer);

                in.read(buffer);
                String sender = new String(buffer);

                if (callback != null) {
                    callback.onMessageReceived(new Message(text, sender));
                }

            } catch (InterruptedIOException e) {
                Log.d(TAG, "Interrupted, closing stream and exciting at the finally block.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not close the stream");
                    }
                }
                return;
            }
        }
    }

    void addOnMessageReceivedListener(OnMessageReceivedListener listener) {
        callback = listener;
    }

    void removeOnMessageReceivedListener() {
        callback = null;
    }

    static void cleanInstance(MessageReader reader) {
        reader.interrupt();
        reader.removeOnMessageReceivedListener();
        reader = null;
    }
}
