package gr.kalymnos.skemelio.p2pchat.mvc_views;

import android.support.v7.widget.Toolbar;

public interface ViewMvcWithToolbar extends ViewMvc {

    Toolbar getToolbar();

    void bindToolbarTitle(String title);
}
