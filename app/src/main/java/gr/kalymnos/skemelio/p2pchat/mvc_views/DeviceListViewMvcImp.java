package gr.kalymnos.skemelio.p2pchat.mvc_views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gr.kalymnos.skemelio.p2pchat.R;

public class DeviceListViewMvcImp implements DeviceListViewMvc {

    private View root;
    private RecyclerView list;
    private DeviceAdapter adapter;

    public DeviceListViewMvcImp(LayoutInflater inflater, ViewGroup parent) {
        initializeViews(inflater, parent);
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup parent) {
        root = inflater.inflate(R.layout.screen_device_list, parent, false);
        adapter = new DeviceAdapter(root.getContext());
        initializeRecyclerView();
    }

    @Override
    public void bindDevices(List<String> devices) {
        adapter.addDevices(devices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
        adapter.setOnItemClickListener(listener);
    }

    @Override
    public Toolbar getToolbar() {
        return root.findViewById(R.id.toolbar);
    }

    @Override
    public void bindToolbarTitle(String title) {
        getToolbar().setTitle(title);
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeRecyclerView() {
        list = root.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        list.setAdapter(adapter);
        list.setLayoutManager(layoutManager);
        list.setHasFixedSize(true);
    }
}
