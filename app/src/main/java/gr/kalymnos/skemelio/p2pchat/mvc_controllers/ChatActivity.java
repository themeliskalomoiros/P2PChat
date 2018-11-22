package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import gr.kalymnos.skemelio.p2pchat.mvc_views.chat.ChatViewMvc;

public class ChatActivity extends AppCompatActivity implements ChatViewMvc.OnSendClickListener {

    private ChatViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSendClicked(String msg) {

    }
}
