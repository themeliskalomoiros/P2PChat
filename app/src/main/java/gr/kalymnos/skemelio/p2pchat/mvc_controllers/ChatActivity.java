package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.skemelio.p2pchat.mvc_model.chat.ChatService;
import gr.kalymnos.skemelio.p2pchat.mvc_views.chat.ChatViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.chat.ChatViewMvcImp;
import gr.kalymnos.skemelio.p2pchat.pojos.Message;

import static gr.kalymnos.skemelio.p2pchat.mvc_model.chat.ChatConstants.Actions.ACTION_MESSAGE_RECEIVED;
import static gr.kalymnos.skemelio.p2pchat.mvc_model.chat.ChatConstants.Extras.EXTRA_MESSAGE;
import static gr.kalymnos.skemelio.p2pchat.mvc_model.chat.ChatConstants.Extras.EXTRA_WIFI_P2P_INFO;

public class ChatActivity extends AppCompatActivity implements ChatViewMvc.OnSendClickListener {

    public static final String EXTRA_DEVICE_NAME = "extra wifi p2p device name";

    private ChatViewMvc viewMvc;
    private String username;
    private WifiP2pInfo info;

    private ChatService chatService;
    private List<Message> messages = new ArrayList<>();
    private final IntentFilter messageIntentFilter = new IntentFilter();
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_MESSAGE_RECEIVED:
                    Message message = intent.getParcelableExtra(EXTRA_MESSAGE);
                    messages.add(message);
                    viewMvc.bindMessages(messages);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra(EXTRA_DEVICE_NAME);
        info = getIntent().getParcelableExtra(EXTRA_WIFI_P2P_INFO);
        messageIntentFilter.addAction(ACTION_MESSAGE_RECEIVED);
        chatService = ChatService.getInstance(this, getIntent().getParcelableExtra(EXTRA_WIFI_P2P_INFO));
        setupUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatService.cleanChatResources();
    }

    @Override
    public void onSendClicked(String msg) {
        chatService.send(msg);
    }

    private void setupUi() {
        initializeViewMvc();
        if (info.isGroupOwner) {
            viewMvc.indicateDeviceIsGroupOwner();
        } else {
            viewMvc.indicateDeviceIsNotGroupOwner();
        }
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new ChatViewMvcImp(LayoutInflater.from(this), null);
        viewMvc.setOnSendClickListener(this);
    }

    public static Bundle createBundle(String device, WifiP2pInfo info) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEVICE_NAME, device);
        bundle.putParcelable(EXTRA_WIFI_P2P_INFO, info);
        return bundle;
    }
}
