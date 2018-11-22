package gr.kalymnos.skemelio.p2pchat.mvc_views;

import java.util.List;

public interface DeviceListViewMvc extends ViewMvcWithToolbar {

    interface OnDeviceClickListener {
        void onDeviceClicked(int position);
    }

    void bindDevices(List<String> devices);

    void setOnDeviceClickListener(OnDeviceClickListener listener);
}
