package gr.kalymnos.skemelio.p2pchat.mvc_views.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gr.kalymnos.skemelio.p2pchat.R;
import gr.kalymnos.skemelio.p2pchat.pojos.Message;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private Context context;
    private List<Message> messages;

    MessageAdapter(Context context) {
        this.context = context;
    }

    public void addMessages(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.screen_chat_room, viewGroup, false);
        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
        if (messages != null && messages.size() > 0) {
            messageHolder.bind(messages.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null && messages.size() > 0) {
            return messages.size();
        }
        return 0;
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        TextView message, sender;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            sender = itemView.findViewById(R.id.sender);
        }

        void bind(Message message) {
            this.message.setText(message.getMessage());
            this.sender.setText(message.getSender());
        }
    }
}
