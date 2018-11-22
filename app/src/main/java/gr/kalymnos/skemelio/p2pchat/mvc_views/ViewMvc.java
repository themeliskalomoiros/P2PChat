package gr.kalymnos.skemelio.p2pchat.mvc_views;

import android.view.View;

public interface ViewMvc {

    /*
     * Get the root Android View which is used internally by this MVC View for
     * presenting data to the user.
     * THe returned Android View might be used by an MVC Controller in order to query
     * or alter the properties of either the root Android View itself, or any of its
     * child Android View's.*/
    View getRootView();

}

