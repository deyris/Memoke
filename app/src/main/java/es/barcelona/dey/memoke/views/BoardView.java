package es.barcelona.dey.memoke.views;

import android.content.Context;
import android.widget.FrameLayout;

import es.barcelona.dey.memoke.beans.Play;

/**
 * Created by deyris.drake on 1/10/16.
 */
public interface BoardView {

    Context getContext();

    void disappearsTabForGame (Play currentPlay, FrameLayout frameLayout);

    void warnYouWin();

    void turnTabForGame(Play currentPlay, FrameLayout frameLayout, int positionInBoard);

    void setAnimationToFrame(FrameLayout frame, int position);
}
