package es.barcelona.dey.memoke.presenters;

import es.barcelona.dey.memoke.interactors.CreationInteractor;
import es.barcelona.dey.memoke.views.ContentView;
import es.barcelona.dey.memoke.views.CreationView;

/**
 * Created by deyris.drake on 26/9/16.
 */
public class ContentPresenter implements Presenter<ContentView>{

    ContentView contentView;

    @Override
    public void setView(ContentView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");

        contentView = view;
    }

    @Override
    public void detachView() {
        contentView = null;
    }
}
