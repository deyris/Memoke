package es.barcelona.dey.memoke.views;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import es.barcelona.dey.memoke.ui.ContentFragment;
import es.barcelona.dey.memoke.ui.CreationFragment;

/**
 * Created by deyris.drake on 18/9/16.
 */
public interface CreationView {

    Context getContext();

    Bundle actualizeBundle(Bundle bundle, String nameData, String data);

    FragmentManager getFragmentManager();

    void inicializeButtonNext();

    void inicializeButtonPast();

    void hideNextButton();

    void actualicePairNumberInContentFragment(CreationFragment creationFragment);

     void setListenerBtnSgte();

}
