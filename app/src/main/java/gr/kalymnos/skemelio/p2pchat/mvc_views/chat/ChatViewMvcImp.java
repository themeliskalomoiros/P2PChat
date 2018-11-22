package gr.kalymnos.skemelio.p2pchat.mvc_views.chat;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

import gr.kalymnos.skemelio.p2pchat.R;
import gr.kalymnos.skemelio.p2pchat.mvc_model.Message;

public class ChatViewMvcImp implements ChatViewMvc {

    private View root;
    private FloatingActionButton sendButton;
    private EditText input;
    private ImageView smilley;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private Toolbar toolbar;

    private OnSendClickListener onSendClickListener;

    public ChatViewMvcImp(LayoutInflater inflater, ViewGroup parent) {
        initializeViews(inflater, parent);
    }

    @Override
    public void bindMessages(List<Message> messages) {
        adapter.addMessages(messages);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnSendClickListener(OnSendClickListener listener) {
        if (listener != null) {
            sendButton.setOnClickListener((view) -> listener.onSendClicked(input.getText().toString()));
        }
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void bindToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup parent) {
        root = inflater.inflate(R.layout.screen_chat_room, parent, false);
        sendButton = root.findViewById(R.id.send_fab);
        input = root.findViewById(R.id.textInput);
        smilley = root.findViewById(R.id.smilley);
        toolbar = root.findViewById(R.id.toolbar);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        adapter = new MessageAdapter(root.getContext());
        recyclerView.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

}
