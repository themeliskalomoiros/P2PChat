package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

import gr.kalymnos.skemelio.p2pchat.pojos.Message;

public class MessageReader extends Thread {
    private static final String TAG = "Skemelio MessageReader";
    static volatile int instanceCounter = 0;

    interface OnMessageReceivedListener {
        void onMessageReceived(Message message);
    }

    private InputStream in;
    private byte[] buffer = new byte[1024];
    private OnMessageReceivedListener callback;

    public MessageReader(@NonNull InputStream in) {
        Log.d(TAG, "Created instance" + (++instanceCounter));
        this.in = in;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            // Keep listening to the stream until an exception occurs.
            try {
                if (callback != null) {

                    in.read(buffer);
                    String text = new String(buffer);

                    in.read(buffer);
                    String sender = new String(buffer);

                    callback.onMessageReceived(new Message(text, sender));
                }else {
                    throw new UnsupportedOperationException(TAG+" callback is null.");
                }

            } catch (InterruptedIOException e) {
                Log.d(TAG, "Interrupted, closing stream and exciting at the finally block.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (!Thread.currentThread().isInterrupted() && in != null) {
                    try {
                        in.close();
                        Log.d(TAG, "MessageReader closed the socket in finally block.");
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
}
