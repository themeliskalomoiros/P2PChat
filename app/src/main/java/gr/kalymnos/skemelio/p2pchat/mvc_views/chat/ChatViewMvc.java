package gr.kalymnos.skemelio.p2pchat.mvc_views.chat;

import java.util.List;

import gr.kalymnos.skemelio.p2pchat.pojos.Message;
import gr.kalymnos.skemelio.p2pchat.mvc_views.ViewMvcWithToolbar;

public interface ChatViewMvc extends ViewMvcWithToolbar {

    interface OnSendClickListener {
        void onSendClicked(String msg);
    }

    void bindMessages(List<Message> messages);

    void indicateDeviceIsGroupOwner();

    void indicateDeviceIsNotGroupOwner();

    void setOnSendClickListener(OnSendClickListener listener);
}
