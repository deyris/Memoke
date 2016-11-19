package es.barcelona.dey.memoke.presenters;

import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import es.barcelona.dey.memoke.R;
import es.barcelona.dey.memoke.interactors.CreationInteractor;
import es.barcelona.dey.memoke.views.CreationView;
import es.barcelona.dey.memoke.views.HeaderView;

/**
 * Created by deyris.drake on 11/10/16.
 */
public class HeaderPresenter extends ComunPresenter implements Presenter<HeaderView>{

    public static String PARAM_TITLE = "TITLE";
    HeaderView headerView;

    @Override
    public void setView(HeaderView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");

        headerView = view;
    }

    @Override
    public void detachView() {
        headerView = null;
    }

    public void creationFragmentOnCreate(Bundle bundle){
        if (bundle == null) throw new IllegalArgumentException("You can't fill header data with null object.");
        else if (bundle!=null) {
            headerView.fillHeaderData(bundle);
        }

    }
}
