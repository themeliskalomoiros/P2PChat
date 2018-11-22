package gr.kalymnos.skemelio.p2pchat.mvc_views.device_list;

import java.util.List;

import gr.kalymnos.skemelio.p2pchat.mvc_views.ViewMvcWithToolbar;

public interface DeviceListViewMvc extends ViewMvcWithToolbar {

    interface OnDeviceClickListener {
        void onDeviceClicked(int position);
    }

    void bindDevices(List<String> devices);

    void setOnDeviceClickListener(OnDeviceClickListener listener);
}
