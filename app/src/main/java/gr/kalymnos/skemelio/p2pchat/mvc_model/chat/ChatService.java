package gr.kalymnos.skemelio.p2pchat.mvc_model.chat;

import android.content.Context;

public abstract class ChatService {

    protected Context context;

    protected ChatService(Context context) {
        this.context = context;
    }
}
