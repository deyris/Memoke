package es.barcelona.dey.memoke.presenters;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import es.barcelona.dey.memoke.views.BoardView;

/**
 * Created by deyris.drake on 1/10/16.
 */
public class BoardPresenter extends ComunPresenter implements Presenter<BoardView>{

    BoardView boardView;

    @Override
    public void setView(BoardView view) {
        if (view == null) throw new IllegalArgumentException("You can't set a null view");

        boardView = view;
    }

    @Override
    public void detachView() {
        boardView = null;
    }

    public void doLock(boolean locked, Activity activity) {
        if (locked) {
            int o = boardView.getContext().getResources().getConfiguration().orientation;
            if (o == Configuration.ORIENTATION_LANDSCAPE)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            else if (o == Configuration.ORIENTATION_PORTRAIT)
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }
}
