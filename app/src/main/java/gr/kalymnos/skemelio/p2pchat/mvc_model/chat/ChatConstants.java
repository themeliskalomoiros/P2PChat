package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

public interface ChatConstants {

    interface Actions {
        String TAG = "Actions";
        String ACTION_MESSAGE_RECEIVED = TAG + " receive message";
    }

    interface Extras {
        String TAG = "Extras";
        String EXTRA_MESSAGE = " message";
    }
}
