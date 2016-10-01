package es.barcelona.dey.memoke.views;

import android.content.Context;

import es.barcelona.dey.memoke.beans.Play;

/**
 * Created by deyris.drake on 1/10/16.
 */
public interface BoardView {

    Context getContext();

    void disappearsTabForGame (Play currentPlay);

    void warnYouWin();

    void turnTabForGame(Play currentPlay);
}
