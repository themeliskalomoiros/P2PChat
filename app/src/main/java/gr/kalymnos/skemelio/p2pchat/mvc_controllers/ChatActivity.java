package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import gr.kalymnos.skemelio.p2pchat.mvc_views.chat.ChatViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.chat.ChatViewMvcImp;

public class ChatActivity extends AppCompatActivity implements ChatViewMvc.OnSendClickListener {

    public static final String EXTRA_DEVICE_NAME = "extra wifi p2p device name";
    public static final String EXTRA_IS_GROUP_OWNER = "extra wifi p2p is group owner";

    private ChatViewMvc viewMvc;
    private String username; // Or device name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra(EXTRA_DEVICE_NAME);
        setupUi();
    }

    @Override
    public void onSendClicked(String msg) {

    }

    private void setupUi() {
        initializeViewMvc();
        if (getIntent().getBooleanExtra(EXTRA_IS_GROUP_OWNER, false)) {
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

    public static Bundle createBundle(String device, boolean isGroupOwner) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEVICE_NAME, device);
        bundle.putBoolean(EXTRA_IS_GROUP_OWNER, isGroupOwner);
        return bundle;
    }
}
