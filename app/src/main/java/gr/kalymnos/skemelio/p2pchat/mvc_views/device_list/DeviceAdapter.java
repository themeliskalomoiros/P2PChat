package gr.kalymnos.skemelio.p2pchat.mvc_views.device_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gr.kalymnos.skemelio.p2pchat.R;

class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    private Context context;
    private List<String> devices;
    private DeviceListViewMvc.OnDeviceClickListener onItemClickListener;

    public DeviceAdapter(Context context) {
        this.context = context;
    }

    void addDevices(List<String> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_device, viewGroup, false);
        return new DeviceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder deviceHolder, int i) {
        if (devices != null && devices.size() > 0) {
            deviceHolder.bindDevice(devices.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (devices != null && devices.size() > 0) {
            return devices.size();
        }
        return 0;
    }

    public void setOnItemClickListener(DeviceListViewMvc.OnDeviceClickListener listener) {
        onItemClickListener = listener;
    }

    class DeviceHolder extends RecyclerView.ViewHolder {
        TextView device;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener((view) -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onDeviceClicked(getAdapterPosition());
                }
            });
            device = (TextView) itemView;
        }

        void bindDevice(String device) {
            this.device.setText(device);
        }
    }
}
