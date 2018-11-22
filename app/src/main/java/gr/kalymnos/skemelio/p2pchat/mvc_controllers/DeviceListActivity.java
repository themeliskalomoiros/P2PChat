package gr.kalymnos.skemelio.p2pchat.mvc_controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvc;
import gr.kalymnos.skemelio.p2pchat.mvc_views.DeviceListViewMvcImp;

public class DeviceListActivity extends AppCompatActivity implements DeviceListViewMvc.OnDeviceClickListener {

    private DeviceListViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc();
        setContentView(viewMvc.getRootView());
    }

    @Override
    public void onDeviceClicked(int position) {
        // TODO: Must implement
    }

    private void initializeViewMvc() {
        viewMvc = new DeviceListViewMvcImp(LayoutInflater.from(this), null);
        viewMvc.setOnDeviceClickListener(this);
    }
}
