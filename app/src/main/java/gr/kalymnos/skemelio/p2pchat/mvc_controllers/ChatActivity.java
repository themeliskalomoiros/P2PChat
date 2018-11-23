package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import gr.kalymnos.skemelio.p2pchat.mvc_views.chat.ChatViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.chat.ChatViewMvcImp;

public class ChatActivity extends AppCompatActivity implements ChatViewMvc.OnSendClickListener {

    public static final String EXTRA_DEVICE = "extra wifi p2p device";
    private ChatViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
    }

    @Override
    public void onSendClicked(String msg) {

    }

    private void setupUi() {
        initializeViewMvc();
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new ChatViewMvcImp(LayoutInflater.from(this), null);
        viewMvc.setOnSendClickListener(this);
    }
}
