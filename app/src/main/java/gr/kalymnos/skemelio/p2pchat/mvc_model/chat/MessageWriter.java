package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import gr.kalymnos.skemelio.p2pchat.pojos.Message;

public class MessageWriter extends Thread {
    private static final String TAG = "MessageWriter";
    ObjectOutputStream objOut;
    Message message;

    MessageWriter(@NonNull OutputStream out, Message message) {
        try {
            this.objOut = new ObjectOutputStream(out);
        } catch (IOException e) {
            Log.e(TAG, "Error creating ObjectOutputStream", e);
        }
        this.message = message;
    }

    @Override
    public void run() {
        writeMessage();
    }

    private void writeMessage() {
        try {
            objOut.writeObject(message);
        } catch (InvalidClassException e) {
            Log.e(TAG, "Something is wrong with a class used by serialization.", e);
        } catch (NotSerializableException e) {
            Log.e(TAG, "Some object to be serialized does not implement the java.io.Serializable interface.", e);
        } catch (IOException e) {
            Log.e(TAG, "Something went wrong with the OutputStream.", e);
        }
    }
}

