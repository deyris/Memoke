package es.barcelona.dey.memoke.views;

import android.app.Activity;
import android.content.Context;

/**
 * Created by deyris.drake on 18/9/16.
 */
public interface MainView {

    Context getContext();

    void launchAlertExistsThisBoard();

    void openToCreateBoardFromZero(String title);

    void openToCreateBoardFromOther(String jsonSelectedBoard, String title);

     void hideButtonMoreBoards();

    void showButtonMoreBoards();
}
