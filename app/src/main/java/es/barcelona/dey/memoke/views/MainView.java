package es.barcelona.dey.memoke.views;

import android.content.Context;

/**
 * Created by deyris.drake on 18/9/16.
 */
public interface MainView {

    Context getContext();

    void launchAlertExistsThisBoard();

    void openToCreateBoard(boolean fromZero);
}
